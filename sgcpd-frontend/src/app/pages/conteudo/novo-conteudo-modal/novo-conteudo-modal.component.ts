import { Component, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { take } from 'rxjs/operators';

import { CategoriaService } from '../../../shared/service/categoria.service';
import { TagService } from '../../../shared/service/tag.service';
import { ConteudoService } from '../../../shared/service/conteudo.service';

import { Categoria } from '../../../shared/types/categoria.type';
import { Tag } from '../../../shared/types/tag.type';
import { StatusConteudoEnum } from '../../../shared/enums/status-conteudo.enum';
import { ConteudoCriacao } from '../../../shared/types/conteudo-criacao.type';
import { ConteudoAlteracao } from '../../../shared/types/conteudo-alteracao.type';
import { ConteudoCompleto } from '../../../shared/types/conteudo-completo.type';
import {QuillWrapperComponent} from '../../../shared/components/quill-wrapper/quill-wrapper.component';
import {Arquivo} from '../../../shared/types/arquivo.type';
import {ArquivoService} from '../../../shared/service/arquivo.service';

type ModalMode = 'create' | 'edit';

@Component({
  selector: 'app-novo-conteudo-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, QuillWrapperComponent],
  templateUrl: './novo-conteudo-modal.component.html',
  styleUrls: ['./novo-conteudo-modal.component.scss'],
})
export class NovoConteudoModalComponent implements OnChanges {
  @Input({ required: true }) open = false;

  files: Arquivo[] = [];

  @Input() mode: ModalMode = 'create';

  @Input() editId: number | null = null;

  @Output() saved  = new EventEmitter<ConteudoCriacao | ConteudoAlteracao>();
  @Output() closed = new EventEmitter<void>();

  private categoriaService = inject(CategoriaService);
  private tagService       = inject(TagService);
  private conteudoService  = inject(ConteudoService);
  protected arquivoService = inject(ArquivoService);

  categorias: Categoria[] = [];
  tags: Tag[] = [];

  loadingCategorias = false;
  loadingTags = false;
  loadingConteudo = false;

  errorCategorias: string | null = null;
  errorTags: string | null = null;
  errorConteudo: string | null = null;

  form: { titulo: string; corpo: string; idCategoria: number | null; status: StatusConteudoEnum } = {
    titulo: '',
    corpo: '',
    idCategoria: null,
    status: StatusConteudoEnum.RASCUNHO
  };
  selectedTagIds = new Set<number>();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['open']?.currentValue && !changes['open']?.previousValue) {
      this.reset();
      this.loadCategorias(() => {
        if (this.mode === 'edit' && this.editId != null) {
          this.loadConteudo(this.editId);
        }
      });
    }
  }

  /** 1) categorias; opcionalmente roda um callback depois (útil na edição) */
  private loadCategorias(after?: () => void) {
    this.loadingCategorias = true;
    this.errorCategorias = null;

    this.categoriaService.listar().pipe(take(1)).subscribe({
      next: (cats) => {
        this.categorias = cats ?? [];
        this.loadingCategorias = false;
        after?.();
      },
      error: () => {
        this.errorCategorias = 'Falha ao carregar categorias.';
        this.loadingCategorias = false;
        after?.();
      }
    });
  }

  /** 2) conteúdo na edição: preenche form e busca tags da categoria */
  private loadConteudo(id: number) {
    this.loadingConteudo = true;
    this.errorConteudo = null;

    this.conteudoService.obter(id).pipe(take(1)).subscribe({
      next: (dados) => {
        this.form.titulo = dados.titulo ?? '';
        this.form.corpo  = dados.corpo ?? '';
        this.form.status = (dados.status as StatusConteudoEnum) ?? StatusConteudoEnum.RASCUNHO;

        this.form.idCategoria = (dados as any).idCategoria ?? null;

        if (this.form.idCategoria != null) {
          this.loadTagsForCategoria(this.form.idCategoria, () => {
            const ids = (dados.idsTags ?? []);
            this.selectedTagIds = new Set<number>(ids);
          });
        }

        this.files = dados.arquivos;

        this.loadingConteudo = false;
      },
      error: () => {
        this.errorConteudo = 'Falha ao carregar o conteúdo para edição.';
        this.loadingConteudo = false;
      }
    });
  }

  private loadTagsForCategoria(idCategoria: number, after?: () => void) {
    this.loadingTags = true;
    this.errorTags = null;
    this.tags = [];
    this.selectedTagIds.clear();

    this.tagService.listar(idCategoria).pipe(take(1)).subscribe({
      next: (tags) => {
        this.tags = tags ?? [];
        this.loadingTags = false;
        after?.();
      },
      error: () => {
        this.errorTags = 'Falha ao carregar tags da categoria.';
        this.loadingTags = false;
        after?.();
      }
    });
  }

  onCategoriaChange(idCategoria: number | null) {
    this.form.idCategoria = idCategoria;
    if (idCategoria == null) {
      this.tags = [];
      this.selectedTagIds.clear();
      return;
    }
    this.loadTagsForCategoria(idCategoria);
  }

  onBackdropClick() { this.close(); }
  stop(e: Event) { e.stopPropagation(); }
  onKeydown(e: KeyboardEvent) { if (e.key === 'Escape') this.close(); }

  toggleTag(tagId: number, checked: boolean) {
    if (checked) this.selectedTagIds.add(tagId);
    else this.selectedTagIds.delete(tagId);
  }

  canSave(): boolean {
    if (this.mode === 'edit' && (this.loadingConteudo || this.loadingCategorias)) return false;

    return !!this.form.titulo?.trim()
      && !!this.form.corpo?.trim()
      && this.form.idCategoria != null
      && this.selectedTagIds.size > 0;
  }

  save() {
    if (!this.canSave()) return;
    console.log(this.files)
    let payload = {
      titulo: this.form.titulo.trim(),
      corpo: this.form.corpo!.trim(),
      idCategoria: this.form.idCategoria!,
      status: this.form.status,
      idsTags: Array.from(this.selectedTagIds.values()),
      arquivos: this.files
    } as any;

    if (this.mode === 'edit' && this.editId != null) {
      payload = { ...payload, id: this.editId }
    }

    this.saved.emit(payload);
    this.reset();
    this.close();
  }

  close() { this.closed.emit(); }

  private reset() {
    this.form = { titulo: '', corpo: '', idCategoria: null, status: StatusConteudoEnum.RASCUNHO };
    this.selectedTagIds.clear();
    this.tags = [];

    this.errorCategorias = this.errorTags = this.errorConteudo = null;
    this.loadingCategorias = this.loadingTags = this.loadingConteudo = false;
  }

  onFilesSelected(evt: Event) {
    const input = evt.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const chosen = Array.from(input.files);

    const existingKeys = new Set(this.files.map(f => `${f.nome}|${f.tamanho}`));

    const toAdd: Arquivo[] = chosen
      .filter(f => !existingKeys.has(`${f.name}|${f.size}`))
      .map(f => ({
        nome: f.name,
        tamanho: f.size,
        file: f
      }));

    this.files = this.files.concat(toAdd);
    input.value = '';
  }

  removeFile(index: number) {
    this.files.splice(index, 1);
  }

}
