import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TopicsListComponent } from './topics-list.component';
import { TopicService } from '../../../core/services/topic.service';
import { AuthService } from '../../../core/services/auth.service';
import { SubscriptionService } from '../../../core/services/subscription.service';

const mockTopics = [
  { id: 1, name: 'Java', description: 'Java topic' },
  { id: 2, name: 'Angular', description: 'Angular topic' },
];

const mockUser = {
  id: 1,
  email: 'alice@test.com',
  username: 'alice',
  subscriptions: [{ id: 1, name: 'Java', description: 'Java topic' }],
};

describe('TopicsListComponent (integration)', () => {
  let component: TopicsListComponent;
  let fixture: ComponentFixture<TopicsListComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopicsListComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        TopicService,
        AuthService,
        SubscriptionService,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(TopicsListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  function initComponent(): void {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:3001/api/topics').flush(mockTopics);
    httpMock.expectOne('http://localhost:3001/api/user/me').flush(mockUser);
  }

  it('should load topics and subscriptions on init', () => {
    initComponent();

    expect(component.topics).toEqual(mockTopics);
    expect(component.isSubscribed(1)).toBe(true);
    expect(component.isSubscribed(2)).toBe(false);
  });

  it('should subscribe and reload subscriptions', () => {
    initComponent();

    component.subscribe(2);

    const subReq = httpMock.expectOne('http://localhost:3001/api/topics/2/subscribe');
    expect(subReq.request.method).toBe('POST');
    subReq.flush(null);

    const meReq = httpMock.expectOne('http://localhost:3001/api/user/me');
    meReq.flush({
      ...mockUser,
      subscriptions: [...mockUser.subscriptions, { id: 2, name: 'Angular', description: 'Angular topic' }],
    });

    expect(component.isSubscribed(2)).toBe(true);
  });
});
