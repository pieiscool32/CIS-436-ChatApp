package com.example.cmunte.chatapp;

import java.util.Date;

public class Message {
    public String username;
    public String message;
    public String timestamp;

    public Message() {}

    public Message(String username, String message, Date timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp.toString();
    }
}
