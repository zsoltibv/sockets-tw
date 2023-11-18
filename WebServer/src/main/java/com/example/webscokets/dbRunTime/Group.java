package com.example.webscokets.dbRunTime;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Group {
    private String name;
    private List<User> users = new ArrayList<>();
}
