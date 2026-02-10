package com.yy.my_tutor.ark.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 火山引擎 Ark API 服务封装（使用官方SDK）
 */
@Slf4j
@Service
public class ArkAIService {

    @Value("${ark.api-key:3d10b544-6433-4493-8922-2375647a86ac}")
    private String apiKey;

    @Value("${ark.model:ep-20250421140255-d6sfx}")
    private String defaultModel;

    @Value("${ark.temperature:0.8}")
    private Double defaultTemperature;

    private ArkService arkService;

    @PostConstruct
    public void init() {
        arkService = ArkService.builder()
                .apiKey(apiKey)
                .build();
        log.info("ArkAIService initialized with model: {}", defaultModel);
    }

    @PreDestroy
    public void destroy() {
        if (arkService != null) {
            arkService.shutdownExecutor();
        }
    }

    /**
     * 同步调用 AI 生成内容
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt   用户提示词
     * @return AI 生成的内容
     */
    public String chat(String systemPrompt, String userPrompt) {
        return chat(systemPrompt, userPrompt, defaultModel, defaultTemperature);
    }

    /**
     * 同步调用 AI 生成内容（指定温度）
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt   用户提示词
     * @param temperature  温度参数（0.0-1.0），值越高结果越随机多样
     * @return AI 生成的内容
     */
    public String chat(String systemPrompt, String userPrompt, Double temperature) {
        return chat(systemPrompt, userPrompt, defaultModel, temperature);
    }

    /**
     * 同步调用 AI 生成内容（指定模型和温度）
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt   用户提示词
     * @param model        模型ID
     * @param temperature  温度参数（0.0-1.0），值越高结果越随机多样
     * @return AI 生成的内容
     */
    public String chat(String systemPrompt, String userPrompt, String model, Double temperature) {
        List<ChatMessage> messages = new ArrayList<>();

        // 添加系统提示词
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content(systemPrompt)
                    .build());
        }

        // 添加用户提示词
        messages.add(ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .content(userPrompt)
                .build());

        ChatCompletionRequest.Builder requestBuilder = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages);

        // 设置温度参数
        if (temperature != null) {
            requestBuilder.temperature(temperature);
        }

        ChatCompletionRequest request = requestBuilder.build();

        log.info("Calling Ark API with model: {}, temperature: {}, userPrompt length: {}",
                model, temperature, userPrompt.length());

        try {
            ChatCompletionResult result = arkService.createChatCompletion(request);
            String content = result.getChoices().get(0).getMessage().getContent().toString();
            log.info("Ark API response received, content length: {}", content.length());
            return content;
        } catch (Exception e) {
            log.error("Ark API call failed", e);
            throw new RuntimeException("AI 服务调用失败: " + e.getMessage(), e);
        }
    }

    /**
     * 同步调用 AI（多轮对话）
     *
     * @param messages 消息列表
     * @return AI 生成的内容
     */
    public String chat(List<ChatMessage> messages) {
        return chat(messages, defaultModel, defaultTemperature);
    }

    /**
     * 同步调用 AI（多轮对话，指定模型和温度）
     *
     * @param messages    消息列表
     * @param model       模型ID
     * @param temperature 温度参数
     * @return AI 生成的内容
     */
    public String chat(List<ChatMessage> messages, String model, Double temperature) {
        ChatCompletionRequest.Builder requestBuilder = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages);

        if (temperature != null) {
            requestBuilder.temperature(temperature);
        }

        ChatCompletionRequest request = requestBuilder.build();

        log.info("Calling Ark API with model: {}, temperature: {}, messages count: {}",
                model, temperature, messages.size());

        try {
            ChatCompletionResult result = arkService.createChatCompletion(request);
            String content = result.getChoices().get(0).getMessage().getContent().toString();
            log.info("Ark API response received, content length: {}", content.length());
            return content;
        } catch (Exception e) {
            log.error("Ark API call failed", e);
            throw new RuntimeException("AI 服务调用失败: " + e.getMessage(), e);
        }
    }

    /**
     * 流式调用 AI 生成内容
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt   用户提示词
     * @return Flux<String>，每个元素是一个 content delta
     */
    public Flux<String> streamChat(String systemPrompt, String userPrompt) {
        String url = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", defaultModel);
        requestBody.put("stream", true);

        JSONArray messages = new JSONArray();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            JSONObject sysMsg = new JSONObject();
            sysMsg.put("role", "system");
            sysMsg.put("content", systemPrompt);
            messages.add(sysMsg);
        }
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userPrompt);
        messages.add(userMsg);
        requestBody.put("messages", messages);

        if (defaultTemperature != null) {
            requestBody.put("temperature", defaultTemperature);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json"), requestBody.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();

        log.info("Calling Ark streaming API, model: {}, userPrompt length: {}", defaultModel, userPrompt.length());

        return Flux.create(fluxSink -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("Ark streaming API request failed", e);
                    fluxSink.error(new RuntimeException("AI 流式服务调用失败: " + e.getMessage(), e));
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) {
                            String errorBody = responseBody != null ? responseBody.string() : "unknown";
                            log.error("Ark streaming API response failed: {}, body: {}", response.code(), errorBody);
                            fluxSink.error(new RuntimeException("AI 流式服务响应失败: " + response.code()));
                            return;
                        }
                        if (responseBody == null) {
                            fluxSink.error(new RuntimeException("AI 流式服务响应为空"));
                            return;
                        }
                        BufferedSource source = responseBody.source();
                        while (!source.exhausted()) {
                            String line = source.readUtf8Line();
                            if (StrUtil.isBlank(line)) {
                                continue;
                            }
                            // SSE format: "data: {...}"
                            if (line.startsWith("data: ")) {
                                String data = line.substring(6).trim();
                                if ("[DONE]".equals(data)) {
                                    break;
                                }
                                try {
                                    JSONObject jsonObj = JSON.parseObject(data);
                                    JSONArray choices = jsonObj.getJSONArray("choices");
                                    if (choices != null && !choices.isEmpty()) {
                                        JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                                        if (delta != null) {
                                            String content = delta.getString("content");
                                            if (content != null && !content.isEmpty()) {
                                                fluxSink.next(content);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    log.debug("Skip non-JSON SSE line: {}", line);
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("Ark streaming API read error", e);
                        fluxSink.error(new RuntimeException("AI 流式服务读取异常: " + e.getMessage(), e));
                    } finally {
                        fluxSink.complete();
                    }
                }
            });
        }, FluxSink.OverflowStrategy.BUFFER);
    }

    /**
     * 获取当前使用的模型ID
     */
    public String getDefaultModel() {
        return defaultModel;
    }
}
