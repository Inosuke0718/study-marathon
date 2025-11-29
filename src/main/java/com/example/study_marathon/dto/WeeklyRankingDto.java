package com.example.study_marathon.dto;

public class WeeklyRankingDto {

    private final String username;
    private final int totalMinutes;

    public WeeklyRankingDto(String username, long totalMinutes) {
        this.username = username;
        this.totalMinutes = (int) totalMinutes;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }
}
