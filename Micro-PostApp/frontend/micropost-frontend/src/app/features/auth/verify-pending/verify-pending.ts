import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-verify-pending',
  standalone: false,
  templateUrl: './verify-pending.html',
  styleUrls: ['./verify-pending.scss']
})
export class VerifyPendingComponent {
  email = '';

  constructor(private route: ActivatedRoute) {
    this.email = this.route.snapshot.queryParamMap.get('email') ?? '';
  }
}