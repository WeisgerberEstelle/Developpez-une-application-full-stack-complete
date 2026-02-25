import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA, Component } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AppComponent } from './app.component';

@Component({ template: '', standalone: true })
class DummyComponent {}

describe('AppComponent', () => {
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        provideRouter([
          { path: '', component: DummyComponent },
          { path: 'feed', component: DummyComponent },
        ]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
      imports: [AppComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    router = TestBed.inject(Router);
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should hide header on home route', async () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    fixture.detectChanges();

    await router.navigateByUrl('/');
    fixture.detectChanges();

    expect(app.showHeader).toBe(false);
  });

  it('should show header on other routes', async () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    fixture.detectChanges();

    await router.navigateByUrl('/feed');
    fixture.detectChanges();

    expect(app.showHeader).toBe(true);
  });
});
