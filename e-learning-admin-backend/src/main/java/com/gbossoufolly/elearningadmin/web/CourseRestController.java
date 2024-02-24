package com.gbossoufolly.elearningadmin.web;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.services.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@CrossOrigin("*")
public class CourseRestController {

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Page<CourseDto> searchCourses(@RequestParam(name = "keyword", defaultValue= "") String keyword,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "6") int size){

        return courseService.findCoursesByCourseName(keyword, page, size);

    }

    @PostMapping
    public CourseDto saveCourse(@RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto);
    }

    @PutMapping("/{courseId}")
    public CourseDto updateCourse(@RequestBody CourseDto courseDto, @PathVariable("courseId") Long courseId){
        courseDto.setCourseId(courseId);

        return courseService.updateCourse(courseDto);
    }

    @PostMapping("/{courseId}/enroll/students/{studentId}")
    public void enrollStudentInCourse(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId) {
        courseService.assignStudentToCourse(courseId, studentId);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable("courseId") Long courseId){
        courseService.removeCourse(courseId);
    }

}
