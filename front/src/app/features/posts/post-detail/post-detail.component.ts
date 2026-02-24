import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { PostService } from '../../../core/services/post.service';
import { CommentService } from '../../../core/services/comment.service';
import { Post } from '../../../models/post.interface';
import { Comment } from '../../../models/comment.interface';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [RouterModule, FormsModule, DatePipe],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.scss'
})
export class PostDetailComponent implements OnInit {
  post: Post | null = null;
  comments: Comment[] = [];
  newComment = '';
  commentError = '';
  private destroyRef = inject(DestroyRef);

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getById(id).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (post) => {
        this.post = post;
        this.loadComments(id);
      },
      error: (err) => console.error('Failed to load post', err)
    });
  }

  loadComments(postId: number): void {
    this.commentService.getComments(postId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (comments) => this.comments = comments,
      error: (err) => console.error('Failed to load comments', err)
    });
  }

  addComment(): void {
    if (!this.post || !this.newComment.trim()) {
      return;
    }
    this.commentError = '';
    this.commentService.addComment(this.post.id, { content: this.newComment.trim() }).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (comment) => {
        this.comments.push(comment);
        this.newComment = '';
      },
      error: () => this.commentError = 'Erreur lors de l\'ajout du commentaire.'
    });
  }
}
