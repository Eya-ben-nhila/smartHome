import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.scss'
})
export class AlertsComponent {
  showRuleForm = false;
  
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

  acknowledgeAlert(alertId: string) {
    const alert = this.alerts.find(a => a.id === alertId);
    if (alert) {
      alert.status = 'resolved';
    }
  }

  resolveAlert(alertId: string) {
    const alert = this.alerts.find(a => a.id === alertId);
    if (alert) {
      alert.status = 'resolved';
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
}
