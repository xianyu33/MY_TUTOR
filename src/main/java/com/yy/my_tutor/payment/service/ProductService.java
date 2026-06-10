package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.AddPriceRequest;
import com.yy.my_tutor.payment.domain.dto.CreateProductRequest;
import com.yy.my_tutor.payment.domain.dto.ProductDTO;
import com.yy.my_tutor.payment.domain.dto.UpdateProductRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(CreateProductRequest req, Integer operatorUserId);
    ProductDTO updateProduct(Integer productId, UpdateProductRequest req, Integer operatorUserId);
    PaymentPrice addPrice(Integer productId, AddPriceRequest req, Integer operatorUserId);
    PaymentPrice updatePriceStatus(Integer priceId, Integer status, Integer operatorUserId);
    List<ProductDTO> listActiveProducts(String type, String currency);
    ProductDTO getProductDetail(Integer productId);
}
