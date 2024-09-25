package com.example.funfitnessblender.models;

public class Session {
    private String sessionId;
    private String clientId;
    private String program;
    private String sessionDate;
    private String status;

    // Constructor with all fields
    public Session(String sessionId, String clientId, String program, String sessionDate, String status) {
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.program = program;
        this.sessionDate = sessionDate;
        this.status = status;
    }



    // Empty constructor for Firebase or serialization
    public Session() {}

    // Getters
    public String getSessionId() {
        return sessionId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getProgram() {
        return program;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
