import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

interface LoginResponse {
  id: number;
  username: string;
  name: string;
  email: string;
}

interface SignupRequest {
  name: string;
  username: string;
  email: string;
  password: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  private http = inject(HttpClient);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  private authService = inject(AuthService);
  private apiUrl = '/api';

  // Toggle between login and signup
  isSignup = false;

  // Login fields
  loginUsername = '';
  loginPassword = '';

  // Signup fields
  signupName = '';
  signupUsername = '';
  signupEmail = '';
  signupPassword = '';
  signupConfirmPassword = '';

  // State
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  toggleMode() {
    this.isSignup = !this.isSignup;
    this.errorMessage = '';
    this.successMessage = '';
  }

  login() {
    if (!this.loginUsername.trim() || !this.loginPassword.trim()) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, {
      username: this.loginUsername,
      password: this.loginPassword
    }).subscribe({
      next: (user) => {
        this.isLoading = false;
        // Store user in session via AuthService
        this.authService.login(user.id, user.username);
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 401) {
          this.errorMessage = 'Invalid username or password';
        } else if (err.status === 404) {
          this.errorMessage = 'User not found. Please sign up.';
        } else {
          this.errorMessage = 'Login failed. Please try again.';
        }
        this.cdr.detectChanges();
      }
    });
  }

  signup() {
    if (!this.signupName.trim() || !this.signupUsername.trim() || 
        !this.signupEmail.trim() || !this.signupPassword.trim()) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    if (this.signupPassword !== this.signupConfirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    if (this.signupPassword.length < 4) {
      this.errorMessage = 'Password must be at least 4 characters';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const signupData: SignupRequest = {
      name: this.signupName,
      username: this.signupUsername,
      email: this.signupEmail,
      password: this.signupPassword
    };

    this.http.post<LoginResponse>(`${this.apiUrl}/auth/signup`, signupData).subscribe({
      next: (user) => {
        this.isLoading = false;
        this.successMessage = 'Account created! You can now log in.';
        this.isSignup = false;
        this.loginUsername = this.signupUsername;
        // Clear signup fields
        this.signupName = '';
        this.signupUsername = '';
        this.signupEmail = '';
        this.signupPassword = '';
        this.signupConfirmPassword = '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 409) {
          this.errorMessage = 'Username or email already exists';
        } else {
          this.errorMessage = 'Signup failed. Please try again.';
        }
        this.cdr.detectChanges();
      }
    });
  }
}
