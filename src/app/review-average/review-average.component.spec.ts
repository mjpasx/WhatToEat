import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewAverageComponent } from './review-average.component';

describe('ReviewAverageComponent', () => {
  let component: ReviewAverageComponent;
  let fixture: ComponentFixture<ReviewAverageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewAverageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewAverageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
