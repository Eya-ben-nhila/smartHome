import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ActivityComponent } from './pages/activity/activity.component';
import { AutomationsComponent } from './pages/automations/automations.component';
import { CameraEventsComponent } from './pages/camera-events/camera-events.component';
import { SearchComponent } from './pages/search/search.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'activity', component: ActivityComponent },
      { path: 'automations', component: AutomationsComponent },
      { path: 'camera-events', component: CameraEventsComponent },
      { path: 'search', component: SearchComponent }
    ]
  }
];
