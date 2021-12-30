import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DissagregationTwoIntegerDimentionsComponent } from './dissagregation-two-integer-dimentions.component';

describe('DissagregationTwoIntegerDimentionsComponent', () => {
  let component: DissagregationTwoIntegerDimentionsComponent;
  let fixture: ComponentFixture<DissagregationTwoIntegerDimentionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DissagregationTwoIntegerDimentionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DissagregationTwoIntegerDimentionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
