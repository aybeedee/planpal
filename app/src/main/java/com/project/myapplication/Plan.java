package com.project.myapplication;

public class Plan {

    String id;
    String name;
    String location;
    String date;
    String startTime;
    String endTime;
    Integer attendingCount;
    Integer notAttendingCount;

    public Plan() {}

    public Plan(String id, String name, String location, String date, String startTime, String endTime, Integer attendingCount, Integer notAttendingCount) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendingCount = attendingCount;
        this.notAttendingCount = notAttendingCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(Integer attendingCount) {
        this.attendingCount = attendingCount;
    }

    public Integer getNotAttendingCount() {
        return notAttendingCount;
    }

    public void setNotAttendingCount(Integer notAttendingCount) {
        this.notAttendingCount = notAttendingCount;
    }
}
