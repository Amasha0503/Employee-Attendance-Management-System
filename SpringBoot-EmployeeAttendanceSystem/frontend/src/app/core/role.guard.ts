import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const expectedRole = route.data['role'] as string;
    const role = this.authService.getRole();

    if (role === expectedRole) {
      return true;
    }

    if (this.authService.isLoggedIn()) {
      return this.router.createUrlTree(['/attendance']);
    }

    return this.router.createUrlTree(['/login']);
  }
}
