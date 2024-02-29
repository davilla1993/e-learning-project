package com.gbossoufolly.elearningadmin.web;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.dto.StudentDto;
import com.gbossoufolly.elearningadmin.models.User;
import com.gbossoufolly.elearningadmin.services.CourseService;
import com.gbossoufolly.elearningadmin.services.StudentService;
import com.gbossoufolly.elearningadmin.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@CrossOrigin("*")
public class StudentRestController {
    private final StudentService studentService;
    private final UserService userService;
    private final CourseService courseService;

    public StudentRestController(StudentService studentService, UserService userService,
                                 CourseService courseService) {
        this.studentService = studentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<StudentDto> searchStudents(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {

        return studentService.loadStudentsByName(keyword, page, size);

    }
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public StudentDto saveStudent(@RequestBody StudentDto studentDto) {
        User user = userService.loadUserByEmail(studentDto.getUser().getEmail());
        if(user != null) throw new RuntimeException("Email already used");

        return studentService.createStudent(studentDto);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    public StudentDto updateStudent(@RequestBody StudentDto studentDto, @PathVariable("studentId") Long studentId) {
       studentDto.setStudentId(studentId);

         return studentService.updateStudent(studentDto);
    }

    @GetMapping("/{studentId}/courses")
    @PreAuthorize("hasAuthority('STUDENT')")
    public Page<CourseDto> coursesByStudentId(@PathVariable("studentId") Long studentId,
                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {


    return courseService.fetchCoursesForStudent(studentId, page, size);

    }

    @GetMapping("/{studentId}/other-courses")
    @PreAuthorize("hasAuthority('STUDENT')")
    public Page<CourseDto> otherCoursesByStudentId(@PathVariable("studentId") Long studentId,
                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {

        return courseService.fetchNonEnrolledInCoursesForStudent(studentId, page, size);
    }

    @GetMapping("/find")
    @PreAuthorize("hasAuthority('STUDENT')")
    public StudentDto findStudentByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return studentService.loadStudentByEmail(email);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.removeStudent(studentId);
    }
}
