import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PostService } from '../../../core/services/post.service';
import { TopicService } from '../../../core/services/topic.service';
import { Topic } from '../../../models/topic.interface';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.scss'
})
export class CreatePostComponent implements OnInit {
  topics: Topic[] = [];
  topicId: number | null = null;
  title = '';
  content = '';
  errorMessage = '';

  constructor(
    private postService: PostService,
    private topicService: TopicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe({
      next: (topics) => this.topics = topics,
      error: (err) => console.error('Failed to load topics', err)
    });
  }

  onSubmit(): void {
    if (!this.topicId) return;

    this.errorMessage = '';
    this.postService.create({
      topicId: this.topicId,
      title: this.title,
      content: this.content
    }).subscribe({
      next: (post) => this.router.navigate(['/posts', post.id]),
      error: (err) => {
        if (err.error && typeof err.error === 'object') {
          this.errorMessage = Object.values(err.error).join(', ');
        } else {
          this.errorMessage = 'Failed to create post';
        }
      }
    });
  }
}
