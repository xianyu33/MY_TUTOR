package com.yy.my_tutor.chat.service.impl;

import com.yy.my_tutor.chat.domain.ChatMessage;
import com.yy.my_tutor.chat.mapper.ChatMessageMapper;
import com.yy.my_tutor.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public boolean addChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getCreateTime() == null) {
            chatMessage.setCreateTime(new Date());
        }
        return chatMessageMapper.insert(chatMessage) > 0;
    }

    @Override
    public ChatMessage findById(Integer id) {
        return chatMessageMapper.findById(id);
    }

    @Override
    public List<ChatMessage> findByConversationId(String conversationId) {
        return chatMessageMapper.findByConversationId(conversationId);
    }

    @Override
    public List<ChatMessage> findByUserId(String userId) {
        return chatMessageMapper.findByUserId(userId);
    }

    @Override
    public boolean updateChatMessage(ChatMessage chatMessage) {
        return chatMessageMapper.update(chatMessage) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return chatMessageMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteByConversationId(String conversationId) {
        return chatMessageMapper.deleteByConversationId(conversationId) > 0;
    }

    @Override
    public List<ChatMessage> findAll() {
        return chatMessageMapper.findAll();
    }
} 