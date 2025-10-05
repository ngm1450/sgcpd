import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import {Usuario} from '../../../shared/types/usuario.type';

type ModalMode = 'create' | 'edit';

export interface UsuarioForm {
  nome: string | null;
  email: string | null;
  senha: string | null;   // no modo edição pode ficar em branco
  ativo: boolean;
}

@Component({
  selector: 'novo-usuario-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './novo-usuario-modal.component.html'
})
export class NovoUsuarioModalComponent implements OnChanges {
  /** abre/fecha o modal (mesma mecânica do modal de conteúdo) */
  @Input() open = false;

  /** 'create' ou 'edit' */
  @Input() mode: ModalMode = 'create';

  /** dados existentes quando for edição */
  @Input() usuario?: Usuario;

  /** eventos de ciclo de vida */
  @Output() closed = new EventEmitter<void>();
  @Output() saved = new EventEmitter<Usuario>();

  /** estados/erros */
  loading = false;
  errorUsuario?: string;

  /** form */
  form: UsuarioForm = {
    nome: null,
    email: null,
    senha: null,
    ativo: true
  };

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['usuario'] || changes['mode'] || changes['open']) && this.mode === 'edit' && this.usuario) {
      this.form = {
        nome: this.usuario.nome ?? null,
        email: this.usuario.email ?? null,
        senha: null,          // em edição, senha é opcional; null = manter
        ativo: this.usuario.ativo
      };
    }
    if (this.mode === 'create' && this.open && (!changes['mode'] || changes['mode'].currentValue === 'create')) {
      // reset para criação
      this.form = { nome: null, email: null, senha: null, ativo: true };
      this.errorUsuario = undefined;
    }
  }

  /** UX iguais ao modal de conteúdo */
  onBackdropClick() { this.close(); }
  onKeydown(ev: KeyboardEvent) {
    if (ev.key === 'Escape') this.close();
  }
  stop(ev: Event) { ev.stopPropagation(); }

  close() {
    this.closed.emit();
  }

  canSave(): boolean {
    const nomeOk = !!(this.form.nome && this.form.nome.trim().length > 0);
    const emailOk = this.isEmailValid(this.form.email);
    const senhaOk = this.mode === 'edit'
      ? true
      : !!(this.form.senha && this.form.senha.length >= 6);

    return nomeOk && emailOk && senhaOk;
  }

  private isEmailValid(email: string | null): boolean {
    if (!email) return false;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  async save(_formRef?: NgForm) {
    if (!this.canSave()) return;

    try {
      this.loading = true;
      this.errorUsuario = undefined;

      this.saved.emit({
        id: this.mode === 'edit' ? this.usuario?.id : null,
        nome: this.form.nome?.trim() ?? '',
        email: this.form.email?.trim() ?? '',
        senha: (this.mode === 'edit' ? (this.form.senha?.trim() || null) : (this.form.senha?.trim() ?? null)),
        ativo: this.form.ativo
      } as any);

      this.close()

    } catch (e: any) {
      this.errorUsuario = e?.message || 'Falha ao salvar usuário.';
    } finally {
      this.loading = false;
    }
  }
}
