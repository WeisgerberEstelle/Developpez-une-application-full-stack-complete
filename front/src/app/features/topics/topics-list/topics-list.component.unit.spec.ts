import { TestBed } from '@angular/core/testing';
import { TopicsListComponent } from './topics-list.component';

describe('TopicsListComponent (unit)', () => {
  let component: TopicsListComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    component = TestBed.runInInjectionContext(
      () => new TopicsListComponent(null as any, null as any, null as any)
    );
    component.subscribedTopicIds = new Set([1, 3, 5]);
  });

  it('should return true for a subscribed topic', () => {
    expect(component.isSubscribed(3)).toBe(true);
  });

  it('should return false for a non-subscribed topic', () => {
    expect(component.isSubscribed(2)).toBe(false);
  });
});
