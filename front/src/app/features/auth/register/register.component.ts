import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  email = '';
  username = '';
  password = '';
  fieldErrors: Record<string, string> = {};
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.fieldErrors = {};
    this.errorMessage = '';
    this.authService.register({
      email: this.email,
      username: this.username,
      password: this.password
    }).subscribe({
      next: () => {
        this.router.navigate(['/topics']);
      },
      error: (err) => {
        if (err.error && typeof err.error === 'object') {
          this.fieldErrors = err.error;
        } else {
          this.errorMessage = 'Registration failed';
        }
      }
    });
  }
}
