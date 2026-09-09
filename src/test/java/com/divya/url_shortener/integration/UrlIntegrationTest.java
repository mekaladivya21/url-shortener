package com.divya.url_shortener.integration;
import com.divya.url_shortener.dto.CreateUrlRequest;
import com.divya.url_shortener.repository.ClickEventRepository;
import com.divya.url_shortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UrlIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private ClickEventRepository clickEventRepository;
    private String baseUrl;
    private HttpClient httpClient;
    @BeforeEach
    void setUp() {
        clickEventRepository.deleteAll();
        urlRepository.deleteAll();
        baseUrl =
                "http://localhost:" + port;
        /*
         * Disable automatic redirect handling.
         * This allows us to verify the 3xx response
         * returned by the URL shortener.
         */
        httpClient =
                HttpClient.newBuilder()
                        .followRedirects(
                                HttpClient.Redirect.NEVER
                        )
                        .build();
    }
    @Test
    void shouldCreateShortUrlSuccessfully()
            throws Exception {
        CreateUrlRequest request =
                new CreateUrlRequest();
        request.setOriginalUrl(
                "https://www.google.com"
        );
        String json =
                """
                {
                    "originalUrl": "https://www.google.com"
                }
                """;
        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl + "/api/urls"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();
        HttpResponse<String> response =
                httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                201,
                response.statusCode()
        );
        assertNotNull(response.body());
        assertTrue(
                response.body()
                        .contains("shortCode")
        );
        assertEquals(
                1,
                urlRepository.count()
        );
    }
    @Test
    void shouldReturnBadRequestForInvalidUrl()
            throws Exception {
        String json =
                """
                {
                    "originalUrl": "invalid-url"
                }
                """;
        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl + "/api/urls"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();
        HttpResponse<String> response =
                httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                400,
                response.statusCode()
        );
        assertNotNull(response.body());
        assertTrue(
                response.body()
                        .contains(
                                "Please provide a valid URL"
                        )
        );
    }
    @Test
    void shouldReturnNotFoundForMissingShortCode()
            throws Exception {
        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl +
                                                "/doesnotexist"
                                )
                        )
                        .GET()
                        .build();
        HttpResponse<String> response =
                httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                404,
                response.statusCode()
        );
        assertNotNull(response.body());
        assertTrue(
                response.body()
                        .contains(
                                "Short URL not found"
                        )
        );
    }
    @Test
    void shouldTrackClicksInAnalytics() throws Exception {
        /*
         * Create a shortened URL.
         */
        String createJson =
                """
                {
                    "originalUrl": "https://www.google.com"
                }
                """;
        HttpRequest createRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl + "/api/urls"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(createJson)
                        )
                        .build();
        HttpResponse<String> createResponse =
                httpClient.send(
                        createRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                201,
                createResponse.statusCode()
        );
        /*
         * Get the saved URL from the database.
         */
        var savedUrl =
                urlRepository.findAll().get(0);
        String shortCode =
                savedUrl.getShortCode();
        /*
         * Trigger the redirect endpoint.
         *
         * HttpClient is configured with Redirect.NEVER,
         * so the redirect response itself is returned.
         */
        HttpRequest redirectRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl + "/" + shortCode
                                )
                        )
                        .GET()
                        .build();
        HttpResponse<String> redirectResponse =
                httpClient.send(
                        redirectRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        /*
         * Verify that the redirect occurred.
         */
        assertTrue(
                redirectResponse.statusCode() >= 300 &&
                        redirectResponse.statusCode() < 400
        );
        /*
         * Verify click was recorded by calling analytics endpoint.
         */
        HttpRequest analyticsRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl
                                                + "/api/urls/"
                                                + shortCode
                                                + "/analytics"
                                )
                        )
                        .GET()
                        .build();
        HttpResponse<String> analyticsResponse =
                httpClient.send(
                        analyticsRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                200,
                analyticsResponse.statusCode()
        );
        assertNotNull(
                analyticsResponse.body()
        );
        assertTrue(
                analyticsResponse.body()
                        .contains("totalClicks")
        );
    }


    @Test
    void shouldRejectExpiredUrl() throws Exception {
        /*
         * Create a normal URL first.
         */
        String createJson =
                """
                {
                    "originalUrl": "https://www.google.com"
                }
                """;
        HttpRequest createRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl + "/api/urls"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(createJson)
                        )
                        .build();
        HttpResponse<String> createResponse =
                httpClient.send(
                        createRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                201,
                createResponse.statusCode()
        );
        /*
         * Get the saved URL from the database.
         */
        var savedUrl =
                urlRepository.findAll().get(0);
        /*
         * Update it directly for testing.
         *
         * Set expiration time to one minute in the past.
         */
        savedUrl.setExpiresAt(
                java.time.LocalDateTime.now()
                        .minusMinutes(1)
        );
        urlRepository.save(savedUrl);
        /*
         * Call the redirect endpoint.
         */
        HttpRequest redirectRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl
                                                + "/"
                                                + savedUrl.getShortCode()
                                )
                        )
                        .GET()
                        .build();
        HttpResponse<String> response =
                httpClient.send(
                        redirectRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        /*
         * Verify that the application returns HTTP 410 GONE.
         */
        assertEquals(
                410,
                response.statusCode()
        );
        /*
         * Verify the expected error message.
         */
        assertNotNull(
                response.body()
        );
        assertTrue(
                response.body()
                        .contains(
                                "Short URL has expired"
                        )
        );
    }

    @Test
    void shouldRedirectToOriginalUrl()
            throws Exception {
        String json =
                """
                {
                    "originalUrl": "https://www.google.com"
                }
                """;
        HttpRequest createRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl + "/api/urls"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();
        HttpResponse<String> createResponse =
                httpClient.send(
                        createRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        assertEquals(
                201,
                createResponse.statusCode()
        );
        assertEquals(
                1,
                urlRepository.count()
        );
        var savedUrl =
                urlRepository.findAll().get(0);
        String shortCode =
                savedUrl.getShortCode();
        HttpRequest redirectRequest =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl +
                                                "/" +
                                                shortCode
                                )
                        )
                        .GET()
                        .build();
        HttpResponse<String> redirectResponse =
                httpClient.send(
                        redirectRequest,
                        HttpResponse.BodyHandlers.ofString()
                );
        /*
         * We disabled automatic redirects above,
         * so we can verify the redirect response directly.
         */
        assertTrue(
                redirectResponse.statusCode() >= 300 &&
                        redirectResponse.statusCode() < 400
        );
        assertTrue(
                redirectResponse.headers()
                        .firstValue("Location")
                        .isPresent()
        );
        assertEquals(
                "https://www.google.com",
                redirectResponse.headers()
                        .firstValue("Location")
                        .orElseThrow()
        );
    }
}



