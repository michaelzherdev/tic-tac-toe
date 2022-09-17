package com.mzherdev.tictactoe.controller;

import com.mzherdev.tictactoe.dto.ApiCreateGame;
import com.mzherdev.tictactoe.model.Game;
import com.mzherdev.tictactoe.service.TicTacToeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TicTacToeController {

    private final TicTacToeService ticTacToeService;

    @GetMapping("/game/{gameId}")
    public Game getGame(@PathVariable("gameId") int gameId) {
        return ticTacToeService.getGame(gameId);
    }

    @PutMapping("/game/{gameId}/player/{playerId}")
    public Game doMove(@PathVariable("gameId") int gameId, @PathVariable("playerId") int playerId, @RequestParam int cell) {
        return ticTacToeService.doMove(gameId, playerId, cell);
    }

    @PostMapping("/game")
    public Game createGame(@RequestBody @Valid ApiCreateGame createGame) {
        return ticTacToeService.createGame(createGame.getPlayer1Id(), createGame.getPlayer2Id());
    }
}
