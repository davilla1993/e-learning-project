package com.gbossoufolly.elearningadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long courseId;

    private String courseName;

    private String courseDuration;

    private String courseDescription;

    private InstructorDto instructor;
}
