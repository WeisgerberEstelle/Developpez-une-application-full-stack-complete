import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreatePostRequest, Post } from '../../models/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getFeed(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/posts/feed`);
  }

  getById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/posts/${id}`);
  }

  create(request: CreatePostRequest): Observable<Post> {
    return this.http.post<Post>(`${this.apiUrl}/posts`, request);
  }
}
