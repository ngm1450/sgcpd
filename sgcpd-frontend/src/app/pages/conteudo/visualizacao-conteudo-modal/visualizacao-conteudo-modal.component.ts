import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConteudoService } from '../../../shared/service/conteudo.service';
import { ConteudoCompleto } from '../../../shared/types/conteudo-completo.type';
import {ArquivoService} from '../../../shared/service/arquivo.service';

@Component({
  selector: 'app-visualizar-conteudo-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './visualizacao-conteudo-modal.component.html',
  styleUrls: ['./visualizacao-conteudo-modal.component.scss']
})
export class VisualizarConteudoModalComponent implements OnChanges {
  @Input({ required: true }) open = false;
  @Input({ required: true }) conteudoId: number | null = null;

  @Output() closed = new EventEmitter<void>();

  private conteudoService = inject(ConteudoService);
  protected arquivoService = inject(ArquivoService);

  loading = false;
  error: string | null = null;
  dados: ConteudoCompleto | null = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['open']?.currentValue && !changes['open']?.previousValue) {
      this.load();
    }
  }

  private load() {
    this.error = null;
    this.dados = null;

    if (!this.conteudoId) return;
    this.loading = true;
    this.conteudoService.obter(this.conteudoId).subscribe({
      next: (resp) => { this.dados = resp; this.loading = false; },
      error: () => { this.error = 'Falha ao carregar conteúdo.'; this.loading = false; }
    });
  }

  onBackdropClick() { this.close(); }

  onKeydown(e: KeyboardEvent) { if (e.key === 'Escape') this.close(); }

  stop(e: Event) { e.stopPropagation(); }

  close() { this.closed.emit(); }

}
