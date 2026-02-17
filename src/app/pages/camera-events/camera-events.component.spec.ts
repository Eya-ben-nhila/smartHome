import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CameraEventsComponent } from './camera-events.component';

describe('CameraEventsComponent', () => {
  let component: CameraEventsComponent;
  let fixture: ComponentFixture<CameraEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CameraEventsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CameraEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
