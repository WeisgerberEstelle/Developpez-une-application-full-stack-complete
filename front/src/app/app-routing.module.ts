import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { authGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent) },
  { path: 'topics', loadComponent: () => import('./features/topics/topics-list/topics-list.component').then(m => m.TopicsListComponent), canActivate: [authGuard] },
  { path: 'profile', loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent), canActivate: [authGuard] },
  { path: 'feed', loadComponent: () => import('./features/feed/feed.component').then(m => m.FeedComponent), canActivate: [authGuard] },
  { path: 'posts/create', loadComponent: () => import('./features/posts/create-post/create-post.component').then(m => m.CreatePostComponent), canActivate: [authGuard] },
  { path: 'posts/:id', loadComponent: () => import('./features/posts/post-detail/post-detail.component').then(m => m.PostDetailComponent), canActivate: [authGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
