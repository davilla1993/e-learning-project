package com.gbossoufolly.elearningadmin.mappers;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.models.Course;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {

    public static CourseDto fromCourseToCourseDto(Course course) {
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course, courseDto);
        courseDto.setInstructor(InstructorMapper.fromInstructorToInstructorDto(course.getInstructor()));

        return courseDto;
    }

    public static Course fromCourseDtoToCourse(CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        course.setInstructor(InstructorMapper.fromInstructorDtoToInstructor(courseDto.getInstructor()));

        return course;
    }
}
