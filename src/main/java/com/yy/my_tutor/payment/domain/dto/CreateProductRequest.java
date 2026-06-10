package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest {
    private String productType;          // ProductType 枚举字符串
    private Integer targetRefId;         // 一次性商品关联的业务实体 id;订阅型为 null
    private String name;                 // 必填,英文
    private String nameZh;
    private String nameFr;
    private String description;
    private List<PriceDefDTO> prices;    // 至少一条
}
