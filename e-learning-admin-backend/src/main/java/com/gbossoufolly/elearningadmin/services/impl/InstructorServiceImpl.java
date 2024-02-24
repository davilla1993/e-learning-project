package com.gbossoufolly.elearningadmin.services.impl;

import com.gbossoufolly.elearningadmin.dao.InstructorDao;
import com.gbossoufolly.elearningadmin.dto.InstructorDto;
import com.gbossoufolly.elearningadmin.mappers.InstructorMapper;
import com.gbossoufolly.elearningadmin.models.Course;
import com.gbossoufolly.elearningadmin.models.Instructor;
import com.gbossoufolly.elearningadmin.models.User;
import com.gbossoufolly.elearningadmin.services.CourseService;
import com.gbossoufolly.elearningadmin.services.InstructorService;
import com.gbossoufolly.elearningadmin.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final InstructorDao instructorDao;

    private final UserService userService;

    private final CourseService courseService;

    public InstructorServiceImpl(InstructorDao instructorDao, UserService userService,
                                 CourseService courseService) {
        this.instructorDao = instructorDao;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public Instructor loadInstructorById(Long instructorId) {
        return instructorDao.findById(instructorId).orElseThrow(
                () -> new EntityNotFoundException("Instructor with id " + instructorId + " not found")
        );
    }

    @Override
    public Page<InstructorDto> findInstructorsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Instructor> instructorsPage = instructorDao.findInstructorByName(name, pageRequest);

        return new PageImpl<>(instructorsPage.getContent().stream()
                .map(InstructorMapper::fromInstructorToInstructorDto)
                .collect(Collectors.toList()), pageRequest, instructorsPage.getTotalElements());
    }

    @Override
    public InstructorDto loadInstructorByEmail(String email) {
        return InstructorMapper.fromInstructorToInstructorDto(instructorDao.findInstructorByEmail(email));

    }

    @Override
    public InstructorDto createInstructor(InstructorDto instructorDto) {
        User user = userService.createUser(instructorDto.getUser().getEmail(), instructorDto.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "INSTRUCTOR");
        Instructor instructor = InstructorMapper.fromInstructorDtoToInstructor(instructorDto);
        instructor.setUser(user);
        instructorDao.save(instructor);

        return InstructorMapper.fromInstructorToInstructorDto(instructor);
    }

    @Override
    public InstructorDto updateInstructor(InstructorDto instructorDto) {
        Instructor loadedInstructor = loadInstructorById(instructorDto.getInstructorId());
        Instructor instructor = InstructorMapper.fromInstructorDtoToInstructor(instructorDto);
        instructor.setUser(loadedInstructor.getUser());
        instructor.setCourses(loadedInstructor.getCourses());

        Instructor updatedInstructor = instructorDao.save(instructor);

        return InstructorMapper.fromInstructorToInstructorDto(updatedInstructor);
    }

    @Override
    public List<InstructorDto> fetchInstructors() {
        return instructorDao.findAll().stream()
                .map(InstructorMapper::fromInstructorToInstructorDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeInstructor(Long instructorId) {
        Instructor instructor = loadInstructorById(instructorId);
        for(Course course : instructor.getCourses()) {
            courseService.removeCourse(course.getCourseId());
        }
        instructorDao.deleteById(instructorId);
    }
}
