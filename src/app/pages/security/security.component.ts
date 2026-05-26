import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-security',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './security.component.html',
  styleUrl: './security.component.scss'
})
export class SecurityComponent implements OnInit {
  isArmed = true;
  systemStatus = 'Armed (Facility)';
  lastActivity = 'Motion detected - Portail atelier';
  
  cameras = [
    { id: 1, name: 'Portail atelier', status: 'Live', feed: 'https://images.unsplash.com/photo-1558002038-1055907df827?auto=format&fit=crop&q=80&w=800', detections: ['Person'] },
    { id: 2, name: 'Zone stockage', status: 'Live', feed: 'https://images.unsplash.com/photo-1583847268964-b28dc8f51f92?auto=format&fit=crop&q=80&w=800', detections: [] },
    { id: 3, name: 'Accès salle serveur', status: 'Live', feed: 'https://images.unsplash.com/photo-1554415707-6e8cfc93fe23?auto=format&fit=crop&q=80&w=800', detections: [] },
    { id: 4, name: 'Poste de garde', status: 'Live', feed: 'https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&q=80&w=800', detections: ['Vehicle'] }
  ];

  events = [
    { time: '10:42 AM', type: 'Motion', location: 'Portail atelier', description: 'Présence détectée', icon: 'fa-user', severity: 'medium' },
    { time: '09:15 AM', type: 'Materials', location: 'Portail atelier', description: 'Réception matières premières', icon: 'fa-box', severity: 'low' },
    { time: '04:30 AM', type: 'Security', location: 'System', description: 'Mode de nuit activé', icon: 'fa-moon', severity: 'low' }
  ];

  ngOnInit(): void {}

  toggleSystem() {
    this.isArmed = !this.isArmed;
    this.systemStatus = this.isArmed ? 'Armed (Facility)' : 'Disarmed';
  }

  getSeverityClass(severity: string) {
    return `severity-${severity}`;
  }
}
