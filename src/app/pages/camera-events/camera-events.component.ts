import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '../../pipes/translate.pipe';

@Component({
  selector: 'app-camera-events',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './camera-events.component.html',
  styleUrl: './camera-events.component.scss'
})
export class CameraEventsComponent {

}
