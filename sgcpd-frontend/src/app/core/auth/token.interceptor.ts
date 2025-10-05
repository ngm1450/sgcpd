import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import { AuthService } from './auth.service';

const AUTH_WHITELIST = [
  '/api/auth/login',
  '/api/auth/refresh'
];

export const TokenInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.method === 'OPTIONS' || AUTH_WHITELIST.some(u => req.url.includes(u))) {
    return next(req);
  }

  const auth = inject(AuthService);
  const token = auth.getToken();

  if (!token) return next(req);

  const cloned = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  return next(cloned);
};
