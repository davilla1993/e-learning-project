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
import { Student } from '../models/student.model';
import { Instructor } from '../models/instructor.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  jwtHelperService = new JwtHelperService();
  user = new BehaviorSubject<LoggedUser | null>(null);
  tokenExpirationTimer: any;

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
    this.autoLogout(this.getExpirationDate(decodedAccessToken.exp).valueOf() - new Date().valueOf());
    localStorage.setItem('userData', JSON.stringify(loggedUser));

    this.redirectLoggedInUser(decodedAccessToken, jwtTokens.accessToken);
  }

  redirectLoggedInUser(decodedToken: any, accessToken: string) {
    if(decodedToken.roles.includes('ADMIN')) this.router.navigateByUrl("/courses");

    else if (decodedToken.roles.includes('INSTRUCTOR'))
      this.instructorService.loadInstructorByEmail(decodedToken.sub).subscribe(instructor=>{
        const loggedUser = new LoggedUser(decodedToken.sub, decodedToken.roles, accessToken, this.getExpirationDate(decodedToken.exp), undefined, instructor);
        this.user.next(loggedUser);
        localStorage.setItem('userData', JSON.stringify(loggedUser));
        this.router.navigateByUrl("/instructor-courses/"+instructor.instructorId);
      })

    else if (decodedToken.roles.includes('STUDENT'))
      this.studentService.loadStudentByEmail(decodedToken.sub).subscribe(student=>{
        const loggedUser = new LoggedUser(decodedToken.sub, decodedToken.roles, accessToken, this.getExpirationDate(decodedToken.exp), student, undefined);
        this.user.next(loggedUser);
        localStorage.setItem('userData', JSON.stringify(loggedUser));
        this.router.navigateByUrl("/student-courses/"+student.studentId);
      })
  }

  autoLogin() {
    const userData: {
      username: string,
      roles: string[],
      _token: string,
      _expiration: Date,
      student: Student | undefined,
      instructor: Instructor | undefined
    } = JSON.parse(localStorage.getItem('userData') || '{}');
    if(!userData) return;
     const loadedUser = new LoggedUser(userData.username, userData.roles, userData._token, new Date(userData._expiration), userData.student, userData.instructor);
      if(loadedUser.token) {
        this.user.next(loadedUser);
        this.autoLogout(loadedUser._expiration.valueOf() - new Date().valueOf());
      }
    }

    logout() {
      localStorage.clear();
      this.user.next(null);
      this.router.navigate(['/']);
      if(this.tokenExpirationTimer) {
        clearTimeout(this.tokenExpirationTimer);
      }
      this.tokenExpirationTimer = null;
    }

    autoLogout(expirationDuration: number) {
     this.tokenExpirationTimer = setTimeout(() => {
        this.logout();
      }, expirationDuration);
    }

  getExpirationDate(exp: number){
    const date = new Date(0);
    date.setUTCSeconds(exp);

    return date;
  }

  refreshInstructor(instructor: Instructor){
   const userData: {
    username: string,
    roles: string[],
    _token: string,
    _expiration: Date,
    student: Student | undefined,
    instructor: Instructor | undefined
   } = JSON.parse(localStorage.getItem('userData') || '{}');
   if(!userData) return;
    const loggedUser = new LoggedUser(userData.username, userData.roles, userData._token, userData._expiration, userData.student, instructor);
    if(loggedUser.token){
      this.user.next(loggedUser);
      localStorage.setItem('userData', JSON.stringify(loggedUser));
    }
  }

  refreshStudent(student: Student){
    const userData: {
     username: string,
     roles: string[],
     _token: string,
     _expiration: Date,
     student: Student | undefined,
     instructor: Instructor | undefined
    } = JSON.parse(localStorage.getItem('userData') || '{}');
    if(!userData) return;
     const loggedUser = new LoggedUser(userData.username, userData.roles, userData._token, userData._expiration, student, userData.instructor);
     if(loggedUser.token){
      this.user.next(loggedUser);
      localStorage.setItem('userData', JSON.stringify(loggedUser));
    }
   }
}


