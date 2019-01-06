package com.packtpublishing.tddjava.ch04ship;

import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

@Test
public class ShipSpec {
    private Ship ship;
    private Location location;
    private Planet planet;
    private List<Point> obsPoints;

    @BeforeMethod
    public void beforeTest() {
        location = new Location(new Point(21,13), Direction.NORTH);
//        ship = new Ship(location);
        Point max = new Point(50,50);
//        planet = new Planet(max);
        obsPoints = new ArrayList<>();
        obsPoints.add(new Point(20,13));
        obsPoints.add(new Point(23,13));
        planet = new Planet(max, obsPoints);
        ship = new Ship(location, planet, obsPoints);
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

    public void overpassEastBoundary() {
        location.setDirection(Direction.EAST);
        location.getPoint().setX(planet.getMax().getX());
        ship.recieveCommands("f");
        assertEquals(location.getX(), 1);
    }

    public void overpassEastBoundaryBackward() {
        location.setDirection(Direction.WEST);
        location.getPoint().setX(planet.getMax().getX());
        ship.recieveCommands("b");
        assertEquals(location.getX(), 1);
    }


    public void overpassSouthBoundary() {
        location.setDirection(Direction.SOUTH);
        location.getPoint().setY(planet.getMax().getY());
        ship.recieveCommands("f");
        assertEquals(location.getY(), 1);
    }
    public void overpassSouthBoundaryBackward() {
        location.setDirection(Direction.NORTH);
        location.getPoint().setY(planet.getMax().getY());
        ship.recieveCommands("b");
        assertEquals(location.getY(), 1);
    }

    public void leftForwardThenObsAndBackward() {
        String fb = ship.recieveCommands("lfbblf");
        assertEquals(fb, "XOXO");
    }
}
