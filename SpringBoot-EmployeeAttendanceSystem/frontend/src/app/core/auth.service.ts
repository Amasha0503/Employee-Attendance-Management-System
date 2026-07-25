import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  token: string;
  username: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'eams-token';
  private readonly userKey = 'eams-user';
  private readonly apiUrl = 'http://localhost:8080/api/v1/auth/login';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl, { username, password }).pipe(
      tap((response) => this.storeSession(response))
    );
  }

  logout(): void {
    sessionStorage.removeItem(this.storageKey);
    sessionStorage.removeItem(this.userKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated();
  }

  getRole(): string | null {
    const user = this.getStoredUser();
    return user?.role ?? null;
  }

  getUserRole(): string | null {
    return this.getRole();
  }

  getUsername(): string | null {
    const user = this.getStoredUser();
    return user?.username ?? null;
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.storageKey);
  }

  private storeSession(response: AuthResponse): void {
    sessionStorage.setItem(this.storageKey, response.token);
    sessionStorage.setItem(this.userKey, JSON.stringify({ username: response.username, role: response.role }));
  }

  private getStoredUser(): { username: string; role: string } | null {
    const raw = sessionStorage.getItem(this.userKey);
    return raw ? JSON.parse(raw) : null;
  }
}
