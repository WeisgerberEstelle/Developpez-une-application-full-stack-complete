import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Topic } from '../../models/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getAllTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.apiUrl}/topics`);
  }
}
