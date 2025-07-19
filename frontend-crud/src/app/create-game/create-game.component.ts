import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {GameService} from '../game.service';
import { Router} from '@angular/router';
import { Game } from '../game.model';
import {GameSessionService} from '../game-session.service';

interface CreateGameResponse {
  gameId: string;
  password: string;
}

@Component({
  selector: 'app-create-game',
  imports: [
    FormsModule
  ],
  templateUrl: './create-game.component.html',
  standalone: true
})
export class CreateGameComponent {

  recentlyCreatedGameCount = 0;
  get createdGameCount() {
    return this.gameService.createdGamesCount;
  }
  errorCreatingGameCount = 0;
  player1Name: string = 'joueur1';
  player2Name: string = 'joueur2';
  remainCreateCount: number = 0;

  constructor(
    private httpClient: HttpClient,
    private gameService: GameService,
    private router: Router,
    private gameSessionService: GameSessionService
  ) {}

  createGame() {
    this.httpClient.post<CreateGameResponse>('http://localhost:8080/api/games/create', {
      player1Name: this.player1Name,
      player1TimeSeconds: 300,
      player2TimeSeconds: 300
    }).subscribe({
      next: (data) => {
        this.gameSessionService.setSession(data.password, 1);
        this.router.navigate(['/game', data.gameId, 'board']);
      },
      error: (err) => {
        console.error('Create game error:', err);
      }
    });
  }

}
