package com.divya.url_shortener.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SecurityHeadersConfig {

    @Bean
    public Filter securityHeadersFilter() {

        return (
                ServletRequest request,
                ServletResponse response,
                FilterChain chain
        ) -> {

            HttpServletResponse httpResponse =
                    (HttpServletResponse) response;

            httpResponse.setHeader(
                    "X-Content-Type-Options",
                    "nosniff"
            );

            httpResponse.setHeader(
                    "X-Frame-Options",
                    "DENY"
            );

            httpResponse.setHeader(
                    "Referrer-Policy",
                    "no-referrer"
            );

            chain.doFilter(
                    request,
                    response
            );
        };
    }
}