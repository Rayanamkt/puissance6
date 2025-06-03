import { Component } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular'; // Angular Data Grid Component;
import { ColDef } from 'ag-grid-community';
import {Game, GameService} from '../game.service';


@Component({
  selector: 'app-list-game',
  imports: [AgGridAngular],
  templateUrl: './list-game.component.html',
})
export class ListGameComponent {

  // Row Data: The data to be displayed.
  get rowData(): Game[] { return this.gameService.createdGames; }

  // Column Definitions: Defines the columns to be displayed.
  colDefs: ColDef<Game>[] = [
    { headerName: 'Id', valueGetter: p => p.data?.gameId },
    { headerName: 'Player1 Name', valueGetter: p => p.data?.player1Name },
    { headerName: 'Player2 Name', valueGetter: p => p.data?.player2Name },
  ];

  defaultColDef:  ColDef<any> = {
    sortable: true,
    filter: true,
    resizable: true,
  };

  constructor(private gameService: GameService) {}

  generateRows() {}
  // generateRows() {
  //   let old = this.rowData;
  //   this.rowData = [ ...old, ...old];
  // }
}
