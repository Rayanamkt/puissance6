import { HomeComponent } from './home/home.component';
import { Routes } from '@angular/router';
import {ListGameComponent} from './list-game/list-game.component';
import {CreateGameComponent} from './create-game/create-game.component';
import { RulesComponent } from './rules/rules.component';
import { GameComponent } from './game/game.component';
import { BoardComponent } from './board/board.component';
import { JoinGameComponent } from './join-game.component';
import {GameOverComponent} from './game-over/game-over.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'rules', component: RulesComponent },
  { path: 'list', component: ListGameComponent },
  { path: 'create', component: CreateGameComponent },
  { path: 'game/:gameId', component: GameComponent },
  { path: 'game/:gameId/board', component: BoardComponent },
  { path: 'game/:gameId/join', component: JoinGameComponent },
  { path: 'game-over/:gameId', component: GameOverComponent },
];

