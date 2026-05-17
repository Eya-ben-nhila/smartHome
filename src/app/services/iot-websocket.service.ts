import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable, Subject } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

export interface IoTMessage {
  type: string;
  deviceId?: string;
  data?: any;
  alert?: any;
  timestamp?: number;
}

@Injectable({
  providedIn: 'root'
})
export class IotWebsocketService {
  private socket$: WebSocketSubject<any> | null = null;
  private messageSubject = new Subject<IoTMessage>();
  public messages$ = this.messageSubject.asObservable();

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    if (isPlatformBrowser(this.platformId)) {
      this.connect();
    }
  }

  private connect() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // Using hardcoded localhost for now as requested for testing
    const url = `${protocol}//localhost:8080/api/ws/smarthome`;
    
    console.log('Connecting to IoT WebSocket:', url);
    
    this.socket$ = webSocket({
      url,
      openObserver: {
        next: () => console.log('IoT WebSocket connection established')
      },
      closeObserver: {
        next: () => {
          console.log('IoT WebSocket connection closed, retrying in 5s...');
          this.socket$ = null;
          setTimeout(() => this.connect(), 5000);
        }
      }
    });

    this.socket$.subscribe({
      next: (msg) => this.messageSubject.next(msg),
      error: (err) => console.error('IoT WebSocket error:', err)
    });
  }

  public sendMessage(msg: any) {
    if (this.socket$) {
      this.socket$.next(msg);
    }
  }
}
