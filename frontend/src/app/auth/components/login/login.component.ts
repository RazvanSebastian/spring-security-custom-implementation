import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      principal: ['', [Validators.required]],
      credentials: ['', [Validators.required]]
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
}
