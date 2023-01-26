import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReplaySubject, take, tap } from 'rxjs';
import { GoogleAuthConsentUriResponseModel } from '../../models/auth';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  isAuthenticated$: ReplaySubject<boolean>;
  loginForm: FormGroup;

  googleAuthConsentUri: string;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router) {
    this.isAuthenticated$ = this.authenticationService.isAuthenticated$;
  }

  ngOnInit(): void {
    this.initializetGoogleAuthConsentUri();
    this.initializeComponent();
  }

  ngOnDestroy(): void {
    this.isAuthenticated$.unsubscribe();
  }

  onLoginFormSubmit(): void {
    this.authenticationService.login(this.loginForm.value).subscribe(
      () => {
        window.alert("login success");
        this.router.navigate(['resources'])
      }
    )
  }

  private initializetGoogleAuthConsentUri() {
    this.authenticationService.getGoogleAuthConsentUri().subscribe({
      next: (response: GoogleAuthConsentUriResponseModel) => {
        this.googleAuthConsentUri = response.uri;
      }
    })
  }

  private initializeComponent() {
    this.isAuthenticated$.pipe(
      take(1),
      tap(isAuthenticated => {
        if (isAuthenticated) {
          this.router.navigate(['resources'])
        }
        else {
          this.initializeLoginForm();
        }
      })).subscribe()
  }

  private initializeLoginForm() {
    this.loginForm = this.formBuilder.group({
      principal: ['', [Validators.required]],
      credentials: ['', [Validators.required]]
    })
  }
}
