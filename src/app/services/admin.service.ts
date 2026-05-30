import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdminUser {
  id: string;
  fullName: string;
  email: string;
  role: 'ADMIN' | 'EMPLOYEE' | 'USER';
  enabled: boolean;
  profileImageUrl?: string | null;
}

export interface CreateUserPayload {
  fullName: string;
  email: string;
  password: string;
  role: 'ADMIN' | 'EMPLOYEE';
  enabled: boolean;
  profileImageUrl?: string;
}

export interface UpdateUserPayload {
  fullName?: string;
  role?: 'ADMIN' | 'EMPLOYEE';
  enabled?: boolean;
  password?: string;
  profileImageUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly apiUrl = 'http://localhost:8080/api/admin/users';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(this.apiUrl);
  }

  createUser(payload: CreateUserPayload): Observable<AdminUser> {
    return this.http.post<AdminUser>(this.apiUrl, payload);
  }

  updateUser(id: string, payload: UpdateUserPayload): Observable<AdminUser> {
    return this.http.put<AdminUser>(`${this.apiUrl}/${id}`, payload);
  }

  disableUser(id: string): Observable<unknown> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  deleteUser(id: string): Observable<unknown> {
    return this.http.delete(`${this.apiUrl}/${id}?hardDelete=true`);
  }
}
