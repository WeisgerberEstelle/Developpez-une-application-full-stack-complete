import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { CreatePostComponent } from './create-post.component';
import { PostService } from '../../../core/services/post.service';
import { TopicService } from '../../../core/services/topic.service';

const mockTopics = [
  { id: 1, name: 'Java', description: 'Java topic' },
  { id: 2, name: 'Angular', description: 'Angular topic' },
];

describe('CreatePostComponent (integration)', () => {
  let component: CreatePostComponent;
  let fixture: ComponentFixture<CreatePostComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatePostComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        PostService,
        TopicService,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(CreatePostComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load topics via GET /topics on init', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:3001/api/topics');
    expect(req.request.method).toBe('GET');
    req.flush(mockTopics);

    expect(component.topics).toEqual(mockTopics);
  });

  it('should not send create request when topicId is null', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:3001/api/topics').flush(mockTopics);

    component.topicId = null;
    component.onSubmit();

    httpMock.expectNone('http://localhost:3001/api/posts');
  });

  it('should send POST /posts and navigate to the new post on success', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:3001/api/topics').flush(mockTopics);

    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const createdPost = { id: 99, title: 'New', content: 'Body', authorId: 1, authorUsername: 'alice', topicId: 1, topicName: 'Java', createdAt: '2025-01-01' };

    component.topicId = 1;
    component.title = 'New';
    component.content = 'Body';
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/posts');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ topicId: 1, title: 'New', content: 'Body' });
    req.flush(createdPost);

    expect(navigateSpy).toHaveBeenCalledWith(['/posts', 99]);
  });

  it('should join validation errors into errorMessage when server returns an object', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:3001/api/topics').flush(mockTopics);

    component.topicId = 1;
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/posts');
    req.flush(
      { title: 'Title is required', content: 'Content is required' },
      { status: 400, statusText: 'Bad Request' }
    );

    expect(component.errorMessage).toContain('Title is required');
    expect(component.errorMessage).toContain('Content is required');
  });

  it('should set generic errorMessage when server error is not an object', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:3001/api/topics').flush(mockTopics);

    component.topicId = 1;
    component.onSubmit();

    const req = httpMock.expectOne('http://localhost:3001/api/posts');
    req.flush('Server error', { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toBe('Failed to create post');
  });
});
