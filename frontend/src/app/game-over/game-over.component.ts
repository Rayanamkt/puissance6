import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GameService } from '../game.service';
import { CommonModule } from '@angular/common';
import { Game } from '../game.model';

@Component({
  selector: 'app-game-over',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game-over.component.html',
  styleUrls: ['./game-over.component.css']
})
export class GameOverComponent implements OnInit {
  gameId!: string;
  game!: Game;
  message: string = '';

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.gameId = this.route.snapshot.paramMap.get('gameId')!;
    this.loadGame();
  }

  loadGame() {
    this.gameService.getGameById(this.gameId).subscribe(game => {
      this.game = game;

      if (game.status === 'FINISHED_PLAYER1_WIN') {
        this.message = `🎉 Victoire de ${game.player1Name} !`;
      } else if (game.status === 'FINISHED_PLAYER2_WIN') {
        this.message = `🎉 Victoire de ${game.player2Name} !`;
      } else if (game.status === 'FINISHED_DRAW') {
        this.message = "🤝 Match nul !";
      } else {
        this.message = "⏳ Partie non terminée.";
      }
    });
  }

  retourAccueil() {
    this.router.navigate(['/list']);
  }
}
