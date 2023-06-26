import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MYRP2023Component } from './myrp2023.component';

describe('MYRP2023Component', () => {
  let component: MYRP2023Component;
  let fixture: ComponentFixture<MYRP2023Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MYRP2023Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MYRP2023Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
