import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DragDropModule, CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { IotWebsocketService } from '../../services/iot-websocket.service';
import { TranslatePipe } from '../../pipes/translate.pipe';
import { TranslationService } from '../../services/translation.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule, TranslatePipe],
  templateUrl: './dashboard.component.html',
  styles: [`
    .dashboard-content {
      padding: 20px;
      overflow-y: auto;
      height: 100vh;
      background: #f0f2f5;
    }

    .header-strip {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 32px;
      padding: 12px 20px;
      background: rgba(255, 255, 255, 0.7);
      backdrop-filter: blur(12px);
      border-radius: 20px;
      border: 1px solid rgba(255, 255, 255, 0.4);
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.03);
    }

    .strip-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
    }

    .left-section {
      display: flex;
      align-items: center;
      gap: 30px;
    }

    .dashboard-title {
      font-size: 24px;
      font-weight: 800;
      background: linear-gradient(135deg, #1a1a2e, #4361ee);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      margin: 0;
    }

    .search-bar {
      background: rgba(255, 255, 255, 0.9);
      border-radius: 14px;
      padding: 8px 16px;
      display: flex;
      align-items: center;
      gap: 12px;
      border: 1px solid rgba(0, 0, 0, 0.05);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.02);
      width: 280px;
      transition: all 0.3s ease;
    }

    .search-bar:focus-within {
      width: 320px;
      border-color: #2196F3;
      box-shadow: 0 8px 20px rgba(33, 150, 243, 0.1);
    }

    .search-bar i {
      color: #757575;
      font-size: 14px;
    }

    .search-bar input {
      border: none;
      outline: none;
      background: transparent;
      width: 100%;
      font-size: 13px;
      font-weight: 500;
      color: #333;
    }

    .right-section {
      display: flex;
      align-items: center;
      gap: 16px;
    }

    .add-device-btn {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 20px;
      background: #4CAF50;
      color: white;
      border: none;
      border-radius: 12px;
      cursor: pointer;
      font-weight: 600;
      font-size: 14px;
      transition: all 0.3s ease;
    }

    .add-device-btn i {
      font-size: 16px;
    }

    .add-device-btn:hover {
      background: #45a049;
    }

    .analytics-btn {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 20px;
      background: #2196F3;
      color: white;
      border: none;
      border-radius: 12px;
      cursor: pointer;
      font-weight: 600;
      font-size: 14px;
      transition: all 0.3s ease;
    }

    .analytics-btn i {
      font-size: 16px;
    }

    .analytics-btn:hover {
      background: #1976D2;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
    }

    .category-filters {
      display: flex;
      gap: 8px;
    }

    .filter-chip {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 8px 16px;
      background: #f5f5f5;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;
    }

    .filter-chip i {
      font-size: 14px;
      color: #757575;
    }

    .filter-chip span {
      font-size: 13px;
      color: #757575;
      font-weight: 500;
    }

    .filter-chip:hover {
      background: #e8e8e8;
    }

    .filter-chip.active {
      background: #2196F3;
      border-color: #2196F3;
    }

    .filter-chip.active i,
    .filter-chip.active span {
      color: white;
    }

    .logout-btn {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 20px;
      background: #f44336;
      color: white;
      border: none;
      border-radius: 12px;
      cursor: pointer;
      font-weight: 600;
      font-size: 14px;
      transition: all 0.3s ease;
    }

    .logout-btn i {
      font-size: 16px;
    }

    .logout-btn:hover {
      background: #d32f2f;
    }

    // Device Cards Grid
    .devices-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 20px;
      padding: 0 20px;
    }

    .column {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .device-card {
      background: rgba(255, 255, 255, 0.8);
      backdrop-filter: blur(8px);
      border-radius: 16px;
      border: 1px solid rgba(255, 255, 255, 0.4);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
      transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      position: relative;
      cursor: pointer;
      overflow: hidden;
    }

    .device-card:hover {
      transform: translateY(-4px) scale(1.01);
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
      border-color: rgba(33, 150, 243, 0.3);
      background: white;
    }

    .card-content {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 16px;
    }

    .device-icon {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 10px;
      font-size: 20px;
      color: white;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s ease;
    }

    .device-card:hover .device-icon {
      transform: scale(1.05) rotate(3deg);
    }

    .device-logo {
      width: 24px;
      height: 24px;
      background: white;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      color: #333;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      border: 2px solid #fff;
      margin-left: -4px;
      z-index: 2;
    }

    .device-info {
      flex: 1;
    }

    .device-info h4 {
      margin: 0 0 2px 0;
      font-size: 15px;
      font-weight: 700;
      color: #1a1a2e;
    }

    .device-info p {
      margin: 0;
      font-size: 11px;
      color: #666;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.3px;
    }

    // Camera Cards
    .camera-card.large {
      grid-column: span 2;
      grid-row: span 2;
      height: 250px;
      padding: 0;
      border-radius: 16px;
      overflow: hidden;
    }

    .camera-feed {
      width: 100%;
      height: 100%;
      border-radius: 16px;
      overflow: hidden;
      position: relative;
    }

    .camera-feed img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .live-indicator {
      position: absolute;
      top: 12px;
      right: 12px;
      background: rgba(0, 0, 0, 0.7);
      color: white;
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 12px;
      font-weight: 600;
      display: flex;
      align-items: center;
      gap: 4px;
    }

    .live-dot {
      width: 8px;
      height: 8px;
      background: #4CAF50;
      border-radius: 50%;
      animation: pulse 2s infinite;
    }

    .camera-controls {
      position: absolute;
      bottom: 12px;
      right: 12px;
      color: white;
      font-size: 16px;
      cursor: pointer;
      opacity: 0.8;
      transition: opacity 0.3s ease;
    }

    // Lock Cards
    .lock-card .device-icon {
      background: linear-gradient(135deg, #f44336, #d32f2f);
    }

    .lock-card .device-info p {
      color: #f44336;
    }

    // Garage Cards
    .garage-card .device-icon {
      background: linear-gradient(135deg, #795548, #5D4037);
    }

    .garage-card .device-info p {
      color: #795548;
    }

    // Light Cards
    .light-card .device-icon {
      background: linear-gradient(135deg, #FFC107, #FF9800);
    }

    .light-card .device-info p {
      color: #4CAF50;
    }

    // TV Cards
    .tv-card .device-icon {
      background: linear-gradient(135deg, #9C27B0, #6A1B9A);
    }

    .tv-card .device-info p {
      color: #6A1B9A;
    }

    // Speaker Cards
    .speaker-card .device-icon {
      background: linear-gradient(135deg, #2196F3, #1976D2);
    }

    .speaker-card .device-info p {
      color: #1976D2;
    }

    // Plug Cards
    .plug-card .device-icon {
      background: linear-gradient(135deg, #FF9800, #F57C00);
    }

    .plug-card .device-info p {
      color: #F57C00;
    }

    // Floor Lamp Cards
    .floor-lamp-card .device-icon {
      background: linear-gradient(135deg, #FFC107, #FF9800);
    }

    .floor-lamp-card .device-info p {
      color: #4CAF50;
    }

    // Temperature Cards
    .temperature-card {
      background: linear-gradient(135deg, #fff5eb, #ffe8d4) !important;
    }

    .temperature-card .device-icon {
      background: linear-gradient(135deg, #FF9800, #F57C00);
    }

    .temperature-card .device-info {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .temperature-display {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
    }

    .temp-value {
      font-size: 24px;
      font-weight: 700;
      color: #333;
    }

    .temp-label {
      font-size: 12px;
      color: #666;
      font-weight: 500;
    }

    .temp-controls {
      display: flex;
      gap: 8px;
    }

    .temp-btn {
      width: 32px;
      height: 32px;
      border: none;
      border-radius: 50%;
      background: white;
      color: #333;
      font-size: 16px;
      cursor: pointer;
      transition: all 0.2s ease;
      box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }

    .temp-btn:hover {
      background: #f5f5f5;
      color: #111;
    }

    .temp-btn:active {
      transform: scale(0.95);
    }

    // Weather Cards
    .weather-card .device-icon {
      background: linear-gradient(135deg, #FFD54F, #FFC107);
    }

    .weather-card .device-info p {
      color: #FFC107;
    }

    // Air Quality Cards
    .air-quality-card .device-icon {
      background: linear-gradient(135deg, #4CAF50, #8BC34A);
    }

    .air-quality-card .device-info p {
      color: #8BC34A;
    }

    // Fan Cards
    .fan-card .device-icon {
      background: linear-gradient(135deg, #03A9F4, #0288D0);
    }

    .fan-card .device-info p {
      color: #0288D1;
    }

    // Indoor Temperature Cards
    .indoor-temp .device-icon {
      background: linear-gradient(135deg, #FF9800, #F57C00);
    }

    .room-list {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-bottom: 12px;
    }

    .room-item {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 12px;
      color: #666;
    }

    .select-sensors-btn {
      background: white;
      border: none;
      border-radius: 8px;
      padding: 8px 16px;
      color: #333;
      font-size: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }

    .select-sensors-btn:hover {
      background: #f5f5f5;
      color: #111;
    }

    // Modal Styles
    .modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 1000;
    }

    .modal-content {
      background: white;
      border-radius: 16px;
      box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
      max-width: 500px;
      width: 90%;
      max-height: 90vh;
      overflow-y: auto;
    }

    .modal-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 24px;
      border-bottom: 1px solid #e0e0e0;
    }

    .modal-header h3 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #333;
    }

    .device-name-input {
      font-size: 20px;
      font-weight: 600;
      color: #333;
      border: 2px solid #2196F3;
      border-radius: 8px;
      padding: 8px 12px;
      background: white;
      outline: none;
    }

    .device-select,
    .device-status-input,
    .device-location-input {
      padding: 8px 12px;
      border: 2px solid #2196F3;
      border-radius: 6px;
      font-size: 14px;
      font-weight: 500;
      color: #333;
      background: white;
      outline: none;
    }

    .device-select:focus,
    .device-status-input:focus,
    .device-location-input:focus {
      border-color: #1976D2;
      box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.2);
    }

    .close-btn {
      background: none;
      border: none;
      font-size: 20px;
      color: #666;
      cursor: pointer;
      padding: 4px;
      border-radius: 50%;
      transition: all 0.2s ease;
    }

    .close-btn:hover {
      background: #f5f5f5;
      color: #333;
    }

    .modal-body {
      padding: 24px;
    }

    .form-group {
      margin-bottom: 20px;
    }

    .form-group label {
      display: block;
      margin-bottom: 8px;
      font-weight: 500;
      color: #333;
    }

    .form-group input,
    .form-group select {
      width: 100%;
      padding: 12px;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 14px;
      transition: border-color 0.3s ease;
    }

    .form-group input:focus,
    .form-group select:focus {
      outline: none;
      border-color: #2196F3;
    }

    .device-detail-info {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .detail-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px;
      background: #f5f5f5;
      border-radius: 8px;
    }

    .detail-item label {
      font-weight: 500;
      color: #666;
    }

    .detail-item span {
      font-weight: 600;
      color: #333;
    }

    .modal-footer {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
      padding: 20px 24px;
      border-top: 1px solid #e0e0e0;
    }

    .btn-cancel,
    .btn-add,
    .btn-edit,
    .btn-delete,
    .btn-save {
      padding: 10px 20px;
      border: none;
      border-radius: 8px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .btn-cancel {
      background: #e0e0e0;
      color: #666;
    }

    .btn-cancel:hover {
      background: #d0d0d0;
    }

    .btn-add {
      background: #4CAF50;
      color: white;
    }

    .btn-add:hover {
      background: #45a049;
    }

    .btn-edit {
      background: #2196F3;
      color: white;
    }

    .btn-edit:hover {
      background: #1976D2;
    }

    .btn-save {
      background: #4CAF50;
      color: white;
    }

    .btn-save:hover {
      background: #45a049;
    }

    .btn-delete {
      background: #f44336;
      color: white;
    }

    .btn-delete:hover {
      background: #d32f2f;
    }

    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }

    .alert-toast {
      position: fixed;
      top: 30px;
      right: 30px;
      z-index: 9999;
      background: #ff5252;
      color: white;
      padding: 20px 30px;
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(255, 82, 82, 0.4);
      display: flex;
      align-items: center;
      gap: 15px;
      animation: slideIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      transition: all 0.5s ease;
    }

    .alert-toast.acknowledged {
      background: #e8f5e9;
      color: #2e7d32;
      box-shadow: 0 10px 30px rgba(46, 125, 50, 0.2);
    }

    .alert-toast.acknowledged .close-alert {
      background: rgba(46, 125, 50, 0.1);
      color: #2e7d32;
    }

    .acknowledge-btn {
      background: white;
      color: #ff5252;
      border: none;
      padding: 8px 16px;
      border-radius: 8px;
      font-weight: 700;
      font-size: 12px;
      cursor: pointer;
      margin-left: 10px;
      transition: all 0.3s ease;
    }

    .acknowledge-btn:hover {
      transform: scale(1.05);
      box-shadow: 0 4px 10px rgba(0,0,0,0.1);
    }

    .alert-toast i {
      font-size: 24px;
      animation: pulse 1s infinite;
    }

    .alert-toast-content h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 700;
    }

    .alert-toast-content p {
      margin: 5px 0 0;
      font-size: 14px;
      opacity: 0.9;
    }

    .close-alert {
      margin-left: 20px;
      background: rgba(255, 255, 255, 0.2);
      border: none;
      color: white;
      width: 24px;
      height: 24px;
      border-radius: 50%;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
    }

    @keyframes pulse {
      0% {
        transform: scale(1);
      }
      50% {
        transform: scale(1.2);
      }
      100% {
        transform: scale(1);
      }
    }

  `]
})
export class DashboardComponent implements OnInit {
  showAddDeviceModal = false;
  showDeviceDetailsModal = false;
  isEditingDevice = false;
  currentFilter = 'All';
  showAlertNotification = false;
  isAlertAcknowledged = false;
  alertMessage = 'Attention : Intrusion zone dangereuse détectée !';
  
