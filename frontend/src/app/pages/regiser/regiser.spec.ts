import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Regiser } from './regiser';

describe('Regiser', () => {
  let component: Regiser;
  let fixture: ComponentFixture<Regiser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Regiser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Regiser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
