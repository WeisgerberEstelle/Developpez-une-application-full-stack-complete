import { TestBed } from '@angular/core/testing';
import { FeedComponent } from './feed.component';

describe('FeedComponent (unit)', () => {
  let component: FeedComponent;
  const mockRouter = { navigate: jest.fn() };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    component = TestBed.runInInjectionContext(
      () => new FeedComponent(null as any, mockRouter as any)
    );
  });

  it('should have empty posts and sortAsc false by default', () => {
    expect(component.posts).toEqual([]);
    expect(component.sortAsc).toBe(false);
  });

  it('should toggle sortAsc and reverse posts', () => {
    component.posts = [{ id: 1 }, { id: 2 }, { id: 3 }] as any;

    component.toggleSort();

    expect(component.sortAsc).toBe(true);
    expect(component.posts.map((p: any) => p.id)).toEqual([3, 2, 1]);
  });

  it('should navigate to post detail', () => {
    component.goToPost(42);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/posts', 42]);
  });
});
