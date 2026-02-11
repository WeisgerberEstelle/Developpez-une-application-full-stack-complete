import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Comment, CreateCommentRequest } from '../../models/comment.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/posts/${postId}/comments`);
  }

  addComment(postId: number, request: CreateCommentRequest): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/posts/${postId}/comments`, request);
  }
}
