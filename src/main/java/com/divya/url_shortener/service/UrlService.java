package com.divya.url_shortener.service;

import com.divya.url_shortener.dto.AnalyticsResponse;
import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.dto.UrlResponse;
import com.divya.url_shortener.entity.Url;
import com.divya.url_shortener.repository.UrlRepository;
import com.divya.url_shortener.exception.UrlExpiredException;
import com.divya.url_shortener.exception.UrlNotFoundException;
import com.divya.url_shortener.entity.ClickEvent;
import com.divya.url_shortener.repository.ClickEventRepository;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;


    private final ClickEventRepository clickEventRepository;

    public UrlService(
            UrlRepository urlRepository,
            ClickEventRepository clickEventRepository
    ) {
        this.urlRepository = urlRepository;
        this.clickEventRepository = clickEventRepository;
    }

    @Transactional
    public UrlResponse createShortUrl(CreateUrlRequest request) {

        String shortCode = generateUniqueShortCode();

        Url url = new Url();

        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortCode(shortCode);
        url.setActive(true);

        Url savedUrl = urlRepository.save(url);

        String shortUrl =
                "http://localhost:8080/" +
                        savedUrl.getShortCode();

        return new UrlResponse(
                savedUrl.getOriginalUrl(),
                savedUrl.getShortCode(),
                shortUrl,
                savedUrl.getExpiresAt()
        );
    }

    @Transactional
    public void recordClick(
            Long urlId,
            String userAgent
    ) {

        ClickEvent clickEvent = new ClickEvent();

        clickEvent.setUrlId(urlId);
        clickEvent.setUserAgent(userAgent);

        clickEventRepository.save(clickEvent);
    }
    @Transactional(readOnly = true)
    public AnalyticsResponse getAnalytics(
            String shortCode
    ) {

        Url url = urlRepository
                .findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "Short URL not found"
                        )
                );

        long totalClicks =
                clickEventRepository.countByUrlId(
                        url.getId()
                );

        return new AnalyticsResponse(
                url.getShortCode(),
                url.getOriginalUrl(),
                totalClicks
        );
    }
    @Transactional(readOnly = true)
    public Url getOriginalUrl(String shortCode) {

        Url url = urlRepository
                .findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "Short URL not found"
                        )
                );

        if (!url.isActive()) {
            throw new UrlNotFoundException(
                    "Short URL is inactive"
            );
        }

        if (url.getExpiresAt() != null &&
                url.getExpiresAt().isBefore(LocalDateTime.now())) {

            throw new UrlExpiredException(
                    "Short URL has expired"
            );
        }

        return url;
    }


    private String generateUniqueShortCode() {

        String shortCode;

        do {

            shortCode = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8);

        } while (urlRepository.existsByShortCode(shortCode));

        return shortCode;
    }
}