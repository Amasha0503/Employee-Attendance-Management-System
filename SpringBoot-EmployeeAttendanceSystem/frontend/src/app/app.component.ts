import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavbarComponent],
  template: `
    <app-navbar></app-navbar>
    <main class="app-shell">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [
    `
      .app-shell {
        min-height: calc(100vh - 64px);
        background-color: #fafafa;
      }
    `
  ]
})
export class AppComponent {}