# 第5章-设计

## 为何要关心设计

### 设计原则

- SOLID
  - 单一职责原则：一个类应该只有一个导致它需要修改的原因。
  - 开-闭原则：类应该对扩展是开放的，对修改是封闭的。
  - 里氏替换原则：类应该能够被扩展它的类替换。
  - 接口分离原则：提供多个具体接口胜过提供单个通用及接口。
  - 依赖倒转原则：类应该依赖于抽象而不是实现，这意味着类依赖必须专注于做什么而不是如何做。

## Connect4

### 需求

- 棋盘分为7列6行，所有格子都是空的。
- 玩家将列顶放入盘片，如果整列为空，放入的碟片将落到底部。在特定列中，后放入的碟片将叠在前面放入的碟片之上。
- 这是一款两人玩的游戏，每位玩家的碟片用一种颜色表示：一位玩家为红色（'R'）。另一位玩家为绿色（'G'）。玩家轮流放入碟片，每次放入一个。
- 我们要在玩家放入碟片或发生错误时提供反馈，每当玩家放入碟片后，都使用输出指出棋盘状态。
- 无法放入碟片时游戏结束，结果为平局。
- 玩家放入碟片后，如果将其3个以上碟片连成垂直线，该玩家将获胜。
- 玩家放入碟片后，如果将其3个以上碟片连成水平线，该玩家将获胜。
- 玩家放入碟片后，如果将其3个以上碟片连成对角线，该玩家将获胜。

## 完成Connect4实现后再测试

### 需求1

- 棋盘为7列6行，且整个棋盘都是空的。

  ```java
  import java.util.Arrays;
  
  public class Connect4 {
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
  
      public Connect4() {
          for (Color [] column : board) {
              Arrays.fill(column, Color.EMPTY);
          }
      }
  }
  ```

### 需求2

- 玩家从列顶放入碟片。如果整列都是空的，放入的碟片将落入到最底部。在特定列中，放入的碟片将叠在前面放入的碟片之上。

  ```java
      public void putDisc(int column) {
          if (column > 0 && column <= COLUMNS) {
              int numOfDics = getNumberOfDicsInColumn(column - 1);
              if (numOfDics < ROWS) {
                  board[column - 1][numOfDics] = Color.RED;
              }
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
  ```

### 需求3

- 这是一款双人玩的游戏，每位玩家的碟片用一种颜色表示：一位玩家为红色（'R'），另一位玩家为绿色（'G'）。玩家轮流放入碟片，每次放入一个。

  ```java
      public void putDisc(int column) {
          if (column > 0 && column <= COLUMNS) {
              int numOfDics = getNumberOfDicsInColumn(column - 1);
              if (numOfDics < ROWS) {
                  board[column - 1][numOfDics] = currentPlayer;
                  switchPlayer();
              }
          }
      }
      private Color currentPlayer = Color.RED;
  
      private void switchPlayer() {
          if (Color.RED == currentPlayer) {
              currentPlayer = Color.GREEN;
          } else {
              currentPlayer = Color.RED;
          }
      }
  ```

### 需求4

- 我们要在玩家放入碟片或发生错误时提供反馈：每当玩家放入碟片后，都使输出指出棋盘状态。

  ```java
  	public void putDisc(int column) {
          if (column > 0 && column <= COLUMNS) {
              int numOfDics = getNumberOfDicsInColumn(column - 1);
              if (numOfDics < ROWS) {
                  board[column - 1][numOfDics] = currentPlayer;
                  printBoard();
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
      private Color currentPlayer = Color.RED;
  
      private void switchPlayer() {
          if (Color.RED == currentPlayer) {
              currentPlayer = Color.GREEN;
          } else {
              currentPlayer = Color.RED;
          }
          System.out.println("Current turn: " + currentPlayer);
      }
  ```

### 需求5

- 无法放入碟片时游戏结束，将结果为平局。

  ```java
      // 需求5
      public boolean isFinished() {
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
  ```

### 需求6

- 玩家放入碟片后，如果将其3个以上碟片连接成垂直线，该玩家将获胜。

  ```java
      private Color winner;
  
      public static final int DISCS_FOW_WIN = 4;
  
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
          }
      }
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
  ```

### 需求7

- 玩家放入碟片后，如果将其3个以上碟片连成水平线，该玩家将获胜。

  ```java
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
          }
  
          // 检查水平方向
          for (int column = 0; column < COLUMNS; ++column) {
              stringJoiner.add(board[column][row].toString());
          }
  
          if (winPattern.matcher(stringJoiner.toString()).matches()) {
              winner = currentPlayer;
              System.out.println(currentPlayer + " wins");
          }
      }
  ```

