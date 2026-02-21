import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout.component';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ActivityComponent } from './pages/activity/activity.component';
import { AutomationsComponent } from './pages/automations/automations.component';
import { CameraEventsComponent } from './pages/camera-events/camera-events.component';
import { SearchComponent } from './pages/search/search.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { AlertsComponent } from './pages/alerts/alerts.component';
import { SecurityComponent } from './pages/security/security.component';
import { EnergyComponent } from './pages/energy/energy.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
      { path: 'welcome', component: WelcomeComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'activity', component: ActivityComponent },
      { path: 'automations', component: AutomationsComponent },
      { path: 'camera-events', component: CameraEventsComponent },
      { path: 'search', component: SearchComponent },
      { path: 'profile', component: ProfileComponent },
      { path: 'alerts', component: AlertsComponent },
      { path: 'security', component: SecurityComponent },
      { path: 'energy', component: EnergyComponent }
    ]
  }
];
