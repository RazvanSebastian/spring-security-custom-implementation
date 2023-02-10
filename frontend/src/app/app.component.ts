import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { filter, Observable, of, switchMap, tap } from 'rxjs';
import { UserClaimsResponseModel, UserInfo } from './auth/models/auth';
import { AuthenticationService } from './auth/services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  isAuthenticated: boolean;
  userClaims: UserClaimsResponseModel;
  isAdmin: boolean;

  constructor(private authService: AuthenticationService, private router: Router) {
    authService.getCsrf().pipe().subscribe();
    this.authService.isAuthenticated$.subscribe(value => this.isAuthenticated = value);
    this.authService.authenticatedUserInfo$
      .pipe(filter(value => !!value && Object.keys(value).length > 0))
      .subscribe(value => {
        this.userClaims = value
        this.isAdmin = !!this.userClaims.grantedAuthorities.find(grantedAuthority => grantedAuthority.authority.startsWith("ROLE_ADMIN"));
      });
  }

  onLogout() {
    this.authService.logout().subscribe(() => {
      this.authService.isAuthenticated$.next(false);
      this.router.navigate(['/login']);
    });
  }
}
