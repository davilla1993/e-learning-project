package com.gbossoufolly.elearningadmin.services;

import com.gbossoufolly.elearningadmin.dto.StudentDto;
import com.gbossoufolly.elearningadmin.models.Student;
import org.springframework.data.domain.Page;

public interface StudentService {

    Student loadStudentById(Long studentId);

    Page<StudentDto> loadStudentsByName(String name, int page, int size);

    StudentDto loadStudentByEmail(String email);

    StudentDto createStudent(StudentDto studentDto);

    StudentDto updateStudent(StudentDto studentDto);

    void removeStudent(Long studentId);
}
