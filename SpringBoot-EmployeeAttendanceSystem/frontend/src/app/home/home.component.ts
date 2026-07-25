import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  template: `
    <section class="hero">
      <p class="eyebrow">Employee Attendance Management System</p>
      <h1>Backend and frontend scaffolding is ready.</h1>
      <p class="copy">
        This Angular workspace is initialized with standalone routing so the later auth,
        dashboard, attendance, leave, overtime, and admin screens can be added phase by phase.
      </p>
    </section>
  `,
  styles: [
    `
      .hero {
        max-width: 760px;
        margin: 0 auto;
        padding: 6rem 1.5rem;
      }

      .eyebrow {
        margin: 0 0 1rem;
        letter-spacing: 0.12em;
        text-transform: uppercase;
        font-size: 0.8rem;
        color: #4b647a;
      }

      h1 {
        margin: 0;
        font-size: clamp(2.2rem, 5vw, 4rem);
        line-height: 1.05;
      }

      .copy {
        margin-top: 1rem;
        font-size: 1.05rem;
        line-height: 1.7;
        max-width: 60ch;
        color: #34495e;
      }
    `
  ]
})
export class HomeComponent {}