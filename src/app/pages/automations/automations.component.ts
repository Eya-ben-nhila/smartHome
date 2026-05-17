import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-automations',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './automations.component.html',
  styleUrl: './automations.component.scss'
})
export class AutomationsComponent implements OnInit {
  indoorTemp = 22.5;
  outdoorTemp = 18.2;
  humidity = 45;
  airQuality = 'Excellent (12 AQI)';
  
  rules = [
    { id: 1, name: 'Eco-Climate Save', description: 'Turn off AC when windows are open', active: true, icon: 'fa-leaf', color: '#4CAF50' },
    { id: 2, name: 'Smart Evening Lights', description: 'Dim living room lights at sunset', active: true, icon: 'fa-moon', color: '#673AB7' },
    { id: 3, name: 'Humidity Control', description: 'Activate dehumidifier if > 60%', active: false, icon: 'fa-tint', color: '#2196F3' },
    { id: 4, name: 'Security Presence', description: 'Randomize lights when away', active: true, icon: 'fa-user-shield', color: '#F44336' }
  ];

  schedules = [
    { time: '07:00 AM', action: 'Wake-up Scene', status: 'Next', icon: 'fa-sun' },
    { time: '08:30 AM', action: 'All Lights Off', status: 'Scheduled', icon: 'fa-lightbulb' },
    { time: '06:00 PM', action: 'Evening Comfort', status: 'Scheduled', icon: 'fa-couch' }
  ];

  ngOnInit(): void {}

  toggleRule(rule: any) {
    rule.active = !rule.active;
  }

  addRule() {
    const newRule = {
      id: Date.now(), // Unique ID
      name: 'New Smart Rule',
      description: 'Configure custom trigger and action',
      active: true,
      icon: 'fa-cog',
      color: '#9E9E9E'
    };
    this.rules = [newRule, ...this.rules];
    console.log('Current rules:', this.rules);
  }
}
