package com.yy.my_tutor.ark.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.my_tutor.ark.domain.ArkClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ArkClientController {
    private final String API_KEY = "3d10b544-6433-4493-8922-2375647a86ac";

    private final String  picHost = "http://60.205.171.41:18888";

    private final String picDir = "/ark/file/";




    @PostMapping(value = "/chat", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody ArkClient arkClient) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + API_KEY);

        arkClient.setModel("bot-20250414102731-lwpsh");

        arkClient.setStream(true);

        JSONObject stream_options = new JSONObject();
        stream_options.put("include_usage", true);
        arkClient.setStream_options(stream_options);



        String url = "https://ark.cn-beijing.volces.com/api/v3/bots/chat/completions";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(arkClient));
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();
        log.info("调用请求:{},接口入参：{},请求头:{}", url, JSON.toJSONString(arkClient), JSON.toJSONString(headers));
        return Flux.create(fluxSink -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //处理请求失败情况
                    log.error("*********流式请求人工机器人接口请求失败", e);
                    //fluxSink.error(e);
//                        fluxSink.next();
                    fluxSink.complete();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (!response.isSuccessful()) {
                        fluxSink.error(new RuntimeException("响应失败:"+response));
                        log.error("*********流式请求人工机器人接口响应失败:{}",response);
                        fluxSink.next("请求失败");
                        return;
                    }
                    try(ResponseBody responseBody = response.body()) {
                        if (ObjectUtil.isNotEmpty(responseBody)) {
                            BufferedSource source = responseBody.source();
                            while (!source.exhausted()) {
                                String line = source.readUtf8Line();
                                if (StrUtil.isNotBlank(line)) {
                                    log.info("==============获取到的流信息结果为：{}",line);
                                    fluxSink.next(line);
                                }
                            }
                        } else {
                            log.info("~~~~~~~~流式请求人工机器人接口响应失败:ResponseBody为空");
                            fluxSink.error(new RuntimeException("请求失败:ResponseBody为空"));
                            fluxSink.next("请求失败");
                        }
                    } catch (IOException e){
                        log.error("~~~~~~~~流式请求人工机器人异常：",e);
                        //fluxSink.error(e);
//                            fluxSink.next(joinErrMsg);
                    } finally {
                        fluxSink.complete();
                    }
                }
            });
        }, FluxSink.OverflowStrategy.BUFFER);
    }
}
