import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneralIndicators2023Component } from './general-indicators2023.component';

describe('GeneralIndicators2023Component', () => {
  let component: GeneralIndicators2023Component;
  let fixture: ComponentFixture<GeneralIndicators2023Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GeneralIndicators2023Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GeneralIndicators2023Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
