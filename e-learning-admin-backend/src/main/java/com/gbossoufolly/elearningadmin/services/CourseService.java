package com.gbossoufolly.elearningadmin.services;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.models.Course;
import org.springframework.data.domain.Page;

public interface CourseService {

    Course loadCourseById(Long courseId);

    CourseDto createCourse(CourseDto courseDto);

    CourseDto updateCourse(CourseDto courseDto);

    Page<CourseDto> findCoursesByCourseName(String keyword, int page, int size);

    void assignStudentToCourse(Long courseId, Long studentId);

    Page<CourseDto> fetchCoursesForStudent(Long studentId, int page, int size);

    Page<CourseDto> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size);

    void removeCourse(Long courseId);

    Page<CourseDto> fetchCoursesForInstructor(Long instructorId, int page, int size);


}
