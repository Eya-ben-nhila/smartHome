import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslationService } from '../../services/translation.service';
import { TranslatePipe } from '../../pipes/translate.pipe';

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
    agreeTerms: false
  };

  constructor(private router: Router, public translationService: TranslationService) {}

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

    // Simulate login process
    console.log('Login attempt:', this.loginForm);
    
    // For demo purposes, redirect to dashboard
    // In real app, this would make an API call to authenticate
    if (this.loginForm.email && this.loginForm.password) {
      // Store login state if remember me is checked
      if (this.loginForm.rememberMe) {
        localStorage.setItem('rememberMe', 'true');
        localStorage.setItem('userEmail', this.loginForm.email);
      }
      
      // Navigate to dashboard
      this.router.navigate(['/dashboard']);
    }
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
    if (this.signupForm.password.length < 8) {
      alert('Password must be at least 8 characters long');
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

    // Simulate signup process
    console.log('Signup attempt:', this.signupForm);
    
    // For demo purposes, redirect to dashboard after successful signup
    // In real app, this would make an API call to create account
    alert('Account created successfully! Redirecting to dashboard...');
    this.router.navigate(['/dashboard']);
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
