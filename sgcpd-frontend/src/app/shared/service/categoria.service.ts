import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedResponse } from '../types/paged-response.type';
import {Conteudo} from '../types/conteudo.type';
import {environment} from '../../../environments/environment';
import {Categoria} from '../types/categoria.type';

export interface ConteudoQuery {
  page?: number;        // 0-based
  size?: number;        // default 10/20
  q?: string;           // busca por título/corpo
  status?: string;      // RASCUNHO|PUBLICADO|ARQUIVADO
  categoriaId?: number; // filtro por categoria
  tagId?: number;       // opcional: filtro por tag
  sort?: string;        // ex.: "dataAtualizacao,desc"
}

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private readonly http = inject(HttpClient);

  private readonly baseUrl = `${environment.apiBaseUrl}/categorias`;

  listar(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.baseUrl);
  }

}
