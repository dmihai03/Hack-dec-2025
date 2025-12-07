import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RetroRoom } from './retro-room';

describe('RetroRoom', () => {
  let component: RetroRoom;
  let fixture: ComponentFixture<RetroRoom>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RetroRoom]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RetroRoom);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
