package com.packtpublishing.tddjava.ch03tictactoe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class TicTacToeSpec {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private TicTacToe ticTacToe;

    @Before
    public final void before() {
        ticTacToe = new TicTacToe();
    }

    //======================= 需求1 =======================
    @Test
    public void whenXOutsideBorderThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(5, 2);
    }

    @Test
    public void whenYOutsideBorderThenRuntimeException() {
        exception.expect(RuntimeException.class);
        ticTacToe.play(2,5);
    }

    @Test
    public void whenOccupiedThenRuntimeException() {
        ticTacToe.play(2, 1);
        exception.expect(RuntimeException.class);
        ticTacToe.play(2,1);
    }
    //======================= 需求1 =======================

    //======================= 需求2 =======================
    @Test
    public void givenFirstTurnWhenNextPlayerThenX() {
        assertEquals('X', ticTacToe.nextPlayer());
    }

    @Test
    public void givenLastTurnWasXWhenNextPlayerThenO() {
        ticTacToe.play(1,1);
        assertEquals('O', ticTacToe.nextPlayer());
    }
    //======================= 需求2 =======================


    //======================= 需求3 =======================
    @Test
    public void whenPlayNoWinner() {
        String actual = ticTacToe.play(1,1);
        assertEquals("No Winner", actual);
    }

    @Test
    public void whenPlayAndWholeHorizontalLineThenWinner() {
        // X
        ticTacToe.play(1,1);
        // O
        ticTacToe.play(1,2);
        // X
        ticTacToe.play(2,1);
        // O
        ticTacToe.play(2,2);
        // X
        String actual = ticTacToe.play(3,1);
        assertEquals("X is the winner", actual);
    }

    @Test
    public void whenPlayAndWholeVerticalLineThenWinner() {
        // X
        ticTacToe.play(2,1);
        // O
        ticTacToe.play(1,1);
        // X
        ticTacToe.play(3,1);
        // O
        ticTacToe.play(1,2);
        // X
        ticTacToe.play(2,2);
        // O
        String actual = ticTacToe.play(1,3);
        assertEquals("O is the winner", actual);
    }

    @Test
    public void whenPlayAndTopBottomDiagonalLineThenWinner() {
        // X
        ticTacToe.play(1,1);
        // O
        ticTacToe.play(1,2);
        // X
        ticTacToe.play(2,2);
        // O
        ticTacToe.play(1,3);
        // X
        String actual = ticTacToe.play(3,3);
        assertEquals("X is the winner", actual);
    }

    @Test
    public void whenPlayAndBottomTopDiagonglLineThenWinner() {
        // X
        ticTacToe.play(1,3);
        // O
        ticTacToe.play(1,1);
        // X
        ticTacToe.play(2,2);
        // O
        ticTacToe.play(1,2);
        // X
        String actual = ticTacToe.play(3, 1);
        assertEquals("X is the winner", actual);
    }
    //======================= 需求3 =======================


    //======================= 需求4 =======================
    @Test
    public void whenAllBoxesAreFilledThenDraw() {
        ticTacToe.play(1,1);
        ticTacToe.play(1,2);
        ticTacToe.play(1,3);
        ticTacToe.play(2,1);
        ticTacToe.play(2,3);
        ticTacToe.play(2,2);
        ticTacToe.play(3,1);
        ticTacToe.play(3,3);
        String actual = ticTacToe.play(3,2);
        assertEquals("The result is draw", actual);
    }
    //======================= 需求4 =======================
}

