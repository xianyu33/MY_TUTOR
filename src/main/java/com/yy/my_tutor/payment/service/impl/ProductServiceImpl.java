package com.yy.my_tutor.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.payment.domain.dto.AddPriceRequest;
import com.yy.my_tutor.payment.domain.dto.CreateProductRequest;
import com.yy.my_tutor.payment.domain.dto.PriceDefDTO;
import com.yy.my_tutor.payment.domain.dto.ProductDTO;
import com.yy.my_tutor.payment.domain.dto.UpdateProductRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.enums.ProductType;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.service.ProductService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Resource private StripeClientService stripeClient;
    @Resource private PaymentProductMapper productMapper;
    @Resource private PaymentPriceMapper priceMapper;
    @Resource private StripeConfig stripeConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO createProduct(CreateProductRequest req, Integer operatorUserId) {
        validateCreateRequest(req);

        // 1. 创建 Stripe Product
        Map<String, String> productMeta = new HashMap<>();
        productMeta.put("product_type", req.getProductType());
        if (req.getTargetRefId() != null) {
            productMeta.put("target_ref_id", String.valueOf(req.getTargetRefId()));
        }
        Product stripeProduct;
        try {
            stripeProduct = stripeClient.createProduct(req.getName(), req.getDescription(), productMeta);
        } catch (StripeException e) {
            log.error("Stripe createProduct failed", e);
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }

        // 2. 写本地 product
        PaymentProduct product = new PaymentProduct();
        product.setStripeProductId(stripeProduct.getId());
        product.setProductType(req.getProductType());
        product.setTargetRefId(req.getTargetRefId());
        product.setName(req.getName());
        product.setNameZh(req.getNameZh());
        product.setNameFr(req.getNameFr());
        product.setDescription(req.getDescription());
        product.setStatus(1);
        product.setCreateBy(String.valueOf(operatorUserId));
        product.setUpdateBy(String.valueOf(operatorUserId));
        productMapper.insert(product);

        // 3. 循环创建 Stripe Price + 本地 price
        List<PaymentPrice> localPrices = new ArrayList<>();
        for (PriceDefDTO pd : req.getPrices()) {
            validatePriceDef(pd);
            PaymentPrice lp = createOnePrice(product, pd, operatorUserId);
            localPrices.add(lp);
        }

        return ProductDTO.from(product, localPrices);
    }

    /** 创建一条 Price(Stripe + 本地),由 createProduct / addPrice 复用 */
    private PaymentPrice createOnePrice(PaymentProduct product, PriceDefDTO pd, Integer operatorUserId) {
        // Stripe quarter 用 month + interval_count=3 表达
        String stripeInterval = pd.getBillingInterval();
        Integer intervalCount = 1;
        if ("quarter".equals(stripeInterval)) {
            stripeInterval = "month";
            intervalCount = 3;
        }

        // 先插本地拿到 id,再用 id 作为 metadata 创建 Stripe Price
        PaymentPrice lp = new PaymentPrice();
        lp.setProductId(product.getId());
        lp.setCurrency(pd.getCurrency());
        lp.setUnitAmount(pd.getUnitAmount());
        lp.setBillingInterval(pd.getBillingInterval());
        lp.setIntervalCount(intervalCount);
        lp.setStatus(1);
        lp.setCreateBy(String.valueOf(operatorUserId));
        lp.setUpdateBy(String.valueOf(operatorUserId));
        // 先用占位 stripe_price_id 插入(然后立刻 update),避免唯一索引冲突需要把占位设为唯一
        lp.setStripePriceId("pending_" + System.nanoTime());
        priceMapper.insert(lp);

        Map<String, String> priceMeta = new HashMap<>();
        priceMeta.put("local_price_id", String.valueOf(lp.getId()));
        priceMeta.put("local_product_id", String.valueOf(product.getId()));
        if (pd.getBillingInterval() != null) {
            priceMeta.put("billing_interval", pd.getBillingInterval());
        }

        Price stripePrice;
        try {
            stripePrice = stripeClient.createPrice(
                    product.getStripeProductId(), pd.getCurrency(), pd.getUnitAmount(),
                    stripeInterval, intervalCount, priceMeta);
        } catch (StripeException e) {
            log.error("Stripe createPrice failed", e);
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }

        lp.setStripePriceId(stripePrice.getId());
        priceMapper.updateById(lp);
        return lp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO updateProduct(Integer productId, UpdateProductRequest req, Integer operatorUserId) {
        PaymentProduct product = productMapper.selectById(productId);
        if (product == null) throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND");

        try {
            stripeClient.updateProduct(product.getStripeProductId(),
                    req.getStatus() != null ? req.getStatus() == 1 : null,
                    req.getName());
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }

        if (req.getName() != null) product.setName(req.getName());
        if (req.getNameZh() != null) product.setNameZh(req.getNameZh());
        if (req.getNameFr() != null) product.setNameFr(req.getNameFr());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
        if (req.getStatus() != null) product.setStatus(req.getStatus());
        product.setUpdateBy(String.valueOf(operatorUserId));
        productMapper.updateById(product);

        return getProductDetail(productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentPrice addPrice(Integer productId, AddPriceRequest req, Integer operatorUserId) {
        PaymentProduct product = productMapper.selectById(productId);
        if (product == null) throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND");

        PriceDefDTO pd = new PriceDefDTO();
        pd.setCurrency(req.getCurrency());
        pd.setUnitAmount(req.getUnitAmount());
        pd.setBillingInterval(req.getBillingInterval());
        validatePriceDef(pd);

        return createOnePrice(product, pd, operatorUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentPrice updatePriceStatus(Integer priceId, Integer status, Integer operatorUserId) {
        PaymentPrice price = priceMapper.selectById(priceId);
        if (price == null) throw PaymentException.of("PAYMENT_PRICE_NOT_FOUND");
        if (status == null || (status != 0 && status != 1)) {
            throw PaymentException.of("PAYMENT_INVALID_STATUS");
        }
        try {
            stripeClient.updatePriceActive(price.getStripePriceId(), status == 1);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
        price.setStatus(status);
        price.setUpdateBy(String.valueOf(operatorUserId));
        priceMapper.updateById(price);
        return price;
    }

    @Override
    public List<ProductDTO> listActiveProducts(String type, String currency) {
        QueryWrapper<PaymentProduct> qw = new QueryWrapper<>();
        qw.eq("status", 1).eq("delete_flag", "N");
        if (type != null) qw.eq("product_type", type);
        List<PaymentProduct> products = productMapper.selectList(qw);
        if (products.isEmpty()) return Collections.emptyList();

        return products.stream().map(p -> {
            List<PaymentPrice> prices = priceMapper.selectActiveByProductId(p.getId());
            if (currency != null) {
                prices = prices.stream().filter(x -> currency.equalsIgnoreCase(x.getCurrency())).collect(Collectors.toList());
            }
            return ProductDTO.from(p, prices);
        }).collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductDetail(Integer productId) {
        PaymentProduct product = productMapper.selectById(productId);
        if (product == null) throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND");
        List<PaymentPrice> prices = priceMapper.selectActiveByProductId(productId);
        return ProductDTO.from(product, prices);
    }

    private void validateCreateRequest(CreateProductRequest req) {
        if (req.getName() == null || req.getName().isEmpty()) {
            throw PaymentException.of("PAYMENT_PRODUCT_NAME_REQUIRED");
        }
        try {
            ProductType.valueOf(req.getProductType());
        } catch (Exception e) {
            throw PaymentException.of("PAYMENT_INVALID_PRODUCT_TYPE");
        }
        if (req.getPrices() == null || req.getPrices().isEmpty()) {
            throw PaymentException.of("PAYMENT_PRICES_REQUIRED");
        }
    }

    private void validatePriceDef(PriceDefDTO pd) {
        if (pd.getCurrency() == null || !stripeConfig.supportedCurrenciesSet().contains(pd.getCurrency().toLowerCase())) {
            throw PaymentException.of("PAYMENT_UNSUPPORTED_CURRENCY");
        }
        if (pd.getUnitAmount() == null || pd.getUnitAmount() <= 0) {
            throw PaymentException.of("PAYMENT_INVALID_AMOUNT");
        }
        if (pd.getBillingInterval() != null
                && !"month".equals(pd.getBillingInterval())
                && !"quarter".equals(pd.getBillingInterval())
                && !"year".equals(pd.getBillingInterval())) {
            throw PaymentException.of("PAYMENT_INVALID_INTERVAL");
        }
    }
}
