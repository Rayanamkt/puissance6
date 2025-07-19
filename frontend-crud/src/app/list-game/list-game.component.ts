import { Component, OnInit } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular'; // Angular Data Grid Component;
import { ColDef } from 'ag-grid-community';
import { GameService} from '../game.service';
import { Game } from '../game.model';
import { GridApi } from 'ag-grid-community';


@Component({
  selector: 'app-list-game',
  imports: [AgGridAngular],
  templateUrl: './list-game.component.html',
  standalone: true,
  styleUrls: ['./list-game.component.css']
})
export class ListGameComponent implements OnInit{
  gridApi!: GridApi;
  rowData: Game[] = [];
  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.gameService.getAllGames().subscribe({
      next: (games) => {
        this.rowData = games;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des parties :", err);
      }
    });
  }

  colDefs: ColDef<Game>[] = [
    { headerName: 'ID', field: 'gameId' },
    { headerName: 'Nom du joueur 1', field: 'player1Name' },
    { headerName: 'Nom du joueur 2', field: 'player2Name' },
    {
      headerName: 'Statut',
      valueGetter: (params:any) => {
        const statusMap: any = {
          'WAITING_FOR_PLAYER2': 'En attente',
          'IN_PROGRESS': 'En cours',
          'FINISHED_PLAYER1_WIN': 'Terminé',
          'FINISHED_PLAYER2_WIN': 'Terminé',
          'FINISHED_DRAW': 'Match nul',
          'FINISHED_TIME_OUT': 'Temps écoulé'
        };
        return statusMap[params.data.status] || params.data.status;
      }
    },
    {
      headerName: 'Gagnant',
      valueGetter: (params:any) => params.data.winner ? params.data.winner : '-'
    },
    { headerName: 'Action',cellRenderer:(params:any)=>{
        if (params.data.status === 'WAITING_FOR_PLAYER2' || !params.data.player2Name) {
          return '<button class="btn btn-primary">Rejoindre</button>';
        }
      return '';
      }
    }
  ];

  defaultColDef:  ColDef<any> = {
    sortable: true,
    filter: true,
    resizable: true,
  };

  localeText = {
    noRowsToShow: 'Aucune partie à afficher',
    page: 'Page',
    more: 'Plus',
    to: 'à',
    of: 'de',
    next: 'Suivant',
    last: 'Dernier',
    first: 'Premier',
    previous: 'Précédent',
    loadingOoo: 'Chargement...',
  };

  onGridReady(params: any) {
    this.gridApi = params.api;

    params.api.sizeColumnsToFit();
    params.api.addEventListener('cellClicked', this.onCellClicked.bind(this));
  }

  onCellClicked(event: any) {
    if (
      event.colDef.headerName === 'Action' &&
      event.event.target?.textContent === 'Rejoindre'
    ) {
      const gameId = event.data.gameId;

      window.location.href = `/game/${gameId}/join`;
    }
  }
}
