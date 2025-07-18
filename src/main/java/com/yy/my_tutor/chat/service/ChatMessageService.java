package com.yy.my_tutor.chat.service;

import com.alibaba.fastjson.JSONObject;
import com.yy.my_tutor.chat.domain.ChatMessage;
import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.domain.User;

import java.util.List;
import java.util.Map;

public interface ChatMessageService {

    /**
     * 新增聊天消息
     * @param chatMessage 聊天消息
     * @return 是否成功
     */
    boolean addChatMessage(ChatMessage chatMessage);

    /**
     * 根据ID查询聊天消息
     * @param id 消息ID
     * @return 聊天消息
     */
    ChatMessage findById(Integer id);

    /**
     * 根据对话ID查询聊天消息列表
     * @param conversationId 对话ID
     * @return 聊天消息列表
     */
    List<ChatMessage> findByConversationId(String conversationId);

    /**
     * 根据用户ID查询聊天消息列表
     * @param userId 用户ID
     * @return 聊天消息列表
     */
    List<ChatMessage> findByUserId(String userId);

    /**
     * 更新聊天消息
     * @param chatMessage 聊天消息
     * @return 是否成功
     */
    boolean updateChatMessage(ChatMessage chatMessage);

    /**
     * 根据ID删除聊天消息
     * @param id 消息ID
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 根据对话ID删除聊天消息
     * @param conversationId 对话ID
     * @return 是否成功
     */
    boolean deleteByConversationId(String conversationId);

    /**
     * 查询所有聊天消息
     * @return 聊天消息列表
     */
    List<ChatMessage> findAll();

    Map<String, User> findByParent(String userAccount);
}
