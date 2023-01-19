import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginModel } from '../models/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private httpClient: HttpClient) { }

  login(loginModel: LoginModel) {
    return this.httpClient.post('/api/auth', JSON.stringify(loginModel));
  }

  getCsrf() {
    return this.httpClient.get('/api/auth/csrf');
  }
}
