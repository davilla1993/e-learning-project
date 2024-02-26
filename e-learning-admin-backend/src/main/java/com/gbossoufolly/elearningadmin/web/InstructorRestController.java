package com.gbossoufolly.elearningadmin.web;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.dto.InstructorDto;
import com.gbossoufolly.elearningadmin.models.User;
import com.gbossoufolly.elearningadmin.services.CourseService;
import com.gbossoufolly.elearningadmin.services.InstructorService;
import com.gbossoufolly.elearningadmin.services.UserService;
import org.springframework.data.domain.Page;
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
    public Page<InstructorDto> searchInstructors(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "6") int size) {

        return instructorService.findInstructorsByName(keyword, page, size);
    }

    @GetMapping("/all")
    public List<InstructorDto> findAllInstructor() {
        return instructorService.fetchInstructors();
    }

    @PostMapping
    public InstructorDto saveInstructor(@RequestBody InstructorDto instructorDto) {
        User user = userService.loadUserByEmail(instructorDto.getUser().getEmail());
        if(user != null) throw new RuntimeException("User with email " + instructorDto.getUser().getEmail() + " already exists");

        return instructorService.createInstructor(instructorDto);
    }

    @DeleteMapping("/{instructorId}")
    public void deleteInstructor(@PathVariable("instructorId") Long instructorId) {
        instructorService.removeInstructor(instructorId);
    }

    @PutMapping("/{instructorId}")
    public InstructorDto updateInstructor(@RequestBody InstructorDto instructorDto, @PathVariable("instructorId") Long instructorId) {
        instructorDto.setInstructorId(instructorId);
        return instructorService.updateInstructor(instructorDto);
    }

    @GetMapping("/{instructorId}/courses")
    public Page<CourseDto> coursesByInstructorId(@PathVariable("instructorId") Long instructorId,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size) {

        return courseService.fetchCoursesForInstructor(instructorId, page, size);
    }

    @GetMapping("/find")
    public InstructorDto loadInstructorByEmail(@RequestParam(value = "email", defaultValue = "") String email) {
        return instructorService.loadInstructorByEmail(email);
    }
}
