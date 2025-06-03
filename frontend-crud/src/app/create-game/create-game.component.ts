import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {Game, GameService} from '../game.service';

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
})
export class CreateGameComponent {

  recentlyCreatedGameCount = 0;
  get createdGameCount() {
    return this.gameService.createdGamesCount;
  }
  errorCreatingGameCount = 0;
  player1Name: string = 'player1';
  remainCreateCount: number = 0;

  constructor(private httpClient: HttpClient,
              private gameService: GameService) {
  }

  createGame() {
    this.httpClient.post<CreateGameResponse>('http://localhost:8080/api/games/create', {
      playerName: this.player1Name,
      player1TimeSeconds: 300,
      player2TimeSeconds: 300
    }).subscribe({
      next: (data) => {
        console.log('Create game response:', data);
        this.recentlyCreatedGameCount += 1;

        const gameId = data.gameId;
        this.httpClient.get<Game>(`http://localhost:8080/api/games/${gameId}/state`).subscribe({
          next: game => {
            console.log('Get game response:', game);
            this.gameService.addCreatedGame(game);
          },
        });

      }, error: (err) => {
        console.error('Create game error:', err);
        this.errorCreatingGameCount += 1;
      }
    });
  }

  create10000Games() {
    this.remainCreateCount = 10000;
    this.recursiveCreateRemainGames();
  }

  private recursiveCreateRemainGames() {
    this.httpClient.post<any>('http://localhost:8080/api/games/create', {
      playerName: this.player1Name,
      player1TimeSeconds: 300,
      player2TimeSeconds: 300
    }).subscribe({
      next: (data) => {
        // console.log('Create game response:', data);
        this.recentlyCreatedGameCount += 1;
        this.remainCreateCount -= 1;
        // *** recursive async call ***
        if (this.remainCreateCount > 0) {
          this.recursiveCreateRemainGames();
        }
      }, error: (err) => {
        console.error('Create game error:', err);
        this.errorCreatingGameCount += 1;
        this.remainCreateCount = 0; // Cancel!
      }
    });
  }
}
