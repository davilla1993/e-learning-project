package com.gbossoufolly.elearningadmin.services.impl;

import com.gbossoufolly.elearningadmin.dao.CourseDao;
import com.gbossoufolly.elearningadmin.dao.InstructorDao;
import com.gbossoufolly.elearningadmin.dao.StudentDao;
import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.mappers.CourseMapper;
import com.gbossoufolly.elearningadmin.models.Course;
import com.gbossoufolly.elearningadmin.models.Instructor;
import com.gbossoufolly.elearningadmin.models.Student;
import com.gbossoufolly.elearningadmin.services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    private final InstructorDao instructorDao;

    private final StudentDao studentDao;

    public CourseServiceImpl(CourseDao courseDao, InstructorDao instructorDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.instructorDao = instructorDao;
        this.studentDao = studentDao;
    }

    @Override
    public Course loadCourseById(Long courseId) {
        return courseDao.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Course not found with ID " + courseId));
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = CourseMapper.fromCourseDtoToCourse(courseDto);

        Instructor instructor = instructorDao.findById(courseDto.getInstructor().getInstructorId()).orElseThrow(() ->
                new EntityNotFoundException("Instructor not found with ID " + courseDto.getInstructor().getInstructorId()));

        course.setInstructor(instructor);

        Course savedCourse = courseDao.save(course);

        return CourseMapper.fromCourseToCourseDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        Course loadedCourse = loadCourseById(courseDto.getCourseId());

        Instructor instructor = instructorDao.findById(courseDto.getInstructor().getInstructorId()).orElseThrow(() ->
                new EntityNotFoundException("Instructor not found with ID " + courseDto.getInstructor().getInstructorId()));

        Course course = CourseMapper.fromCourseDtoToCourse(courseDto);
        course.setInstructor(instructor);
        course.setStudents(loadedCourse.getStudents());
        Course updatedCourse = courseDao.save(course);

        return CourseMapper.fromCourseToCourseDto(updatedCourse);
    }

    @Override
    public Page<CourseDto> findCoursesByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursesPage = courseDao.findCoursesByCourseNameContains(keyword, pageRequest);

        /*return new PageImpl<>(coursesPage.getContent().stream().map(
                course -> CourseMapper.fromCourseToCourseDto(course))
                .collect(Collectors.toList()));*/
        return new PageImpl<>(coursesPage.getContent().stream().map(
                        CourseMapper::fromCourseToCourseDto)
                .collect(Collectors.toList()), pageRequest, coursesPage.getTotalElements());
    }

    @Override
    public void assignStudentToCourse(Long courseId, Long studentId) {
       Student student = studentDao.findById(studentId).orElseThrow(() ->
                new EntityNotFoundException("Student not found with ID " + studentId));

       Course course = loadCourseById(courseId);

       course.assignStudentToCourse(student);
    }

    @Override
    public Page<CourseDto> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> studentCoursesPage = courseDao.getCoursesByStudentId(studentId, pageRequest);

        return new PageImpl<>(studentCoursesPage.getContent().stream().map(
                CourseMapper::fromCourseToCourseDto)
                .collect(Collectors.toList()), pageRequest, studentCoursesPage.getTotalElements());
    }

    @Override
    public Page<CourseDto> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> nonEnrolledInCoursesPage = courseDao.getNonEnrolledInCoursesByStudentId(studentId, pageRequest);

        return new PageImpl<>(nonEnrolledInCoursesPage.getContent().stream().map(
                CourseMapper::fromCourseToCourseDto)
                .collect(Collectors.toList()), pageRequest, nonEnrolledInCoursesPage.getTotalElements());
    }

    @Override
    public void removeCourse(Long courseId) {
        courseDao.deleteById(courseId);
    }

    @Override
    public Page<CourseDto> fetchCoursesForInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> instructorCoursesPage = courseDao.getCoursesByInstructorId(instructorId, pageRequest);

        return new PageImpl<>(instructorCoursesPage.getContent().stream().map(
                CourseMapper::fromCourseToCourseDto)
                .collect(Collectors.toList()), pageRequest, instructorCoursesPage.getTotalElements());
    }
}
