package com.yy.my_tutor.chat.service.impl;

import com.yy.my_tutor.chat.domain.ChatMessageDetail;
import com.yy.my_tutor.chat.mapper.ChatMessageDetailMapper;
import com.yy.my_tutor.chat.service.ChatMessageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatMessageDetailServiceImpl implements ChatMessageDetailService {

    @Autowired
    private ChatMessageDetailMapper chatMessageDetailMapper;

    @Override
    public boolean addChatMessageDetail(ChatMessageDetail chatMessageDetail) {
        if (chatMessageDetail.getCreateTime() == null) {
            chatMessageDetail.setCreateTime(new Date());
        }
        return chatMessageDetailMapper.insert(chatMessageDetail) > 0;
    }

    @Override
    public ChatMessageDetail findById(Integer id) {
        return chatMessageDetailMapper.findById(id);
    }

    @Override
    public List<ChatMessageDetail> findByMessageId(String messageId) {
        return chatMessageDetailMapper.findByMessageId(messageId);
    }

    @Override
    public List<ChatMessageDetail> findByConversationId(String conversationId) {
        return chatMessageDetailMapper.findByConversationId(conversationId);
    }

    @Override
    public List<ChatMessageDetail> findByUserId(String userId) {
        return chatMessageDetailMapper.findByUserId(userId);
    }

    @Override
    public List<ChatMessageDetail> findByConversationIdAndSort(String conversationId, Integer sort) {
        return chatMessageDetailMapper.findByConversationIdAndSort(conversationId, sort);
    }

    @Override
    public boolean updateChatMessageDetail(ChatMessageDetail chatMessageDetail) {
        return chatMessageDetailMapper.update(chatMessageDetail) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return chatMessageDetailMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteByMessageId(String messageId) {
        return chatMessageDetailMapper.deleteByMessageId(messageId) > 0;
    }

    @Override
    public boolean deleteByConversationId(String conversationId) {
        return chatMessageDetailMapper.deleteByConversationId(conversationId) > 0;
    }

    @Override
    public List<ChatMessageDetail> findAll() {
        return chatMessageDetailMapper.findAll();
    }
} 