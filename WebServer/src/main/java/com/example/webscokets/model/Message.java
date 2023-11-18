package com.example.webscokets.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Message {
    private String from;
    private String to;
    private String content;
    @SerializedName("type")
    private MessageType type = MessageType.MESSAGE_TO_GROUP; /* Default value */
}