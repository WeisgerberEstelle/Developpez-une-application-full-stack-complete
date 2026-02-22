import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterModule],
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
