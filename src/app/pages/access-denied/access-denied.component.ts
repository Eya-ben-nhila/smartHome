import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-access-denied',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './access-denied.component.html',
  styleUrl: './access-denied.component.scss'
})
export class AccessDeniedComponent {
  title = 'Access denied';
  message = 'You do not have permission to access this page.';

  constructor(private route: ActivatedRoute, private router: Router) {
    const reason = this.route.snapshot.queryParamMap.get('reason');
    const customMessage = this.route.snapshot.queryParamMap.get('message');
    if (reason === 'invalid-credentials') {
      this.title = 'Invalid credentials';
      this.message = customMessage || 'Email or password is incorrect.';
    } else if (reason === 'signup-failed') {
      this.title = 'Signup failed';
      this.message = customMessage || 'Unable to create account.';
    } else if (reason === 'backend-unreachable') {
      this.title = 'Backend unavailable';
      this.message = 'Cannot reach backend API on http://localhost:8080/api. Start backend then try again.';
    }
  }

  goToWelcome(): void {
    this.router.navigate(['/welcome']);
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
