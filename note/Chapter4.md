# 第4章-单元测试

## 单元测试

### 何为单元测试

- 单元测试(UT)是一种实现，要求我们对每个隔离的小型代码单元进行测试。

### 代码重构

- 代码重构指的是对既有代码的结构进行修改，同时不改变其外部行为。

### 为何不只使用单元测试

- 单元测试旨在对小型功能单元进行检查。旨在检查代码内部的质量。
- 功能测试和验收测试的职责是核实整个应用程序像预期的那样工作。用于确保整个系统在客户和用户看来能够正常工作。
- 集成测试旨在合适各个单元，模块，应用程序乃至整改系统被妥善集成在一起。

## TestNG

### 注解@Test

- TestNG允许在类级使用这个注解，类上注解`@Test`指定该类中所有共有方法都被是为测试。

### 其余注解

- TestNG可使用XML配置将测试编组为套件。
- 使用`@BeforeSuite`和`@AfterSuite`注解的方法分别在指定套件中的所有测试运行之前和运行之后运行。
- 使用`@BeforeTest`和`@AfterTest`注解的方法分别在测试类中的每个测试运行之前和之后运行。
- TestNG测试还可以组织为编组，使用注解`@BeforeGroups`和`@AfterGroups`让你能够在指定编组中的所有测试运行之前和之后运行某些方法。
- 使用`@BeforeClass`和`@AfterClass`注解的方法分别在当前类中的所有测试运行之前和运行之后运行。唯一和JUnit的差别是，TestNG不要求这些方法是静态的。
- 使用`@BeforeMethod`和`@AfterMethod`注解的方法将在每个测试运行之前和之后运行。
- JUnit使用独立注解`@Ignore`禁用测试，TestNG使用注解`@Test(enable = false)`禁用注解。

## 开发“遥控军舰"

### 需求1

- 给定军舰的起始位置（x，y）以及它面向的方向（N，S，E，或W）

#### 规范

```java
package com.packtpublishing.tddjava.ch04ship;

import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test
public class ShipSpec {
    public void whenInstantiaedThenLocationIsSet() {
        Location location = new Location(new Point(21,13), Direction.NONE);
        Ship ship = new Ship(location);
        assertEquals(ship.getLocation(), location);
    }
}
```

#### 实现

```java
package com.packtpublishing.tddjava.ch04ship;

public class Ship {
    private final Location location;

    public Location getLocation() {
        return location;
    }

    public Ship(Location location) {
        this.location = location;
    }
}
```

#### 重构

```java
package com.packtpublishing.tddjava.ch04ship;

import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test
public class ShipSpec {
    private Ship ship;
    private Location location;
    
    @BeforeMethod
    public void beforeTest() {
        Location location = new Location(new Point(21,13), Direction.NORTH);
        ship = new Ship(location);
    }
    
    public void whenInstantiaedThenLocationIsSet() {
//        Location location = new Location(new Point(21,13), Direction.NONE);
//        Ship ship = new Ship(location);
        assertEquals(ship.getLocation(), location);
    }
}
```

### 需求2

- 实现军舰前进和后退的命令（f和b）

#### 规范

```java
    public void whenMoveForwardThenForward() {
        Location expected = location.copy();
        expected.forward();
        ship.moveForward();
        assertEquals(ship.getLocation(), expected);
    }
    public void whenMoveBackwardThenBackward() {
        Location expected = location.copy();
        expected.backward();
        ship.moveBackward();
        assertEquals(ship.getLocation(), expected);
    }
```

#### 实现

```java
    public boolean moveForward() {
        return location.forward();
    }
    public boolean moveBackward() {
        return location.backward();
    }
```

### 需求3

- 实现让军舰左转和右转的命令（l和r）

#### 规范

```java
    public void whenTurnLeftThenLeft() {
        Location expected = location.copy();
        expected.turnLeft();
        ship.turnLeft();
        assertEquals(ship.getLocation(), expected);
    }
    public void whenTurnRightThenRight() {
        Location expected = location.copy();
        expected.turnRight();
        ship.turnRight();
        assertEquals(ship.getLocation(), expected);
    }
```

#### 实现

```java
public void turnLeft() {
    location.turnLeft();
}
public void turnRight() {
    location.turnRight();
}
```

### 需求4

- 军舰可以接收一个包含命令的字符串（例如，lrfb相当于左转，右转，前进，再后退）。

#### 规范1

```java
    public void whenReciveCommandsFThenForward() {
        Location expected = location.copy();
        expected.forward();
        ship.recieveCommands("f");
        assertEquals(ship.getLocation(), expected);
    }
```

#### 实现1

```java
    public void recieveCommands(String command) {
        if (command.charAt(0) == 'f') {
            moveForward();
        }
    }
```

#### 规范2

```java
    public void whenReciveCommandsThenAllAreExecuted() {
        Location expected = location.copy();
        expected.turnRight();
        expected.forward();
        expected.turnLeft();
        expected.backward();
        ship.recieveCommands("rflb");
        assertEquals(ship.getLocation(), expected);
    }
```

