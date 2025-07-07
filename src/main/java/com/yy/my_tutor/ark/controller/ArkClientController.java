package com.yy.my_tutor.ark.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yy.my_tutor.ark.domain.ArkClient;
import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.util.HttpUtil;
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

    private final String MODEL = "ep-20250421140255-d6sfx";


    @PostMapping(value = "/context")
    public Object context(@RequestBody ArkClient arkClient) {

        JSONArray messages = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "你是一位智能AI老师，能够解答小学到高中的任何学科问题，并对难点进行剖析，同时支持中英文切换。根据以下规则一步步执行任务：1. 首先确定用户提问的学科领域以及所在学段（小学或高中）。2. 针对用户的问题，给出准确清晰的解答内容。3. 分析问题中的难点部分，并进行详细的剖析讲解。4. 如果用户有中英文切换需求，按照用户要求的语言进行回复;5.同时你也可以回答常识性问题;6.用户未指定时，使用英文回复");
        jsonObject.put("role", "system");
        messages.add(jsonObject);

        arkClient.setMessages(messages);
        arkClient.setModel(MODEL);
        arkClient.setMode("session");
        arkClient.setTtl(3600);

        JSONObject truncation_strategy = new JSONObject();
        truncation_strategy.put("type", "rolling_tokens");
        arkClient.setTruncation_strategy(truncation_strategy);

        String url = "https://ark.cn-beijing.volces.com/api/v3/context/create";
        return HttpUtil.postHttp(JSON.toJSONString(arkClient), url, "Bearer " + API_KEY);
    }


    @PostMapping(value = "/chat", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody ArkClient arkClient) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + API_KEY);

        arkClient.setModel("bot-20250414102731-lwpsh");

        arkClient.setStream(true);

        JSONObject message = new JSONObject();
        message.put("role", "system");
        message.put("content", "英文回答");
        arkClient.getMessages().add(message);

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

    @PostMapping(value = "/context/chat", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> contextChat(@RequestBody ArkClient arkClient) {
        log.info("上下文对话入参:{}", JSON.toJSONString(arkClient));
        if (null != arkClient.getEnc_param()) {
            String decryptedBody = AESUtil.decryptBase64(arkClient.getEnc_param());
            arkClient = JSON.parseObject(decryptedBody, ArkClient.class);
            log.info("解析后的入参:{}", JSON.toJSONString(arkClient));
            //获取问题

        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + API_KEY);

        JSONObject message = new JSONObject();
        message.put("role", "system");
        message.put("content", "英文回答");
        arkClient.getMessages().add(message);


        arkClient.setModel(MODEL);
        arkClient.setStream(true);

        String url = "https://ark.cn-beijing.volces.com/api/v3/context/chat/completions";
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
                            StringBuilder responseStr = new StringBuilder();
                            while (!source.exhausted()) {
                                String line = source.readUtf8Line();
                                if (StrUtil.isNotBlank(line)) {
                                    log.info("==============获取到的流信息结果为：{}",line);
                                    responseStr.append(line);
                                    fluxSink.next(line);
                                }
                            }
                            //入库
                            if (StrUtil.isNotBlank(responseStr)) {

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
