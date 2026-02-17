import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HeaderComponent } from './header.component';
import { AuthService } from '../../core/services/auth.service';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: Router;

  beforeEach(async () => {
    const authMock = {
      logout: jest.fn(),
      isLoggedIn: jest.fn().mockReturnValue(true),
    };

    await TestBed.configureTestingModule({
      declarations: [HeaderComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authMock },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('toggleMenu', () => {
    it('should toggle menuOpen from false to true', () => {
      expect(component.menuOpen).toBe(false);
      component.toggleMenu();
      expect(component.menuOpen).toBe(true);
    });

    it('should toggle menuOpen from true to false', () => {
      component.menuOpen = true;
      component.toggleMenu();
      expect(component.menuOpen).toBe(false);
    });
  });

  describe('closeMenu', () => {
    it('should set menuOpen to false', () => {
      component.menuOpen = true;
      component.closeMenu();
      expect(component.menuOpen).toBe(false);
    });
  });

  describe('logout', () => {
    it('should call authService.logout, close menu and navigate to /login', () => {
      const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
      component.menuOpen = true;

      component.logout();

      expect(authService.logout).toHaveBeenCalled();
      expect(component.menuOpen).toBe(false);
      expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    });
  });
});
