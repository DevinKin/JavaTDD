# 第三章-红灯-绿灯-重构

## "红灯-绿灯-重构"过程

### 编写一个测试

- 每次添加新功能时都首先编写一个测试，这旨在编写代码前专注于需求和代码设计。

### 运行所有测试并确认最后一个未通过

- 确认最后一个测试未通过后，我们断定它不会再没有引入新代码的情况下错误通过。

### 编写实现代码

- 这个阶段的目标是编写代码使最后一个测试通过。先编写测试通过的代码，再进行优化。

### 运行所有测试

- 应运行所有测试，而不是只运行最后编写的那个测试。

### 重构

- 以更优的方式重写代码。

### 重复

- 重复上述步骤

## “井字游戏”的需求

- “井字游戏”是两个人使用纸和铅笔玩的一种游戏，双方轮流在一个3×3
  的网格中画X和O，最先在水平、垂直或对角线上将自己的3个标记连起来的玩
  家获胜。

## 开发“井字游戏”

### 需求1

- 我们先定义边界，以及棋子放在哪些地方非法。

- 该需求分成三个测试

  -  如果棋子放在超出了X轴边界的地方，就引发 RuntimeException 异常；
  -  如果棋子放在超出了Y轴边界的地方，就引发 RuntimeException 异常；
  - 如果棋子放在已经有棋子的地方，就引发 RuntimeException 异常。

- JUnit测试异常：JUnit4.7引入规则的功能

  - `exception.expect(RuntimeException.class);`指定期待引发的异常。

  ```java
  @Rule
  public ExpectedException exception = ExpectedException.none();
  ```

- 编写测试代码

  ```java
  package com.packtpublishing.tddjava.ch03tictactoe;
  
  import org.junit.Before;
  import org.junit.Rule;
  import org.junit.Test;
  import org.junit.rules.ExpectedException;
  
  public class TicTacToeSpec {
  
      @Rule
      public ExpectedException exception = ExpectedException.none();
  
      private TicTacToe ticTacToe;
  
      @Before
      public final void before() {
          ticTacToe = new TicTacToe();
      }
  
      @Test
      public void whenXOutsideBorderThenRuntimeException() {
          exception.expect(RuntimeException.class);
          ticTacToe.play(5, 2);
      }
  }
  ```

- 编写实现代码

  ```java
  package com.packtpublishing.tddjava.ch03tictactoe;
  
  public class TicTacToe {
      public void play(int x, int y) {
          if (x < 1 || x > 3) {
              throw new RuntimeException("X is outside board");
          }
      }
  }
  ```

- 重构代码

  ```java
  package com.packtpublishing.tddjava.ch03tictactoe;
  
  public class TicTacToe {
      private Character[][] board = {{'\0', '\0', '\0'}, {'\0', '\0', '\0'}, {'\0', '\0', '\0'}};
  
      public void play(int x, int y) {
          checkAxis(x);
          checkAxis(y);
          setBox(x,y);
      }
  
      private void setBox(int x, int y) {
          if (board[x][y - 1] != '\0') {
              throw new RuntimeException("Box is occupied");
          } else {
              board[x][y - 1] = 'X';
          }
      }
  
      private void checkAxis(int axis) {
          if (axis < 1 || axis > 3) {
              throw new RuntimeException("X is outside board");
          }
      }
  }
  ```

### 需求2

- 需要提供一种途径，用于判断接下来该谁落子。
- 需求分为三个测试
  - 玩家X先下。
  - 如果上一次是X下的，接下来是轮到O下。
  - 如果上一次是O下的，接下来是轮到X下。
- 使用JUnit断言，需要导入`org.junit.Assert`类中的静态(static)方法`import static org.junit.Assert.*;`

- 测试代码

  ```java
      @Test
      public void givenFirstTurnWhenNextPlayerThenX() {
          assertEquals('X', ticTacToe.nextPlayer());
      }
  
      @Test
      public void givenLastTurnWasXWhenNextPlayerThenO() {
          ticTacToe.play(1,1);
          assertEquals('O', ticTacToe.nextPlayer());
      }
  ```

- 实现代码

  ```java
      public char nextPlayer() {
          if (lastPlayer == 'X') {
              return 'O';
          }
          return 'X';
      }
  ```

### 需求3

- 最先在水平，垂直或者对角线上将自己的三个标记连起来的玩家获胜。

- 测试-没有赢家

  ```java
      @Test
      public void whenPlayNoWinner() {
          String actual = ticTacToe.play(1,1);
          assertEquals("No Winner", actual);
      }
  ```

- 实现-没有赢家

  ```java
  public String play(int x, int y) {
      checkAxis(x);
      checkAxis(y);
      setBox(x,y);
      lastPlayer = nextPlayer();
      return "No winner";
  }
  ```

- 测试-水平有赢家

  ```java
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
  ```

- 实现-水平有赢家

  ```java
      public String play(int x, int y) {
          checkAxis(x);
          checkAxis(y);
          lastPlayer = nextPlayer();
          setBox(x,y, lastPlayer);
  		for (index = 0; index < 3; index++) {
              if (board[0][index] == lastPlayer && 
              	board[1][index] == lastPlayer &&
              	board[2][index] == lastPlayer) {
                      return lastPlayer + " is the winner";
              	}
  		}
          return "No Winner";
      }
  
      private void setBox(int x, int y, char lastPlayer) {
          if (board[x - 1][y - 1] != '\0') {
              throw new RuntimeException("Box is occupied");
          } else {
              board[x - 1][y - 1] = lastPlayer;
          }
      }
  ```

