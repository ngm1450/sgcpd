import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ArquivoService {
  private readonly http = inject(HttpClient);

  private readonly baseUrl = `${environment.apiBaseUrl}/arquivos`;

  buscarDadosPorId(idArquivo: number | undefined): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/buscar-dados/${idArquivo}`, {
      responseType: 'blob'
    });
  }

  baixarArquivo(idArquivo: number | undefined, nomeArquivo: string) : void {
    this.buscarDadosPorId(idArquivo).subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = nomeArquivo;
      a.click();
      URL.revokeObjectURL(url);
    });
  }

}
