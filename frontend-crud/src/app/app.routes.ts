import { Routes } from '@angular/router';
import {ListGameComponent} from './list-game/list-game.component';
import {CreateGameComponent} from './create-game/create-game.component';

export const routes: Routes = [
  {path:'list', component: ListGameComponent},
  {path:'create', component: CreateGameComponent},
];
