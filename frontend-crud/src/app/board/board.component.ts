import {Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GameService } from '../game.service';
import { CommonModule } from '@angular/common';
import { Game } from '../game.model';
import {GameSessionService} from '../game-session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-board',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit,OnDestroy {
  game!: Game;
  gameId!: string;
  playerName: string = '';
  connectedPassword: string | null = null;
  connectedPlayer: number | null = null;
  private gameInterval: any;

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private gameSessionService: GameSessionService,
    private router: Router  // 👈 ici
  ) {}

  ngOnInit(): void {
    this.gameId = this.route.snapshot.paramMap.get('gameId')!;
    this.connectedPassword = this.gameSessionService.password;
    this.connectedPlayer = this.gameSessionService.playerId;


    this.loadGame();
    this.gameInterval = setInterval(() => this.loadGame(), 2000);

  }
  ngOnDestroy(): void {
    if (this.gameInterval) {
      clearInterval(this.gameInterval);
    }
  }


  loadGame() {
    this.gameService.getGameById(this.gameId).subscribe(game => {
      this.game = game;

      if (
        this.game.status === 'FINISHED_PLAYER1_WIN' ||
        this.game.status === 'FINISHED_PLAYER2_WIN' ||
        this.game.status === 'FINISHED_DRAW'
      ) {
        clearInterval(this.gameInterval);
        this.router.navigate(['/game-over', this.gameId]);
      }
    });
  }

  play(colIndex: number) {
    if (!this.connectedPassword || !this.game) return;

    if (this.connectedPlayer !== this.game.currentPlayer) {
      alert("Ce n'est pas ton tour !");
      return;
    }

    this.gameService.playMove(this.gameId, this.connectedPassword, colIndex).subscribe({
      next: updatedGame => {
        this.game = updatedGame;
      },
      error: err => {
        alert(err.error?.message || "Erreur lors du placement du jeton.");
      }
    });
  }

  handleColumnClick(colIndex: number): void {
    console.log("🖱️ Clic détecté sur colonne :", colIndex);

    if (this.game?.status !== 'IN_PROGRESS') {
      console.warn("🚫 La partie n'est pas encore démarrée.");
      return;
    }


    this.onColumnClick(colIndex);
  }

  onColumnClick(col: number) {
    console.log("📦 Envoi du coup | colonne:", col);

    const password = this.gameSessionService.password;

    if (!password) {
      console.error('Mot de passe introuvable pour', password);
      return;
    }

    this.gameService.playMove(this.game.gameId, password, col).subscribe({
      next: (updatedGame) => {
        const newRow = this.findNewRow(updatedGame.board, col);
        this.game = updatedGame;

        setTimeout(() => {
          this.animateDrop(newRow, col);
        }, 50);
      },
      error: (err) => {
        console.error("Erreur lors du coup :", err);
      }
    });
  }

  findNewRow(board: number[][], col: number): number {
    for (let row = board.length - 1; row >= 0; row--) {
      if (board[row][col] !== 0) {
        return board.length - 1 - row;
      }
    }
    return 0;
  }

  animateDrop(row: number, col: number) {
    const cellSelector = `.row-${row} .col-${col}`;
    const cell = document.querySelector(cellSelector);
    if (cell) {
      console.log("🎯 Animation cible:", cellSelector, cell);
      cell.classList.add('falling');
      setTimeout(() => cell.classList.remove('falling'), 200);
    }
  }

  get board(): number[][] {
    return this.game?.board || Array(15).fill(null).map(() => Array(15).fill(0));
  }
}
