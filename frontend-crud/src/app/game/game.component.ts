import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GameService } from '../game.service';
import { CommonModule } from '@angular/common';
import { interval, Subscription } from 'rxjs';
import { Game } from '../game.model';
import {GameSessionService} from '../game-session.service';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {
  game: Game | undefined;
  gameId: string = '';
  playerRole: string | null = null;
  pollingSub: Subscription | undefined;
  wasAlreadyRedirected: boolean = false;
  playerNumber!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private gameService: GameService,
    private gameSessionService: GameSessionService
  ) {}

  ngOnInit(): void {
    this.gameId = this.route.snapshot.paramMap.get('gameId')!;

    // ✅ Ajout : récupérer playerNumber depuis GameSessionService
    this.playerNumber = this.gameSessionService.playerId ?? 0;
    this.playerRole = this.playerNumber === 1 ? 'p1' : this.playerNumber === 2 ? 'p2' : null;

    console.log("👤 Joueur détecté (playerNumber) :", this.playerNumber);
    console.log("🧪 playerRole récupéré :", this.playerRole);

    this.markReadyAuto();
    this.loadGame();
    this.pollingSub = interval(1000).subscribe(() => {
      this.loadGame();
    });
  }

  loadGame(): void {
    this.gameService.getGameById(this.gameId).subscribe({
      next: (data) => {
        this.game = data;
        console.log("📦 Données reçues :", data);

        if (data.status === 'IN_PROGRESS' && !this.wasAlreadyRedirected) {
          this.wasAlreadyRedirected = true;
          this.router.navigate([`/game/${this.gameId}/board`]);
        }
      },
      error: (err) => console.error('❌ Erreur récupération game:', err)
    });
  }

  markReadyAuto(): void {
    const password = this.gameSessionService.password;
    if (!password) return;
    this.gameService.markReady(this.gameId, password).subscribe();
  }

  ngOnDestroy(): void {
    this.pollingSub?.unsubscribe();
  }


}
