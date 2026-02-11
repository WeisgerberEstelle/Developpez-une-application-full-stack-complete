import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { TopicsListComponent } from './features/topics/topics-list/topics-list.component';
import { ProfileComponent } from './features/profile/profile.component';
import { FeedComponent } from './features/feed/feed.component';
import { PostDetailComponent } from './features/posts/post-detail/post-detail.component';
import { CreatePostComponent } from './features/posts/create-post/create-post.component';
import { authGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'topics', component: TopicsListComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'feed', component: FeedComponent, canActivate: [authGuard] },
  { path: 'posts/create', component: CreatePostComponent, canActivate: [authGuard] },
  { path: 'posts/:id', component: PostDetailComponent, canActivate: [authGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
