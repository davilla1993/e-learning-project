import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse } from '../models/page.response.model';
import { Student } from '../models/student.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentsService {

  constructor(private http: HttpClient) {
  }


  public searchStudents(keyword: string, currentPage: number, pageSize: number): Observable<PageResponse<Student>> {
    return this.http.get<PageResponse<Student>>(environment.backendHost + "/students?keyword=" + keyword + "&page=" + currentPage + "&size=" + pageSize);
  }

  public deleteStudent(studentId: number) {
    return this.http.delete(environment.backendHost + "/students/" + studentId);
  }

  public saveStudent(student: Student): Observable<Student> {
    return this.http.post<Student>(environment.backendHost + "/students", student);
  }
}
