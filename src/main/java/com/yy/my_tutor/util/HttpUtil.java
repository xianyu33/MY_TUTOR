package com.yy.my_tutor.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtil {


    /**
     * POST请求，json格式可使用该公共方法
     * @param paramJsonStr 请求体json字符串
     * @param url 请求url
     * @return
     */
    public static Object postHttp(String paramJsonStr, String url, String token) {

        Map<String, String> headers = new HashMap<>();
        log.info("token:{}", token);
        if (StrUtil.isNotBlank(token)) {
            headers.put("Authorization", token);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();


        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json"), paramJsonStr);
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();
        log.info("调用请求:{},接口入参：{},请求头:{}", url, paramJsonStr, JSON.toJSONString(headers));
        try {
            Response response = client.newCall(request).execute();
            log.info(JSON.toJSONString(response));
            return response.isSuccessful() ? JSON.toJSON(response.body().string()) : null;
        } catch (Exception e) {
            log.error("请求失败", e);
        }
        return null;
    }

}
