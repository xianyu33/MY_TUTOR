package com.yy.my_tutor.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDetail implements Serializable {
    private Integer id;
    private String userId;
    private Integer sort;
    private String type;
    private String context;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String conversationId;
    private String messageId;
} 