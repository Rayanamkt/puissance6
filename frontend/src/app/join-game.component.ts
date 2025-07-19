import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {GameSessionService} from './game-session.service';

interface JoinResponse {
  password: string;
}

@Component({
  selector: 'app-join-game',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './join-game.component.html'
})
export class JoinGameComponent implements OnInit {
  playerName: string = '';
  gameId: string = '';
  error: string = '';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private gameSessionService: GameSessionService
  ) {}

  ngOnInit(): void {
    this.gameId = this.route.snapshot.paramMap.get('gameId')!;
  }

  joinGame(): void {
    this.http.post<JoinResponse>(`http://localhost:8080/api/games/${this.gameId}/join`, {
      playerName: this.playerName
    }).subscribe({
      next: (data) => {
        this.gameSessionService.setSession(data.password, 2);
        this.router.navigate(['/game', this.gameId, 'board']);
      },
      error: (err) => {
        this.error = 'Erreur pour rejoindre la partie';
      }
    });
  }
}
