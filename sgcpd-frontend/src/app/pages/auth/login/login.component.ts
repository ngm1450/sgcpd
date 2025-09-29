import { Component } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {PerfilStrategyFactory} from '../../../core/navigation/perfil-strategy.factory';

@Component({
  standalone: false,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private toastr: ToastrService,
              private router: Router) {
    this.form = this.fb.group({
      matricula: ['', Validators.required],
      senha: ['', Validators.required]
    });
  }

  login() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;
    this.authService.login(this.form.value).subscribe({
      next: () => {
        const perfil = this.authService.getPerfil();
        const strategy = new PerfilStrategyFactory(this.router).getStrategy(perfil);
        strategy.redirecionar();
      },
      error: err => {
        const mensagem = err?.error?.erro || 'Erro desconhecido ao tentar logar.';
        this.toastr.error(mensagem, 'Erro');
      }
    });
  }

  campoInvalido(nome: string): boolean {
    const campo = this.form.get(nome) as FormControl;
    if (!campo) return false;
    return campo.invalid && campo.touched;
  }
}
