import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
    { id: 'motion-activity', type: 'motion', icon: 'fa-running', title: 'Motion Detected', desc: 'Living room and hallway - 2 minutes ago', status: 'active' },
    { id: 'door-activity', type: 'security', icon: 'fa-door-open', title: 'Door Access', desc: 'Main entrance - 15 minutes ago', status: 'active' },
    { id: 'temp-activity', type: 'system', icon: 'fa-thermometer-half', title: 'Temperature Alert', desc: 'Living room - 1 hour ago', status: 'active' },
    { id: 'energy-activity', type: 'energy', icon: 'fa-bolt', title: 'High Energy Usage', desc: 'Whole house - 2 hours ago', status: 'active' },
    { id: 'camera-activity', type: 'security', icon: 'fa-video', title: 'Camera Motion', desc: 'Backyard - 3 hours ago', status: 'active' }
  ];

  acknowledgeAlert(alertId: string) {
    const alert = this.alerts.find(a => a.id === alertId);
    if (alert) {
      alert.status = 'acknowledged';
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
