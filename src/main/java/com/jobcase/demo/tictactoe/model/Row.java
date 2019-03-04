package com.jobcase.demo.tictactoe.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class to represent a row on a TicTacToe game grid.
 *
 * The grid is assumed to be represented as a continuous
 * sequence of cells: 0, 1, ..., 8.
 *
 * @author jromm
 */
public class Row {
    private final Set<Integer> cells;

    public Row(int... cells) {
        this.cells = Arrays.stream(cells).boxed().collect(Collectors.toSet());
    }

    public Set<Integer> getCells() {
        return cells;
    }
}
