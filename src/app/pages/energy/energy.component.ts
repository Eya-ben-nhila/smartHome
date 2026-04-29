import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-energy',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './energy.component.html',
  styleUrl: './energy.component.scss'
})
export class EnergyComponent {
  selectedSegment = 'Week';
  selectedFilter = 'Last 7 days';

  // Mock data for different segments
  chartDataSets: any = {
    'Day': {
      points: [
        { x: 100, y: 220 }, { x: 160, y: 210 }, { x: 220, y: 180 }, 
        { x: 280, y: 150 }, { x: 340, y: 100 }, { x: 400, y: 120 },
        { x: 460, y: 140 }, { x: 520, y: 160 }, { x: 580, y: 80 },
        { x: 640, y: 60 }, { x: 700, y: 90 }, { x: 750, y: 110 }
      ],
      labels: ['00:00', '02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00']
    },
    'Week': {
      points: [
        { x: 130, y: 180 }, { x: 230, y: 120 }, { x: 330, y: 160 }, 
        { x: 430, y: 80 }, { x: 530, y: 140 }, { x: 630, y: 100 }, { x: 730, y: 150 }
      ],
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    'Month': {
      points: [
        { x: 150, y: 160 }, { x: 300, y: 100 }, { x: 450, y: 180 }, 
        { x: 600, y: 120 }, { x: 750, y: 140 }
      ],
      labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5']
    }
  };

  get currentPoints() {
    return this.chartDataSets[this.selectedSegment].points;
  }

  get currentLabels() {
    return this.chartDataSets[this.selectedSegment].labels;
  }

  get linePath(): string {
    const points = this.currentPoints;
    if (points.length === 0) return '';
    let d = `M ${points[0].x},${points[0].y}`;
    for (let i = 1; i < points.length; i++) {
      d += ` L ${points[i].x},${points[i].y}`;
    }
    return d;
  }

  get areaPath(): string {
    const points = this.currentPoints;
    if (points.length === 0) return '';
    let d = this.linePath;
    d += ` L ${points[points.length - 1].x},250 L ${points[0].x},250 Z`;
    return d;
  }

  onFilterChange() {
    // Map select filter to segment buttons for demonstration
    if (this.selectedFilter === 'Last 24 hours') this.selectedSegment = 'Day';
    else if (this.selectedFilter === 'Last 7 days') this.selectedSegment = 'Week';
    else if (this.selectedFilter === 'Last 30 days') this.selectedSegment = 'Month';
    else if (this.selectedFilter === 'Last year') this.selectedSegment = 'Month';
  }

  setSegment(segment: string) {
    this.selectedSegment = segment;
  }
}
