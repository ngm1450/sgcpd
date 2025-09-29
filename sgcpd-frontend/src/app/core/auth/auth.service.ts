import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string, senha: string }): Observable<any> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
        })
      );
  }

  getUsuarioLogado(): { nome: string, perfil: string, matricula: string, sexo: 'M' | 'F' } {
    const token = this.getToken();
    const { nome, perfil, sub: matricula, sexo } = JSON.parse(atob(token.split('.')[1]));
    return { nome, perfil, matricula, sexo };
  }

  getPerfil(): string {
    const token = this.getToken();
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.perfil || null;
  }

  logout(): void {
    localStorage.removeItem('token');
    void this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string {
    const token =  localStorage.getItem('token');
    if (!token) {
      throw new Error('Não há usuário logado!')
    }
    return token;
  }
}
