import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslationService } from '../../services/translation.service';
import { TranslatePipe } from '../../pipes/translate.pipe';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslatePipe],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  showLogin = false;
  showSignup = false;
  isLoggingIn = false;
  googleStep: 'button' | 'selection' | 'confirm' | 'add' = 'button';
  selectedEmail = '';
  accounts = [
    { name: 'Eya Ben Nhila', email: 'eya.ben.nhila@gmail.com', avatar: 'E' },
    { name: 'Work Account', email: 'e.bennhila@company.com', avatar: 'W' }
  ];
  newAccountEmail = '';
  loginForm = {
    email: '',
    password: '',
    rememberMe: false
  };
  signupForm = {
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'EMPLOYEE' as 'ADMIN' | 'EMPLOYEE',
    agreeTerms: false
  };

  constructor(
    private router: Router,
    public translationService: TranslationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {}

  toggleLanguage(): void {
    this.translationService.toggleLanguage();
  }

  toggleLogin(): void {
    this.showLogin = !this.showLogin;
  }

  toggleSignup(): void {
    this.showSignup = !this.showSignup;
  }

  onLogin(): void {
    // Basic validation
    if (!this.loginForm.email || !this.loginForm.password) {
      alert('Please fill in all fields');
      return;
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.loginForm.email)) {
      alert('Please enter a valid email address');
      return;
    }

    this.authService.login(this.loginForm.email.trim().toLowerCase(), this.loginForm.password).subscribe({
      next: (response) => {
        if (this.loginForm.rememberMe) {
          localStorage.setItem('rememberMe', 'true');
          localStorage.setItem('userEmail', this.loginForm.email);
        }
        if (response.user.role === 'ADMIN') {
          this.router.navigate(['/admin']);
          return;
        }
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        const msg = err?.error?.error;
        if (err?.status === 0) {
          this.router.navigate(['/access-denied'], { queryParams: { reason: 'backend-unreachable' } });
          return;
        }
        this.router.navigate(['/access-denied'], {
          queryParams: { reason: 'invalid-credentials', message: msg || 'Invalid credentials' }
        });
      }
    });
  }

  onSignup(): void {
    // Basic validation
    if (!this.signupForm.name || !this.signupForm.email || !this.signupForm.password || !this.signupForm.confirmPassword) {
      alert('Please fill in all fields');
      return;
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.signupForm.email)) {
      alert('Please enter a valid email address');
      return;
    }

    // Password validation
    if (this.signupForm.password.length < 6) {
      alert('Password must be at least 6 characters long');
      return;
    }

    // Password confirmation
    if (this.signupForm.password !== this.signupForm.confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    // Terms agreement
    if (!this.signupForm.agreeTerms) {
      alert('Please agree to the Terms of Service and Privacy Policy');
      return;
    }

    this.authService.register(
      this.signupForm.name.trim(),
      this.signupForm.email.trim().toLowerCase(),
      this.signupForm.password,
      this.signupForm.role
    ).subscribe({
      next: () => {
        alert('Compte cree avec succes. Connectez-vous.');
        this.toggleSignup();
        this.toggleLogin();
      },
      error: (err) => {
        const msg = err?.error?.error ?? 'Unable to create account';
        if (err?.status === 0) {
          this.router.navigate(['/access-denied'], { queryParams: { reason: 'backend-unreachable' } });
          return;
        }
        this.router.navigate(['/access-denied'], {
          queryParams: { reason: 'signup-failed', message: msg }
        });
      }
    });
  }

  onGoogleLogin(): void {
    this.googleStep = 'selection';
  }

  selectGoogleAccount(account: any): void {
    this.selectedEmail = account.email;
    this.googleStep = 'confirm';
  }

  showAddAccount(): void {
    this.googleStep = 'add';
  }

  addNewAccount(): void {
    if (this.newAccountEmail && this.newAccountEmail.includes('@')) {
      const name = this.newAccountEmail.split('@')[0].split('.').map(s => s.charAt(0).toUpperCase() + s.slice(1)).join(' ');
      const avatar = name.charAt(0);
      const newAcc = { name, email: this.newAccountEmail, avatar };
      this.accounts.unshift(newAcc);
      this.selectGoogleAccount(newAcc);
      this.newAccountEmail = '';
    }
  }

  confirmGoogleLogin(): void {
    this.isLoggingIn = true;
    
    setTimeout(() => {
      this.isLoggingIn = false;
      this.googleStep = 'button';
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('loginType', 'google');
      localStorage.setItem('userEmail', this.selectedEmail);
      this.router.navigate(['/dashboard']);
    }, 1500);
  }

  cancelGoogleLogin(): void {
    this.googleStep = 'button';
    this.selectedEmail = '';
    this.isLoggingIn = false;
    this.newAccountEmail = '';
  }

  goToSignup(): void {
    // For now, just show an alert
    // In real app, this would navigate to a signup page
    alert('Sign up functionality coming soon! For demo, use any email and password to login.');
  }

  scrollToSection(sectionId: string): void {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
