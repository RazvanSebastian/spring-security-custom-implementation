import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { catchError, map, Observable, of, tap } from "rxjs";
import { AuthenticationService } from "../services/authentication.service";

@Injectable()
export class UserAuthGuard implements CanActivate {

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return this.authenticationService.authDetails().pipe(
      tap(() => this.authenticationService.isAuthenticated$.next(true)),
      map(() => true),
      catchError(() => {
        this.authenticationService.isAuthenticated$.next(false)
        this.router.navigate(['login'])
        return of(false)
      })
    )
  }

}
