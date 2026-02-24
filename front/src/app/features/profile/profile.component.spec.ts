import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ProfileComponent } from './profile.component';
import { AuthService } from '../../core/services/auth.service';
import { SubscriptionService } from '../../core/services/subscription.service';

const mockUser = {
  id: 1,
  email: 'alice@test.com',
  username: 'alice',
  subscriptions: [{ id: 10, name: 'Java', description: 'Java topic' }],
};

describe('ProfileComponent (integration)', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        SubscriptionService,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  function initComponent(): void {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:3001/api/user/me').flush(mockUser);
  }

  it('should load user and populate form on init', () => {
    initComponent();

    expect(component.user).toEqual(mockUser);
    expect(component.email).toBe('alice@test.com');
    expect(component.username).toBe('alice');
  });

  it('should update only changed username on save', () => {
    initComponent();

    component.username = 'alice-new';
    component.onSave();

    const req = httpMock.expectOne('http://localhost:3001/api/user/me');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({ username: 'alice-new' });

    const updatedUser = { ...mockUser, username: 'alice-new' };
    req.flush(updatedUser);

    expect(component.user).toEqual(updatedUser);
    expect(component.successMessage).toBe('Profile updated successfully');
    expect(component.password).toBe('');
  });

  it('should update only changed email on save', () => {
    initComponent();

    component.email = 'new@test.com';
    component.onSave();

    const req = httpMock.expectOne('http://localhost:3001/api/user/me');
    expect(req.request.body).toEqual({ email: 'new@test.com' });
    req.flush({ ...mockUser, email: 'new@test.com' });
  });

  it('should skip request when no fields changed', () => {
    initComponent();

    component.onSave();

    httpMock.expectNone('http://localhost:3001/api/user/me');
  });

  it('should display error message on update failure', () => {
    initComponent();

    component.email = 'taken@test.com';
    component.onSave();

    const req = httpMock.expectOne('http://localhost:3001/api/user/me');
    req.flush({ error: 'Email already in use' }, { status: 400, statusText: 'Bad Request' });

    expect(component.errorMessage).toBe('Email already in use');
  });

  it('should unsubscribe and reload user', () => {
    initComponent();

    component.unsubscribe(10);

    const unsubReq = httpMock.expectOne('http://localhost:3001/api/topics/10/subscribe');
    expect(unsubReq.request.method).toBe('DELETE');
    unsubReq.flush(null);

    const meReq = httpMock.expectOne('http://localhost:3001/api/user/me');
    const updatedUser = { ...mockUser, subscriptions: [] };
    meReq.flush(updatedUser);

    expect(component.user!.subscriptions.length).toBe(0);
  });

  it('should clear token and navigate to home on logout', () => {
    initComponent();
    localStorage.setItem('mdd_token', 'some-token');

    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    component.logout();

    expect(localStorage.getItem('mdd_token')).toBeNull();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });
});
