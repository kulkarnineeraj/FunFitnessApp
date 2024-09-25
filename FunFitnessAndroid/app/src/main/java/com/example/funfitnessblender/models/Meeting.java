package com.example.funfitnessblender.models;

public class Meeting {
    private String meetingId;
    private String date;
    private String personName;
    private String company;
    private String motive;
    private String result;
    private String status;
    private String potential;

    // Default constructor required for calls to DataSnapshot.getValue(Meeting.class)
    public Meeting() {}

    public Meeting(String meetingId, String date, String personName, String company, String motive, String result, String status, String potential) {
        this.meetingId = meetingId;
        this.date = date;
        this.personName = personName;
        this.company = company;
        this.motive = motive;
        this.result = result;
        this.status = status;
        this.potential = potential;
    }

    // Getters and setters
    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPotential() {
        return potential;
    }

    public void setPotential(String potential) {
        this.potential = potential;
    }
}

