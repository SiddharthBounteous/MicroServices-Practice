import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const raw = localStorage.getItem('auth_user');

  if (raw) {
    const auth = JSON.parse(raw);
    const token = auth?.token;
    const userId = auth?.userId;

    const headers: Record<string, string> = {};

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    if (userId) {
      headers['X-User-Id'] = String(userId);
    }

    req = req.clone({
      setHeaders: headers
    });
  }

  return next(req);
};