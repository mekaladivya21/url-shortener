package com.divya.url_shortener.dto;

import java.time.LocalDateTime;

public class UrlResponse {

    private final String originalUrl;
    private final String shortCode;
    private final String shortUrl;
    private final LocalDateTime expiresAt;

    public UrlResponse(
            String originalUrl,
            String shortCode,
            String shortUrl,
            LocalDateTime expiresAt
    ) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.expiresAt = expiresAt;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}