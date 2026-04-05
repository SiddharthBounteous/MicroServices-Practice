import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  role = 'USER';
  loading = false;
  message = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  submitRegister(): void {
    const username = this.username.trim();
    const email = this.email.trim();
    const password = this.password.trim();

    if (!username || !email || !password) return;

    this.loading = true;
    this.message = '';
    this.errorMessage = '';

    this.authService.register({
      username,
      email,
      password,
      role: 'USER'
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/verify-pending'], {
          queryParams: { email }
        });
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          typeof err.error === 'string' ? err.error : 'Registration failed';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}