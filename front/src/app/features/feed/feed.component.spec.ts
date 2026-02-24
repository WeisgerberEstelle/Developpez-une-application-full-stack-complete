import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { FeedComponent } from './feed.component';
import { PostService } from '../../core/services/post.service';

const mockPosts = [
  { id: 1, title: 'Post A', content: 'Content A', author: { id: 1, username: 'alice' }, topic: { id: 1, name: 'Java' }, createdAt: '2025-01-01' },
  { id: 2, title: 'Post B', content: 'Content B', author: { id: 2, username: 'bob' }, topic: { id: 2, name: 'Angular' }, createdAt: '2025-01-02' },
];

describe('FeedComponent (integration)', () => {
  let component: FeedComponent;
  let fixture: ComponentFixture<FeedComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        PostService,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(FeedComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load posts on init', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:3001/api/posts/feed');
    expect(req.request.method).toBe('GET');
    req.flush([...mockPosts]);

    expect(component.posts.length).toBe(2);
    expect(component.posts[0].title).toBe('Post A');
    expect(component.posts[1].title).toBe('Post B');
  });

  it('should reverse posts order on toggleSort', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:3001/api/posts/feed');
    req.flush([...mockPosts]);

    expect(component.sortAsc).toBe(false);

    component.toggleSort();

    expect(component.sortAsc).toBe(true);
    expect(component.posts[0].title).toBe('Post B');
    expect(component.posts[1].title).toBe('Post A');
  });

  it('should navigate to post detail on goToPost', () => {
    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);

    component.goToPost(42);

    expect(navigateSpy).toHaveBeenCalledWith(['/posts', 42]);
  });
});
