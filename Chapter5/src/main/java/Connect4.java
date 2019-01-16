import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class Connect4 {
    // 需求1
    public enum Color {
        RED('R'), GREEN('G'), EMPTY(' ');

        private final char value;

        Color(char value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static final int COLUMNS = 7;

    public static final int ROWS = 6;

    private Color[][] board = new Color[COLUMNS][ROWS];


    // 需求2
    public Connect4() {
        for (Color [] column : board) {
            Arrays.fill(column, Color.EMPTY);
        }
    }

    public void putDisc(int column) {
        if (column > 0 && column <= COLUMNS) {
            int numOfDics = getNumberOfDicsInColumn(column - 1);
            if (numOfDics < ROWS) {
                board[column - 1][numOfDics] = currentPlayer;
                printBoard();
                checkWinCondition(column - 1, numOfDics);
                switchPlayer();
            } else {
                System.out.println(numOfDics);
                System.out.println("There's not room" +
                        "for a new disc in this column");
                printBoard();
            }
        } else {
            System.out.println("Column out of bounds");
            printBoard();
        }
    }


    private int getNumberOfDicsInColumn(int column) {
        if (column >= 0 && column < COLUMNS) {
            int row;
            for (row = 0; row < ROWS; row++) {
                if (Color.EMPTY == board[column][row]) {
                    return row;
                }
            }
        }
        return -1;
    }

    // 需求3
    private Color currentPlayer = Color.RED;

    private void switchPlayer() {
        if (Color.RED == currentPlayer) {
            currentPlayer = Color.GREEN;
        } else {
            currentPlayer = Color.RED;
        }
        System.out.println("Current turn: " + currentPlayer);
    }


    // 需求4
    private static final String DELIMITER = "|";

    public void printBoard() {
        for (int row = ROWS - 1; row >= 0; --row) {
            StringJoiner stringJoiner = new StringJoiner(DELIMITER,DELIMITER,DELIMITER);
            for (int col = 0; col < COLUMNS; ++col) {
                stringJoiner.add(board[col][row].toString());
            }
            System.out.println(stringJoiner.toString());
        }
    }

    // 需求5
    public boolean isFinished() {
        if (winner != null) {
            return true;
        }
        int numOfDiscs = 0;
        for (int col = 0; col < COLUMNS; ++col) {
            numOfDiscs += getNumberOfDicsInColumn(col);
        }
        if (numOfDiscs >= COLUMNS * ROWS) {
            System.out.println("It's a draw");
            return true;
        }
        return false;
    }

    // 需求6
    private Color winner;

    public static final int DISCS_FOW_WIN = 4;

    private void checkWinCondition(int col, int row) {
        // 第一种获胜条件
        Pattern winPattern = Pattern.compile(".*" + currentPlayer +
                "{" + DISCS_FOW_WIN + "}.*");

        // 检查垂直方向
        StringJoiner stringJoiner = new StringJoiner("");
        for (int auxRow = 0; auxRow < ROWS; ++auxRow) {
            stringJoiner.add(board[col][auxRow].toString());
        }

        if (winPattern.matcher(stringJoiner.toString()).matches()) {
            winner = currentPlayer;
            System.out.println(currentPlayer + " wins");
            return;
        }

        // 检查水平方向
        for (int column = 0; column < COLUMNS; ++column) {
            stringJoiner.add(board[column][row].toString());
        }

        if (winPattern.matcher(stringJoiner.toString()).matches()) {
            winner = currentPlayer;
            System.out.println(currentPlayer + " wins");
            return;
        }

        // 检查对角线方向
        int startOffset = Math.min(col, row);
        int column = col - startOffset;
        int auxRow = row - startOffset;
        stringJoiner = new StringJoiner("");
        do {
            stringJoiner.add(board[column++][auxRow++].toString());
        } while (column < COLUMNS && auxRow < ROWS);

        if (winPattern.matcher(stringJoiner.toString()).matches()) {
            winner = currentPlayer;
            System.out.println(currentPlayer + " wins");
            return;
        }

        startOffset = Math.min(col, ROWS - 1 - row);
        column = col - startOffset;
        auxRow = row + startOffset;
        do {
            stringJoiner.add(board[column++][auxRow--].toString());
        } while (column < COLUMNS && auxRow >= 0);

        if (winPattern.matcher(stringJoiner.toString()).matches()) {
            winner = currentPlayer;
            System.out.println(currentPlayer + " wins");
        }
    }
}
