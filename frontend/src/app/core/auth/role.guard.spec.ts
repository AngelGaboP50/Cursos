import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, provideRouter, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { roleGuard } from './role.guard';

describe('roleGuard', () => {
  it('redirects a USER away from the ADMIN route', () => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: { hasRole: () => false } },
      ],
    });
    const route = { data: { role: 'ADMIN' } } as unknown as ActivatedRouteSnapshot;
    const result = TestBed.runInInjectionContext(() =>
      roleGuard(route, { url: '/admin' } as RouterStateSnapshot),
    );
    expect(TestBed.inject(Router).serializeUrl(result as never)).toBe('/account');
  });
});
