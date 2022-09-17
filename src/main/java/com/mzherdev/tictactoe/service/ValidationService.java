package com.mzherdev.tictactoe.service;

import com.mzherdev.tictactoe.dao.GameRepository;
import com.mzherdev.tictactoe.exception.IllegalMoveException;
import com.mzherdev.tictactoe.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final GameRepository gameRepository;

    public void validateCellRangeLegal(int cell) {
        if (cell < 1 || cell > 9) {
            throw new IllegalArgumentException("Parameter cell should be between 1 and 9");
        }
    }
    public void validateCellIsAvailable(String board, int cell) {
        if (Character.getNumericValue(board.charAt(cell - 1)) != cell) {
            throw new IllegalMoveException("Cell already busy");
        }
    }
    public void validateGame(Game game, int playerId) {
        if(game.isFinished()) {
            throw new IllegalArgumentException("Game is finished. No more moves allowed");
        }
        if(playerId != game.getPlayer1().getId() && playerId != game.getPlayer2().getId()) {
            throw new IllegalArgumentException("Player doesn't exist or doesn't play this game");
        }
        if(playerId != game.getActivePlayerId()) {
            throw new IllegalMoveException("It is not your turn!");
        }
    }

}
