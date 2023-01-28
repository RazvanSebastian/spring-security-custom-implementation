import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, switchMap, tap } from 'rxjs';
import { UserInfo } from './auth/models/auth';
import { AuthenticationService } from './auth/services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  isAuthenticated: boolean;
  userInfo: UserInfo;

  constructor(private authService: AuthenticationService, private router: Router) {
    authService.getCsrf().pipe().subscribe();
    this.authService.isAuthenticated$.subscribe(value => this.isAuthenticated = value);
    this.authService.authenticatedUserInfo$.subscribe(value => this.userInfo = value);
  }

  onLogout() {
    this.authService.logout().subscribe(() => {
      this.authService.isAuthenticated$.next(false);
      this.router.navigate(['/login']);
    });
  }
}
