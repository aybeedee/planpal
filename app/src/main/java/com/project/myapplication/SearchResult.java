package com.project.myapplication;

public class SearchResult {
    private String name;
    private int profileImageResource;

    public SearchResult(String name, int profileImageResource) {
        this.name = name;
        this.profileImageResource = profileImageResource;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setProfileImageResource(int profileImageResource){
        this.profileImageResource=profileImageResource;
    }

    public String getName() {
        return name;
    }

    public int getProfileImageResource() {
        return profileImageResource;
    }
}

