package com.yy.my_tutor.chat.mapper;

import com.yy.my_tutor.chat.domain.ChatMessage;
import com.yy.my_tutor.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    
    /**
     * 插入聊天消息
     * @param chatMessage 聊天消息
     * @return 影响行数
     */
    int insert(ChatMessage chatMessage);
    
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
     * @return 影响行数
     */
    int update(ChatMessage chatMessage);
    
    /**
     * 根据ID删除聊天消息
     * @param id 消息ID
     * @return 影响行数
     */
    int deleteById(Integer id);
    
    /**
     * 根据对话ID删除聊天消息
     * @param conversationId 对话ID
     * @return 影响行数
     */
    int deleteByConversationId(String conversationId);
    
    /**
     * 查询所有聊天消息
     * @return 聊天消息列表
     */
    List<ChatMessage> findAll();

    List<User> findUserByParent(String id);

    List<ChatMessage> findChatByUsers(List<User> list);

    User findParent(String userId);
}