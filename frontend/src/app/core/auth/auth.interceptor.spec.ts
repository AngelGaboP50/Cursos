import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });
  });

  it('adds the bearer token to API requests', () => {
    localStorage.setItem('cursos.auth.token', 'jwt-demo');
    const auth = TestBed.inject(AuthService);
    const httpTesting = TestBed.inject(HttpTestingController);

    auth.loadCurrentUser().subscribe();

    const request = httpTesting.expectOne(`${environment.apiUrl}/account/me`);
    expect(request.request.headers.get('Authorization')).toBe('Bearer jwt-demo');
    request.flush({});
    httpTesting.verify();
  });
});
