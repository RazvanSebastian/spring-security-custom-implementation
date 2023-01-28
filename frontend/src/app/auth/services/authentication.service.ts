import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { of } from 'rxjs/internal/observable/of';
import { catchError } from 'rxjs/internal/operators/catchError';
import { map } from 'rxjs/internal/operators/map';
import { ReplaySubject } from 'rxjs/internal/ReplaySubject';
import { GoogleAuthConsentUriResponseModel, LoginModel, UserInfo } from '../models/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  isAuthenticated$ = new ReplaySubject<boolean>(1);
  authenticatedUserInfo$ = new BehaviorSubject<UserInfo>({} as UserInfo);

  constructor(private httpClient: HttpClient) {
    this.authDetails().pipe(
      map(() => this.isAuthenticated$.next(true)),
      catchError(() => {
        this.isAuthenticated$.next(false);
        return of(false);
      })
    ).subscribe();
  }

  authDetails() {
    return this.httpClient.get<UserInfo>('/api/auth/details').pipe(
      tap(userInfo => this.authenticatedUserInfo$.next(userInfo))
    );
  }

  login(loginModel: LoginModel) {
    return this.httpClient.post('/api/auth', JSON.stringify(loginModel));
  }

  logout() {
    return this.httpClient.get('/api/auth/logout');
  }

  getCsrf() {
    return this.httpClient.get('/api/auth/csrf');
  }

  getRedirectToGoogleConsent() {
    return this.httpClient.get<GoogleAuthConsentUriResponseModel>('/api/google-auth/consent');
  }
}