  newDevice = {
    name: '',
    type: '',
    location: ''
  };
  
  selectedDevice = {
    id: '',
    name: '',
    type: '',
    status: '',
    location: '',
    isLarge: false,
    isFavorite: false
  };

  // Backup pour annuler les modifications
  originalDevice = {
    id: '',
    name: '',
    type: '',
    status: '',
    location: '',
    isLarge: false,
    isFavorite: false
  };

  // Stockage des dispositifs avec leurs informations
  devices = [
    { id: '1', name: 'Vanne pneumatique', type: 'lock', status: 'Fermée', location: 'Atelier', isFavorite: true, isLarge: false },
    { id: '2', name: 'Moteur convoyeur', type: 'garage', status: 'Arrêté', location: 'Ligne A', isFavorite: true, isLarge: false },
    { id: '3', name: 'Éclairage atelier', type: 'light', status: 'On - 50%', location: 'Atelier', isFavorite: true, isLarge: false },
    { id: '4', name: 'Convoyeur principal', type: 'tv', status: 'Off', location: 'Ligne B', isFavorite: false, isLarge: false },
    { id: '5', name: 'Capteur de vibration', type: 'speaker', status: 'Actif', location: 'Compresseur', isFavorite: true, isLarge: false },
    { id: '6', name: 'Niveau cuve', type: 'plug', status: '85%', location: 'Zone Stockage', isFavorite: false, isLarge: false },
    { id: '7', name: 'Sirène d\'alarme', type: 'floor-lamp', status: 'Off', location: 'Atelier', isFavorite: false, isLarge: false },
    { id: '8', name: 'Température four', type: 'temperature', status: '220°C', location: 'Zone Four', isFavorite: true, isLarge: false },
    { id: '9', name: 'Caméra Zone stockage', type: 'camera', status: 'Live', location: 'Zone stockage', isLarge: true, isFavorite: true },
    { id: '9-2', name: 'Caméra Portail', type: 'camera', status: 'Live', location: 'Portail', isLarge: false, isFavorite: true },
    { id: '10', name: 'Ventilateur extracteur', type: 'fan', status: 'On', location: 'Atelier', isFavorite: false, isLarge: false },
    { id: '11', name: 'Qualité air atelier', type: 'air-quality', status: '32 - Good', location: 'Atelier', isFavorite: false, isLarge: false },
    { id: '12', name: 'Station météo site', type: 'weather', status: '18°C Nuageux', location: 'Extérieur', isFavorite: false, isLarge: false },
    { id: '13', name: 'Températures zones', type: 'indoor-temp', status: 'Multiple zones', location: 'Atelier', isFavorite: true, isLarge: false }
  ];

