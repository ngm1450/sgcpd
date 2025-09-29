import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import {provideHttpClient} from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {HeaderComponent} from './shared/components/header/header.component';
import {LoginComponent} from './pages/auth/login/login.component';
import {routes} from './app.routes';
import {
  PaginaInicialAlunoComponent,
} from "./pages/aluno/pagina-inicial/pagina-inicial-aluno.component";
import {PaginaInicialProfessorComponent} from "./pages/professor/pagina-inicial/pagina-inicial-professor.component";
import {
  PaginaInicialFuncionarioComponent
} from "./pages/funcionario/pagina-inicial/pagina-inicial-funcionario.component";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    PaginaInicialAlunoComponent,
    PaginaInicialProfessorComponent,
    PaginaInicialFuncionarioComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      preventDuplicates: true
    }),
    RouterModule.forRoot(routes),
    NgOptimizedImage
  ],
  providers: [provideHttpClient()],
  bootstrap: [AppComponent]
})
export class AppModule {}
