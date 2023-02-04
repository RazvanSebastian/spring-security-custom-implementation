import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription, take, tap } from 'rxjs';
import { SocialAuthConsentUriModel, SocialAuthOption } from '../../models/auth';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  readonly GOOGLE_AUTH_OPTION = SocialAuthOption.GOOGLE;
  readonly GITHUB_AUTH_OPTION = SocialAuthOption.GITHUB;

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

  onRedirectToGoogleConsent(option: SocialAuthOption) {
    this.authenticationService.getSocialAuthRedirectUri(option).subscribe({
      next: (response: SocialAuthConsentUriModel) => {
        window.location.href = response.authUri;
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
