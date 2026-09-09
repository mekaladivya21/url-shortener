package com.divya.url_shortener.dto;

public class AnalyticsResponse {

    private final String shortCode;
    private final String originalUrl;
    private final long totalClicks;

    public AnalyticsResponse(
            String shortCode,
            String originalUrl,
            long totalClicks
    ) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.totalClicks = totalClicks;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public long getTotalClicks() {
        return totalClicks;
    }
}