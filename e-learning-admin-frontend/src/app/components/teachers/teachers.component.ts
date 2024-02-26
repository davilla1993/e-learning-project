import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, catchError, throwError } from 'rxjs';
import { Course } from 'src/app/models/course.model';
import { Instructor } from 'src/app/models/instructor.model';
import { PageResponse } from 'src/app/models/page.response.model';
import { CoursesService } from 'src/app/services/courses.service';
import { InstructorsService } from 'src/app/services/instructors.service';
import { UsersService } from 'src/app/services/users.service';
import { EmailExistsValidator } from 'src/app/validators/emailExists.validator';

@Component({
  selector: 'app-teachers',
  templateUrl: './teachers.component.html',
  styleUrls: ['./teachers.component.css']
})

export class TeachersComponent implements OnInit {

  searchFormGroup!: FormGroup;
  instructorFormGroup!: FormGroup;
  pageInstructors$!: Observable<PageResponse<Instructor>>;
  pageCourses$!: Observable<PageResponse<Course>>;
  modalInstructor!: Instructor;
  currentPage:number = 0;
  pageSize:number = 6;
  coursesCurrentPage: number = 0;
  coursesPageSize: number = 6;

  errorMessage!: string
  courseErrorMessage!: string;
  submitted:boolean = false;

  constructor(private modalService: NgbModal, private fb: FormBuilder,
                  private instructorService: InstructorsService,
                  private userService: UsersService,
                  private courseService: CoursesService) {}

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('')
    })

    this.instructorFormGroup = this.fb.group({
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      summary: ["", Validators.required],
      user: this.fb.group({
        email: ["", [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")],
                        [EmailExistsValidator.validate(this.userService)]],
        password: ["", Validators.required]
      })
    })

    this.handleSearchInstructor();
  }

  getModal(content: any){
    this.submitted=false;
    this.modalService.open(content, { size: 'xl' })
  }

  handleSearchInstructor() {
    let keyword = this.searchFormGroup.value.keyword;
    this.pageInstructors$ = this.instructorService.searchInstructors(keyword, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err)
      })
    );
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handleSearchInstructor();
  }

  handleDeleteInstructor(i: Instructor) {
    let conf = confirm("Are you sure you want delete the instructor ?");
    if(!conf) return;
    this.instructorService.deleteIntructor(i.instructorId).subscribe({
      next: () => {
        this.handleSearchInstructor();
      },
      error: err => {
        alert(err.message);
        console.log(err)
      }
    })
  }

  onSaveInstructor(modal: any) {
    this.submitted=true;
    if(this.instructorFormGroup.invalid) return;
    this.instructorService.saveInstructor(this.instructorFormGroup.value).subscribe({
      next: ()=> {
        alert("Success saving new instructor");
        this.handleSearchInstructor();
        this.instructorFormGroup.reset();
        this.submitted=false;
        modal.close();
      },

      error: err => {
        alert(err.message)
        console.log(err)
      }

    })
  }

  onCloseModal(modal: any) {
    modal.close();
    this.instructorFormGroup.reset();
  }

  getCoursesModal(i: Instructor, coursesContent: any) {
    this.modalInstructor = i;
    this.handleSearchCourses(i);
    this.modalService.open(coursesContent, {size: 'xl'});
  }

  handleSearchCourses(i:Instructor) {
    this.pageCourses$ = this.courseService.getCoursesByInstructor(i.instructorId, this.coursesCurrentPage, this.coursesPageSize).pipe(
      catchError(err => {
        this.courseErrorMessage = err.message;
        return throwError(err);
      })
    )
  }

  gotoCoursesPage(page: number) {
    this.coursesCurrentPage = page;
    this.handleSearchCourses(this.modalInstructor);
  }
}

