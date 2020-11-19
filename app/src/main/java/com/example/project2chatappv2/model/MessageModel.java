package com.example.project2chatappv2.model;



public class MessageModel {
    private String sender;
    private String receiver;
    private String message;
    private Long time;

    public MessageModel() {
    }

    public MessageModel(String sender, String receiver, String message, Long time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
