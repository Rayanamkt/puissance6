package com.example.power6game.dto;

import lombok.Data;

@Data
public class MakeMoveRequest {
    private String password;
    private int column;
}
