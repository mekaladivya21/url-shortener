package com.divya.url_shortener.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "urls",
        indexes = {
                @Index(
                        name = "idx_short_code",
                        columnList = "shortCode"
                )
        }
)
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 10
    )
    private String shortCode;

    @Column(
            nullable = false,
            length = 2048
    )
    private String originalUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean active = true;



    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


    public Url() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getShortCode() {
        return shortCode;
    }


    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }


    public String getOriginalUrl() {
        return originalUrl;
    }


    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }


    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }
}