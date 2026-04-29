import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  showSuccessModal = false;
  
  userProfile = {
    firstName: 'Alex',
    lastName: 'Johnson',
    email: 'alex.johnson@smarthome.com',
    phone: '+1 (555) 123-4567',
    twoFactorEnabled: true,
    loginAlertsEnabled: true,
    emailNotifications: true,
    pushNotifications: true,
    smsAlerts: false,
    language: 'English',
    timezone: 'Pacific Time (PT)',
    theme: 'Light'
  };

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadProfile();
    }
  }

  saveChanges() {
    console.log('saveChanges called');
    if (isPlatformBrowser(this.platformId)) {
      console.log('Saving profile to localStorage:', this.userProfile);
      localStorage.setItem('userProfile', JSON.stringify(this.userProfile));
      this.showSuccessModal = true;
      console.log('showSuccessModal set to true');
      // Fallback alert to confirm the function completed
      alert('Changes saved successfully!');
    } else {
      console.log('Not in browser, skipping localStorage save');
    }
  }

  loadProfile() {
    console.log('loadProfile called');
    const savedProfile = localStorage.getItem('userProfile');
    if (savedProfile) {
      this.userProfile = JSON.parse(savedProfile);
      console.log('Profile loaded from localStorage');
    }
  }

  closeModal() {
    this.showSuccessModal = false;
  }

  cancelChanges() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadProfile();
    }
  }
}
