import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { PostDetailComponent } from './post-detail.component';
import { PostService } from '../../../core/services/post.service';
import { CommentService } from '../../../core/services/comment.service';

const mockPost = {
  id: 1,
  title: 'Test Post',
  content: 'Post content',
  author: { id: 1, username: 'alice' },
  topic: { id: 1, name: 'Java' },
  createdAt: '2025-01-01',
};

const mockComments = [
  { id: 1, content: 'Nice post', author: { id: 2, username: 'bob' }, createdAt: '2025-01-02' },
];

describe('PostDetailComponent (integration)', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostDetailComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        PostService,
        CommentService,
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '1' } } },
        },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load post via GET /posts/1 then comments via GET /posts/1/comments on init', () => {
    fixture.detectChanges();

    const postReq = httpMock.expectOne('http://localhost:3001/api/posts/1');
    expect(postReq.request.method).toBe('GET');
    postReq.flush(mockPost);

    const commentsReq = httpMock.expectOne('http://localhost:3001/api/posts/1/comments');
    expect(commentsReq.request.method).toBe('GET');
    commentsReq.flush([...mockComments]);

    expect(component.post).toEqual(mockPost);
    expect(component.comments.length).toBe(1);
    expect(component.comments[0].content).toBe('Nice post');
  });

  it('should send POST /posts/1/comments when adding a comment, then update the list', () => {
    fixture.detectChanges();

    httpMock.expectOne('http://localhost:3001/api/posts/1').flush(mockPost);
    httpMock.expectOne('http://localhost:3001/api/posts/1/comments').flush([...mockComments]);

    const newComment = { id: 2, content: 'Great!', author: { id: 1, username: 'alice' }, createdAt: '2025-01-03' };
    component.newComment = 'Great!';
    component.addComment();

    const req = httpMock.expectOne('http://localhost:3001/api/posts/1/comments');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ content: 'Great!' });
    req.flush(newComment);

    expect(component.comments.length).toBe(2);
    expect(component.comments[1]).toEqual(newComment);
    expect(component.newComment).toBe('');
  });

  it('should not send request when comment is empty', () => {
    fixture.detectChanges();

    httpMock.expectOne('http://localhost:3001/api/posts/1').flush(mockPost);
    httpMock.expectOne('http://localhost:3001/api/posts/1/comments').flush([...mockComments]);

    component.newComment = ' ';
    component.addComment();

    httpMock.expectNone('http://localhost:3001/api/posts/1/comments');
  });

  it('should set commentError when adding a comment fails', () => {
    fixture.detectChanges();

    httpMock.expectOne('http://localhost:3001/api/posts/1').flush(mockPost);
    httpMock.expectOne('http://localhost:3001/api/posts/1/comments').flush([...mockComments]);

    component.newComment = 'A comment';
    component.addComment();

    const req = httpMock.expectOne('http://localhost:3001/api/posts/1/comments');
    req.flush(null, { status: 500, statusText: 'Server Error' });

    expect(component.commentError).toBe("Erreur lors de l'ajout du commentaire.");
  });
});
