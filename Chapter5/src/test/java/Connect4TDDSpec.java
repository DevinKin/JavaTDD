import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class Connect4TDDSpec {
    private Connect4TDD tested;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void beforeEachTest() {
        output = new ByteArrayOutputStream();
        tested = new Connect4TDD(new PrintStream(output));
    }

    @Test
    public void whenTheGameIsStartedTheBoardIsEmpty() {
        assertThat(tested.getNumberOfDiscs(), is(0));
    }

    @Test
    public void whenDiscOutsideBoardThenRuntimeException() {
        int column = -1;
        exception.expect(RuntimeException.class);
        exception.expectMessage("Invalid column " + column);
        tested.putDiscInColumn(column);
    }

    @Test
    public void whenFirstDiscInsertedInColumnThenPositionIsZero() {
        int column = 1;
        assertThat(tested.putDiscInColumn(column), is(0));
    }

    @Test
    public void whenSecondDiscInsertInColumnThenPositionIsOne() {
        int column = 1;
        tested.putDiscInColumn(column);
        assertThat(tested.putDiscInColumn(column), is(1));
    }

    @Test
    public void whenDiscInsertedThenNumberOfDiscsIncreases() {
        int column = 1;
        tested.putDiscInColumn(column);
        assertThat(tested.getNumberOfDiscs(), is(1));
    }

    @Test
    public void whenNoMoreRoomInColumnThenRuntimeException() {
        int column = 1;
        // The number of ros
        int maxDiscsInColumn = 6;
        for (int times = 0; times < maxDiscsInColumn; ++times) {
            tested.putDiscInColumn(column);
        }
        exception.expect(RuntimeException.class);
        exception.expectMessage("No more room in column " + column);
        tested.putDiscInColumn(column);
    }


    // 需求3
    @Test
    public void whenFirstPlayerPlaysThenDiscColorIsRed() {
        assertThat(tested.getCurrentPlayer(), is("R"));
    }

    @Test
    public void whenSecondPlayerPlaysColorIsGreen() {
        int column = 1;
        tested.putDiscInColumn(column);
        assertThat(tested.getCurrentPlayer(), is("G"));
    }


    // 需求4
    private OutputStream output;


    @Test
    public void whenAskedForCurrentPlayerThenOutputNotice() {
        tested.getCurrentPlayer();
        assertThat(output.toString(), containsString("Player R turn"));
    }


    @Test
    public void whenADiscIsIntroducedTheBoardIsPrinted() {
        int column = 1;
        tested.putDiscInColumn(column);
        assertThat(output.toString(), containsString("| |R| | | | | |"));
    }


    // 需求5
    @Test
    public void whenTheGameStartsItIsNotFinished() {
        assertFalse("The game must not be finished",
                tested.isFinished());
    }

    @Test
    public void whenDisCanBeIntroducedTheGamesIsFinished() {
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 7; column++) {
                tested.putDiscInColumn(column);
            }
        }
        assertTrue("The game must be finished", tested.isFinished());
    }


    // 需求6
    @Test
    public void when4VerticalDiscsAreConnectedThenPlayerWins() {
        for (int row = 0; row < 3; row++) {
            // R
            tested.putDiscInColumn(1);
            // G
            tested.putDiscInColumn(2);
        }
        assertThat(tested.getWinner(),
                isEmptyString());
        // R
        tested.putDiscInColumn(1);
        assertThat(tested.getWinner(),
                is("R"));
    }


    // 需求7
    @Test
    public void when4HorizontalDisAreConnectedThenPlayerWins() {
        int column;
        for (column = 0; column < 3; column++) {
            // R
            tested.putDiscInColumn(column);
            // G
            tested.putDiscInColumn(column);
        }
        assertThat(tested.getWinner(),
                isEmptyString());
        // R
        tested.putDiscInColumn(column);
        assertThat(tested.getWinner(),
                is("R"));
    }

    // 需求8
    @Test
    public void when4Diagona11DiscsAreConnectedThenThatPlayerWins() {
        int[] gameplay = new int[] {1, 2, 2, 3, 4, 3, 3, 4, 4, 5, 4};
        for (int column : gameplay) {
            tested.putDiscInColumn(column);
        }
        assertThat(tested.getWinner(), is("R"));
    }


    @Test
    public void when4Diagona12DiscsAreConnectedThenThatPlayerWins() {
        int[] gameplay = new int[] {3, 4, 2, 3, 2, 2, 1, 1, 1, 1};
        for (int column : gameplay) {
            tested.putDiscInColumn(column);
        }
        assertThat(tested.getWinner(), is("G"));
    }
}
