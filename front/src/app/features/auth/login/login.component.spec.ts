import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../core/services/auth.service';

describe('LoginComponent (integration)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([{ path: 'feed', redirectTo: '' }]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should store token and navigate to /feed on successful login', () => {
    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);

    component.emailOrUsername = 'alice@test.com';
    component.password = 'Password1!';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      emailOrUsername: 'alice@test.com',
      password: 'Password1!',
    });

    req.flush({ token: 'jwt-token-123' });

    expect(localStorage.getItem('mdd_token')).toBe('jwt-token-123');
    expect(navigateSpy).toHaveBeenCalledWith(['/feed']);
  });

  it('should display error message when login fails', () => {
    component.emailOrUsername = 'alice@test.com';
    component.password = 'wrong';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/auth/login');
    req.flush({ error: 'Invalid credentials' }, { status: 400, statusText: 'Bad Request' });

    expect(component.errorMessage).toBe('Invalid credentials');
    expect(localStorage.getItem('mdd_token')).toBeNull();
  });

  it('should display fallback message when server returns no error detail', () => {
    component.emailOrUsername = 'alice@test.com';
    component.password = 'wrong';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/auth/login');
    req.flush(null, { status: 401, statusText: 'Server Error' });

    expect(component.errorMessage).toBe('Login failed');
  });

  it('should clear previous error on new submit', () => {
    component.errorMessage = 'old error';
    component.emailOrUsername = 'alice@test.com';
    component.password = 'Password1!';
    component.onSubmit();

    expect(component.errorMessage).toBe('');

    const req = httpMock.expectOne('http://localhost:3001/api/auth/login');
    req.flush({ token: 'tok' });
  });
});
