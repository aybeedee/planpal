package com.project.myapplication;

public class Contact {
    private String name;
    private String latestChat;
    private String chatTime;
    private int profileImageResource; // Image resource ID

    // Constructors, getters, and setters for the fields
    Contact(String name1, String latestChat1, String chatTime2, int profileImageResource1){
        name=name1;
        latestChat=latestChat1;
        chatTime=chatTime2;
        profileImageResource=profileImageResource1;
    }

    void setName(String name1){
        name=name1;
    }
    String getName(){
        return name;
    }

    void setLatestChat(String latestChat1){
        latestChat=latestChat1;
    }
    String getLatestChat(){
        return latestChat;
    }

    void setChatTime(String chatTime1){
        chatTime=chatTime1;
    }
    String getChatTime(){
        return chatTime;
    }

    void setProfileImageResource(int profileImageResource1){
        profileImageResource=profileImageResource1;
    }
    int getProfileImageResource(){
        return profileImageResource;
    }
}
