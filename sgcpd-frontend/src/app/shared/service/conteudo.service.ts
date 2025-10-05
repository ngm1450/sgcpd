import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedResponse } from '../types/paged-response.type';
import {Conteudo} from '../types/conteudo.type';
import {environment} from '../../../environments/environment';
import {ConteudoCriacao} from '../types/conteudo-criacao.type';
import {ConteudoCompleto} from '../types/conteudo-completo.type';
import {ConteudoAlteracao} from '../types/conteudo-alteracao.type';
import {Arquivo} from '../types/arquivo.type';

export interface ConteudoQuery {
  page?: number;        // 0-based
  size?: number;        // default 10/20
  textoBusca?: string;           // busca por título/corpo
  status?: string;      // RASCUNHO|PUBLICADO|ARQUIVADO
  idCategoria?: number; // filtro por categoria
  tagId?: number;       // opcional: filtro por tag
  sort?: string;        // ex.: "dataAtualizacao,desc"
}

@Injectable({ providedIn: 'root' })
export class ConteudoService {
  private readonly http = inject(HttpClient);

  private readonly baseUrl = `${environment.apiBaseUrl}/conteudos`; // ajuste se necessário

  listar(query: ConteudoQuery = {}): Observable<PagedResponse<Conteudo>> {
    let params = new HttpParams();

    if (query.page != null) params = params.set('page', query.page);
    if (query.size != null) params = params.set('size', query.size);

    if (query.textoBusca) params = params.set('textoBusca', query.textoBusca);
    if (query.status) params = params.set('status', query.status);
    if (query.idCategoria != null) params = params.set('idCategoria', query.idCategoria);

    if (query.sort) params = params.set('sort', query.sort);

    return this.http.get<PagedResponse<Conteudo>>(this.baseUrl, { params });
  }

  obter(id: number): Observable<ConteudoCompleto> {
    return this.http.get<ConteudoCompleto>(`${this.baseUrl}/${id}`);
  }

  criar(payload: ConteudoCriacao, files?: File[]): Observable<Conteudo> {
    const formData = this.buildFormData(payload, files);
    return this.http.post<Conteudo>(this.baseUrl, formData);
  }

  atualizar(id: number, payload: ConteudoAlteracao, files?: File[]): Observable<Conteudo> {
    const formData = this.buildFormData(payload, files);
    return this.http.put<Conteudo>(`${this.baseUrl}/${id}`, formData);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  private buildFormData(payload: any, files?: File[] | null): FormData {
    const fd = new FormData();

    const somenteExistentes = (payload.arquivos || []).filter((a: Arquivo) => !!a.id);
    const json = { ...payload, arquivos: somenteExistentes };

    fd.append('conteudo', new Blob([JSON.stringify(json)], { type: 'application/json' }));

    if (files && files.length) {
      for (const f of files) {
        fd.append('files', f, f.name);
      }
    }

    return fd;
  }

}
