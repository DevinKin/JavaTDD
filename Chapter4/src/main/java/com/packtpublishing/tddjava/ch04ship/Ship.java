package com.packtpublishing.tddjava.ch04ship;

import java.util.List;

public class Ship {
    private final Location location;
    private Planet planet;
    private List<Point> obsPoints;

    public Planet getPlanet() {
        return planet;
    }

    public Ship(Location location, Planet planet, List<Point> obsPoints) {
        this.location = location;
        this.planet = planet;
        this.obsPoints = obsPoints;
    }


    public Location getLocation() {
        return location;
    }

//    public Ship(Location location) {
//        this.location = location;
//    }

    public boolean moveForward() {
//        return location.forward();
        return location.forward(planet.getMax(), obsPoints);
    }

    public boolean moveBackward() {
//        return location.backward();
        return location.backward(planet.getMax(), obsPoints);
    }

    public void turnLeft() {
        location.turnLeft();
    }

    public void turnRight() {
        location.turnRight();
    }

    public String recieveCommands(String commands) {

//        if (command.charAt(0) == 'f') {
//            moveForward();
//        }
        StringBuilder sb = new StringBuilder();
        for (char command : commands.toCharArray()) {
            switch (command) {
                case 'f':
                    if (moveForward()) {
                        sb.append("O");
                    } else {
                        sb.append("X");
                    }
                    break;
                case 'b':
                    if (moveBackward()) {
                        sb.append("O");
                    } else {
                        sb.append("X");
                    }
                    break;
                case 'l':
                    turnLeft();
                    break;
                case 'r':
                    turnRight();
                    break;
            }
        }
        return sb.toString();
    }
}
