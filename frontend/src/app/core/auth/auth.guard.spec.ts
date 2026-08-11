import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, provideRouter, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  it('redirects an unauthenticated visitor to login', () => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: { authenticated: () => false } },
      ],
    });
    const result = TestBed.runInInjectionContext(() =>
      authGuard(
        {} as ActivatedRouteSnapshot,
        { url: '/account' } as RouterStateSnapshot,
      ),
    );
    expect(TestBed.inject(Router).serializeUrl(result as never)).toBe(
      '/login?returnUrl=%2Faccount',
    );
  });
});
