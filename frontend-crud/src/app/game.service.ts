import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Game } from './game.model';
import { HttpHeaders } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class GameService {

  createdGames: Game[] = [];

  get createdGamesCount() { return this.createdGames.length; }

  constructor(private httpClient: HttpClient) { }


  getAllGames(): Observable<Game[]> {
    return this.httpClient.get<Game[]>(`api/games/list`);
  }

  getGameById(id: string): Observable<Game> {
    return this.httpClient.get<Game>(`api/games/${id}/state`);
  }
  playMove(gameId: string, password: string, column: number): Observable<Game> {
    return this.httpClient.post<Game>(`api/games/${gameId}/move`, {
      password,
      column
    });
  }
  markReady(gameId: string, password: string): Observable<Game> {
    return this.httpClient.post<Game>(
      `api/games/${gameId}/ready`,
      { password }
    );
  }
}
