import { TestBed } from '@angular/core/testing';
import { PostDetailComponent } from './post-detail.component';

describe('PostDetailComponent (unit)', () => {
  let component: PostDetailComponent;
  const mockCommentService = { addComment: jest.fn() };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    component = TestBed.runInInjectionContext(
      () => new PostDetailComponent(null as any, null as any, mockCommentService as any)
    );
    mockCommentService.addComment.mockClear();
  });

  it('should not call addComment when post is null', () => {
    component.post = null;
    component.newComment = 'hello';

    component.addComment();

    expect(mockCommentService.addComment).not.toHaveBeenCalled();
  });

  it('should not call addComment when newComment is empty', () => {
    component.post = { id: 1 } as any;
    component.newComment = '   ';

    component.addComment();

    expect(mockCommentService.addComment).not.toHaveBeenCalled();
  });
});
