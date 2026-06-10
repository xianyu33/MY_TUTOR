package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;          // null=不改
    private String nameZh;
    private String nameFr;
    private String description;
    private Integer status;       // null=不改;1-上架,0-下架
}
