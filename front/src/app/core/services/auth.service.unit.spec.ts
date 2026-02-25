import { AuthService } from './auth.service';

describe('AuthService (unit)', () => {
  let service: AuthService;

  beforeEach(() => {
    localStorage.clear();
    service = new AuthService(null as any);
  });

  afterEach(() => localStorage.clear());

  it('should store the token in localStorage', () => {
    service.setToken('abc123');
    expect(localStorage.getItem('mdd_token')).toBe('abc123');
  });

  it('should return the token from localStorage', () => {
    localStorage.setItem('mdd_token', 'xyz');
    expect(service.getToken()).toBe('xyz');
  });

  it('should return true when a token exists', () => {
    localStorage.setItem('mdd_token', 'token');
    expect(service.isLoggedIn()).toBe(true);
  });

  it('should return false when no token exists', () => {
    expect(service.isLoggedIn()).toBe(false);
  });

  it('should remove the token on logout', () => {
    localStorage.setItem('mdd_token', 'token');
    service.logout();
    expect(localStorage.getItem('mdd_token')).toBeNull();
  });
});
