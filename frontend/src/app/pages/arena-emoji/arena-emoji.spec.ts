import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArenaEmoji } from './arena-emoji';

describe('ArenaEmoji', () => {
  let component: ArenaEmoji;
  let fixture: ComponentFixture<ArenaEmoji>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArenaEmoji]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArenaEmoji);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
