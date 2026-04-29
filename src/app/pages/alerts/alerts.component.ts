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

  dismissAlert(alertType: string) {
    console.log(`Dismissing alert: ${alertType}`);
    // Here you would typically call a service to remove the alert
    // For now, we'll just log it
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
