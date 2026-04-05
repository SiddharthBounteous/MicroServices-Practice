import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
import {
  LoginRequest,
  RegisterRequest,
  LoginResponse
} from '../model/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = 'http://localhost:8080/api/v1/auth';
  private readonly AUTH_KEY = 'auth_user';

  constructor(private http: HttpClient) {}

  register(payload: RegisterRequest): Observable<string> {
    return this.http.post(`${this.authUrl}/register`, payload, {
      responseType: 'text'
    });
  }

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.authUrl}/login`, payload).pipe(
      tap((response) => this.saveAuth(response))
    );
  }

  verifyEmail(token: string): Observable<string> {
    return this.http.get(`${this.authUrl}/verify?token=${token}`, {
      responseType: 'text'
    });
  }

  saveAuth(data: LoginResponse): void {
    localStorage.setItem(this.AUTH_KEY, JSON.stringify(data));
  }

  getAuth(): LoginResponse | null {
    const raw = localStorage.getItem(this.AUTH_KEY);
    return raw ? JSON.parse(raw) : null;
  }

  getToken(): string | null {
    return this.getAuth()?.token ?? null;
  }

  getUserId(): string | null {
    return this.getAuth()?.userId ?? null;
  }

  getUsername(): string | null {
    return this.getAuth()?.username ?? null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem(this.AUTH_KEY);
  }
}