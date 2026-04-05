import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyPendingComponent } from './verify-pending';

describe('VerifyPending', () => {
  let component: VerifyPendingComponent;
  let fixture: ComponentFixture<VerifyPendingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerifyPendingComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VerifyPendingComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
