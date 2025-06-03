package com.example.power6game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGameResponse {
    private String gameId;
    private String password;
}
