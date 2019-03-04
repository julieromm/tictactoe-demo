package com.jobcase.demo.tictactoe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The class modeling the state of the TicTacToe game grid,
 * as well as corresponding behavior.
 *
 * The grid is represented as a continuous sequence of cells,
 * starting from 0 to 8 (inclusive).  The cells can be thought of as a
 * sequence of concatenated rows of a 3x3 matrix, visualized as follows:
 *
 *    0 | 1 | 2
 *    3 | 4 | 5
 *    6 | 7 | 8
 *
 * @author jromm
 */
@Component
public class Grid {
    /**
     * Character constants to represent possible cell values
     */
    private static final char EMPTY = '.';
    private static final char X = 'X';
    private static final char O = 'O';

    private static final int GRID_SIZE = 9;

    /**
     * This is a static collection of rows, defined for convenience
     */
    private static final Set<Row> ALL_ROWS = Set.of(
            new Row(0,1,2),
            new Row(3,4,5),
            new Row(6,7,8),
            new Row(0,3,6),
            new Row(1,4,7),
            new Row(2,5,8),
            new Row(0,4,8),
            new Row(2,4,6)
    );

    /**
     * An array reflecting current state of each cell in the grid.
     */
    private char[] cells = new char[GRID_SIZE];

    /**
     * Unordered collections representing all empty cells,
     * X-marked cell, and O-marked cells.
     *
     * Defined for observability and convenience.
     *
     * These collections get updated by various methods
     * modifying the state of the grid.
     */
    private Set<Integer> availableCells;
    private Set<Integer> markedXCells;
    private Set<Integer> markedOCells;

    /**
     * Fields containing state information for
     * a variety of attributes.
     */
    private GameStatus gameStatus;
    private Row winningRow;
    private char winningTag = EMPTY;
    private String errorMsg;
    private String winningMsg;

    public Grid() {
        init();
    }

    /**
     * Grid initializer method.
     */
    public void init() {
        gameStatus = GameStatus.NEW;
        availableCells = IntStream.range(0,GRID_SIZE).boxed().collect(Collectors.toSet());
        markedXCells = new HashSet<>();
        markedOCells = new HashSet<>();

        for (int i = 0; i < GRID_SIZE; i++) {
            cells[i] = EMPTY;
        }
    }

    public Set<Integer> getAvailableCells() {
        return availableCells;
    }

    public Set<Integer> getMarkedXCells() {
        return markedXCells;
    }

    public Set<Integer> getMarkedOCells() {
        return markedOCells;
    }

    public char[] getCells() {
        return cells;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Row getWinningRow() {
        return winningRow;
    }

    public char getWinningTag() {
        return winningTag;
    }

    /**
     * A helper method to return all rows for a given cell.
     *
     * @param cell cell position in the grid
     * @return set of {@link Row}s which contain a given cell.
     */
    public Set<Row> rowsForCell(int cell) {
        return ALL_ROWS.stream()
                .filter(row -> row.getCells().contains(cell))
                .collect(Collectors.toSet());
    }

    /**
     * The core logic of the game is encapsulated in this method.
     * A "move" is made by marking a cell with X or O,
     * and all corresponding data structures and fields
     * get updated accordingly, including final game status.
     *
     * @param cellPosition cell to be marked with the char tag
     * @param tag X or O
     */
    public void markCell(int cellPosition, char tag) {
        assert(availableCells.contains(cellPosition));
        assert(tag == X || tag == O);
        assert(cells[cellPosition] == EMPTY);

        if (gameStatus.equals(GameStatus.NEW)) {
            gameStatus = GameStatus.IN_PROGRESS;
        }

        if (tag == X) {
            markedXCells.add(cellPosition);
            cells[cellPosition] = X;
        }
        if (tag == O) {
            markedOCells.add(cellPosition);
            cells[cellPosition] = O;
        }
        availableCells.remove(cellPosition);

        for (Row row : rowsForCell(cellPosition)) {
            Set<Integer> difference = new HashSet<>(row.getCells());
            difference.removeAll(tag == X ? markedXCells : markedOCells);
            if (difference.isEmpty()) {
                gameStatus = GameStatus.GAME_OVER;
                winningRow = row;
                winningTag = tag;
                winningMsg = String.format("%s wins!", tag);
            }
        }

        if (availableCells.isEmpty() && gameStatus.equals(GameStatus.IN_PROGRESS)) {
            gameStatus = GameStatus.GAME_OVER;
            winningMsg = "It's a draw!";
        }
    }

    /**
     * Convenience method to accommodate random game strategy.
     *
     * @return random available cell number
     */
    @JsonIgnore
    public int getRandomAvailableCell() {
        int randPosition = new Random().nextInt(availableCells.size());
        int i = 0;
        for (int cell : availableCells) {
            if (i++ == randPosition)
                return cell;
        }
        return -1;
    }
}
