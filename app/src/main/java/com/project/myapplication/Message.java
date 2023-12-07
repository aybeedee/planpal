package com.project.myapplication;

public class Message {

    String messageId;
    String text;
    String imageUrl;
    Long timestamp;
    String senderId;

    public Message() {}

    public Message(String messageId, String text, String imageUrl, Long timestamp, String senderId) {
        this.messageId = messageId;
        this.text = text;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
