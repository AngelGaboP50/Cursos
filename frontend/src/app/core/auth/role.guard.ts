import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthUser } from './auth.models';
import { AuthService } from './auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const expectedRole = route.data['role'] as AuthUser['role'];
  return auth.hasRole(expectedRole) ? true : router.createUrlTree(['/account']);
};
