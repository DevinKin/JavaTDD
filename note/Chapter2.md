# 第二章-工具，框架和环境

## 单元测试框架

### JUnit

- `@BeforeClass`注解指定，方法只在执行类中的测试方法前执行一次。
- `@Before`注解指定，方法将在每个测试方法前运行。
- `@AfterClass`注解指定，方法在执行类中所有的测试方法结束后执行一次。
- `@After`注解指定，方法在每个测试方法结束后都执行。

### TestNG

- `@BeforeClass`注解指定，方法只在执行类中的测试方法前执行一次。
- `@BeforeMethod`注解指定，方法将在每个测试方法前运行。
- `@AfterClass`注解指定，方法在执行类中所有的测试方法结束后执行一次。
- `@AfterMethod`注解指定，方法在每个测试方法结束后都执行。
- TestNG使用同一个测试类实例执行所有测试方法，测试方法默认不是彼此隔离的。
- TestNG中，可在类层级设置所有共有方法都转换为测试。

## Hamcrest和AssertJ

### Hamcrest

- Hamcrest添加了大量被称为“配置器”的方法，每个配置器都设计用于执行特定的比较操作。
- Hamcrest提供的断言非常多，简化了代码，表达能力更强。

### AssertJ

- AssertJ断言可以串接。

## 代码覆盖率工具

### JaCoCo

- gradle添加JaCoCo插件：`apply plugin: 'jacoco'`

- Java Code Coverage：使用`gradle test jacocoTestReport`，生成html文件。

## 行为驱动开发

- 行为驱动开发(BDD)是一种敏捷过程，旨在整个项目开发过程中都专注于相关方的利益。