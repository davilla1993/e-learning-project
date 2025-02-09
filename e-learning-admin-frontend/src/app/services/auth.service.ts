import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/login.model';
import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { LoggedUser } from '../models/logged-user.model';
import { Router } from '@angular/router';
import { InstructorsService } from './instructors.service';
import { StudentsService } from './students.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  jwtHelperService = new JwtHelperService();
  user = new BehaviorSubject<LoggedUser | null>(null);

  constructor(private http: HttpClient, private router: Router, private instructorService: InstructorsService,
                                        private studentService: StudentsService) { }


  public login(user: LoginRequest) : Observable<LoginResponse> {
    const formData = new FormData();
    formData.append('username', user.username);
    formData.append('password', user.password);

    return this.http.post<LoginResponse>(environment.backendHost + "/login", formData);
  }

  saveToken(jwtTokens: LoginResponse) {
    const decodedAccessToken = this.jwtHelperService.decodeToken(jwtTokens.accessToken);
    const loggedUser = new LoggedUser(decodedAccessToken.sub, decodedAccessToken.roles, jwtTokens.accessToken,
      this.getExpirationDate(decodedAccessToken.exp), undefined, undefined);

    this.user.next(loggedUser);
    this.redirectLoggedInUser(decodedAccessToken, jwtTokens.accessToken);
  }

  redirectLoggedInUser(decodedToken: any, accessToken: string) {
    if(decodedToken.roles.includes('ADMIN')) this.router.navigateByUrl("/courses");

    else if (decodedToken.roles.includes('INSTRUCTOR')) 
      this.instructorService.loadInstructorByEmail(decodedToken.sub).subscribe(instructor=>{
        const loggedUser = new LoggedUser(decodedToken.sub, decodedToken.roles, accessToken, this.getExpirationDate(decodedToken.exp), undefined, instructor);
        this.user.next(loggedUser);
        this.router.navigateByUrl("/instructor-courses/"+instructor.instructorId);
      })

    else if (decodedToken.roles.includes('STUDENT'))
      this.studentService.loadStudentByEmail(decodedToken.sub).subscribe(student=>{
        const loggedUser = new LoggedUser(decodedToken.sub, decodedToken.roles, accessToken, this.getExpirationDate(decodedToken.exp), student, undefined);
        this.user.next(loggedUser);
        this.router.navigateByUrl("/student-courses/"+student.studentId);
      })
  }

  getExpirationDate(exp: number){
    const date = new Date(0);
    date.setUTCSeconds(exp);

    return date;
  }
}


