import { TestBed } from '@angular/core/testing';
import { CreatePostComponent } from './create-post.component';

describe('CreatePostComponent (unit)', () => {
  let component: CreatePostComponent;
  const mockPostService = { create: jest.fn() };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    component = TestBed.runInInjectionContext(
      () => new CreatePostComponent(mockPostService as any, null as any, null as any)
    );
    mockPostService.create.mockClear();
  });

  it('should have default empty state', () => {
    expect(component.topicId).toBeNull();
    expect(component.title).toBe('');
    expect(component.content).toBe('');
    expect(component.errorMessage).toBe('');
  });

  it('should not call create when topicId is null', () => {
    component.topicId = null;

    component.onSubmit();

    expect(mockPostService.create).not.toHaveBeenCalled();
  });
});
