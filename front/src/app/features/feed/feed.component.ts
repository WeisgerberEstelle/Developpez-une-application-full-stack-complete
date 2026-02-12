import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../models/post.interface';

@Component({
  selector: 'app-feed',
  standalone: false,
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss'
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  sortAsc = false;

  constructor(
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postService.getFeed().subscribe({
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
