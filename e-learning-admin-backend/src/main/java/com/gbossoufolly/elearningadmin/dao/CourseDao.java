package com.gbossoufolly.elearningadmin.dao;

import com.gbossoufolly.elearningadmin.models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseDao extends JpaRepository<Course, Long> {
    Page<Course> findCoursesByCourseNameContains(String keyword, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM courses as c WHERE c.course_id IN " +
            "(select e.course_id from enrolled_in as e where e.student_id =:studentId)")
    Page<Course> getCoursesByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM courses as c WHERE c.course_id NOT IN " +
            "(select e.course_id from enrolled_in as e where e.student_id =:studentId)")
    Page<Course> getNonEnrolledInCoursesByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query(value = "SELECT c FROM Course as c WHERE c.instructor.instructorId =:instructorId")
    Page<Course> getCoursesByInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);

}
