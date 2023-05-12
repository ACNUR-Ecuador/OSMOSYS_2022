import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RBAIndicators2023Component } from './rbaindicators2023.component';

describe('RBAIndicators2023Component', () => {
  let component: RBAIndicators2023Component;
  let fixture: ComponentFixture<RBAIndicators2023Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RBAIndicators2023Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RBAIndicators2023Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
