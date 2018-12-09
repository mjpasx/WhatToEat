import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AveragesComponent } from './averages.component';

describe('AveragesComponent', () => {
  let component: AveragesComponent;
  let fixture: ComponentFixture<AveragesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AveragesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AveragesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
