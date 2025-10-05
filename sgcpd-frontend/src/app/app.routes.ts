import {Routes} from '@angular/router';
import {LoginComponent} from './pages/auth/login/login.component';
import {AuthGuard} from './core/auth/auth.guard';
import {LoginRedirectGuard} from "./core/auth/login.guard";
import {ListagemConteudosComponent} from './pages/conteudo/listagem-conteudos/listagem-conteudos.component';
import {ListagemUsuariosComponent} from './pages/usuario/listagem-usuarios/listagem-usuarios.component';
import {PagesComponent} from './shared/components/pages/pages.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginRedirectGuard],
  },

  {
    path: '',
    component: PagesComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'conteudo', component: ListagemConteudosComponent },
      { path: 'usuarios', component: ListagemUsuariosComponent },
      { path: '', pathMatch: 'full', redirectTo: 'conteudo' },
    ],
  },

  { path: '**', redirectTo: 'login' },
];
