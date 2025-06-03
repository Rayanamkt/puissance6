package com.example.power6game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameListItem {
    private String gameId;
    private String player1Name;
    private int player1TimeSeconds;
    private int player2TimeSeconds;
}
