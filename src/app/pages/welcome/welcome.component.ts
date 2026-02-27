import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  showLogin = false;
  loginForm = {
    email: '',
    password: '',
    rememberMe: false
  };

  constructor(private router: Router) {}

  ngOnInit(): void {}

  toggleLogin(): void {
    this.showLogin = !this.showLogin;
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
