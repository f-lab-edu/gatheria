package com.gatheria.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailCheckResponseDto {
    private final boolean available;

    public static EmailCheckResponseDto of(boolean isDuplicate) {
        return new EmailCheckResponseDto(!isDuplicate);
    }
}
