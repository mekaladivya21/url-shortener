package com.divya.url_shortener.service;

import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.dto.UrlResponse;
import com.divya.url_shortener.entity.Url;
import com.divya.url_shortener.repository.UrlRepository;
import com.divya.url_shortener.exception.UrlExpiredException;
import com.divya.url_shortener.exception.UrlNotFoundException;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public UrlResponse createShortUrl(CreateUrlRequest request) {

        String shortCode = generateUniqueShortCode();

        Url url = new Url();

        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortCode(shortCode);
        url.setExpiresAt(request.getExpiresAt());
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
                !url.getExpiresAt().isAfter(LocalDateTime.now())) {

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