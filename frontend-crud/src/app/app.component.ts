import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  currentRoute: string = '';

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      this.currentRoute = this.router.url;
    });
  }

  isHomeRoute(): boolean {
    const currentRoute = this.router.url;
    return currentRoute === '/' || currentRoute === '/rules';
  }
}
