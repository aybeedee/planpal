package com.project.myapplication;

public class selectedmember {
    private String name;
    private int profileImageResource;

    public selectedmember(String name, int profileImageResource) {
        this.name = name;
        this.profileImageResource = profileImageResource;
    }

    public String getName() {
        return name;
    }

    public int getProfileImageResource() {
        return profileImageResource;
    }
}
