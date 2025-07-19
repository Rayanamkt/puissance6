package com.example.power6game.controller;

import com.example.power6game.dto.*;
import com.example.power6game.model.Game;
import com.example.power6game.model.GameStatus;
import com.example.power6game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public CreateGameResponse createGame(
            @RequestBody CreateGameRequest request
    ) {
        Game game = gameService.createGame(
                request.getPlayer1Name(),
                request.getPlayer2Name(),
                request.getPlayer1TimeSeconds(),
                request.getPlayer2TimeSeconds()
        );
        return new CreateGameResponse(game.getId(), game.getPassword1(), 1);
    }

    @GetMapping("/list")
    public List<GameListItem> getAllGames() {
        List<Game> allGames = gameService.getAllGames();
        return allGames.stream()
                .map(game -> new GameListItem(
                        game.getId(),
                        game.getPlayer1Name(),
                        game.getPlayer2Name(),
                        game.getPlayer1TimeSeconds(),
                        game.getPlayer2TimeSeconds(),
                        game.getStatus(),
                        game.getWinner()
                ))
                .toList();
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<JoinGameResponse> joinGame(
            @PathVariable String gameId,
            @RequestBody JoinGameRequest request) {

        Game game = gameService.joinGame(gameId, request.getPlayerName());

        if (game == null) {
            return ResponseEntity.badRequest().build();
        }

        if (game.getPlayer1Name() != null && game.getPlayer2Name() != null) {
            game.setStatus(GameStatus.IN_PROGRESS);
        }

        return ResponseEntity.ok(new JoinGameResponse(game.getPassword2(), 2));
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
        response.setPlayer1Ready(game.isPlayer1Ready());
        response.setPlayer2Ready(game.isPlayer2Ready());
        response.setStatus(game.getStatus().toString());
        response.setCurrentMoveCount(game.getCurrentMoveCount());

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
    @PostMapping("{gameId}/ready")
    public ResponseEntity<Game> markPlayerReady(@PathVariable("gameId") String gameId, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        try {
            Game game = gameService.markPlayerReady(gameId, password);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameStateResponse> makeMove(
            @PathVariable String gameId,
            @RequestBody MakeMoveRequest request) {

        System.out.println("🎯 Requête MOVE reçue pour gameId: " + gameId);
        System.out.println("🔑 Mot de passe reçu: " + request.getPassword());
        System.out.println("📍 Colonne reçue: " + request.getColumn());

        Game game = gameService.makeMove(gameId, request.getPassword(), request.getColumn());

        if (game == null) {
            return ResponseEntity.badRequest().build();
        }

        return getGameState(gameId);
    }

}