### 需求8

- 玩家放入碟片后，如果将其3个以上碟片连成对角线，该玩家将获胜。

  ```java
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
  ```

## 使用TDD实现Connect4

- 使用Hamcrest框架。

### 需求1

- 棋盘为7列6行，所有各自都是空的

#### 测试

```java
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;


public class Connect4TDDSpec {
    private Connect4TDD tested;

    @Before
    public void beforeEachTest() {
        tested = new Connect4TDD();
    }

    @Test
    public void whenTheGameIsStartedTheBoardIsEmpty() {
        assertThat(tested.getNumberOfDiscs(), is(0));
    }
}
```

#### 实现代码

```java
public class Connect4TDD {

    public int getNumberOfDiscs() {
        return 0;
    }
}
```

### 需求2

- 玩家从列顶放入碟片。如果整列都是空的，放入的碟片将落入底部。在特定列中，后放入的碟片将叠在前面放入的碟片之上。
- 需求分解
  - 碟片被加入空列时，其位置为0。
  - 碟片被加入已经有一个碟片的列时，其位置为1。
  - 每加入一个碟片，碟片数都加1。
  - 如果碟片位于棋盘边界外，将引发运行时阶段异常。
  - 向已满的列中加入碟片时，将引发运行时阶段异常。

#### 测试

```java
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
```



#### 实现代码

```java
import java.util.Arrays;
import java.util.stream.IntStream;

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

    public Connect4TDD() {
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
        board[row][column] = "X";
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
}
```

### 需求3

- 这是一款两人玩的游戏，每位玩家的碟片用一种颜色表示：一位玩家为红色（'R'），另一位玩家为绿色（'G'）。玩家轮流放入碟片，每次放入一个。

#### 测试

```java
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
```

#### 实现

```java
    public int putDiscInColumn(int column) {
        checkColumn(column);
        int row = getNumberOfDiscsInColumn(column);
        checkPositionToInsert(row, column);
        board[row][column] = currentPlayer;
        switchPlayer();
        return row;
    }
    private static final String RED = "R";
    private static final String GREEN = "G";

    private String currentPlayer = RED;


    public String getCurrentPlayer() {
        return currentPlayer;
    }

    private void switchPlayer() {
        if (RED.equals(currentPlayer)) {
            currentPlayer = GREEN;
        } else {
            currentPlayer = RED;
        }
    }
```

### 需求4

- 我们要在玩家放入碟片或发生错误时提供反馈：每当玩家放入碟片后，都使用输出指出棋盘的状态。

#### 测试

```java
    // 需求4
    private OutputStream output;

    @Before
    public void beforeEachTest() {
        output = new ByteArrayOutputStream();
        tested = new Connect4TDD(new PrintStream(output));
    }

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
```

#### 实现

```java
    // 需求4
    private static final String DELIMITER = "|";

    private PrintStream outputChannel;

    public Connect4TDD(PrintStream printStream) {
        outputChannel = printStream;
        for (String[] row : board) {
            Arrays.fill(row, EMPTY);
        }
    }

    public int putDiscInColumn(int column) {
        checkColumn(column);
        int row = getNumberOfDiscsInColumn(column);
        checkPositionToInsert(row, column);
        board[row][column] = currentPlayer;
        printBoard();
        switchPlayer();
        return row;
    }

    private void printBoard() {
        for (int row = ROWS - 1; row >= 0; row--) {
            StringJoiner stringJoiner = new StringJoiner(DELIMITER, DELIMITER, DELIMITER);
            Stream.of(board[row])
                    .forEachOrdered(stringJoiner::add);
            outputChannel.println(stringJoiner.toString());
        }
    }
```

### 需求5

- 无法再放入碟片时游戏结束，结果为平局。

#### 测试

```java
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
```

#### 实现

```java
    // 需求5
    public boolean isFinished() {
        return getNumberOfDiscs() == ROWS * COLUMNS;
    }
```

### 需求6

- 玩家放入碟片后，如果将其3个以上碟片连成一条垂直线，则当前玩家获胜。

#### 测试

```java
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
```

#### 实现

```java
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
        }
    }

    public String getWinner() {
        return winner;
    }
```

### 需求7

- 玩家放入碟片后，如果将其3个以上碟片连成水平线，该玩家获胜。

#### 测试

```java
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
```

#### 实现

```java
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
        }
    }
```

### 需求8

- 玩家放入碟片后，如果将其3个以上碟片连成对角线，该玩家获胜。

#### 测试

```java
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
```

#### 实现

```java
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
```

