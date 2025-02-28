import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuditAdministrationComponent } from './audit-administration.component';

describe('AuditAdministrationComponent', () => {
  let component: AuditAdministrationComponent;
  let fixture: ComponentFixture<AuditAdministrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuditAdministrationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuditAdministrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
