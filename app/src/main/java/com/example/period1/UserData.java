// UserData.java
package com.example.period1;

public class UserData {
    private String name;
    private int periodDuration;
    private String lastPeriodDate;
    private int weight;
    private int height;
    private int cycleWindow;

    public UserData(String name, int periodDuration, String lastPeriodDate, int weight, int height, int cycleWindow) {
        this.name = name;
        this.periodDuration = periodDuration;
        this.lastPeriodDate = lastPeriodDate;
        this.weight = weight;
        this.height = height;
        this.cycleWindow = cycleWindow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeriodDuration() {
        return periodDuration;
    }

    public void setPeriodDuration(int periodDuration) {
        this.periodDuration = periodDuration;
    }

    public String getLastPeriodDate() {
        return lastPeriodDate;
    }

    public void setLastPeriodDate(String lastPeriodDate) {
        this.lastPeriodDate = lastPeriodDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCycleWindow() {
        return cycleWindow;
    }

    public void setCycleWindow(int cycleWindow) {
        this.cycleWindow = cycleWindow;
    }
}
