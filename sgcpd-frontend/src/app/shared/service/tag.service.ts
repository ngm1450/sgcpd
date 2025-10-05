import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {Tag} from '../types/tag.type';

@Injectable({ providedIn: 'root' })
export class TagService {

  private readonly http = inject(HttpClient);

  private readonly baseUrl = `${environment.apiBaseUrl}/tags`;

  listar(idCategoria?: number): Observable<Tag[]> {
    return this.http.get<Tag[]>(this.baseUrl, { params: idCategoria && { idCategoria } || {} });
  }

}
