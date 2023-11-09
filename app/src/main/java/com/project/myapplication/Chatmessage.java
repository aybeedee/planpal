package com.project.myapplication;



public class Chatmessage {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;


    private String sender;
    private String recipient;
    private String text;
    private long timestamp;

    private String type;

    // Constructors, getters, and setters

    // Default constructor (required for Firebase)
    public Chatmessage() {
    }
    public Chatmessage(String sender, String recipient, String text, long timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Chatmessage(String sender, String recipient, String text, long timestamp, String type) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.timestamp = timestamp;
        this.type = type;
    }



    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }
    public void setType(String Type) {
        this.type = Type;
    }



}

