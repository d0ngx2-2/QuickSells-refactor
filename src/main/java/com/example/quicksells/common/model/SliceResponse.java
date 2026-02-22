package com.example.quicksells.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SliceResponse {

    private final boolean success;
    private final String message;
    private final SliceData data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    private SliceResponse(boolean success, String message, SliceData data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 성공 응답
    public static SliceResponse success(String message, Slice slice) {
        return new SliceResponse(
                true,
                message,
                new SliceData(slice)
        );
    }

    // 실패 응답
    public static SliceResponse error(String message) {
        return new SliceResponse(false, message, null);
    }

    @Getter
    public static class SliceData {
        private final List content;
        private final int size;
        private final int number;
        private final boolean hasNext;
        private final boolean first;
        private final boolean last;

        public SliceData(Slice slice) {
            this.content = slice.getContent();
            this.size = slice.getSize();
            this.number = slice.getNumber();
            this.hasNext = slice.hasNext();
            this.first = slice.isFirst();
            this.last = slice.isLast();
        }
    }
}
