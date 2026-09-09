package com.divya.url_shortener.integration;

import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.repository.ClickEventRepository;
import com.divya.url_shortener.repository.UrlRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UrlIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ClickEventRepository clickEventRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {

        clickEventRepository.deleteAll();
        urlRepository.deleteAll();

        baseUrl =
                "http://localhost:" + port;
    }
}