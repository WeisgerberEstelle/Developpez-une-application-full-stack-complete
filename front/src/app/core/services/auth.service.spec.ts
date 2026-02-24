import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const apiUrl = environment.apiBaseUrl;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  describe('login', () => {
    it('should store the token on successful login', () => {
      service.login({ emailOrUsername: 'alice@test.com', password: 'Password1!' }).subscribe();

      const req = httpMock.expectOne(`${apiUrl}/auth/login`);
      expect(req.request.method).toBe('POST');
      req.flush({ token: 'jwt-token' });

      expect(localStorage.getItem('mdd_token')).toBe('jwt-token');
    });
  });

  describe('register', () => {
    it('should store the token on successful register', () => {
      service.register({ email: 'bob@test.com', username: 'bob', password: 'Password1!' }).subscribe();

      const req = httpMock.expectOne(`${apiUrl}/auth/register`);
      expect(req.request.method).toBe('POST');
      req.flush({ token: 'jwt-token' });

      expect(localStorage.getItem('mdd_token')).toBe('jwt-token');
    });
  });

  describe('getToken', () => {
    it('should return the token from localStorage', () => {
      localStorage.setItem('mdd_token', 'stored-token');
      expect(service.getToken()).toBe('stored-token');
    });

    it('should return null when no token is stored', () => {
      expect(service.getToken()).toBeNull();
    });
  });

  describe('setToken', () => {
    it('should store the token in localStorage', () => {
      service.setToken('new-token');
      expect(localStorage.getItem('mdd_token')).toBe('new-token');
    });
  });

  describe('isLoggedIn', () => {
    it('should return true when a token exists', () => {
      localStorage.setItem('mdd_token', 'some-token');
      expect(service.isLoggedIn()).toBe(true);
    });

    it('should return false when no token exists', () => {
      expect(service.isLoggedIn()).toBe(false);
    });
  });

  describe('logout', () => {
    it('should remove the token from localStorage', () => {
      localStorage.setItem('mdd_token', 'token-to-remove');
      service.logout();
      expect(localStorage.getItem('mdd_token')).toBeNull();
    });
  });
});
