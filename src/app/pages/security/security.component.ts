import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-security',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './security.component.html',
  styleUrl: './security.component.scss'
})
export class SecurityComponent implements OnInit {
  isArmed = true;
  systemStatus = 'Armed (Home)';
  lastActivity = 'Motion detected - Front Door';
  
  cameras = [
    { id: 1, name: 'Front Door', status: 'Live', feed: 'https://images.unsplash.com/photo-1558002038-1055907df827?auto=format&fit=crop&q=80&w=800', detections: ['Person'] },
    { id: 2, name: 'Living Room', status: 'Live', feed: 'https://images.unsplash.com/photo-1583847268964-b28dc8f51f92?auto=format&fit=crop&q=80&w=800', detections: [] },
    { id: 3, name: 'Backyard', status: 'Live', feed: 'https://images.unsplash.com/photo-1554415707-6e8cfc93fe23?auto=format&fit=crop&q=80&w=800', detections: ['Pet'] },
    { id: 4, name: 'Garage', status: 'Live', feed: 'https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&q=80&w=800', detections: ['Vehicle'] }
  ];

  events = [
    { time: '10:42 AM', type: 'Motion', location: 'Front Door', description: 'Person detected', icon: 'fa-user', severity: 'medium' },
    { time: '09:15 AM', type: 'Package', location: 'Front Door', description: 'Delivery dropped off', icon: 'fa-box', severity: 'low' },
    { time: '04:30 AM', type: 'Security', location: 'System', description: 'Night mode activated', icon: 'fa-moon', severity: 'low' }
  ];

  ngOnInit(): void {}

  toggleSystem() {
    this.isArmed = !this.isArmed;
    this.systemStatus = this.isArmed ? 'Armed (Home)' : 'Disarmed';
  }

  getSeverityClass(severity: string) {
    return `severity-${severity}`;
  }
}
