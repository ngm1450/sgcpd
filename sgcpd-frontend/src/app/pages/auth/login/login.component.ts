import { Component } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

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
      email: ['', Validators.required],
      senha: ['', Validators.required]
    });
  }

  login() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;
    this.authService.login(this.form.value).subscribe({
      next: () => {
        void this.router.navigate(['/conteudo']);
      },
      error: err => {
        const mensagem = err?.error?.erro || 'Erro desconhecido ao tentar logar.';
        console.error(err)
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
