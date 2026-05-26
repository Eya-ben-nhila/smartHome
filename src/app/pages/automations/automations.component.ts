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
    { id: 1, name: 'Sécurité Surchauffe', description: 'Arrêter les moteurs si la température du four dépasse 250°C', active: true, icon: 'fa-exclamation-triangle', color: '#F44336' },
    { id: 2, name: 'Extraction Fumées', description: 'Activer la ventilation si la qualité de l\'air < 50 AQI', active: true, icon: 'fa-wind', color: '#00BCD4' },
    { id: 3, name: 'Régulation Cuve', description: 'Activer la vanne d\'admission si le niveau de cuve < 20%', active: false, icon: 'fa-tint', color: '#2196F3' },
    { id: 4, name: 'Éclairage Sécurité', description: 'Activer l\'éclairage périphérique hors heures de production', active: true, icon: 'fa-shield-alt', color: '#4CAF50' }
  ];

  schedules = [
    { time: '06:00 AM', action: 'Démarrage production (Ligne A/B)', status: 'Next', icon: 'fa-play' },
    { time: '02:00 PM', action: 'Cycle de maintenance préventive', status: 'Scheduled', icon: 'fa-wrench' },
    { time: '10:00 PM', action: 'Mode veille / Arrêt sécurité de nuit', status: 'Scheduled', icon: 'fa-power-off' }
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
