package com.yy.my_tutor.ark.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;
import com.yy.my_tutor.ark.domain.ArkClient;
import com.yy.my_tutor.ark.domain.Context;
import com.yy.my_tutor.chat.domain.ChatMessage;
import com.yy.my_tutor.chat.domain.ChatMessageDetail;
import com.yy.my_tutor.chat.service.ChatMessageDetailService;
import com.yy.my_tutor.chat.service.ChatMessageService;
import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ArkClientController {
    private final String API_KEY = "3d10b544-6433-4493-8922-2375647a86ac";

    private final String MODEL = "ep-20250421140255-d6sfx";

    @Resource
    ChatMessageService chatMessageService;

    @Resource
    ChatMessageDetailService chatMessageDetailService;

    @PostMapping(value = "/context")
    public Object context(@RequestBody ArkClient arkClient) {

        JSONArray messages = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "你是一位智能AI老师，能够解答小学到高中的任何学科问题，并对难点进行剖析，同时支持中英文切换。根据以下规则一步步执行任务：1. 首先确定用户提问的学科领域以及所在学段（小学或高中）。2. 针对用户的问题，给出准确清晰的解答内容。3. 分析问题中的难点部分，并进行详细的剖析讲解。4. 如果用户有中英文切换需求，按照用户要求的语言进行回复;5.同时你也可以回答常识性问题;6.用户未指定时，使用英文回复");
        jsonObject.put("role", "system");
        messages.add(jsonObject);

//        arkClient.setMessages(messages);
//        arkClient.setModel(MODEL);
//        arkClient.setMode("session");
//        arkClient.setTtl(3600);
//
//        JSONObject truncation_strategy = new JSONObject();
//        truncation_strategy.put("type", "rolling_tokens");
//        arkClient.setTruncation_strategy(truncation_strategy);

//        String url = "https://ark.cn-beijing.volces.com/api/v3/context/create";
//        HttpUtil.postHttp(JSON.toJSONString(arkClient), url, "Bearer " + API_KEY);
        //创建对话id
        ChatMessage chatMessage = new ChatMessage();
        String conversationId = UUID.randomUUID().toString();
        chatMessage.setConversationId(conversationId);
        chatMessage.setUserId(arkClient.getUser_id());
        chatMessage.setTitle(arkClient.getQuestion());
        chatMessageService.addChatMessage(chatMessage);
        return RespResult.data(conversationId);
    }


    public static void main(String[] args) {
        System.out.println(AESUtil.decryptBase64("CH+8oJbnh+IfILiWQWY5OpfP5u2cbBEKWWVBg2bJ+NcLRPh3t8vxgQE2T1M2RRNzH9Sue8RgS4i14VES15YQM/9PlSxiWa0PJ8tI5B2i4iXl4gDYOswtWgxQGVuNCY5RYCQcGz75XoYSpC/Eybcbv9iWYoySa70ngahTd1VkjqNgseWTUji3E0vvnb/fiMjXN4smy0GMdOHa96xtLgK+4Cxcb+E/0bDpd/BsdnN0VDQ="));
    }

    @PostMapping(value = "/chat", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody ArkClient arkClient) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + API_KEY);

        arkClient.setModel("bot-20250414102731-lwpsh");

        arkClient.setStream(true);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "你是一位智能AI老师，能够解答小学到高中的任何学科问题，并对难点进行剖析，同时支持中英文切换。根据以下规则一步步执行任务：1. 首先确定用户提问的学科领域以及所在学段（小学或高中）。2. 针对用户的问题，给出准确清晰的解答内容。3. 分析问题中的难点部分，并进行详细的剖析讲解。4. 如果用户有中英文切换需求，按照用户要求的语言进行回复;5.同时你也可以回答常识性问题;6.用户未指定时，根据提问的语言，使用对应的语言回答");
        jsonObject.put("role", "system");

        arkClient.getMessages().add(jsonObject);
        JSONObject stream_options = new JSONObject();
        stream_options.put("include_usage", true);
        arkClient.setStream_options(stream_options);


        JSONArray messages = arkClient.getMessages();
        messages.forEach(item -> {
            JSONObject jsonObject1 = JSON.parseObject(JSON.toJSONString(item));
            String role = jsonObject1.getString("role");
            if (role.equals("user")) {
                String question = jsonObject1.getString("content");

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setTitle(question);
                if (StrUtil.isBlank(arkClient.getConversation_id())) {
                    String conversationId = UUID.randomUUID().toString();
                    arkClient.setConversation_id(conversationId);
                    chatMessage.setConversationId(conversationId);
                    chatMessage.setUserId(arkClient.getUser_id());
                    chatMessageService.addChatMessage(chatMessage);
                } else {

                }
                //保存
                ChatMessageDetail chatMessageDetail = new ChatMessageDetail();
                chatMessageDetail.setConversationId(arkClient.getConversation_id());
                chatMessageDetail.setType("q");
                chatMessageDetail.setContext(question);
                chatMessageDetail.setUserId(arkClient.getUser_id());
                chatMessageDetailService.addChatMessageDetail(chatMessageDetail);
            }
        });

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
                            StringBuilder responseStr = new StringBuilder();
                            while (!source.exhausted()) {
                                String line = source.readUtf8Line();
                                if (StrUtil.isNotBlank(line)) {
                                    log.info("==============获取到的流信息结果为：{}",line);
                                    responseStr.append(line);
                                    fluxSink.next(line);
                                }
                            }
                            //保存回答
                            //保存
                            ChatMessageDetail chatMessageDetail = new ChatMessageDetail();
                            chatMessageDetail.setConversationId(arkClient.getConversation_id());
                            chatMessageDetail.setType("a");
                            chatMessageDetail.setContext(responseStr.toString());
                            chatMessageDetail.setUserId(arkClient.getUser_id());
                            chatMessageDetailService.addChatMessageDetail(chatMessageDetail);
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


    @PostMapping("/upload")
    public RespResult<String> upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        String endpoint = "tos-cn-shanghai.volces.com";
        String region = "cn-shanghai";
        String accessKey = "AKLTMjdmZDRlZmU4YmQxNDZhZDlhYjkyMmJlYzMwNmFlNjc";
        String secretKey = "TkRrMk1qVmxaR1ptTnpnME5HSm1PRGhsT1dKak9UWmlORGd5WTJRMVptWQ==";
        long expires = 3600;

        String bucketName = "test-yy";
        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        //创建临时文件
        String property = System.getProperty("user.dir");
        String filePath = property + File.separator + originalFilename;
        File newFile = new File(filePath);
        file.transferTo(newFile);
        try(FileInputStream inputStream = new FileInputStream(filePath)){
            PutObjectInput putObjectInput = new PutObjectInput().setBucket(bucketName).setKey(originalFilename)
                    .setContent(inputStream).setContentLength(newFile.length());
            PutObjectOutput output = tos.putObject(putObjectInput);
            System.out.println(JSON.toJSONString(output));
            System.out.println("putObject succeed, object's etag is " + output.getEtag());
            System.out.println("putObject succeed, object's crc64 is " + output.getHashCrc64ecma());
            //删除
            PreSignedURLInput input = new PreSignedURLInput().setBucket(bucketName).setKey(originalFilename)
                    .setHttpMethod(HttpMethod.GET).setExpires(expires);
            PreSignedURLOutput outputUrl = tos.preSignedURL(input);
            log.info(JSON.toJSONString(outputUrl));
            return RespResult.success("上传成功",outputUrl.getSignedUrl());

        } catch (IOException e) {
            System.out.println("putObject read file failed");
            e.printStackTrace();
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        } finally {
            newFile.delete();
        }
        return null;
    }

}
