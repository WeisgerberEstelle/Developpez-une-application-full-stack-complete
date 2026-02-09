import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../../core/services/topic.service';
import { Topic } from '../../../models/topic.interface';

@Component({
  selector: 'app-topics-list',
  standalone: false,
  templateUrl: './topics-list.component.html',
  styleUrls: ['./topics-list.component.scss']
})
export class TopicsListComponent implements OnInit {
  topics: Topic[] = [];

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe({
      next: (topics) => this.topics = topics,
      error: (err) => console.error('Failed to load topics', err)
    });
  }
}
