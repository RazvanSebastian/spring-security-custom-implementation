import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReplaySubject, Subscription, take, tap } from 'rxjs';
import { GoogleAuthConsentUriResponseModel } from '../../models/auth';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  isAuthenticatedSubscription: Subscription;
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router) {
  }

  ngOnInit(): void {
    this.initializeComponent();
  }

  ngOnDestroy(): void {
    this.isAuthenticatedSubscription.unsubscribe();
  }

  onRedirectToGoogleConsent() {
    this.authenticationService.getRedirectToGoogleConsent().subscribe({
      next: (response: GoogleAuthConsentUriResponseModel) => {
        window.location.href = response.uri;
      }
    })
  }

  onLoginFormSubmit(): void {
    this.authenticationService.login(this.loginForm.value).subscribe(
      () => {
        window.alert("login success");
        this.router.navigate(['resources'])
      }
    )
  }

  private initializeComponent() {
    this.isAuthenticatedSubscription = this.authenticationService.isAuthenticated$.pipe(
      take(1),
      tap(isAuthenticated => {
        if (isAuthenticated) {
          this.router.navigate(['resources'])
        }
        else {
          this.initializeLoginForm();
        }
      })).subscribe();
  }

  private initializeLoginForm() {
    this.loginForm = this.formBuilder.group({
      principal: ['', [Validators.required]],
      credentials: ['', [Validators.required]]
    })
  }
}
