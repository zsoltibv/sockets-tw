package com.example.webscokets.dbRunTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.Session;

@Data
public class User {
    private String name;
    private Session session;
}
