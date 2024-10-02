package com.example.funfitnessblender.models;

import java.util.HashMap;
import java.util.Map;

public class Marketing {
    private String marketingId;
    private String month;
    private String strategy;
    private String expense;
    private String responseDetails;

    private static final Map<String, Integer> monthMap = new HashMap<>();

    static {
        monthMap.put("January", 1);
        monthMap.put("February", 2);
        monthMap.put("March", 3);
        monthMap.put("April", 4);
        monthMap.put("May", 5);
        monthMap.put("June", 6);
        monthMap.put("July", 7);
        monthMap.put("August", 8);
        monthMap.put("September", 9);
        monthMap.put("October", 10);
        monthMap.put("November", 11);
        monthMap.put("December", 12);
    }

    // Default constructor required for calls to DataSnapshot.getValue(Marketing.class)
    public Marketing() {
    }

    public Marketing(String marketingId, String month, String strategy, String expense, String responseDetails) {
        this.marketingId = marketingId;
        this.month = month;
        this.strategy = strategy;
        this.expense = expense;
        this.responseDetails = responseDetails;
    }

    // Getters and Setters
    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    // Get month as a number (1 for January, 2 for February, etc.)
    public int getMonthAsNumber() {
        return monthMap.getOrDefault(month, 0); // default to 0 if not found
    }
}
