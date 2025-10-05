import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { provideHttpClient, withInterceptors} from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {HeaderComponent} from './shared/components/header/header.component';
import {LoginComponent} from './pages/auth/login/login.component';
import {routes} from './app.routes';
import {TokenInterceptor} from './core/auth/token.interceptor';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { QuillModule } from 'ngx-quill';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent
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
    NgOptimizedImage,
    NgbModule,
    QuillModule.forRoot()
  ],
  providers: [
    // 👇 nova forma moderna
    provideHttpClient(withInterceptors([TokenInterceptor]))
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
