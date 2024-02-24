package com.gbossoufolly.elearningadmin.mappers;

import com.gbossoufolly.elearningadmin.dto.InstructorDto;
import com.gbossoufolly.elearningadmin.models.Instructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class InstructorMapper {

    public static InstructorDto fromInstructorToInstructorDto(Instructor instructor) {
       InstructorDto instructorDto = new InstructorDto();
        BeanUtils.copyProperties(instructor, instructorDto);

        return instructorDto;
    }

    public static Instructor fromInstructorDtoToInstructor(InstructorDto instructorDto) {
        Instructor instructor = new Instructor();
        BeanUtils.copyProperties(instructorDto, instructor);

        return instructor;
    }
}
