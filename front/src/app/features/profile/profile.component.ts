import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { SubscriptionService } from '../../core/services/subscription.service';
import { User } from '../../models/user.interface';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  email = '';
  username = '';
  password = '';
  successMessage = '';
  errorMessage = '';

  private destroyRef = inject(DestroyRef);

  constructor(
    private authService: AuthService,
    private subscriptionService: SubscriptionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUser();
  }

  onSave(): void {
    this.successMessage = '';
    this.errorMessage = '';

    const request: any = {};
    if (this.email !== this.user?.email) {
      request.email = this.email;
    }
    if (this.username !== this.user?.username) {
      request.username = this.username;
    }
    if (this.password) {
      request.password = this.password;
    }

    if (Object.keys(request).length === 0) {
      return;
    }

    this.authService.updateProfile(request).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (user) => {
        this.user = user;
        this.password = '';
        this.successMessage = 'Profile updated successfully';
      },
      error: (err) => {
        this.errorMessage = err.error?.error || 'Failed to update profile';
      }
    });
  }

  unsubscribe(topicId: number): void {
    this.subscriptionService.unsubscribe(topicId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: () => this.loadUser(),
      error: (err) => console.error('Failed to unsubscribe', err)
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  private loadUser(): void {
    this.authService.me().pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (user) => {
        this.user = user;
        this.email = user.email;
        this.username = user.username;
      },
      error: (err) => console.error('Failed to load user', err)
    });
  }
}
