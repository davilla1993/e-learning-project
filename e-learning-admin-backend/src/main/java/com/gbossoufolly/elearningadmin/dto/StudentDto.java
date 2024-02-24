package com.gbossoufolly.elearningadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Long studentId;

    private String firstName;

    private String lastName;

    private String level;

    private UserDto user;


}
