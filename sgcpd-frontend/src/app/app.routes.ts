import { PerfilGuard } from './core/auth/perfil.guard';
import {Routes} from '@angular/router';
import {LoginComponent} from './pages/auth/login/login.component';
import {Perfil} from './shared/enums/perfil.enum';
import {PaginaInicialAlunoComponent} from './pages/aluno/pagina-inicial/pagina-inicial-aluno.component';
import {PaginaInicialProfessorComponent} from './pages/professor/pagina-inicial/pagina-inicial-professor.component';
import {
  PaginaInicialFuncionarioComponent
} from './pages/funcionario/pagina-inicial/pagina-inicial-funcionario.component';
import {AuthGuard} from './core/auth/auth.guard';
import {LoginRedirectGuard} from "./core/auth/login.guard";

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginRedirectGuard]
  },
  {
    path: 'aluno',
    canActivate: [AuthGuard],
    children: [
      {
        path: 'pagina-inicial',
        component: PaginaInicialAlunoComponent,
        canActivate: [PerfilGuard],
        data: { perfil: Perfil.ALUNO }
      }
    ]
  },
  {
    path: 'professor',
    canActivate: [AuthGuard],
    data: { perfil: Perfil.PROFESSOR },
    children: [
      {
        path: 'pagina-inicial',
        component: PaginaInicialProfessorComponent,
        canActivate: [PerfilGuard],
        data: { perfil: Perfil.PROFESSOR }
      }
    ]
  },
  {
    path: 'funcionario',
    canActivate: [AuthGuard],
    data: { perfil: Perfil.FUNCIONARIO },
    children: [
      {
        path: 'pagina-inicial',
        component: PaginaInicialFuncionarioComponent,
        canActivate: [PerfilGuard],
        data: { perfil: Perfil.FUNCIONARIO }
      }
    ]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
