package com.gbossoufolly.elearningadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {

    private Long instructorId;

    private String firstName;

    private String lastName;

    private String summary;

    private UserDto user;
}
