package com.mzherdev.tictactoe.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ApiCreateGame {
    @NotNull @Valid
    private int player1Id;
    @NotNull @Valid
    private int player2Id;
}
