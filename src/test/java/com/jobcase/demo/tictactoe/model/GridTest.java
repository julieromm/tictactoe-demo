package com.jobcase.demo.tictactoe.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class GridTest {

    private Grid grid;

    @Before
    public void setUp() throws Exception {
        grid = new Grid();
    }

    @Test
    public void givenNewGrid_getAllAvailableCells() {
        assertEquals(9, grid.getAvailableCells().size());
    }

    @Test
    public void test_rowsForCell() {
        assertEquals(2, grid.rowsForCell(1).size());
        assertEquals(3, grid.rowsForCell(0).size());
//        grid.rowsForCell(0).stream()
//                .forEach(row -> System.out.println(Arrays.toString(row.getCells().toArray())));
    }

    @Test(expected = AssertionError.class)
    public void givenInvalidCharacer_shoudlRaiseError() {
        grid.markCell(0, 'Z');
    }

    @Test
    public void testMarkCell() {
        grid.markCell(0, 'X');
        assertEquals(grid.getMarkedXCells().size(), 1);
        assertEquals(grid.getMarkedOCells().size(), 0);
        assertEquals(grid.getAvailableCells().size(), 8);
        assertFalse(grid.getAvailableCells().contains(0));
    }

    @Test(expected = AssertionError.class)
    public void givenInvalidCell_shoudlRaiseError() {
        grid.markCell(100, 'X');
    }

    @Test
    public void testGameOver() {
        grid.markCell(0, 'X');
        grid.markCell(4, 'X');
        grid.markCell(8, 'X');
        assertEquals(grid.getMarkedXCells().size(), 3);
        assertEquals(grid.getMarkedOCells().size(), 0);
        assertEquals(grid.getAvailableCells().size(), 6);

        assertTrue(grid.getGameStatus().equals(GameStatus.GAME_OVER));
        assertEquals(grid.getWinningTag(), 'X');
        Set<Integer> winningCells = grid.getWinningRow().getCells();

        Set<Integer> difference = new HashSet<>(winningCells);
        difference.removeAll(Arrays.asList(0, 4, 8));
        assertTrue(difference.isEmpty());
    }

}