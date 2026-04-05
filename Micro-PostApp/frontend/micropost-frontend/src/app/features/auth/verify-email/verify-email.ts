import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-verify-email',
  standalone: false,
  templateUrl: './verify-email.html',
  styleUrls: ['./verify-email.scss']
})
export class VerifyEmailComponent implements OnInit {
  message = 'Verifying your email...';
  success = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.message = 'Verification token is missing.';
      return;
    }

    this.authService.verifyEmail(token).subscribe({
      next: (response) => {
        this.success = true;
        this.message = response;

        setTimeout(() => {
          this.router.navigate(['/login'], { replaceUrl: true });
        }, 2500);
      },
      error: (err) => {
        this.success = false;
        this.message =
          typeof err.error === 'string'
            ? err.error
            : 'Email verification failed';
      }
    });
  }
}