package com.gatheria.dto.response;

import com.gatheria.domain.Lecture;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureResponseDto {
    private String name;
    private String code;
    private Long instructorId;

    public static LectureResponseDto of(Lecture lecture) {
        return LectureResponseDto.builder()
                .name(lecture.getName())
                .code(lecture.getCode())
                .instructorId(lecture.getInstructorId())
                .build();
    }

    public static List<LectureResponseDto> from(List<Lecture> lectures) {
        return lectures.stream()
                .map(LectureResponseDto::of)
                .toList();
    }
}