  col1Devices: any[] = [];
  col2Devices: any[] = [];
  col3Devices: any[] = [];
  col4Devices: any[] = [];

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef,
    private iotService: IotWebsocketService
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDeviceLayout();
      // Si on a un filtre actif (autre que All), on l'applique
      if (this.currentFilter !== 'All') {
        this.applyFilter();
      }
      
      // Subscribe to real-time IoT updates
      this.iotService.messages$.subscribe(msg => {
        console.log('Real-time message received:', msg);
        if (msg.type === 'DEVICE_DATA' || msg.type === 'DEVICE_STATUS_UPDATE') {
          const deviceId = msg.deviceId || (msg.data && msg.data.id);
          if (!deviceId) return;
          
          const deviceIndex = this.devices.findIndex(d => d.id === deviceId);
          if (deviceIndex !== -1) {
            if (msg.type === 'DEVICE_DATA') {
              this.devices[deviceIndex].status = msg.data;
            } else {
              this.devices[deviceIndex] = { ...this.devices[deviceIndex], ...msg.data };
            }
            this.updateDeviceInColumns(deviceId, this.devices[deviceIndex]);
            this.cdr.detectChanges();
            console.log(`Device ${deviceId} updated successfully`);
          }
        } else if (msg.type === 'DEVICE_ALERT') {
          this.alertMessage = msg.alert || 'Alerte détectée !';
          this.showAlertNotification = true;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.initDefaultLayout();
    }
  }

  testFilter(filter: string) {
    console.log('=== FILTER CLICK TEST ===');
    console.log('Filter clicked:', filter);
    console.log('Total devices in array:', this.devices.length);
    alert('Filter clicked: ' + filter + ' - Devices: ' + this.devices.length); // This will definitely show if clicks are working
    
    this.currentFilter = filter;
    console.log('Current filter set to:', this.currentFilter);
    
    // Force refresh devices array
    console.log('Devices before filter:', this.devices.map(d => ({ id: d.id, name: d.name, type: d.type })));
    
    // Clear all columns first
    this.col1Devices = [];
    this.col2Devices = [];
    this.col3Devices = [];
    this.col4Devices = [];
    
    // Apply filter logic directly
    let filteredDevices = [...this.devices];
    
    switch (filter) {
      case 'Favorites':
        filteredDevices = this.devices.filter(d => d.isFavorite);
        break;
      case 'Cameras':
        filteredDevices = this.devices.filter(d => d.type === 'camera');
        console.log('Camera filter - All devices:', this.devices.map(d => ({ id: d.id, name: d.name, type: d.type })));
        console.log('Camera filter - Filtered devices:', filteredDevices.map(d => ({ id: d.id, name: d.name, type: d.type })));
        break;
      case 'Lights':
        filteredDevices = this.devices.filter(d => d.type === 'light' || d.type === 'floor-lamp');
        break;
      case 'Wifi':
        filteredDevices = this.devices.filter(d => ['plug', 'speaker', 'tv'].includes(d.type));
        break;
      case 'Climate':
        filteredDevices = this.devices.filter(d => ['temperature', 'indoor-temp', 'air-quality', 'fan', 'weather'].includes(d.type));
        break;
      case 'All':
      default:
        // Use the exact same layout as original dashboard - DO NOT CHANGE ANYTHING
        this.col1Devices = [
          this.devices.find(d => d.id === '9'), 
          this.devices.find(d => d.id === '1'), 
          this.devices.find(d => d.id === '2')
        ];
        this.col2Devices = [
          this.devices.find(d => d.id === '3'), 
          this.devices.find(d => d.id === '4'), 
          this.devices.find(d => d.id === '5'), 
          this.devices.find(d => d.id === '6'), 
          this.devices.find(d => d.id === '7')
        ];
        this.col3Devices = [
          this.devices.find(d => d.id === '8'), 
          { ...this.devices.find(d => d.id === '9'), id: '9-small', isLarge: false },
          this.devices.find(d => d.id === '9-2') // Add second camera here
        ];
        this.col4Devices = [
          this.devices.find(d => d.id === '10'), 
          this.devices.find(d => d.id === '11'), 
          this.devices.find(d => d.id === '12'), 
          this.devices.find(d => d.id === '13')
        ];
        console.log('All filter - Original layout restored');
        this.cdr.detectChanges();
        return;
    }
    
    // Distribute filtered devices
    filteredDevices.forEach((device, index) => {
      const colIndex = index % 4;
      if (colIndex === 0) this.col1Devices.push(device);
      else if (colIndex === 1) this.col2Devices.push(device);
      else if (colIndex === 2) this.col3Devices.push(device);
      else this.col4Devices.push(device);
    });
    
    console.log('Filter applied. Column counts:', {
      col1: this.col1Devices.length,
      col2: this.col2Devices.length,
      col3: this.col3Devices.length,
      col4: this.col4Devices.length
    });
    
    // Force change detection
    this.cdr.detectChanges();
    console.log('Change detection forced');
  }

  setFilter(filter: string) {
    this.currentFilter = filter;
    this.applyFilter();
  }

  applyFilter() {
    // Clear columns
    this.col1Devices = [];
    this.col2Devices = [];
    this.col3Devices = [];
    this.col4Devices = [];

    let filteredDevices = [...this.devices];

    switch (this.currentFilter) {
      case 'Favorites':
        filteredDevices = this.devices.filter(d => d.isFavorite);
        break;
      case 'Cameras':
        filteredDevices = this.devices.filter(d => d.type === 'camera');
        break;
      case 'Lights':
        filteredDevices = this.devices.filter(d => d.type === 'light' || d.type === 'floor-lamp');
        break;
      case 'Wifi':
        filteredDevices = this.devices.filter(d => ['plug', 'speaker', 'tv'].includes(d.type));
        break;
      case 'Climate':
        filteredDevices = this.devices.filter(d => ['temperature', 'indoor-temp', 'air-quality', 'fan', 'weather'].includes(d.type));
        break;
      case 'All':
      default:
        // Use original layout
        this.col1Devices = [
          this.devices.find(d => d.id === '9'), 
          this.devices.find(d => d.id === '1'), 
          this.devices.find(d => d.id === '2')
        ];
        this.col2Devices = [
          this.devices.find(d => d.id === '3'), 
          this.devices.find(d => d.id === '4'), 
          this.devices.find(d => d.id === '5'), 
          this.devices.find(d => d.id === '6'), 
          this.devices.find(d => d.id === '7')
        ];
        this.col3Devices = [
          this.devices.find(d => d.id === '8'), 
          { ...this.devices.find(d => d.id === '9'), id: '9-small', isLarge: false },
          this.devices.find(d => d.id === '9-2')
        ];
        this.col4Devices = [
          this.devices.find(d => d.id === '10'), 
          this.devices.find(d => d.id === '11'), 
          this.devices.find(d => d.id === '12'), 
          this.devices.find(d => d.id === '13')
        ];
        this.cdr.detectChanges();
        return;
    }

    // Distribute filtered devices
    filteredDevices.forEach((device, index) => {
      const colIndex = index % 4;
      if (colIndex === 0) this.col1Devices.push(device);
      else if (colIndex === 1) this.col2Devices.push(device);
      else if (colIndex === 2) this.col3Devices.push(device);
      else this.col4Devices.push(device);
    });

    this.cdr.detectChanges();
  }

  private loadDeviceLayout() {
    const savedLayout = localStorage.getItem('industrialIoT_devices_layout');
    if (savedLayout) {
      const layout = JSON.parse(savedLayout);
      this.col1Devices = layout.col1;
      this.col2Devices = layout.col2;
      this.col3Devices = layout.col3;
      this.col4Devices = layout.col4;
      this.devices = layout.allDevices || this.devices;

      // Réparer si "Températures zones" est manquant
      const indoorTempExists = this.devices.some(d => d.id === '13');
      if (!indoorTempExists) {
        const indoorTemp = { id: '13', name: 'Températures zones', type: 'indoor-temp', status: 'Multiple zones', location: 'Atelier', isFavorite: true, isLarge: false };
        this.devices.push(indoorTemp);
        this.col4Devices.push(indoorTemp);
        this.saveDeviceLayout();
      } else {
        // Vérifier s'il est présent dans l'une des colonnes
        const inColumns = [...this.col1Devices, ...this.col2Devices, ...this.col3Devices, ...this.col4Devices].some(d => d.id === '13');
        if (!inColumns) {
          this.col4Devices.push(this.devices.find(d => d.id === '13'));
          this.saveDeviceLayout();
        }
      }
    } else {
      this.initDefaultLayout();
    }
  }

  private saveDeviceLayout() {
    if (!isPlatformBrowser(this.platformId)) return;
    
    const layout = {
      col1: this.col1Devices,
      col2: this.col2Devices,
      col3: this.col3Devices,
      col4: this.col4Devices,
      allDevices: this.devices
    };
    localStorage.setItem('industrialIoT_devices_layout', JSON.stringify(layout));
  }

  private initDefaultLayout() {
    this.col1Devices = [this.devices.find(d => d.id === '9'), this.devices.find(d => d.id === '1'), this.devices.find(d => d.id === '2')];
    this.col2Devices = [this.devices.find(d => d.id === '3'), this.devices.find(d => d.id === '4'), this.devices.find(d => d.id === '5'), this.devices.find(d => d.id === '6'), this.devices.find(d => d.id === '7')];
    this.col3Devices = [this.devices.find(d => d.id === '8'), { ...this.devices.find(d => d.id === '9'), id: '9-small', isLarge: false }];
    this.col4Devices = [this.devices.find(d => d.id === '10'), this.devices.find(d => d.id === '11'), this.devices.find(d => d.id === '12'), this.devices.find(d => d.id === '13')];
  }

  onDrop(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
    this.saveDeviceLayout();
  }

  goToSearch(): void {
    this.router.navigate(['/search']);
  }


  logout(): void {
    localStorage.removeItem('userToken');
    localStorage.removeItem('userData');
    this.router.navigate(['/welcome']);
  }

  openAddDeviceModal(): void {
    this.showAddDeviceModal = true;
  }

  closeAddDeviceModal(): void {
    this.showAddDeviceModal = false;
    this.newDevice = {
      name: '',
      type: '',
      location: ''
    };
  }

  addDevice(): void {
    if (this.newDevice.name && this.newDevice.type) {
      const newId = (this.devices.length + 1).toString();
      const device = {
        id: newId,
        name: this.newDevice.name,
        type: this.newDevice.type,
        status: 'Off',
        location: this.newDevice.location || 'Non spécifiée',
        isFavorite: false,
        isLarge: false
      };
      this.devices.push(device);
      this.col4Devices.push(device); // Ajouter à la 4ème colonne par défaut
      this.saveDeviceLayout();
      console.log('Nouveau dispositif ajouté:', device);
      this.closeAddDeviceModal();
    }
  }

  openDeviceDetails(type: string, name: string, status: string): void {
    // Trouver le dispositif correspondant
    const device = this.devices.find(d => d.name === name);
    if (device) {
      this.selectedDevice = { ...device };
    } else {
      this.selectedDevice = {
        id: Date.now().toString(),
        name,
        type,
        status,
        location: this.getDeviceLocation(name),
        isLarge: type === 'camera',
        isFavorite: false
      };
    }
    this.isEditingDevice = false;
    this.showDeviceDetailsModal = true;
  }

  closeDeviceDetailsModal(): void {
    this.showDeviceDetailsModal = false;
    this.isEditingDevice = false;
  }

  startEditingDevice(): void {
    // Sauvegarder l'état original
    this.originalDevice = { ...this.selectedDevice };
    this.isEditingDevice = true;
  }

  saveDeviceChanges(): void {
    // Mettre à jour le dispositif dans le tableau
    const deviceIndex = this.devices.findIndex(d => d.id === this.selectedDevice.id);
    if (deviceIndex !== -1) {
      this.devices[deviceIndex] = { ...this.selectedDevice };
    } else {
      // Si le dispositif n'existe pas, l'ajouter
      this.devices.push({ ...this.selectedDevice });
    }
    
    console.log('Dispositif modifié et sauvegardé:', this.selectedDevice);
    this.saveDeviceLayout();
    this.isEditingDevice = false;
  }

  cancelEditing(): void {
    // Restaurer l'état original
    this.selectedDevice = { ...this.originalDevice };
    this.isEditingDevice = false;
  }

  editDevice(): void {
    this.startEditingDevice();
  }

  deleteDevice(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer "${this.selectedDevice.name}" ?`)) {
      // Supprimer le dispositif de la colonne où il se trouve
      const filterFn = (d: any) => d.id !== this.selectedDevice.id;
      this.col1Devices = this.col1Devices.filter(filterFn);
      this.col2Devices = this.col2Devices.filter(filterFn);
      this.col3Devices = this.col3Devices.filter(filterFn);
      this.col4Devices = this.col4Devices.filter(filterFn);
      
      this.devices = this.devices.filter(filterFn);
      this.saveDeviceLayout();
      console.log('Dispositif supprimé:', this.selectedDevice);
      this.closeDeviceDetailsModal();
    }
  }

  // Méthode pour obtenir les informations d'un dispositif pour l'affichage
  triggerAlert(): void {
    this.isAlertAcknowledged = false;
    this.alertMessage = 'Attention : Intrusion zone dangereuse détectée !';
    this.showAlertNotification = true;
  }

  acknowledgeAlert(): void {
    this.isAlertAcknowledged = true;
    this.alertMessage = 'Alerte acquittée par l\'utilisateur';
    // On garde la notif affichée en vert
  }

  closeAlertNotification(): void {
    this.showAlertNotification = false;
    this.isAlertAcknowledged = false;
  }

  getDeviceInfo(deviceName: string) {
    const device = this.devices.find(d => d.name === deviceName);
    return device || { name: deviceName, status: 'Unknown' };
  }

  getIcon(type: string): string {
    switch (type) {
      case 'light': return 'fa-lightbulb';
      case 'camera': return 'fa-video';
      case 'lock': return 'fa-cog';
      case 'thermostat': return 'fa-temperature-high';
      case 'speaker': return 'fa-wave-square';
      case 'plug': return 'fa-tint';
      case 'floor-lamp': return 'fa-exclamation-triangle';
      case 'air-quality': return 'fa-wind';
      case 'temperature': return 'fa-thermometer-half';
      case 'garage': return 'fa-industry';
      case 'fan': return 'fa-fan';
      case 'weather': return 'fa-cloud-sun';
      default: return 'fa-circle';
    }
  }

  toggleFavorite(device: any): void {
    // Find the device in the devices array and toggle its favorite status
    const deviceIndex = this.devices.findIndex(d => d.id === device.id);
    if (deviceIndex !== -1) {
      this.devices[deviceIndex].isFavorite = !this.devices[deviceIndex].isFavorite;
      
      // Also update the device in the current column arrays
      this.updateDeviceInColumns(device.id, this.devices[deviceIndex]);
      
      // Save the updated layout
      this.saveDeviceLayout();
      
      // Force change detection
      this.cdr.detectChanges();
    }
  }

  private updateDeviceInColumns(deviceId: string, updatedDevice: any): void {
    // Update device in all column arrays
    const updateInArray = (array: any[]) => {
      const index = array.findIndex(d => d.id === deviceId);
      if (index !== -1) {
        array[index] = updatedDevice;
      }
    };
    
    updateInArray(this.col1Devices);
    updateInArray(this.col2Devices);
    updateInArray(this.col3Devices);
    updateInArray(this.col4Devices);
  }

  private getDeviceLocation(name: string): string {
    if (name.toLowerCase().includes('portail')) return 'Portail';
    if (name.toLowerCase().includes('convoyeur')) return 'Ligne Production';
    if (name.toLowerCase().includes('vibration')) return 'Compresseur';
    if (name.toLowerCase().includes('stockage')) return 'Zone Stockage';
    if (name.toLowerCase().includes('four')) return 'Zone Four';
    if (name.toLowerCase().includes('ventilateur')) return 'Atelier';
    if (name.toLowerCase().includes('air')) return 'Atelier';
    if (name.toLowerCase().includes('salle serveur')) return 'Salle Serveur';
    return 'Atelier';
  }
}
