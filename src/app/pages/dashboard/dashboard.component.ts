import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DragDropModule, CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule],
  templateUrl: './dashboard.component.html',
  styles: [`
    .dashboard-content {
      padding: 20px;
      overflow-y: auto;
      height: 100vh;
      background: #f0f2f5;
    }

    .header-strip {
      background: white;
      border-radius: 16px;
      padding: 20px 30px;
      margin-bottom: 30px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    }

    .strip-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .left-section {
      display: flex;
      align-items: center;
      gap: 30px;
    }

    .dashboard-title {
      font-size: 28px;
      font-weight: 700;
      color: #1a1a1a;
      margin: 0;
    }

    .search-bar {
      display: flex;
      align-items: center;
      background: #f5f5f5;
      border-radius: 12px;
      padding: 12px 20px;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .search-bar i {
      color: #757575;
      margin-right: 12px;
    }

    .search-bar input {
      border: none;
      background: none;
      outline: none;
      font-size: 14px;
      color: #757575;
      cursor: pointer;
    }

    .search-bar:hover {
      background: #e8e8e8;
    }

    .right-section {
      display: flex;
      align-items: center;
      gap: 20px;
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
      background: white;
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      transition: all 0.3s ease;
      overflow: hidden;
      position: relative;
      cursor: pointer;
    }

    .device-card:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
    }

    .card-content {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 20px;
    }

    .device-icon {
      width: 48px;
      height: 48px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 12px;
      font-size: 28px;
      color: white;
    }

    .device-logo {
      width: 32px;
      height: 32px;
      background: white;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
      color: #333;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
      border: 2px solid #fff;
      margin-left: -6px;
      z-index: 2;
    }

    .device-info {
      flex: 1;
    }

    .device-info h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }

    .device-info p {
      margin: 0;
      font-size: 14px;
      color: #666;
      font-weight: 500;
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
    location: ''
  };

  // Backup pour annuler les modifications
  originalDevice = {
    id: '',
    name: '',
    type: '',
    status: '',
    location: ''
  };

  // Stockage des dispositifs avec leurs informations
  devices = [
    { id: '1', name: 'Front door lock', type: 'lock', status: 'Locked', location: 'Entrée' },
    { id: '2', name: 'Garage', type: 'garage', status: 'Closed', location: 'Garage' },
    { id: '3', name: 'Front door light', type: 'light', status: 'On - 50%', location: 'Entrée' },
    { id: '4', name: 'TV', type: 'tv', status: 'Off', location: 'Salon' },
    { id: '5', name: 'Speaker', type: 'speaker', status: 'Playing - 50%', location: 'Salon' },
    { id: '6', name: 'Smart plug', type: 'plug', status: 'Off', location: 'Salon' },
    { id: '7', name: 'Floor lamp', type: 'floor-lamp', status: 'On - 50%', location: 'Salon' },
    { id: '8', name: 'Downstairs', type: 'temperature', status: '68°F', location: 'RDC' },
    { id: '9', name: 'Backyard camera', type: 'camera', status: 'Live', location: 'Jardin', isLarge: true },
    { id: '10', name: 'Fan', type: 'fan', status: 'On', location: 'Salon' },
    { id: '11', name: 'Outdoor AQI', type: 'air-quality', status: '32 - Good', location: 'Extérieur' },
    { id: '12', name: 'San Francisco', type: 'weather', status: '56° Clear', location: 'Extérieur' },
    { id: '13', name: 'Indoor temperatures', type: 'indoor-temp', status: 'Multiple rooms', location: 'Maison' }
  ];

  col1Devices: any[] = [];
  col2Devices: any[] = [];
  col3Devices: any[] = [];
  col4Devices: any[] = [];

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDeviceLayout();
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
    localStorage.setItem('smartHome_devices_layout', JSON.stringify(layout));
  }

  private loadDeviceLayout() {
    const savedLayout = localStorage.getItem('smartHome_devices_layout');
    if (savedLayout) {
      const layout = JSON.parse(savedLayout);
      this.col1Devices = layout.col1;
      this.col2Devices = layout.col2;
      this.col3Devices = layout.col3;
      this.col4Devices = layout.col4;
      this.devices = layout.allDevices || this.devices;
    } else {
      this.initDefaultLayout();
    }
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
        location: this.newDevice.location || 'Non spécifiée'
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
        location: this.getDeviceLocation(name)
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
  getDeviceInfo(deviceName: string) {
    const device = this.devices.find(d => d.name === deviceName);
    return device || { name: deviceName, status: 'Unknown' };
  }

  getIcon(type: string): string {
    switch (type) {
      case 'light': return 'fa-lightbulb';
      case 'camera': return 'fa-video';
      case 'lock': return 'fa-lock';
      case 'thermostat': return 'fa-thermometer-half';
      case 'speaker': return 'fa-volume-up';
      case 'plug': return 'fa-plug';
      case 'floor-lamp': return 'fa-lightbulb';
      case 'air-quality': return 'fa-wind';
      case 'temperature': return 'fa-thermometer-half';
      case 'garage': return 'fa-car';
      default: return 'fa-cube';
    }
  }

  private getDeviceLocation(name: string): string {
    if (name.toLowerCase().includes('front door')) return 'Entrée';
    if (name.toLowerCase().includes('garage')) return 'Garage';
    if (name.toLowerCase().includes('downstairs')) return 'RDC';
    if (name.toLowerCase().includes('backyard')) return 'Jardin';
    if (name.toLowerCase().includes('fan')) return 'Salon';
    if (name.toLowerCase().includes('living room')) return 'Salon';
    if (name.toLowerCase().includes('guest bedroom')) return 'Chambre d\'amis';
    return 'Non spécifiée';
  }
}
