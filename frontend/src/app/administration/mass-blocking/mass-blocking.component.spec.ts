import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MassBlockingComponent } from './mass-blocking.component';

describe('MassBlockingComponent', () => {
  let component: MassBlockingComponent;
  let fixture: ComponentFixture<MassBlockingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MassBlockingComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MassBlockingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
