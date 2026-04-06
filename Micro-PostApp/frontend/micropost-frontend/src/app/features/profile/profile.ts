import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

interface UserProfile {
  id: number;
  username: string;
  email: string;
}

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss'],
})
export class ProfileComponent implements OnInit {
  profile?: UserProfile;
  loading = true;
  error = '';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const authData = localStorage.getItem('auth_user');

    if (!authData) {
      this.error = 'User not logged in';
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    const parsed = JSON.parse(authData);
    const userId = parsed.userId;

    this.http.get<UserProfile>(`http://localhost:8080/api/v1/users/${userId}/profile`)
      .subscribe({
        next: (data) => {
          console.log('Profile response:', data);
          this.profile = data;
          this.loading = false;
          this.error = '';
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.log('Profile error:', err);
          this.error = 'Failed to load profile';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }
}