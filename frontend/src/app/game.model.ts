export interface Game {
  gameId: string;
  player1Name: string;
  player2Name: string;
  player1TimeSeconds: number;
  player2TimeSeconds: number;
  player1Ready: boolean;
  player2Ready: boolean;
  status: string;
  winner: string | null;
  board: number[][];
  currentPlayer: number;
  blitzMinuteDuration?: number;
}
