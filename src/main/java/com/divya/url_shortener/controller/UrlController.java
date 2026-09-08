package com.divya.url_shortener.controller;

import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.dto.UrlResponse;
import com.divya.url_shortener.service.UrlService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(
            @Valid @RequestBody CreateUrlRequest request
    ) {

        UrlResponse response =
                urlService.createShortUrl(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}