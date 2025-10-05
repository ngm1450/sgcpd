import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, switchMap, tap } from 'rxjs/operators';
import {UsuarioService} from '../../../shared/service/usuario.service';
import {PagedResponse} from '../../../shared/types/paged-response.type';
import {Usuario} from '../../../shared/types/usuario.type';
import {NovoUsuarioModalComponent} from '../novo-usuario-modal/novo-usuario-modal.component';
import {ConfirmDialogComponent} from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import {AuthService} from '../../../core/auth/auth.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-listagem-usuarios',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NovoUsuarioModalComponent, ConfirmDialogComponent],
  templateUrl: './listagem-usuarios.component.html',
  styleUrls: ['./listagem-usuarios.component.scss']
})
export class ListagemUsuariosComponent implements OnInit {
  private readonly svc = inject(UsuarioService);
  private readonly fb = inject(FormBuilder);
  private toastr = inject(ToastrService);
  protected authService: AuthService = inject(AuthService);

  carregando = signal(false);
  erro = signal<string | null>(null);
  dados = signal<PagedResponse<Usuario> | null>(null);

  page = signal(0);
  size = signal(5);
  sort = signal('dataAtualizacao,desc');

  filtroForm = this.fb.nonNullable.group({
    q: [''],
    ativo: [true] as any
  });

  modalUsuarioAberto = false as boolean;
  modalUsuarioMode: 'create' | 'edit' = 'create';
  usuarioSelecionado?: Usuario;

  confirmOpen = false;

  deleting = false;
  toDelete: { id: number; nome: string } | null = null;

  ngOnInit(): void {
    this.carregar();

    this.filtroForm.get('q')!.valueChanges
      .pipe(debounceTime(300), tap(() => this.page.set(0)), switchMap(() => this.buscar()))
      .subscribe();

    this.filtroForm.get('ativo')!.valueChanges
      .pipe(tap(() => { this.page.set(0); this.carregar(); }))
      .subscribe();
  }

  private buscar() {
    this.carregando.set(true);
    this.erro.set(null);

    const { q, ativo } = this.filtroForm.getRawValue();
    const ativoBool = ativo === '' ? undefined : ativo === true;

    return this.svc.listar({
      page: this.page(),
      size: this.size(),
      sort: this.sort(),
      q: q?.trim() || undefined,
      ativo: ativoBool
    }).pipe(
      tap({
        next: res => this.dados.set(res),
        error: e => this.erro.set(e?.message ?? 'Erro ao carregar usuários'),
        finalize: () => this.carregando.set(false)
      })
    );
  }

  carregar() { this.buscar().subscribe(); }

  trocarPagina(delta: number) {
    const atual = this.page();
    const total = this.dados()?.totalPages ?? 1;
    const prox = Math.min(Math.max(atual + delta, 0), Math.max(total - 1, 0));
    if (prox !== atual) {
      this.page.set(prox);
      this.carregar();
    }
  }

  ordenar(campo: string) {
    const atual = this.sort();
    const asc = atual.startsWith(`${campo},asc`);
    this.sort.set(`${campo},${asc ? 'desc' : 'asc'}`);
    this.carregar();
  }

  limparFiltros() {
    this.filtroForm.reset({ q: '', ativo: '' });
    this.page.set(0);
    this.carregar();
  }

  statusBadgeClassUsuario(ativo: boolean): string {
    return ativo ? 'text-bg-success' : 'text-bg-secondary';
  }

  abrirNovoUsuario() {
    this.usuarioSelecionado = undefined;
    this.modalUsuarioMode = 'create';
    this.modalUsuarioAberto = true;
  }

  abrirEditarUsuario(u: Usuario) {
    this.usuarioSelecionado = u;
    this.modalUsuarioMode = 'edit';
    this.modalUsuarioAberto = true;
  }

  onSalvarUsuario(payload: any) {
    if (this.modalUsuarioMode === 'create') {
      this.svc.criar(payload).subscribe({
        next: (_res) => this.toastr.success('Usuário cadastrado com sucesso!') && this.carregar(),
        error: err => this.toastr.error('Falha ao excluir: ' + (err?.error?.message || 'tente novamente.'))
      });
    } else {
      this.svc.atualizar(payload.id, payload).subscribe({
        next: (_res) => this.toastr.success('Usuário alterado com sucesso!') && this.carregar(),
        error: err => this.toastr.error('Falha ao alterar: ' + (err?.error?.message || 'tente novamente.'))
      });
    }
  }

  askDelete(c: { id: number, nome: string }) {
    this.toDelete = { id: c.id, nome: c.nome };
    this.confirmOpen = true;
    document.body.style.overflow = 'hidden';
  }

  confirmDelete() {
    if (!this.toDelete || this.deleting) return;

    this.deleting = true;

    this.svc.excluir(this.toDelete.id).subscribe({
      next: () => {
        this.deleting = false;
        this.confirmOpen = false;
        this.carregar();
        this.toastr.success('Usuário excluído com sucesso!');
      },
      error: (err) => {
        this.deleting = false;
        this.toastr.error('Falha ao excluir: ' + (err?.error?.message || 'tente novamente.'));
      }
    });
  }

  cancelDelete() {
    if (this.deleting) return;
    this.confirmOpen = false;
    this.toDelete = null;
  }

}
