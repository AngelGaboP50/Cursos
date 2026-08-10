import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { Observable, catchError, finalize, of, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, AuthUser, LoginRequest, RegisterRequest } from './auth.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenKey = 'cursos.auth.token';
  private readonly userKey = 'cursos.auth.user';
  private readonly currentUser = signal<AuthUser | null>(this.readUser());

  readonly user = this.currentUser.asReadonly();
  readonly isAdmin = computed(() => this.currentUser()?.role === 'ADMIN');

  register(request: RegisterRequest): Observable<AuthUser> {
    return this.http.post<AuthUser>(`${environment.apiUrl}/auth/register`, request);
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap((response) => this.storeSession(response)),
    );
  }

  logout(): Observable<unknown> {
    const request = this.getToken()
      ? this.http.post<void>(`${environment.apiUrl}/auth/logout`, {})
      : of(undefined);
    return request.pipe(
      catchError(() => of(undefined)),
      finalize(() => this.clearSession()),
    );
  }

  loadCurrentUser(): Observable<AuthUser> {
    return this.http.get<AuthUser>(`${environment.apiUrl}/account/me`).pipe(
      tap((user) => {
        this.currentUser.set(user);
        this.storage()?.setItem(this.userKey, JSON.stringify(user));
      }),
    );
  }

  updateProfile(name: string): Observable<AuthUser> {
    return this.http.patch<AuthUser>(`${environment.apiUrl}/account/me`, { name }).pipe(
      tap((user) => {
        this.currentUser.set(user);
        this.storage()?.setItem(this.userKey, JSON.stringify(user));
      }),
    );
  }

  checkAdminAccess(): Observable<{ message: string; user: string }> {
    return this.http.get<{ message: string; user: string }>(
      `${environment.apiUrl}/admin/security-check`,
    );
  }

  getToken(): string | null {
    return this.storage()?.getItem(this.tokenKey) ?? null;
  }

  hasRole(role: AuthUser['role']): boolean {
    return this.hasValidSession() && this.currentUser()?.role === role;
  }

  authenticated(): boolean {
    return this.hasValidSession();
  }

  clearSession(): void {
    const storage = this.storage();
    storage?.removeItem(this.tokenKey);
    storage?.removeItem(this.userKey);
    this.currentUser.set(null);
  }

  private storeSession(response: AuthResponse): void {
    const storage = this.storage();
    storage?.setItem(this.tokenKey, response.token);
    storage?.setItem(this.userKey, JSON.stringify(response.user));
    this.currentUser.set(response.user);
  }

  private hasValidSession(): boolean {
    const token = this.getToken();
    if (!token || !this.currentUser()) {
      return false;
    }
    try {
      const payload = JSON.parse(this.decodeBase64Url(token.split('.')[1] ?? '')) as { exp?: number };
      return typeof payload.exp === 'number' && payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  private decodeBase64Url(value: string): string {
    const normalized = value.replace(/-/g, '+').replace(/_/g, '/');
    const padding = '='.repeat((4 - (normalized.length % 4)) % 4);
    return decodeURIComponent(
      Array.from(atob(normalized + padding))
        .map((character) => `%${character.charCodeAt(0).toString(16).padStart(2, '0')}`)
        .join(''),
    );
  }

  private readUser(): AuthUser | null {
    try {
      const value = this.storage()?.getItem(this.userKey);
      return value ? (JSON.parse(value) as AuthUser) : null;
    } catch {
      return null;
    }
  }

  private storage(): Storage | null {
    return typeof localStorage === 'undefined' ? null : localStorage;
  }
}
