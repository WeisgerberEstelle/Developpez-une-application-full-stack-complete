import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../../core/services/auth.service';

describe('RegisterComponent (integration)', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [RegisterComponent],
      providers: [
        provideRouter([{ path: 'topics', redirectTo: '' }]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should send POST /auth/register, store token and navigate to /topics', () => {
    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);

    component.email = 'new@test.com';
    component.username = 'newuser';
    component.password = 'Password1!';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      email: 'new@test.com',
      username: 'newuser',
      password: 'Password1!',
    });

    req.flush({ token: 'jwt-new-token' });

    expect(localStorage.getItem('mdd_token')).toBe('jwt-new-token');
    expect(navigateSpy).toHaveBeenCalledWith(['/topics']);
  });

  it('should display field-level validation errors from server', () => {
    component.email = 'bad';
    component.username = 'u';
    component.password = 'weak';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/auth/register');
    req.flush(
      { email: 'Invalid email format', password: 'Password too weak' },
      { status: 400, statusText: 'Bad Request' }
    );

    expect(component.fieldErrors['email']).toBe('Invalid email format');
    expect(component.fieldErrors['password']).toBe('Password too weak');
    expect(component.errorMessage).toBe('');
  });

  it('should display generic error when server returns non-object error', () => {
    component.email = 'new@test.com';
    component.username = 'newuser';
    component.password = 'Password1!';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/auth/register');
    req.flush('Server error', { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toBe('Registration failed');
  });

  it('should clear previous errors on new submit', () => {
    component.fieldErrors = { email: 'old error' };
    component.errorMessage = 'old generic';
    component.email = 'new@test.com';
    component.username = 'newuser';
    component.password = 'Password1!';
    component.onSubmit();

    expect(component.fieldErrors).toEqual({});
    expect(component.errorMessage).toBe('');

    const req = httpMock.expectOne('http://localhost:3001/api/auth/register');
    req.flush({ token: 'tok' });
  });
});
