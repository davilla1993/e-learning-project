package com.gbossoufolly.elearningadmin.mappers;

import com.gbossoufolly.elearningadmin.dto.StudentDto;
import com.gbossoufolly.elearningadmin.models.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {

    public static StudentDto fromStudentToStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);

        return studentDto;
    }

    public static Student fromStudentDtoToStudent(StudentDto studentDto) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);

        return student;
    }
}
