package com.example.quicksells.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PageResponse {

    private final boolean success;
    private final String message;
    private final PageData data;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public PageResponse(boolean success, String message, PageData data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 커스텀 메시지와 함께
    public static PageResponse success(String message, Page page) {return new PageResponse(true, message, new PageData(page));}

    // 실패 응답
    public static PageResponse error(String message) {
        return new PageResponse(false, message, null);
    }

    @Getter
    public static class PageData {
        private final List content;
        private final long totalElements;
        private final int totalPages;
        private final int size;
        private final int number;

        public PageData(Page page) {
            this.content = page.getContent();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.size = page.getSize();
            this.number = page.getNumber();
        }
    }
}
