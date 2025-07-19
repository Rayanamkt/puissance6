import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class GameSessionService {
  private _password: string | null = null;
  private _playerId: number | null = null;

  setSession(password: string, playerId: number) {
    this._password = password;
    this._playerId = playerId;
  }

  get password(): string | null {
    return this._password;
  }

  get playerId(): number | null {
    return this._playerId;
  }

  clearSession() {
    this._password = null;
    this._playerId = null;
  }
}
