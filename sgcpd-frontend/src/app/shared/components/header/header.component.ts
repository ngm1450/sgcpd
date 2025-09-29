import { Component } from '@angular/core';
import {AuthService} from '../../../core/auth/auth.service';

@Component({
  standalone: false,
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private authService: AuthService) {}

  logout() {
    this.authService.logout();
  }

  get nomeUsuarioLogado(): string | null {
    return this.authService.getUsuarioLogado().nome;
  }

  get sexoUsuarioLogado(): 'M' | 'F' {
    return this.authService.getUsuarioLogado().sexo;
  }

  get estaLogado(): boolean {
    return this.authService.isAuthenticated();
  }
}
