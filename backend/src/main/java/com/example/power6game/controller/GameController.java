package com.example.power6game.controller;

import com.example.power6game.dto.*;
import com.example.power6game.model.Game;
import com.example.power6game.model.GameStatus;
import com.example.power6game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    // http POST http://localhost:8080/api/games/create -d
    @PostMapping("/create")
    public CreateGameResponse createGame(
            @RequestBody CreateGameRequest request
    ) {
        Game game = gameService.createGame(
                request.getPlayerName(),
                request.getPlayer1TimeSeconds(),
                request.getPlayer2TimeSeconds()
        );
        return new CreateGameResponse(game.getId(), game.getPassword1());
    }

    @GetMapping("/waiting")
    public List<GameListItem> getWaitingGames() {
        List<Game> waitingGames = gameService.getWaitingGames();
        List<GameListItem> gameList = waitingGames.stream()
                .map(game -> new GameListItem(
                        game.getId(),
                        game.getPlayer1Name(),
                        game.getPlayer1TimeSeconds(),
                        game.getPlayer2TimeSeconds()
                ))
                .toList();

        return gameList;
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<JoinGameResponse> joinGame(
            @PathVariable String gameId,
            @RequestBody JoinGameRequest request) {
        Game game = gameService.joinGame(gameId, request.getPlayerName());
        if (game == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new JoinGameResponse(game.getPassword2()));
    }

    @GetMapping("/{gameId}/state")
    public ResponseEntity<GameStateResponse> getGameState(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        GameStateResponse response = new GameStateResponse();
        response.setGameId(game.getId());
        response.setPlayer1Name(game.getPlayer1Name());
        response.setPlayer2Name(game.getPlayer2Name());
        response.setCurrentPlayer(game.getCurrentPlayer());
        response.setBoard(game.getBoard());
        response.setPlayer1RemainingTime(game.getPlayer1RemainingTime());
        response.setPlayer2RemainingTime(game.getPlayer2RemainingTime());
        response.setStatus(game.getStatus().toString());
        response.setCurrentMoveCount(game.getCurrentMoveCount());

        // Set winner information
        if (game.getStatus() == GameStatus.FINISHED_PLAYER1_WIN) {
            response.setWinner(game.getPlayer1Name());
        } else if (game.getStatus() == GameStatus.FINISHED_PLAYER2_WIN) {
            response.setWinner(game.getPlayer2Name());
        } else if (game.getStatus() == GameStatus.FINISHED_TIME_OUT) {
            // Winner is the player who didn't run out of time
            if (game.getPlayer1RemainingTime() <= 0) {
                response.setWinner(game.getPlayer2Name());
            } else {
                response.setWinner(game.getPlayer1Name());
            }
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameStateResponse> makeMove(
            @PathVariable String gameId,
            @RequestBody MakeMoveRequest request) {

        Game game = gameService.makeMove(gameId, request.getPassword(), request.getColumn());
        if (game == null) {
            return ResponseEntity.badRequest().build();
        }

        // Return updated game state
        return getGameState(gameId);
    }
}