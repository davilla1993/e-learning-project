import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { map, Observable, take } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.authService.user.pipe(take(1), map(user => {
      const isAuth = !!user;
      if(isAuth){
        if(user?.roles.includes(route.data['role'])) return true;
        else if(user?.roles.includes('ADMIN')) return this.router.createUrlTree(['/courses']);
        else if(user?.roles.includes('INSTRUCTOR')) return this.router.createUrlTree(['/instructor-courses/' + user?.instructor?.instructorId]);
        else if(user?.roles.includes('STUDENT')) return this.router.createUrlTree(['/student-courses/' + user?.student?.studentId]);
      }
      return this.router.createUrlTree(['/auth']);
    }))
  }
}
