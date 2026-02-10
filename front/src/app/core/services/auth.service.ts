import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../models/auth.interface';
import { UpdateProfileRequest, User } from '../../models/user.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, request).pipe(
      tap(response => this.setToken(response.token))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, request).pipe(
      tap(response => this.setToken(response.token))
    );
  }

  me(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user/me`);
  }

  updateProfile(request: UpdateProfileRequest): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/user/me`, request);
  }

  getToken(): string | null {
    return localStorage.getItem('mdd_token');
  }

  setToken(token: string): void {
    localStorage.setItem('mdd_token', token);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem('mdd_token');
  }
}
