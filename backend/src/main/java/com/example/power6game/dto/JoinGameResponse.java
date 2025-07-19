package com.example.power6game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JoinGameResponse {
    private String password;
    private int playerNumber;

    public JoinGameResponse(String password, int playerNumber) {
        this.password = password;
        this.playerNumber = playerNumber;
    }

}
