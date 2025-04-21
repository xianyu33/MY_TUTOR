package com.yy.my_tutor.ark.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArkClient implements Serializable {
    private String model;

    private Boolean stream;

    private JSONObject stream_options;

    private JSONArray messages;

    private String mode;

    private Integer ttl;

    private JSONObject truncation_strategy;

    private String context_id;
}
