package com.gatheria.dto.response;

import com.gatheria.domain.Instructor;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagedInstructorResponseDto {
    private int totalCount;
    private int pageNumber;
    private int pageSize;
    private List<Instructor> instructors;

    public static PagedInstructorResponseDto from(List<Instructor> instructorList, int totalCount, int pageNumber, int pageSize) {
        return new PagedInstructorResponseDto(totalCount, pageNumber, pageSize, instructorList);
    }
}
