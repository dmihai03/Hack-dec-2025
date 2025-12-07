import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArenaLyrics } from './arena-lyrics';

describe('ArenaLyrics', () => {
  let component: ArenaLyrics;
  let fixture: ComponentFixture<ArenaLyrics>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArenaLyrics]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArenaLyrics);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
