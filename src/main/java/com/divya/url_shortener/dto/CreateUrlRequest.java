package com.divya.url_shortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public class CreateUrlRequest {

    @NotBlank(message = "Original URL is required")
    @URL(message = "Please provide a valid URL")
    @Size(
            max = 2048,
            message = "URL cannot exceed 2048 characters"
    )
    private String originalUrl;

    @Future(message = "Expiration time must be in the future")
    private LocalDateTime expiresAt;

    public CreateUrlRequest() {
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}