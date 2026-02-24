import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterModule } from '@angular/router';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../models/post.interface';
import { DatePipe, SlicePipe } from '@angular/common';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [RouterModule, DatePipe, SlicePipe],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss'
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  sortAsc = false;
  private destroyRef = inject(DestroyRef);

  constructor(
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postService.getFeed().pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (posts) => this.posts = posts,
      error: (err) => console.error('Failed to load feed', err)
    });
  }

  toggleSort(): void {
    this.sortAsc = !this.sortAsc;
    this.posts.reverse();
  }

  goToPost(id: number): void {
    this.router.navigate(['/posts', id]);
  }
}
