package com.yy.my_tutor.common;

import lombok.Data;

@Data
public class RespResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> RespResult<T> success(T data) {
        RespResult<T> result = new RespResult<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> RespResult<T> success(String message, T data) {
        RespResult<T> result = new RespResult<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> RespResult<T> error(String message) {
        RespResult<T> result = new RespResult<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static <T> RespResult<T> error(Integer code, String message) {
        RespResult<T> result = new RespResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
} 