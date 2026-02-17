import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  currentPage = 'dashboard';

  constructor(private router: Router) {}

  navigateTo(page: string) {
    this.currentPage = page;
    this.router.navigate([`/${page}`]);
  }
}
