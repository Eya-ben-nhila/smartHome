import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslatePipe],
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss']
})
export class AnalyticsComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit() {
    this.loadAnalyticsData();
  }

  // Sample analytics data - in real app, this would come from APIs
  energyData = {
    daily: [
      { day: 'Mon', usage: 12.5, cost: 3.20 },
      { day: 'Tue', usage: 14.2, cost: 3.65 },
      { day: 'Wed', usage: 11.8, cost: 3.05 },
      { day: 'Thu', usage: 13.5, cost: 3.45 },
      { day: 'Fri', usage: 15.2, cost: 3.90 },
      { day: 'Sat', usage: 18.5, cost: 4.60 },
      { day: 'Sun', usage: 16.8, cost: 4.20 }
    ],
    monthly: [
      { month: 'Jan', usage: 385, cost: 98.50 },
      { month: 'Feb', usage: 342, cost: 87.60 },
      { month: 'Mar', usage: 412, cost: 105.80 },
      { month: 'Apr', usage: 378, cost: 96.90 },
      { month: 'May', usage: 395, cost: 101.20 }
    ],
    devices: [
      { name: 'HVAC System', usage: 45, percentage: 38, trend: 'up' },
      { name: 'Lighting', usage: 22, percentage: 19, trend: 'stable' },
      { name: 'Kitchen Appliances', usage: 18, percentage: 15, trend: 'down' },
      { name: 'Entertainment', usage: 15, percentage: 13, trend: 'stable' },
      { name: 'Security System', usage: 12, percentage: 10, trend: 'down' },
      { name: 'Other Devices', usage: 5, percentage: 5, trend: 'stable' }
    ]
  };

  securityData = {
    events: [
      { time: '2:30 AM', type: 'motion', location: 'Front Door', severity: 'low' },
      { time: '6:45 AM', type: 'door_open', location: 'Garage', severity: 'normal' },
      { time: '8:15 AM', type: 'motion', location: 'Backyard', severity: 'low' },
      { time: '12:30 PM', type: 'package_detected', location: 'Front Door', severity: 'normal' },
      { time: '3:45 PM', type: 'motion', location: 'Driveway', severity: 'medium' },
      { time: '5:20 PM', type: 'unknown_access', location: 'Front Door', severity: 'high' }
    ],
    weeklyPattern: {
      normal: 85,
      unusual: 15,
      risk: 'low'
    }
  };

  predictions = {
    energy: {
      tomorrow: 14.2,
      cost: 3.65,
      suggestion: 'Slightly higher than normal due to temperature forecast'
    },
    comfort: {
      temperature: 72,
      humidity: 45,
      suggestion: 'Optimal conditions for energy efficiency'
    },
    security: {
      risk: 'low',
      recommendation: 'No unusual patterns detected'
    }
  };

  loadAnalyticsData() {
    // In real implementation, this would fetch from backend APIs
    console.log('Loading analytics data...');
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  exportData(): void {
    // Export analytics data functionality
    console.log('Exporting analytics data...');
  }

  getSecurityIcon(type: string): string {
    const iconMap: { [key: string]: string } = {
      'motion': 'fa-running',
      'door_open': 'fa-door-open',
      'package_detected': 'fa-box',
      'unknown_access': 'fa-user-secret'
    };
    return iconMap[type] || 'fa-exclamation-triangle';
  }

  formatEventType(type: string): string {
    const nameMap: { [key: string]: string } = {
      'motion': 'Motion Detected',
      'door_open': 'Door Opened',
      'package_detected': 'Package Delivered',
      'unknown_access': 'Unknown Access'
    };
    return nameMap[type] || 'Security Event';
  }
}
