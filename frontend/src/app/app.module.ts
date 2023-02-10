import { DatePipe } from '@angular/common';
import { HttpClientModule, HttpClientXsrfModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './auth/components/login/login.component';
import { UnauthorizedPageComponent } from './auth/components/unauthorized-page/unauthorized-page.component';
import { HttpRequestInterceptor } from './auth/interceptors/auth.interceptor';
import { HttpXsrfInterceptor } from './auth/interceptors/csrf.interceptor';
import { HomeComponent } from './home/home.component';
import { ResourcesModule } from './resources/resources.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UnauthorizedPageComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ResourcesModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpXsrfInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
