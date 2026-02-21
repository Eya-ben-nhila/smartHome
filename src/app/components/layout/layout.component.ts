import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [SidebarComponent, CommonModule, RouterOutlet],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
  showSidebar = true;
  showTopBar = true;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const currentUrl = (event as NavigationEnd).urlAfterRedirects;
        this.showSidebar = !currentUrl.includes('/welcome');
        this.showTopBar = !currentUrl.includes('/welcome');
      }
    });
  }

  goToSearch(): void {
    this.router.navigate(['/search']);
  }
}
