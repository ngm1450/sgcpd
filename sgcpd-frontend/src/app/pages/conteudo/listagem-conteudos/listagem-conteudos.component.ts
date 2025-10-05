import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import {debounceTime, switchMap, tap} from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import {PagedResponse} from '../../../shared/types/paged-response.type';
import {Conteudo} from '../../../shared/types/conteudo.type';
import {ConteudoService} from '../../../shared/service/conteudo.service';
import {StatusConteudoEnum} from '../../../shared/enums/status-conteudo.enum';
import {NovoConteudoModalComponent} from '../novo-conteudo-modal/novo-conteudo-modal.component';
import {ConfirmDialogComponent} from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import {VisualizarConteudoModalComponent} from '../visualizacao-conteudo-modal/visualizacao-conteudo-modal.component';
import {Categoria} from '../../../shared/types/categoria.type';
import {CategoriaService} from '../../../shared/service/categoria.service';
import {Observable} from 'rxjs';
import {Arquivo} from '../../../shared/types/arquivo.type';

@Component({
  selector: 'app-listagem-conteudos',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NovoConteudoModalComponent,
    ConfirmDialogComponent,
    VisualizarConteudoModalComponent
  ],
  templateUrl: './listagem-conteudos.component.html',
  styleUrls: ['./listagem-conteudos.component.scss']
})
export class ListagemConteudosComponent implements OnInit {

  private readonly svc = inject(ConteudoService);
  private categoriaService = inject(CategoriaService);
  private readonly fb = inject(FormBuilder);

  // estado
  carregando = signal(false);
  erro = signal<string | null>(null);
  dados = signal<PagedResponse<Conteudo> | null>(null);

  // filtros e paginação
  page = signal(0);
  size = signal(10);
  sort = signal('dataAtualizacao,desc');

  readonly filtroForm = this.fb.nonNullable.group({
    textoBusca: [''],
    status: [StatusConteudoEnum.PUBLICADO],
    idCategoria: ['']
  });

  readonly statusOpcoes: { label: string; value: StatusConteudoEnum }[] = [
    { label: 'Rascunho', value: StatusConteudoEnum.RASCUNHO },
    { label: 'Publicado', value: StatusConteudoEnum.PUBLICADO },
    { label: 'Arquivado', value: StatusConteudoEnum.ARQUIVADO }
  ];

  readonly categorias: Observable<Categoria[]> = this.categoriaService.listar();

  novoOpen = false;
  confirmOpen = false;

  deleting = false;
  toDelete: { id: number; titulo: string } | null = null;

  viewOpen = false;
  viewId: number | null = null;

  editOpen = false;
  editId: number | null = null;

  ngOnInit(): void {
    // busca inicial
    this.carregar();

    // pesquisa reativa com debounce
    this.filtroForm.get('textoBusca')!.valueChanges
      .pipe(
        debounceTime(300),
        tap(() => this.page.set(0)),
        switchMap(() => this.buscar())
      )
      .subscribe();

    // mudanças em status/categoria
    this.filtroForm.get('status')!.valueChanges
      .pipe(tap(() => { this.page.set(0); this.carregar(); }))
      .subscribe();

    this.filtroForm.get('idCategoria')!.valueChanges
      .pipe(tap(() => { this.page.set(0); this.carregar(); }))
      .subscribe();
  }

  private buscar() {
    this.carregando.set(true);
    this.erro.set(null);
    const { textoBusca, status, idCategoria } = this.filtroForm.getRawValue();

    return this.svc.listar({
      page: this.page(),
      size: this.size(),
      sort: this.sort(),
      textoBusca: textoBusca?.trim() || undefined,
      status: status || undefined,
      idCategoria: idCategoria ? Number(idCategoria) : undefined
    }).pipe(
      tap({
        next: (res) => this.dados.set(res),
        error: (e) => this.erro.set(e?.message ?? 'Erro ao carregar conteúdos'),
        finalize: () => this.carregando.set(false)
      })
    );
  }

  carregar(): void {
    this.buscar().subscribe();
  }

  trocarPagina(delta: number): void {
    const atual = this.page();
    const total = this.dados()?.totalPages ?? 1;
    const prox = Math.min(Math.max(atual + delta, 0), Math.max(total - 1, 0));
    if (prox !== atual) {
      this.page.set(prox);
      this.carregar();
    }
  }

  ordenar(campo: string): void {
    const atual = this.sort();
    const asc = atual.startsWith(`${campo},asc`);
    this.sort.set(`${campo},${asc ? 'desc' : 'asc'}`);
    this.carregar();
  }

  limparFiltros(): void {
    this.filtroForm.reset(
      { textoBusca: '', status: StatusConteudoEnum.PUBLICADO, idCategoria: '' },
      { emitEvent: false }
    );
    this.page.set(0);
    this.carregar();
  }

  openNovoConteudo() {
    this.novoOpen = true;
    document.body.style.overflow = 'hidden';
  }

  onNovoClosed() {
    this.novoOpen = false;
    document.body.style.overflow = '';
    this.carregar();
  }

  onConteudoSaved(conteudo: any): void {
    this.svc.criar(conteudo, this.filterNewFiles(conteudo)).subscribe({
      next: (_resp) => this.carregar(),
      error: () => console.log('deu erro')
    });
  }

  onConteudoEdited(conteudo: any) {
    this.svc.atualizar(conteudo.id, conteudo, this.filterNewFiles(conteudo)).subscribe({
      next: (_resp) => this.carregar(),
      error: () => console.log('deu erro')
    });
  }

  statusBadgeClass(status: string) {
    switch (status) {
      case StatusConteudoEnum.PUBLICADO: return 'text-bg-success';
      case StatusConteudoEnum.RASCUNHO:  return 'text-bg-secondary';
      case StatusConteudoEnum.ARQUIVADO: return 'text-bg-dark';
      default:          return 'text-bg-light text-dark';
    }
  }

  confirmDelete() {
    if (!this.toDelete || this.deleting) return;

    this.deleting = true;
    this.svc.excluir(this.toDelete.id).subscribe({
      next: () => {
        this.deleting = false;
        this.confirmOpen = false;
        this.carregar();
      },
      error: (err) => {
        this.deleting = false;
        alert('Falha ao excluir: ' + (err?.error?.message || 'tente novamente.'));
      }
    });
  }

  askDelete(c: { id: number; titulo: string }) {
    this.toDelete = { id: c.id, titulo: c.titulo };
    this.confirmOpen = true;
    document.body.style.overflow = 'hidden';
  }

  cancelDelete() {
    if (this.deleting) return;
    this.confirmOpen = false;
    this.toDelete = null;
  }

  openView(c: { id: number }) {
    this.viewId = c.id;
    this.viewOpen = true;
    document.body.style.overflow = 'hidden';
  }

  openEdit(c: { id: number }) {
    this.editId = c.id;
    this.editOpen = true;
    document.body.style.overflow = 'hidden';
  }

  filterNewFiles(conteudo: any): File[] {
    return (conteudo.arquivos ?? [])
      .filter((a: Arquivo) => !!a.file)
      .map((a: Arquivo) => a.file);
  }

}
