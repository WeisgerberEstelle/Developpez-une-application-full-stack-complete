import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TopicService } from '../../../core/services/topic.service';
import { AuthService } from '../../../core/services/auth.service';
import { SubscriptionService } from '../../../core/services/subscription.service';
import { Topic } from '../../../models/topic.interface';

@Component({
  selector: 'app-topics-list',
  standalone: true,
  templateUrl: './topics-list.component.html',
  styleUrl: './topics-list.component.scss'
})
export class TopicsListComponent implements OnInit {
  topics: Topic[] = [];
  subscribedTopicIds: Set<number> = new Set();
  private destroyRef = inject(DestroyRef);

  constructor(
    private topicService: TopicService,
    private authService: AuthService,
    private subscriptionService: SubscriptionService
  ) {}

  ngOnInit(): void {
    this.topicService.getAllTopics().pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (topics) => this.topics = topics,
      error: (err) => console.error('Failed to load topics', err)
    });
    this.loadSubscriptions();
  }

  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds.has(topicId);
  }

  subscribe(topicId: number): void {
    this.subscriptionService.subscribe(topicId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: () => this.loadSubscriptions(),
      error: (err) => console.error('Failed to subscribe', err)
    });
  }

  private loadSubscriptions(): void {
    this.authService.me().pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (user) => {
        this.subscribedTopicIds = new Set(user.subscriptions.map(t => t.id));
      },
      error: (err) => console.error('Failed to load subscriptions', err)
    });
  }
}
