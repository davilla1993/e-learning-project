package com.gbossoufolly.elearningadmin.runner;

import com.gbossoufolly.elearningadmin.dto.CourseDto;
import com.gbossoufolly.elearningadmin.dto.InstructorDto;
import com.gbossoufolly.elearningadmin.dto.StudentDto;
import com.gbossoufolly.elearningadmin.dto.UserDto;
import com.gbossoufolly.elearningadmin.mappers.StudentMapper;
import com.gbossoufolly.elearningadmin.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MyRunner implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final InstructorService instructorService;
    private final CourseService courseService;
    private final StudentService studentService;

    public MyRunner(RoleService roleService, UserService userService,
                    InstructorService instructorService, CourseService courseService,
                    StudentService studentService) {

        this.roleService = roleService;
        this.userService = userService;
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @Override
    public void run(String... args) throws Exception {
    //    createRoles();
    //    createAdmin();
    //    createInstructors();
    //    createCourses();
    //     StudentDto student = studentService.loadStudentByEmail("student@gmail.com");
    //     assignCourseToStudent(student);
    //     createStudents();
    }
    private void createRoles() {
        Arrays.asList("ADMIN", "INSTRUCTOR", "STUDENT")
                .forEach(roleService::createRole);
    }

    private void createAdmin() {
        userService.createUser("admin@gmail.com", "1234");
        userService.assignRoleToUser("admin@gmail.com","ADMIN");
    }

    private void createInstructors() {
        for(int i = 0; i < 10; i++) {
           InstructorDto instructorDto = new InstructorDto();
           instructorDto.setFirstName("instructor"+i+"FN");
           instructorDto.setLastName("instructor"+i+"LN");
           instructorDto.setSummary("master"+i);
           UserDto userDto = new UserDto();
           userDto.setEmail("instructor"+i+"@gmail.com");
           userDto.setPassword("1234");
           instructorDto.setUser(userDto);
           instructorService.createInstructor(instructorDto);
        }
    }

    private void createCourses() {
        String[] courses = {"Java", "Angular", "Reactjs", "Spring", "Hibernate", "Nodejs", "Expressjs", "MongoDB", "Mysql"};
        for(int i = 0; i <= courses.length-1; i++) {
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseName(courses[i]);
            courseDto.setCourseDuration(i+5+"Hours");
            courseDto.setCourseDescription("Become a master in "+courses[i]+" from scratch");
            InstructorDto instructorDto = new InstructorDto();
            instructorDto.setInstructorId(1L);
            courseDto.setInstructor(instructorDto);
            courseService.createCourse(courseDto);

        }

        /*for(int i = 0; i < 5; i++) {
           CourseDto courseDto = new CourseDto();
           courseDto.setCourseName("Java"+i);
           courseDto.setCourseDuration(i+"Hours");
           courseDto.setCourseDescription("Learn Java"+i+"from scratch");
           InstructorDto instructorDto = new InstructorDto();
           instructorDto.setInstructorId(2L);
           courseDto.setInstructor(instructorDto);
           courseService.createCourse(courseDto);

        }*/
        /*for(int i = 6; i < 17; i++) {
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseName("Angular"+i);
            courseDto.setCourseDuration(i+"Hours");
            courseDto.setCourseDescription("Learn Angular"+i+"from scratch and build a real project");
            InstructorDto instructorDto = new InstructorDto();
            instructorDto.setInstructorId(3L);
            courseDto.setInstructor(instructorDto);
            courseService.createCourse(courseDto);

        }*/

        for(int i = 6; i < 19; i++) {
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseName("Reactjs"+i);
            courseDto.setCourseDuration(i+"Hours");
            courseDto.setCourseDescription("Learn react js"+i+"and get job ready");
            InstructorDto instructorDto = new InstructorDto();
            instructorDto.setInstructorId(4L);
            courseDto.setInstructor(instructorDto);
            courseService.createCourse(courseDto);

        }
    }

    private StudentDto createStudent() {
            StudentDto studentDto = new StudentDto();
            studentDto.setFirstName("studentFN");
            studentDto.setLastName("studentLN");
            studentDto.setLevel("intermediate");
            UserDto userDto = new UserDto();
            userDto.setEmail("student@gmail.com");
            userDto.setPassword("1234");
            studentDto.setUser(userDto);

        return studentService.createStudent(studentDto);

    }
    private void assignCourseToStudent(StudentDto student) {
        courseService.assignStudentToCourse(2L, student.getStudentId());
        courseService.assignStudentToCourse(3L, student.getStudentId());
        courseService.assignStudentToCourse(4L, student.getStudentId());
        courseService.assignStudentToCourse(6L, student.getStudentId());
        courseService.assignStudentToCourse(7L, student.getStudentId());
        courseService.assignStudentToCourse(8L, student.getStudentId());
        courseService.assignStudentToCourse(9L, student.getStudentId());

    }

    private void createStudents() {
        for (int i = 1; i < 10; i++) {
            StudentDto studentDto = new StudentDto();
            studentDto.setFirstName("student"+i+"FN");
            studentDto.setLastName("student"+i+"LN");
            studentDto.setLevel("Level"+i);
            UserDto userDto = new UserDto();
            userDto.setEmail("student"+i+"@gmail.com");
            userDto.setPassword("1234");
            studentDto.setUser(userDto);
            studentService.createStudent(studentDto);
        }
    }
}