- 重构-水平有赢家

  ```java
      public String play(int x, int y) {
          checkAxis(x);
          checkAxis(y);
          lastPlayer = nextPlayer();
          setBox(x,y, lastPlayer);
          if (isWin()) {
              return lastPlayer + " is the winner";
          }
          return "No Winner";
      }
  
      private void setBox(int x, int y, char lastPlayer) {
          if (board[x - 1][y - 1] != '\0') {
              throw new RuntimeException("Box is occupied");
          } else {
              board[x - 1][y - 1] = lastPlayer;
          }
      }
      
      private boolean isWin() {
          for (int i = 0; i < SIZE; i++) {
              if (board[0][i] + board[1][i] + board[2][i] == (lastPlayer * SIZE)) {
                  return true;
              }
          }
          return false;
      }
  ```

- 测试-垂直有赢家

  ```java
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
          assertEquals("O is the Winner", actual);
      }
  ```

- 实现-垂直有赢家

  ```java
      public String play(int x, int y) {
          checkAxis(x);
          checkAxis(y);
          lastPlayer = nextPlayer();
          setBox(x,y, lastPlayer);
          if (isWin()) {
              return lastPlayer + " is the winner";
          }
          return "No Winner";
      }
      private boolean isWin() {
          int playerTotal = lastPlayer * SIZE;
          for (int i = 0; i < SIZE; i++) {
              // 水平方向
              if (board[0][i] + board[1][i] + board[2][i] == playerTotal) {
                  return true;
              }
              // 垂直方向
              if (board[i][0] + board[i][1] + board[i][2] == playerTotal) {
                  return true;
              }
          }
          return false;
      }
  ```

- 测试-对角线有赢家

  ```java
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
  ```

- 实现-对角线有赢家

  ```java
      private boolean isWin() {
          int playerTotal = lastPlayer * SIZE;
          for (int i = 0; i < SIZE; i++) {
              // 水平方向
              if (board[0][i] + board[1][i] + board[2][i] == playerTotal) {
                  return true;
              }
              // 垂直方向
              if (board[i][0] + board[i][1] + board[i][2] == playerTotal) {
                  return true;
              }
  
              // 第一条对角线
              if (board[0][0] + board[1][1] + board[2][2] == playerTotal) {
                  return true;
              }
          }
          return false;
      }
  ```

- 测试-对角线2有赢家

  ```java
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
  ```

- 实现-对角线2有赢家

  ```java
      private boolean isWin() {
          int playerTotal = lastPlayer * SIZE;
          for (int i = 0; i < SIZE; i++) {
              // 水平方向
              if (board[0][i] + board[1][i] + board[2][i] == playerTotal) {
                  return true;
              }
              // 垂直方向
              if (board[i][0] + board[i][1] + board[i][2] == playerTotal) {
                  return true;
              }
  
              // 第一条对角线
              if (board[0][0] + board[1][1] + board[2][2] == playerTotal) {
                  return true;
              }
  
              // 第二条对角线
              if (board[0][2] + board[1][1] + board[2][0] == playerTotal) {
                  return true;
              }
          }
          return false;
      }
  ```

- 重构

  ```java
      private boolean isWin() {
          int playerTotal = lastPlayer * SIZE;
          char diagonal1 = '\0';
          char diagonal2 = '\0';
          for (int i = 0; i < SIZE; i++) {
              diagonal1 += board[i][i];
              diagonal2 += board[i][SIZE - i - 1];
              if (board[0][i] + board[1][i] + board[2][i] == playerTotal) {
                  return true;
              } else if (board[i][2] + board[i][1] + board[i][0] == playerTotal) {
                  return true;
              }
          }
          if (diagonal1 == playerTotal || diagonal2 == playerTotal) {
              return true;
          }
          return false;
      }
  ```

### 需求4

- 所有各自被占满则为平局

- 测试

  ```java
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
  ```

- 实现

  ```java
      public String play(int x, int y) {
          checkAxis(x);
          checkAxis(y);
          lastPlayer = nextPlayer();
          setBox(x,y, lastPlayer);
          if (isWin()) {
              return lastPlayer + " is the winner";
          } else if (isDraw()) {
              return "The result is draw";
          } else {
              return "No Winner";
          }
      }
  
      private boolean isDraw() {
          for (int x = 0; x < SIZE; x++) {
              for (int y = 0; y < SIZE; y++) {
                  if (board[x][y] == '\0') {
                      return false;
                  }
              }
          }
          return true;
      }
  ```

- 重构

  ```java
      public String play(int x, int y) {
          checkAxis(x);
          checkAxis(y);
          lastPlayer = nextPlayer();
          setBox(x,y, lastPlayer);
          if (isWin(x, y)) {
              return lastPlayer + " is the winner";
          } else if (isDraw()) {
              return "The result is draw";
          } else {
              return "No Winner";
          }
      }
      
      private boolean isWin(int x, int y) {
          int playerTotal = lastPlayer * SIZE;
          char horizontal, vertical, diagonal1, diagonal2;
          horizontal = vertical = diagonal1 = diagonal2 = '\0';
          for (int i = 0; i < SIZE; i++) {
              horizontal += board[i][y - 1];
              vertical += board[x - 1][i];
              diagonal1 += board[i][i];
              diagonal2 += board[i][SIZE - i - 1];
          }
          if (horizontal == playerTotal
                  || vertical == playerTotal
                  || diagonal1 == playerTotal
                  || diagonal2 == playerTotal) {
              return true;
          }
          return false;
      }
  ```
