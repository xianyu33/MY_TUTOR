package com.yy.my_tutor.chat.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.chat.domain.ChatMessageDetail;
import com.yy.my_tutor.chat.service.ChatMessageDetailService;
import com.yy.my_tutor.common.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat/message-detail")
public class ChatMessageDetailController {

    @Autowired
    private ChatMessageDetailService chatMessageDetailService;

    /**
     * 新增聊天消息详情
     */
    @PostMapping("/add")
    public RespResult<Boolean> addChatMessageDetail(@RequestBody ChatMessageDetail chatMessageDetail) {
        log.info("新增聊天消息详情: {}", JSON.toJSONString(chatMessageDetail));
        boolean result = chatMessageDetailService.addChatMessageDetail(chatMessageDetail);
        if (result) {
            return RespResult.success("新增成功", true);
        }
        return RespResult.error("新增失败");
    }

    /**
     * 根据ID查询聊天消息详情
     */
    @GetMapping("/{id}")
    public RespResult<ChatMessageDetail> findById(@PathVariable Integer id) {
        log.info("查询聊天消息详情: {}", id);
        ChatMessageDetail chatMessageDetail = chatMessageDetailService.findById(id);
        if (chatMessageDetail != null) {
            return RespResult.success(chatMessageDetail);
        }
        return RespResult.error("聊天消息详情不存在");
    }

    /**
     * 根据消息ID查询聊天消息详情列表
     */
    @GetMapping("/message/{messageId}")
    public RespResult<List<ChatMessageDetail>> findByMessageId(@PathVariable String messageId) {
        log.info("查询消息详情: {}", messageId);
        List<ChatMessageDetail> chatMessageDetails = chatMessageDetailService.findByMessageId(messageId);
        return RespResult.success(chatMessageDetails);
    }

    /**
     * 根据对话ID查询聊天消息详情列表
     */
    @GetMapping("/conversation/{conversationId}")
    public RespResult<List<ChatMessageDetail>> findByConversationId(@PathVariable String conversationId) {
        log.info("查询对话详情: {}", conversationId);
        List<ChatMessageDetail> chatMessageDetails = chatMessageDetailService.findByConversationId(conversationId);
        return RespResult.success(chatMessageDetails);
    }

    /**
     * 根据用户ID查询聊天消息详情列表
     */
    @GetMapping("/user/{userId}")
    public RespResult<List<ChatMessageDetail>> findByUserId(@PathVariable String userId) {
        log.info("查询用户详情: {}", userId);
        List<ChatMessageDetail> chatMessageDetails = chatMessageDetailService.findByUserId(userId);
        return RespResult.success(chatMessageDetails);
    }

    /**
     * 根据对话ID和排序查询聊天消息详情列表
     */
    @GetMapping("/conversation/{conversationId}/sort/{sort}")
    public RespResult<List<ChatMessageDetail>> findByConversationIdAndSort(@PathVariable String conversationId, @PathVariable Integer sort) {
        log.info("查询对话详情排序: conversationId={}, sort={}", conversationId, sort);
        List<ChatMessageDetail> chatMessageDetails = chatMessageDetailService.findByConversationIdAndSort(conversationId, sort);
        return RespResult.success(chatMessageDetails);
    }

    /**
     * 更新聊天消息详情
     */
    @PutMapping("/update")
    public RespResult<Boolean> updateChatMessageDetail(@RequestBody ChatMessageDetail chatMessageDetail) {
        log.info("更新聊天消息详情: {}", JSON.toJSONString(chatMessageDetail));
        boolean result = chatMessageDetailService.updateChatMessageDetail(chatMessageDetail);
        if (result) {
            return RespResult.success("更新成功", true);
        }
        return RespResult.error("更新失败");
    }

    /**
     * 根据ID删除聊天消息详情
     */
    @DeleteMapping("/{id}")
    public RespResult<Boolean> deleteById(@PathVariable Integer id) {
        log.info("删除聊天消息详情: {}", id);
        boolean result = chatMessageDetailService.deleteById(id);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 根据消息ID删除聊天消息详情
     */
    @DeleteMapping("/message/{messageId}")
    public RespResult<Boolean> deleteByMessageId(@PathVariable String messageId) {
        log.info("删除消息详情: {}", messageId);
        boolean result = chatMessageDetailService.deleteByMessageId(messageId);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 根据对话ID删除聊天消息详情
     */
    @DeleteMapping("/conversation/{conversationId}")
    public RespResult<Boolean> deleteByConversationId(@PathVariable String conversationId) {
        log.info("删除对话详情: {}", conversationId);
        boolean result = chatMessageDetailService.deleteByConversationId(conversationId);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 查询所有聊天消息详情
     */
    @GetMapping("/list")
    public RespResult<List<ChatMessageDetail>> findAll() {
        log.info("查询所有聊天消息详情");
        List<ChatMessageDetail> chatMessageDetails = chatMessageDetailService.findAll();
        return RespResult.success(chatMessageDetails);
    }
} 