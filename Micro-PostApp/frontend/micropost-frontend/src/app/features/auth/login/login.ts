import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  loading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  submitLogin(): void {
    const username = this.username.trim();
    const password = this.password.trim();

    if (!username || !password) return;

    this.loading = true;
    this.errorMessage = '';

    this.authService.login({ username, password }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/feed'], { replaceUrl: true });
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = typeof err.error === 'string'
          ? err.error
          : 'Login failed';
      }
    });
  }
}