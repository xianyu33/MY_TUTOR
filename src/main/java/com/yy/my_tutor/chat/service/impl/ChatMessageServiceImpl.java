package com.yy.my_tutor.chat.service.impl;

import com.yy.my_tutor.chat.domain.ChatMessage;
import com.yy.my_tutor.chat.mapper.ChatMessageMapper;
import com.yy.my_tutor.chat.service.ChatMessageService;
import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.ParentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Resource
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

    @Override
    public Map<String, User> findByParent(String userId) {
        Map<String, User> map = new HashMap<>();

        User parent = chatMessageMapper.findParent(userId);

        List<ChatMessage> parentsChat = chatMessageMapper.findByUserId(userId);
        parent.setChatMessages(parentsChat);
        map.put("yours", parent);

        List<User> list = chatMessageMapper.findUserByParent(userId);

        for (User user : list) {
            map.put(user.getUsername(), user);
        }
        List<ChatMessage> chats = chatMessageMapper.findChatByUsers(list);
        for (ChatMessage chat : chats) {
            if (map.containsKey(chat.getUsername())) {
                User user = map.get(chat.getUsername());
                if (user.getChatMessages() == null) {
                    List<ChatMessage> messages = new ArrayList<>();
                    messages.add(chat);
                    user.setChatMessages(messages);
                } else {
                    user.getChatMessages().add(chat);
                }
            }
        }
        return map;
    }
} 