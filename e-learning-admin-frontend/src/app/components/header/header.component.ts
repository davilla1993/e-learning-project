import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  userSub!: Subscription;
  isAuthenticated: boolean = false;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe(loggedUser => {
      this.isAuthenticated = !!loggedUser;
    });

  }

  logout() {
    throw new Error('Method not implemented.');
    }

}