#### 实现2

```java
    public void recieveCommands(String commands) {
//        if (command.charAt(0) == 'f') {
//            moveForward();
//        }
        for (char command : commands.toCharArray()) {
            switch (command) {
                case 'f':
                    moveForward();
                    break;
                case 'b':
                    moveBackward();
                    break;
                case 'l':
                    turnLeft();
                    break;
                case 'r':
                    turnRight();
                    break;
            }
        }
    }
```

### 需求5

- 实现从网格的一边转到另一边。

#### 规范1

```java
    public void whenInstantiatedThenPlanetIsStored() {
        Point max = new Point(50,50);
        Planet planet = new Planet(max);
        ship = new Ship(location, planet);
        assertEquals(ship.getPlanet(), planet);
    }
```

#### 实现2

```java
    private final Location location;

    public Planet getPlanet() {
        return planet;
    }
	public Ship(Location location, Planet planet) {
        this.location = location;
        this.planet = planet;
    }
```

#### 重构

- ShipSpec

  ```java
  package com.packtpublishing.tddjava.ch04ship;
  
  import org.testng.annotations.*;
  
  import static org.testng.Assert.*;
  
  @Test
  public class ShipSpec {
      private Ship ship;
      private Location location;
      private Planet planet;
  
      @BeforeMethod
      public void beforeTest() {
          location = new Location(new Point(21,13), Direction.NORTH);
  //        ship = new Ship(location);
          Point max = new Point(50,50);
          Planet planet = new Planet(max);
          ship = new Ship(location, planet);
      }
  
      public void whenInstantiaedThenLocationIsSet() {
  //        Location location = new Location(new Point(21,13), Direction.NONE);
  //        Ship ship = new Ship(location);
          assertEquals(ship.getLocation(), location);
      }
  
      public void whenMoveForwardThenForward() {
          Location expected = location.copy();
          expected.forward();
          ship.moveForward();
          assertEquals(ship.getLocation(), expected);
      }
  
      public void whenMoveBackwardThenBackward() {
          Location expected = location.copy();
          expected.backward();
          ship.moveBackward();
          assertEquals(ship.getLocation(), expected);
      }
  
      public void whenTurnLeftThenLeft() {
          Location expected = location.copy();
          expected.turnLeft();
          ship.turnLeft();
          assertEquals(ship.getLocation(), expected);
      }
  
      public void whenTurnRightThenRight() {
          Location expected = location.copy();
          expected.turnRight();
          ship.turnRight();
          assertEquals(ship.getLocation(), expected);
      }
  
      public void whenReciveCommandsFThenForward() {
          Location expected = location.copy();
          expected.forward();
          ship.recieveCommands("f");
          assertEquals(ship.getLocation(), expected);
      }
  
      public void whenReciveCommandsThenAllAreExecuted() {
          Location expected = location.copy();
          expected.turnRight();
          expected.forward();
          expected.turnLeft();
          expected.backward();
          ship.recieveCommands("rflb");
          assertEquals(ship.getLocation(), expected);
      }
  
      public void whenInstantiatedThenPlanetIsStored() {
  //        Point max = new Point(50,50);
  //        Planet planet = new Planet(max);
  //        ship = new Ship(location, planet);
          assertEquals(ship.getPlanet(), planet);
      }
  }
  ```

- Ship

  ```java
  package com.packtpublishing.tddjava.ch04ship;
  
  public class Ship {
      private final Location location;
  
      public Planet getPlanet() {
          return planet;
      }
  
      public Ship(Location location, Planet planet) {
          this.location = location;
          this.planet = planet;
      }
  
      private Planet planet;
  
      public Location getLocation() {
          return location;
      }
  
  //    public Ship(Location location) {
  //        this.location = location;
  //    }
  
      public boolean moveForward() {
          return location.forward();
      }
  
      public boolean moveBackward() {
          return location.backward();
      }
  
      public void turnLeft() {
          location.turnLeft();
      }
  
      public void turnRight() {
          location.turnRight();
      }
  
      public void recieveCommands(String commands) {
  
  //        if (command.charAt(0) == 'f') {
  //            moveForward();
  //        }
          for (char command : commands.toCharArray()) {
              switch (command) {
                  case 'f':
                      moveForward();
                      break;
                  case 'b':
                      moveBackward();
                      break;
                  case 'l':
                      turnLeft();
                      break;
                  case 'r':
                      turnRight();
                      break;
              }
          }
      }
  }
  ```

#### 规范2

```java
    public void overpassEastBoundary() {
        location.setDirection(Direction.EAST);
        location.getPoint().setX(planet.getMax().getX());
        ship.recieveCommands("f");
        assertEquals(location.getX(), 1);
    }
```

#### 实现2

```java
    public boolean moveForward() {
//        return location.forward();
        return location.forward(planet.getMax());
    }
```

### 需求6

- 每次移动前都进行障碍检测。如果执行指定的命令将遇到障碍，军舰应该放弃移动，留在原地并报告遇到的障碍。


