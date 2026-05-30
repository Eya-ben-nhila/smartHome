import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '../../pipes/translate.pipe';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  currentPage = 'dashboard';
  isAdmin$ = this.authService.isAdmin$;

  constructor(private router: Router, private authService: AuthService) {}

  navigateTo(page: string) {
    this.currentPage = page;
    this.router.navigate([`/${page}`]);
  }
}
