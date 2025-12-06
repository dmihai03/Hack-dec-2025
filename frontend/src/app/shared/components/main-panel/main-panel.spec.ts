import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainPanelComponent } from './main-panel';

describe('MainPanel', () => {
  let component: MainPanelComponent;
  let fixture: ComponentFixture<MainPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MainPanelComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
