package com.yy.my_tutor.payment.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.domain.dto.AddPriceRequest;
import com.yy.my_tutor.payment.domain.dto.CreateProductRequest;
import com.yy.my_tutor.payment.domain.dto.ProductDTO;
import com.yy.my_tutor.payment.domain.dto.UpdateProductRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.service.ProductService;
import com.yy.my_tutor.payment.util.PaymentSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ProductController {

    @Resource private ProductService productService;
    @Resource private PaymentSecurityUtil securityUtil;

    // ============== C 端公开 ==============
    @GetMapping("/api/payment/products")
    public RespResult<List<ProductDTO>> listProducts(@RequestParam(required = false) String type,
                                                     @RequestParam(required = false) String currency) {
        return RespResult.data(productService.listActiveProducts(type, currency));
    }

    @GetMapping("/api/payment/products/{id}")
    public RespResult<ProductDTO> getDetail(@PathVariable Integer id) {
        return RespResult.data(productService.getProductDetail(id));
    }

    // ============== 后台 admin ==============
    @PostMapping("/api/admin/payment/products")
    public RespResult<ProductDTO> create(@RequestBody CreateProductRequest req) {
        securityUtil.requireAdmin();
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(productService.createProduct(req, uid));
    }

    @PutMapping("/api/admin/payment/products/{id}")
    public RespResult<ProductDTO> update(@PathVariable Integer id,
                                         @RequestBody UpdateProductRequest req) {
        securityUtil.requireAdmin();
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(productService.updateProduct(id, req, uid));
    }

    @PostMapping("/api/admin/payment/products/{id}/prices")
    public RespResult<PaymentPrice> addPrice(@PathVariable Integer id,
                                             @RequestBody AddPriceRequest req) {
        securityUtil.requireAdmin();
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(productService.addPrice(id, req, uid));
    }

    @PutMapping("/api/admin/payment/prices/{priceId}/status")
    public RespResult<PaymentPrice> setPriceStatus(@PathVariable Integer priceId,
                                                   @RequestBody Map<String, Integer> body) {
        securityUtil.requireAdmin();
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(productService.updatePriceStatus(priceId, body.get("status"), uid));
    }
}
