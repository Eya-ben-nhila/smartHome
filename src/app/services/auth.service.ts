import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';

export type AppRole = 'ADMIN' | 'EMPLOYEE' | 'USER' | null;

interface LoginResponse {
  token: string;
  user: {
    id: string;
    fullName: string;
    email: string;
    role: AppRole;
    enabled?: boolean;
  };
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api';
  private readonly tokenKey = 'sm_token';
  private readonly userKey = 'sm_user';

  private readonly isBrowser: boolean;
  private roleSubject = new BehaviorSubject<AppRole>(null);
  role$ = this.roleSubject.asObservable();
  isAdmin$ = this.role$.pipe(map(role => role === 'ADMIN'));

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) platformId: object) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.roleSubject.next(this.getStoredRole());
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, { email, password }).pipe(
      tap((response) => {
        if (this.isBrowser) {
          localStorage.setItem(this.tokenKey, response.token);
          localStorage.setItem(this.userKey, JSON.stringify(response.user));
        }
        this.roleSubject.next(response.user.role);
      })
    );
  }

  register(fullName: string, email: string, password: string, role: 'ADMIN' | 'EMPLOYEE'): Observable<unknown> {
    return this.http.post(`${this.apiUrl}/auth/register`, { fullName, email, password, role });
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.userKey);
    }
    this.roleSubject.next(null);
  }

  getToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }
    return localStorage.getItem(this.tokenKey);
  }

  getRole(): AppRole {
    return this.roleSubject.value;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  private getStoredRole(): AppRole {
    if (!this.isBrowser) return null;
    const token = localStorage.getItem(this.tokenKey);
    const roleFromToken = this.extractRoleFromToken(token);
    if (roleFromToken) {
      return roleFromToken;
    }

    const rawUser = localStorage.getItem(this.userKey);
    if (!rawUser) return null;
    try {
      const parsed = JSON.parse(rawUser);
      return parsed.role ?? null;
    } catch {
      return null;
    }
  }

  private extractRoleFromToken(token: string | null): AppRole {
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded?.role ?? null;
    } catch {
      return null;
    }
  }
}
