import { InstructorsService } from './../../services/instructors.service';
import { Instructor } from './../../models/instructor.model';
import { HttpErrorResponse } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { Observable, catchError, throwError } from 'rxjs';
import { Course } from 'src/app/models/course.model';
import { PageResponse } from 'src/app/models/page.response.model';
import { CoursesService } from 'src/app/services/courses.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})

export class CoursesComponent implements OnInit {

  searchFormGroup! : FormGroup;
  courseFormGroup!: FormGroup;
  updateCourseFormGroup!: FormGroup;
  pageCourses$!: Observable<PageResponse<Course>>;
  instructors$!: Observable<Instructor[]>;
  currentPage:number = 0;
  pageSize: number = 6;
  errorMessage!: string;
  errorInstructorMessage!: string;
  submitted: boolean = false;
  defaultInstructor!: Instructor;


  constructor(private modalService: NgbModal, private fb:FormBuilder,
                                private courseService: CoursesService, 
                                private InstructorsService: InstructorsService) {
  }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('')
    })

    this.handleSearchCourses();

    this.courseFormGroup = this.fb.group({
      courseName: ["", Validators.required],
      courseDuration: ["", Validators.required],
      courseDescription: ["", Validators.required],
      instructor:  ["", Validators.required]
    })
  }

  getModal(content: any) {
    this.submitted = false;
    this.fetchInstructors();
    this.modalService.open(content, {size: 'xl'})
  }

  onSaveCourse(modal: any) {
    this.submitted = true;
    if(this.courseFormGroup.invalid) return;
    this.courseService.saveCourse(this.courseFormGroup.value).subscribe({
      next: () => {
        alert("Success Saving Course");
        this.handleSearchCourses();
        this.courseFormGroup.reset();
        this.submitted = false;
        modal.close();
      },

      error : err => {
        alert(err.message);
      }
    })

  }
    onCloseModal(modal: any) {
      modal.close();
      this.courseFormGroup.reset();
    }

  handleSearchCourses() {
    let keyword = this.searchFormGroup.value.keyword;
    this.pageCourses$ = this.courseService.searchCourses(keyword, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handleSearchCourses();
  }

  handleDeleteCourse(c: Course) {
  let conf = confirm("Are you sure you want delete this course ?")
  if(!conf) return;
  this.courseService.deleteCourse(c.courseId).subscribe({
    next: () => { this.handleSearchCourses()},
    error: err => { alert( err.message), console.log(err) }
  }
  )}

  fetchInstructors() {
    this.instructors$ = this.InstructorsService.findAllInstructors().pipe(
      catchError(err => {
        this.errorInstructorMessage = err.message;
        return throwError(err)
      })
    );
  }

  getUpdateModel(c: Course, updateContent: any) {
    this.fetchInstructors();
    this.updateCourseFormGroup = this.fb.group({
      courseId:[c.courseId, Validators.required],
      courseName:[c.courseName, Validators.required],
      courseDuration:[c.courseDescription, Validators.required],
      courseDescription:[c.courseDuration, Validators.required],
      instructor: [c.instructor, Validators.required]
    })
    this.defaultInstructor = this.updateCourseFormGroup.controls['instructor'].value;
    this.modalService.open(updateContent, {size:'xl'})
  }

  onUpdateCourse(updateModal: any) {
    this.submitted = true;
    if(this.updateCourseFormGroup.invalid) return;
    this.courseService.updateCourse(this.updateCourseFormGroup.value, 
                    this.updateCourseFormGroup.value.courseId).subscribe({
          
                      next: () => { 
                        alert("Success updating course");
                        this.handleSearchCourses();
                        this.submitted = false;
                        updateModal.close();
                      },

                      error: err => {
                        alert(err.message)
                      }
                    })
  }
}
