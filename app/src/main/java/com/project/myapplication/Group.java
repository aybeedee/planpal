package com.project.myapplication;

public class Group {
    String id;
    String Name;
    String description;
    String groupPhotoUrl;

    public Group(String id, String name, String description, String groupPhotoUrl) {
        this.id = id;
        Name = name;
        this.description = description;
        this.groupPhotoUrl = groupPhotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupPhotoUrl() {
        return groupPhotoUrl;
    }

    public void setGroupPhotoUrl(String groupPhotoUrl) {
        this.groupPhotoUrl = groupPhotoUrl;
    }
}
