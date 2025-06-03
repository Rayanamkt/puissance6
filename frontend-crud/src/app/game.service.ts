import { Injectable } from '@angular/core';


export interface Game {
  gameId: string;
  player1Name: string;
  player2Name: string;
  player1TimeSeconds: number;
  player2TimeSeconds: number;
}

@Injectable({
  providedIn: 'root'
})
export class GameService {

  createdGames: Game[] = [];

  get createdGamesCount() { return this.createdGames.length; }

  constructor() { }

  addCreatedGame(game: Game) {
    this.createdGames.push(game);
  }
}
