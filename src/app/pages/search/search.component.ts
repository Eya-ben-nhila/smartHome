import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent {
  searchQuery: string = '';

  constructor(private router: Router) {}

  goBack() {
    this.router.navigate(['/dashboard']);
  }

  selectAction(action: string) {
    this.searchQuery = action;
  }

  performSearch() {
    if (this.searchQuery.trim()) {
      console.log('Searching for:', this.searchQuery);
      // Implement search logic here
    }
  }
}
