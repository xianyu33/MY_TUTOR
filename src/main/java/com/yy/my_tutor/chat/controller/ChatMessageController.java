package com.yy.my_tutor.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.my_tutor.chat.domain.ChatMessage;
import com.yy.my_tutor.chat.service.ChatMessageService;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chat/message")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 新增聊天消息
     */
    @PostMapping("/add")
    public RespResult<Boolean> addChatMessage(@RequestBody ChatMessage chatMessage) {
        log.info("新增聊天消息: {}", JSON.toJSONString(chatMessage));
        boolean result = chatMessageService.addChatMessage(chatMessage);
        if (result) {
            return RespResult.success("新增成功", true);
        }
        return RespResult.error("新增失败");
    }

    /**
     * 根据ID查询聊天消息
     */
    @GetMapping("/{id}")
    public RespResult<ChatMessage> findById(@PathVariable Integer id) {
        log.info("查询聊天消息: {}", id);
        ChatMessage chatMessage = chatMessageService.findById(id);
        if (chatMessage != null) {
            return RespResult.success(chatMessage);
        }
        return RespResult.error("聊天消息不存在");
    }

    /**
     * 根据对话ID查询聊天消息列表
     */
    @GetMapping("/conversation/{conversationId}")
    public RespResult<List<ChatMessage>> findByConversationId(@PathVariable String conversationId) {
        log.info("查询对话消息: {}", conversationId);
        List<ChatMessage> chatMessages = chatMessageService.findByConversationId(conversationId);
        return RespResult.success(chatMessages);
    }

    /**
     * 根据用户ID查询聊天消息列表
     */
    @GetMapping("/user/{userId}")
    public RespResult<List<ChatMessage>> findByUserId(@PathVariable String userId) {
        log.info("查询用户消息: {}", userId);
        List<ChatMessage> chatMessages = chatMessageService.findByUserId(userId);
        return RespResult.success(chatMessages);
    }

    /**
     * 更新聊天消息
     */
    @PutMapping("/update")
    public RespResult<Boolean> updateChatMessage(@RequestBody ChatMessage chatMessage) {
        log.info("更新聊天消息: {}", JSON.toJSONString(chatMessage));
        boolean result = chatMessageService.updateChatMessage(chatMessage);
        if (result) {
            return RespResult.success("更新成功", true);
        }
        return RespResult.error("更新失败");
    }

    /**
     * 根据ID删除聊天消息
     */
    @DeleteMapping("/{id}")
    public RespResult<Boolean> deleteById(@PathVariable Integer id) {
        log.info("删除聊天消息: {}", id);
        boolean result = chatMessageService.deleteById(id);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 根据对话ID删除聊天消息
     */
    @DeleteMapping("/conversation/{conversationId}")
    public RespResult<Boolean> deleteByConversationId(@PathVariable String conversationId) {
        log.info("删除对话消息: {}", conversationId);
        boolean result = chatMessageService.deleteByConversationId(conversationId);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 查询所有聊天消息
     */
    @GetMapping("/list")
    public RespResult<List<ChatMessage>> findAll() {
        log.info("查询所有聊天消息");
        List<ChatMessage> chatMessages = chatMessageService.findAll();
        return RespResult.success(chatMessages);
    }

    /**
     * 根据家长查询想学生对话记录
     */
    @GetMapping("/parent/{userAccount}")
    public RespResult<Map<String, User>> findByParent(@PathVariable String userAccount) {
        log.info("查询家长账号: {}", userAccount);
        Map<String, User> byParent = chatMessageService.findByParent(userAccount);
        return RespResult.data(byParent);
    }
}
