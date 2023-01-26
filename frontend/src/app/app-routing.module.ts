import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/components/login/login.component';
import { UnauthorizedPageComponent } from './auth/components/unauthorized-page/unauthorized-page.component';
import { AuthGuard } from './auth/guards/auth-guard';
import { ResourcesComponent } from './resources/component/resources.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '401',
    component: UnauthorizedPageComponent
  },
  {
    path: 'resources',
    component: ResourcesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule, ReactiveFormsModule],
  providers: [AuthGuard]
})
export class AppRoutingModule { }
