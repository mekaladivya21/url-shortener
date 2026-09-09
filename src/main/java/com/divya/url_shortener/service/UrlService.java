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
import com.divya.url_shortener.exception.InvalidUrlException;

import java.net.URI;
import java.net.URISyntaxException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UrlService {

    private static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyz"
                    + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789";
    private static final int MAX_SHORT_CODE_ATTEMPTS = 10;
    private static final int SHORT_CODE_LENGTH = 8;

    private static final SecureRandom RANDOM =
            new SecureRandom();

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

        validateOriginalUrl(request.getOriginalUrl());
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

        if (url.getExpiresAt() != null
                && !url.getExpiresAt()
                .isAfter(LocalDateTime.now())) {

            throw new UrlExpiredException(
                    "Short URL has expired"
            );
        }

        return url;
    }


    private String generateShortCode() {

        StringBuilder shortCode =
                new StringBuilder(
                        SHORT_CODE_LENGTH
                );

        for (int i = 0;
             i < SHORT_CODE_LENGTH;
             i++) {

            int index =
                    RANDOM.nextInt(
                            CHARACTERS.length()
                    );

            shortCode.append(
                    CHARACTERS.charAt(index)
            );
        }

        return shortCode.toString();
    }
    private String generateUniqueShortCode() {

        for (int attempt = 0;
             attempt < MAX_SHORT_CODE_ATTEMPTS;
             attempt++) {

            String shortCode =
                    generateShortCode();

            if (!urlRepository.existsByShortCode(
                    shortCode
            )) {

                return shortCode;
            }
        }

        throw new IllegalStateException(
                "Unable to generate a unique short code"
        );
    }
    private void validateOriginalUrl(String originalUrl) {

        try {

            URI uri = new URI(originalUrl);

            String scheme = uri.getScheme();

            if (scheme == null ||
                    (!scheme.equalsIgnoreCase("http")
                            && !scheme.equalsIgnoreCase("https"))) {

                throw new InvalidUrlException(
                        "Only HTTP and HTTPS URLs are allowed"
                );
            }

            if (uri.getHost() == null) {

                throw new InvalidUrlException(
                        "URL must contain a valid host"
                );
            }

        } catch (URISyntaxException exception) {

            throw new InvalidUrlException(
                    "Invalid URL format"
            );
        }
    }
}