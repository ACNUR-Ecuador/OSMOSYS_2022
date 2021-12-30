import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DissagregationTwoDimentionsComponent } from './dissagregation-two-dimentions.component';

describe('DissagregationTwoDimentionsComponent', () => {
  let component: DissagregationTwoDimentionsComponent;
  let fixture: ComponentFixture<DissagregationTwoDimentionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DissagregationTwoDimentionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DissagregationTwoDimentionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
