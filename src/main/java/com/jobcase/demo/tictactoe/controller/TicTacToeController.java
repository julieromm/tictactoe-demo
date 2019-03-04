package com.jobcase.demo.tictactoe.controller;

import com.jobcase.demo.tictactoe.model.GameStatus;
import com.jobcase.demo.tictactoe.model.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A collection of HTTP endpoints to interact with the game {@link Grid}.
 *
 * @author jromm
 */
@RestController
public class TicTacToeController {

    @Autowired
    private Grid grid;

    @Value("${index.greeting}")
    private String indexGreeting;

    @GetMapping("/")
    ResponseEntity<String> index() {
        return new ResponseEntity<>(indexGreeting, HttpStatus.OK);
    }

    @GetMapping("/grid")
    ResponseEntity<Grid> grid() {
        return new ResponseEntity<>(grid, HttpStatus.OK);
    }

    @PostMapping("/init")
    ResponseEntity<Grid> init() {
        grid.init();
        return new ResponseEntity<>(grid, HttpStatus.OK);
    }

    @PostMapping("/X/{cell}")
    ResponseEntity<Grid> markXCell(@PathVariable("cell") int cell) {
        boolean ok = true;
        if (grid.getGameStatus().equals(GameStatus.GAME_OVER)) {
            ok = false;
            grid.setErrorMsg("Current game is over. Start a new game!");
        } else if (!grid.getAvailableCells().contains(cell)) {
            ok = false;
            grid.setErrorMsg(String.format("Invalid cell: %d", cell));
        }

        if (ok) {
            // game move executed on behalf of the player
            grid.markCell(cell, 'X');

            // assuming the game is not over, execute
            // the computer's move (using random strategy)
            if (!grid.getGameStatus().equals(GameStatus.GAME_OVER)) {
                grid.markCell(grid.getRandomAvailableCell(), 'O');
            }
        }
        return new ResponseEntity<>(grid, ok ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
