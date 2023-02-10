import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/components/login/login.component';
import { UnauthorizedPageComponent } from './auth/components/unauthorized-page/unauthorized-page.component';
import { AdminAuthGuard } from './auth/guards/admin-auth.guard';
import { UserAuthGuard } from './auth/guards/user-auth.guard';
import { HomeComponent } from './home/home.component';
import { AdminResourcesComponent } from './resources/components/admin/admin-resources.component';
import { UserResourcesComponent } from './resources/components/user/user-resources.component';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent
  },
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
    component: UserResourcesComponent,
    canActivate: [UserAuthGuard]
  },
  {
    path: 'admin-dashboard',
    component: AdminResourcesComponent,
    canActivate: [AdminAuthGuard]
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: '**',
    component: HomeComponent
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule, ReactiveFormsModule],
  providers: [UserAuthGuard, AdminAuthGuard]
})
export class AppRoutingModule { }
