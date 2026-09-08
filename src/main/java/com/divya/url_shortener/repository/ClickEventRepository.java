package com.divya.url_shortener.repository;

import com.divya.url_shortener.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository
        extends JpaRepository<ClickEvent, Long> {

    long countByUrlId(Long urlId);
}