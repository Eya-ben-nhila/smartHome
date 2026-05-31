import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.scss'
})
export class AlertsComponent implements OnInit {
  private readonly recentAlertsStorageKey = 'industrialIoT_recent_alerts';
  showRuleForm = false;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}
  
  newRule = {
    type: 'motion',
    condition: 'detected',
    threshold: '',
    startTime: '',
    endTime: ''
  };

  alerts = [
    { id: 'motion-activity', type: 'motion', icon: 'fa-running', title: 'Intrusion Détectée', desc: 'Zone de stockage et zone de chargement - il y a 2 minutes', status: 'active' },
    { id: 'door-activity', type: 'security', icon: 'fa-door-open', title: 'Accès Salle Contrôle', desc: 'Porte principale salle de contrôle - il y a 15 minutes', status: 'active' },
    { id: 'temp-activity', type: 'system', icon: 'fa-thermometer-half', title: 'Alerte Surchauffe Four', desc: 'Four production 1 - il y a 1 heure', status: 'active' },
    { id: 'energy-activity', type: 'energy', icon: 'fa-bolt', title: 'Charge Électrique Élevée', desc: 'Moteurs de convoyeur Ligne A - il y a 2 heures', status: 'active' },
    { id: 'camera-activity', type: 'security', icon: 'fa-video', title: 'Mouvement Caméra', desc: 'Zone de stockage périphérique - il y a 3 heures', status: 'active' }
  ];

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadRealtimeAlerts();
    }
  }

  acknowledgeAlert(alertId: string) {
    const alert = this.alerts.find(a => a.id === alertId);
    if (alert) {
      alert.status = 'acknowledged';
      if (isPlatformBrowser(this.platformId)) {
        this.saveRealtimeAlerts();
      }
    }
  }

  resolveAlert(alertId: string) {
    const alert = this.alerts.find(a => a.id === alertId);
    if (alert) {
      alert.status = 'resolved';
      if (isPlatformBrowser(this.platformId)) {
        this.saveRealtimeAlerts();
      }
    }
  }

  dismissAlert(alertId: string) {
    this.acknowledgeAlert(alertId);
  }

  showAddRuleForm() {
    this.showRuleForm = true;
  }

  hideAddRuleForm() {
    this.showRuleForm = false;
    this.resetNewRule();
  }

  resetNewRule() {
    this.newRule = {
      type: 'motion',
      condition: 'detected',
      threshold: '',
      startTime: '',
      endTime: ''
    };
  }

  saveRule() {
    console.log('Saving new rule:', this.newRule);
    // Here you would typically call a service to save the rule
    // For now, we'll just log it and hide the form
    this.hideAddRuleForm();
  }

  toggleRule(ruleId: string) {
    console.log(`Toggling rule: ${ruleId}`);
    // Here you would typically call a service to enable/disable the rule
  }

  deleteRule(ruleId: string) {
    console.log(`Deleting rule: ${ruleId}`);
    // Here you would typically call a service to delete the rule
  }

  private loadRealtimeAlerts() {
    const storedAlerts = this.readStoredAlerts();
    const staticAlertIds = new Set(this.alerts.map(alert => alert.id));
    const realtimeAlerts = storedAlerts.filter((alert: any) => !staticAlertIds.has(alert.id));
    this.alerts = [...realtimeAlerts, ...this.alerts];
  }

  private saveRealtimeAlerts() {
    const realtimeAlerts = this.alerts
      .filter((alert: any) => alert.source)
      .slice(0, 20);

    localStorage.setItem(this.recentAlertsStorageKey, JSON.stringify(realtimeAlerts));
  }

  private readStoredAlerts(): any[] {
    try {
      return JSON.parse(localStorage.getItem(this.recentAlertsStorageKey) || '[]');
    } catch {
      return [];
    }
  }
}
