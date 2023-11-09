package com.project.myapplication;

// FriendRequestModel.java

public class FriendRequestModel {
    private int userId;  // Unique identifier for the user
    private String userName;
    private String userEmail;
    private String userProfileImage;  // URL to the profile image

    public FriendRequestModel(int userId, String userName, String userEmail, String userProfileImage) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userProfileImage = userProfileImage;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }
}

