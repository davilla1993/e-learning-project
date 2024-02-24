package com.gbossoufolly.elearningadmin.services.impl;

import com.gbossoufolly.elearningadmin.dao.StudentDao;
import com.gbossoufolly.elearningadmin.dto.StudentDto;
import com.gbossoufolly.elearningadmin.mappers.StudentMapper;
import com.gbossoufolly.elearningadmin.models.Course;
import com.gbossoufolly.elearningadmin.models.Student;
import com.gbossoufolly.elearningadmin.models.User;
import com.gbossoufolly.elearningadmin.services.StudentService;
import com.gbossoufolly.elearningadmin.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    private final UserService userService;

    public StudentServiceImpl(StudentDao studentDao, UserService userService) {
        this.studentDao = studentDao;
        this.userService = userService;
    }

    @Override
    public Student loadStudentById(Long studentId) {
        return studentDao.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("Student not found with id: " + studentId)
        );
    }

    @Override
    public Page<StudentDto> loadStudentsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Student> studentsPage = studentDao.findStudentsByName(name, pageRequest);

        return new PageImpl<>(studentsPage.getContent().stream()
                .map(StudentMapper::fromStudentToStudentDto)
                .collect(Collectors.toList()), pageRequest, studentsPage.getTotalElements());
    }

    @Override
    public StudentDto loadStudentByEmail(String email) {
        return StudentMapper.fromStudentToStudentDto(studentDao.findStudentByEmail(email));
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        User user = userService.createUser(studentDto.getUser().getEmail(),
                                            studentDto.getUser().getPassword());
        userService.assignRoleToUser(studentDto.getUser().getEmail(), "STUDENT");
        Student student = StudentMapper.fromStudentDtoToStudent(studentDto);
        student.setUser(user);
        Student savedStudent = studentDao.save(student);

        return StudentMapper.fromStudentToStudentDto(savedStudent);
    }

    @Override
    public StudentDto updateStudent(StudentDto studentDto) {
        Student loadedStudent = loadStudentById(studentDto.getStudentId());
        Student student = StudentMapper.fromStudentDtoToStudent(studentDto);
        student.setUser(loadedStudent.getUser());
        student.setCourses(loadedStudent.getCourses());
        Student updatedStudent = studentDao.save(student);

        return StudentMapper.fromStudentToStudentDto(updatedStudent);
    }

    @Override
    public void removeStudent(Long studentId) {
        Student student = loadStudentById(studentId);
        Iterator<Course> courseIterator = student.getCourses().iterator();
        if(courseIterator.hasNext()) {
            Course course = courseIterator.next();
            course.removeStudentFromCourse(student);
        }
        studentDao.deleteById(studentId);
    }
}
