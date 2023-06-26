import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Myrp2022Component } from './myrp2022.component';

describe('Myrp2022Component', () => {
  let component: Myrp2022Component;
  let fixture: ComponentFixture<Myrp2022Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Myrp2022Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Myrp2022Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
