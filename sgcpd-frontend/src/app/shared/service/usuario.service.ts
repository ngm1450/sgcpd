import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedResponse } from '../types/paged-response.type';
import {Usuario} from '../types/usuario.type';
import {environment} from '../../../environments/environment';

export interface UsuarioQuery {
  page?: number;
  size?: number;
  q?: string;
  ativo?: boolean;
  sort?: string;
}

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/usuarios`; // ajuste se necessário

  listar(query: UsuarioQuery = {}): Observable<PagedResponse<Usuario>> {
    let params = new HttpParams();
    if (query.page != null) params = params.set('page', query.page);
    if (query.size != null) params = params.set('size', query.size);
    if (query.q) params = params.set('q', query.q);
    if (query.ativo != null) params = params.set('ativo', String(query.ativo));
    if (query.sort) params = params.set('sort', query.sort);

    return this.http.get<PagedResponse<Usuario>>(this.baseUrl, { params });
  }

  criar(payload: Partial<Usuario>) {
    return this.http.post<Usuario>(this.baseUrl, payload);
  }

  atualizar(id: number, payload: Partial<Usuario>) {
    return this.http.put<Usuario>(`${this.baseUrl}/${id}`, payload);
  }

  excluir(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
