import java.io.PrintStream;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Connect4TDD {
    // 需求1
    public int getNumberOfDiscs() {
//        return 0;
        return IntStream.range(0,COLUMNS)
                .map(this::getNumberOfDiscsInColumn).sum();
    }


    // 需求2
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final String EMPTY = " ";

    private String[][] board = new String[ROWS][COLUMNS];

    public Connect4TDD(PrintStream printStream) {
        outputChannel = printStream;
        for (String[] row : board) {
            Arrays.fill(row, EMPTY);
        }
    }

    private int getNumberOfDiscsInColumn(int column) {
        return (int) IntStream.range(0,ROWS)
                .filter(row -> !EMPTY.equals(board[row][column]))
                .count();
    }

    public int putDiscInColumn(int column) {
        checkColumn(column);
        int row = getNumberOfDiscsInColumn(column);
        checkPositionToInsert(row, column);
        board[row][column] = currentPlayer;
        printBoard();
        checkWinner(row,column);
        switchPlayer();
        return row;
    }

    private void checkColumn(int column) {
        if (column < 0 || column >= COLUMNS) {
            throw new RuntimeException("Invalid column " + column);
        }
    }

    private void checkPositionToInsert(int row, int column) {
        if (row == ROWS) {
            throw new RuntimeException("No more room in column " + column);
        }
    }


    // 需求3
    private static final String RED = "R";
    private static final String GREEN = "G";

    private String currentPlayer = RED;


    public String getCurrentPlayer() {
        outputChannel.printf("Player %s turn %n", currentPlayer);
        return currentPlayer;
    }

    private void switchPlayer() {
        if (RED.equals(currentPlayer)) {
            currentPlayer = GREEN;
        } else {
            currentPlayer = RED;
        }
    }


    // 需求4
    private static final String DELIMITER = "|";

    private PrintStream outputChannel;

    private void printBoard() {
        for (int row = ROWS - 1; row >= 0; row--) {
            StringJoiner stringJoiner = new StringJoiner(DELIMITER, DELIMITER, DELIMITER);
            Stream.of(board[row])
                    .forEachOrdered(stringJoiner::add);
            outputChannel.println(stringJoiner.toString());
        }
    }


    // 需求5
    public boolean isFinished() {
        return getNumberOfDiscs() == ROWS * COLUMNS;
    }



    // 需求6
    private static final int DISCS_TO_WIN = 4;
    private String winner = "";

    private void checkWinner(int row, int column) {
        if (winner.isEmpty()) {
            String color = board[row][column];
            Pattern winPattern = Pattern.compile(".*" + color +
                    "{" + DISCS_TO_WIN + "}.*");

            String vertical = IntStream.range(0, ROWS)
                    .mapToObj(r -> board[r][column])
                    .reduce(String::concat).get();
            if (winPattern.matcher(vertical).matches()) {
                winner = color;
            }

            String horizontal = Stream.of(board[row])
                    .reduce(String::concat).get();
            if (winPattern.matcher(horizontal).matches()) {
                winner = color;
            }

            // 左上到右下
            int startOffset = Math.min(column, row);
            int myColumn = column - startOffset;
            int myRow = row - startOffset;
            StringJoiner stringJoiner = new StringJoiner("");
            do {
                stringJoiner.add(board[myRow++][myColumn++]);
            } while (myColumn < COLUMNS && myRow < ROWS);

            if (winPattern.matcher(stringJoiner.toString()).matches()) {
                winner = color;
            }

            // 右下到左上
            startOffset = Math.min(column, ROWS - row - 1);
            myColumn = column - startOffset;
            myRow = row + startOffset;
            stringJoiner = new StringJoiner("");
            do {
                stringJoiner.add(board[myRow--][myColumn++]);
            } while (myColumn < COLUMNS && myRow >= 0);

            if (winPattern.matcher(stringJoiner.toString()).matches()) {
                winner = color;
            }
        }
    }

    public String getWinner() {
        return winner;
    }
}
