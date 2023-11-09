package com.project.myapplication;


public class Friend {
    private String name;
    private String email;
    private int profileImageResId;

    public Friend(String name, String email, int profileImageResId) {
        this.name = name;
        this.email = email;
        this.profileImageResId = profileImageResId;
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail() {
        return email;
    }

    public void setProfileImageResId(int profileImageResId){
        this.profileImageResId=profileImageResId;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }
}

