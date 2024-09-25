package com.example.funfitnessblender.models;

public class Area {
    private String id;
    private String areaName;

    // Default constructor required for calls to DataSnapshot.getValue(Program.class)
    public Area() {
    }

    public Area(String id, String areaName) {
        this.id = id;
        this.areaName = areaName;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for areaName
    public String getAreaName() {
        return areaName;
    }

    // Setter for areaName
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
