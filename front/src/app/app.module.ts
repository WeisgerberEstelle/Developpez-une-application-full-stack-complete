import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { TopicsListComponent } from './features/topics/topics-list/topics-list.component';
import { ProfileComponent } from './features/profile/profile.component';
import { FeedComponent } from './features/feed/feed.component';
import { PostDetailComponent } from './features/posts/post-detail/post-detail.component';
import { CreatePostComponent } from './features/posts/create-post/create-post.component';
import { jwtInterceptor } from './core/interceptors/jwt.interceptor';
import { HeaderComponent } from './shared/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    TopicsListComponent,
    ProfileComponent,
    FeedComponent,
    PostDetailComponent,
    CreatePostComponent,
    HeaderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
  ],
  providers: [provideHttpClient(withInterceptors([jwtInterceptor]))],
  bootstrap: [AppComponent],
})
export class AppModule {}
