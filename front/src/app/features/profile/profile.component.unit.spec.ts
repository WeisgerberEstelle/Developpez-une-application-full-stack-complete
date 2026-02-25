import { TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';

describe('ProfileComponent (unit)', () => {
  let component: ProfileComponent;
  const mockAuthService = {
    updateProfile: jest.fn(),
    logout: jest.fn(),
  };
  const mockRouter = { navigate: jest.fn() };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    component = TestBed.runInInjectionContext(
      () => new ProfileComponent(mockAuthService as any, null as any, mockRouter as any)
    );
    mockAuthService.updateProfile.mockClear();
    mockAuthService.logout.mockClear();
    mockRouter.navigate.mockClear();
  });

  it('should not call updateProfile when nothing changed', () => {
    component.user = { email: 'a@b.com', username: 'alice' } as any;
    component.email = 'a@b.com';
    component.username = 'alice';
    component.password = '';

    component.onSave();

    expect(mockAuthService.updateProfile).not.toHaveBeenCalled();
  });

  it('should call logout and navigate to home', () => {
    component.logout();

    expect(mockAuthService.logout).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
