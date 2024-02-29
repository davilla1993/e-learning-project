package com.gbossoufolly.elearningadmin.web;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.dto.InstructorDto;
import com.gbossoufolly.elearningadmin.models.User;
import com.gbossoufolly.elearningadmin.services.CourseService;
import com.gbossoufolly.elearningadmin.services.InstructorService;
import com.gbossoufolly.elearningadmin.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors")
@CrossOrigin("*")
public class InstructorRestController {

    private final InstructorService instructorService;
    private final UserService userService;

    private final CourseService courseService;

    public InstructorRestController(InstructorService instructorService, UserService userService,
                                    CourseService courseService) {
        this.instructorService = instructorService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<InstructorDto> searchInstructors(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "6") int size) {

        return instructorService.findInstructorsByName(keyword, page, size);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<InstructorDto> findAllInstructor() {
        return instructorService.fetchInstructors();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public InstructorDto saveInstructor(@RequestBody InstructorDto instructorDto) {
        User user = userService.loadUserByEmail(instructorDto.getUser().getEmail());
        if(user != null) throw new RuntimeException("User with email " + instructorDto.getUser().getEmail() + " already exists");

        return instructorService.createInstructor(instructorDto);
    }

    @DeleteMapping("/{instructorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteInstructor(@PathVariable("instructorId") Long instructorId) {
        instructorService.removeInstructor(instructorId);
    }

    @PutMapping("/{instructorId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public InstructorDto updateInstructor(@RequestBody InstructorDto instructorDto, @PathVariable("instructorId") Long instructorId) {
        instructorDto.setInstructorId(instructorId);
        return instructorService.updateInstructor(instructorDto);
    }

    @GetMapping("/{instructorId}/courses")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    public Page<CourseDto> coursesByInstructorId(@PathVariable("instructorId") Long instructorId,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size) {

        return courseService.fetchCoursesForInstructor(instructorId, page, size);
    }

    @GetMapping("/find")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public InstructorDto loadInstructorByEmail(@RequestParam(value = "email", defaultValue = "") String email) {
        return instructorService.loadInstructorByEmail(email);
    }
}
