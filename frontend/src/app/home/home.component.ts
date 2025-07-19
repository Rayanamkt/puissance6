import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  showRulesModal = false;

  constructor(private router: Router) {}

  goToList() {
    this.router.navigate(['/list']);
  }

  showRules() {
    this.router.navigate(['/rules']);
  }

  hideRules() {
    this.showRulesModal = false;
  }

}
