package com.mzherdev.tictactoe.service;

import com.mzherdev.tictactoe.dao.GameRepository;
import com.mzherdev.tictactoe.dao.PlayerRepository;
import com.mzherdev.tictactoe.exception.NotFoundException;
import com.mzherdev.tictactoe.model.Game;
import com.mzherdev.tictactoe.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicTacToeService {

    private static final String CLEAN_BOARD = "123456789";

    private final ValidationService validationService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public Game getGame(int gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException("Game " + gameId + " does not exist"));
    }

    @Transactional
    public Game doMove(int gameId, int playerId, int cell) {
        validationService.validateCellRangeLegal(cell);
        Game game = getGame(gameId);
        validationService.validateGame(game, playerId);
        validationService.validateCellIsAvailable(game.getBoard(), cell);

        String board = game.getBoard();
        char[] chars = board.toCharArray();
        chars[cell - 1] = playerId == game.getPlayer1().getId() ? 'X' : '0';
        game.setBoard(String.valueOf(chars));

        checkWinner(game);
        game.setActivePlayerId(playerId == game.getPlayer1().getId() ? game.getPlayer2().getId() : game.getPlayer1().getId());
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game createGame(int player1Id, int player2Id) {
        if(player1Id == player2Id) {
            throw new IllegalArgumentException("Player " + player1Id + " can't play with himself");
        }
        Player player1 = playerRepository.findById(player1Id).orElseThrow(() -> new NotFoundException("Player " + player1Id + " doesn't exist"));
        Player player2 = playerRepository.findById(player2Id).orElseThrow(() -> new NotFoundException("Player " + player2Id + " doesn't exist"));
        Game game = new Game();
        game.setBoard(CLEAN_BOARD);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setActivePlayerId(player1.getId());
        gameRepository.save(game);
        return game;
    }

    private Game checkWinner(Game game) {
        char[] charsBoard = game.getBoard().toCharArray();

        List<String> lines = new ArrayList<>();
        lines.add("" + charsBoard[0] + charsBoard[1] + charsBoard[2]);
        lines.add("" + charsBoard[3] + charsBoard[4] + charsBoard[5]);
        lines.add("" + charsBoard[6] + charsBoard[7] + charsBoard[8]);

        lines.add("" + charsBoard[0] + charsBoard[3] + charsBoard[6]);
        lines.add("" + charsBoard[1] + charsBoard[4] + charsBoard[7]);
        lines.add("" + charsBoard[2] + charsBoard[5] + charsBoard[8]);

        lines.add("" + charsBoard[0] + charsBoard[4] + charsBoard[8]);
        lines.add("" + charsBoard[2] + charsBoard[4] + charsBoard[6]);

        for (String line : lines) {
            if (line.equals("XXX")) {
                game.setFinished(true);
                game.setResult("Winner is " + game.getPlayer1().getId());
                return game;
            } else if(line.equals("000")) {
                game.setFinished(true);
                game.setResult("Winner is " + game.getPlayer2().getId());
                return game;
            }
        }

        boolean hasFreeSlots = false;
        for (char c : charsBoard) {
            if(Character.isDigit(c) && Character.getNumericValue(c) > 0) {
                hasFreeSlots = true;
                break;
            }
        }

        if(!hasFreeSlots) {
            game.setFinished(true);
            game.setResult("Draw");
        }
        return game;
    }
}
