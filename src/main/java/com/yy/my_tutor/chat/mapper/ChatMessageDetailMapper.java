package com.yy.my_tutor.chat.mapper;

import com.yy.my_tutor.chat.domain.ChatMessageDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageDetailMapper {
    
    /**
     * 插入聊天消息详情
     * @param chatMessageDetail 聊天消息详情
     * @return 影响行数
     */
    int insert(ChatMessageDetail chatMessageDetail);
    
    /**
     * 根据ID查询聊天消息详情
     * @param id 详情ID
     * @return 聊天消息详情
     */
    ChatMessageDetail findById(Integer id);
    
    /**
     * 根据消息ID查询聊天消息详情列表
     * @param messageId 消息ID
     * @return 聊天消息详情列表
     */
    List<ChatMessageDetail> findByMessageId(String messageId);
    
    /**
     * 根据对话ID查询聊天消息详情列表
     * @param conversationId 对话ID
     * @return 聊天消息详情列表
     */
    List<ChatMessageDetail> findByConversationId(String conversationId);
    
    /**
     * 根据用户ID查询聊天消息详情列表
     * @param userId 用户ID
     * @return 聊天消息详情列表
     */
    List<ChatMessageDetail> findByUserId(String userId);
    
    /**
     * 根据对话ID和排序查询聊天消息详情列表
     * @param conversationId 对话ID
     * @param sort 排序
     * @return 聊天消息详情列表
     */
    List<ChatMessageDetail> findByConversationIdAndSort(@Param("conversationId") String conversationId, @Param("sort") Integer sort);
    
    /**
     * 更新聊天消息详情
     * @param chatMessageDetail 聊天消息详情
     * @return 影响行数
     */
    int update(ChatMessageDetail chatMessageDetail);
    
    /**
     * 根据ID删除聊天消息详情
     * @param id 详情ID
     * @return 影响行数
     */
    int deleteById(Integer id);
    
    /**
     * 根据消息ID删除聊天消息详情
     * @param messageId 消息ID
     * @return 影响行数
     */
    int deleteByMessageId(String messageId);
    
    /**
     * 根据对话ID删除聊天消息详情
     * @param conversationId 对话ID
     * @return 影响行数
     */
    int deleteByConversationId(String conversationId);
    
    /**
     * 查询所有聊天消息详情
     * @return 聊天消息详情列表
     */
    List<ChatMessageDetail> findAll();
} 