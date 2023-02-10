import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { catchError, map, Observable, of, tap } from "rxjs";
import { UserClaimsResponseModel } from "../models/auth";
import { AuthenticationService } from "../services/authentication.service";

@Injectable()
export class AdminAuthGuard implements CanActivate {
    constructor(private authenticationService: AuthenticationService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        return this.authenticationService.authDetails().pipe(
            map((userClaims: UserClaimsResponseModel) =>
                !!userClaims.grantedAuthorities.find(grantedAuthority => grantedAuthority.authority.startsWith("ROLE_ADMIN"))
            ),
            tap(() => this.authenticationService.isAuthenticated$.next(true)),
            catchError(() => {
                this.authenticationService.isAuthenticated$.next(false)
                this.router.navigate(['/home'])
                return of(false)
            })
        )
    }
}