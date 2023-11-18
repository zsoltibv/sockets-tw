package com.example.jettyclient;


import com.google.gson.annotations.SerializedName;

public enum MessageType {
    @SerializedName("message_to_group")
    MESSAGE_TO_GROUP("message_to_group"),

    @SerializedName("create_group")
    CREATE_GROUP("create_group"),

    @SerializedName("view_all_users")
    VIEW_ALL_USERS("view_all_users"),
    @SerializedName("view_all_groups")
    VIEW_ALL_GROUPS("view_all_groups"),
    @SerializedName("add_to_group")
    ADD_TO_GROUP("add_to_group"),

    @SerializedName("disconnect")
    DISCONNECT("disconnect"),
    @SerializedName("undefined")
    UNDEFINED("undefined");


    private String text;

    MessageType(String text) {
        this.text = text;
    }
}
