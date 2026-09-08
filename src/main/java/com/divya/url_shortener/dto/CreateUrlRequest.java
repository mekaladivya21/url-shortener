package com.divya.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Setter
@Getter
public class CreateUrlRequest {

    @NotBlank(message = "Original URL is required")

    @URL(message = "Please provide a valid URL")

    @Size(
            max = 2048,
            message = "URL cannot exceed 2048 characters"
    )
    private String originalUrl;

    public CreateUrlRequest() {
    }

}