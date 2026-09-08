package com.divya.url_shortener.service;

import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.dto.UrlResponse;
import com.divya.url_shortener.entity.Url;
//import com.divya.url_shortener.repository.ClickEventRepository;
import com.divya.url_shortener.repository.UrlRepository;
import com.divya.url_shortener.exception.UrlExpiredException;
import com.divya.url_shortener.exception.UrlNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

//    @Mock
//    private ClickEventRepository clickEventRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void shouldCreateShortUrlSuccessfully() {

        CreateUrlRequest request =
                new CreateUrlRequest();

        request.setOriginalUrl(
                "https://www.google.com"
        );

        Url savedUrl = new Url();

        savedUrl.setId(1L);
        savedUrl.setOriginalUrl(
                "https://www.google.com"
        );
        savedUrl.setShortCode("abc12345");
        savedUrl.setActive(true);

        when(urlRepository.existsByShortCode(anyString()))
                .thenReturn(false);

        when(urlRepository.save(any(Url.class)))
                .thenReturn(savedUrl);

        UrlResponse response =
                urlService.createShortUrl(request);

        assertNotNull(response);

        assertEquals(
                "https://www.google.com",
                response.getOriginalUrl()
        );

        assertEquals(
                "abc12345",
                response.getShortCode()
        );

        verify(urlRepository, times(1))
                .save(any(Url.class));
    }

    @Test
    void shouldThrowExceptionWhenUrlNotFound() {

        when(urlRepository.findByShortCode("invalid123"))
                .thenReturn(Optional.empty());

        assertThrows(
                UrlNotFoundException.class,
                () -> urlService.getOriginalUrl("invalid123")
        );

        verify(urlRepository)
                .findByShortCode("invalid123");
    }
    @Test
    void shouldThrowExceptionWhenUrlIsInactive() {

        Url url = new Url();

        url.setId(1L);
        url.setOriginalUrl(
                "https://www.google.com"
        );
        url.setShortCode("abc12345");
        url.setActive(false);

        when(urlRepository.findByShortCode("abc12345"))
                .thenReturn(Optional.of(url));

        assertThrows(
                UrlNotFoundException.class,
                () -> urlService.getOriginalUrl("abc12345")
        );
    }
    @Test
    void shouldThrowExceptionWhenUrlIsExpired() {

        Url url = new Url();

        url.setId(1L);

        url.setOriginalUrl(
                "https://www.google.com"
        );

        url.setShortCode("expired123");

        url.setActive(true);

        url.setExpiresAt(
                LocalDateTime.now().minusDays(1)
        );

        when(urlRepository.findByShortCode("expired123"))
                .thenReturn(Optional.of(url));

        assertThrows(
                UrlExpiredException.class,
                () -> urlService.getOriginalUrl("expired123")
        );
    }
    @Test
    void shouldReturnUrlWhenUrlIsValid() {

        Url url = new Url();

        url.setId(1L);

        url.setOriginalUrl(
                "https://www.google.com"
        );

        url.setShortCode("valid123");

        url.setActive(true);

        when(urlRepository.findByShortCode("valid123"))
                .thenReturn(Optional.of(url));

        Url result =
                urlService.getOriginalUrl("valid123");

        assertNotNull(result);

        assertEquals(
                "https://www.google.com",
                result.getOriginalUrl()
        );

        assertEquals(
                "valid123",
                result.getShortCode()
        );
    }


}