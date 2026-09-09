package com.divya.url_shortener.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "click_events",
        indexes = {
                @Index(
                        name = "idx_click_url_id",
                        columnList = "urlId"
                )
        }
)
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long urlId;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    @Column(length = 1000)
    private String userAgent;

    public ClickEvent() {
    }

    @PrePersist
    public void onCreate() {

        if (clickedAt == null) {
            clickedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(LocalDateTime clickedAt) {
        this.clickedAt = clickedAt;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}