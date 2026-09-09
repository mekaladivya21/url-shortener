package com.divya.url_shortener.performance;

import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.service.UrlService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UrlPerformanceTest {

    @Autowired
    private UrlService urlService;

    @Test
    void shouldCreateUrlsWithinAcceptableTime() {

        long startTime =
                System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {

            CreateUrlRequest request =
                    new CreateUrlRequest();

            request.setOriginalUrl(
                    "https://example.com/page/"
                            + i
            );

            urlService.createShortUrl(request);
        }

        long endTime =
                System.currentTimeMillis();

        long duration =
                endTime - startTime;

        System.out.println(
                "Created 100 URLs in "
                        + duration
                        + " ms"
        );

        assertTrue(
                duration < 10000,
                "Performance test took too long"
        );
    }
}