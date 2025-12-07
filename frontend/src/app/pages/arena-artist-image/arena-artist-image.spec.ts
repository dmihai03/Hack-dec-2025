import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArenaArtistImage } from './arena-artist-image';

describe('ArenaArtistImage', () => {
  let component: ArenaArtistImage;
  let fixture: ComponentFixture<ArenaArtistImage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArenaArtistImage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArenaArtistImage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
