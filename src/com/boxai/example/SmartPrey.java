package com.boxai.example;

import java.util.ArrayList;

import android.graphics.Point;

import com.boxai.Action;
import com.boxai.BoardView;
import com.boxai.Prey;

public class SmartPrey extends Prey {
    Integer prevX = null;
    Integer prevY = null;
    Action prevAction = null;
    Double prevDistance = null;
    Point prevEnemyLocation = null;
    int boardWidth;
    int boardHeight;

    @Override
    public Action move() {
        Action result;
        double distance = this.getEnemyDistance();
        boardWidth = BoardView.BOARD_WIDTH;
        boardHeight = BoardView.BOARD_HEIGHT;

        if (prevAction == null) {
            result = this.getRandomAction();
        } else {

            Point current = new Point(this.getX(), this.getY());
            Point prev = new Point(this.prevX, this.prevY);
            Point enemyPredicted = this.predictEnemy(current, prev, distance, prevDistance);
            this.prevEnemyLocation = enemyPredicted;
            Point target = this.computeTarget(current, enemyPredicted);
            result = getAction(current, target);
        }
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevAction = result;
        this.prevDistance = distance;
        int crazyFactor = this.random(0, 100);

        if (crazyFactor >= 100) {
            return this.getRandomAction();
        } else {
            return result;
        }
    }

    private Point computeTarget(Point current, Point enemyPredicted) {
        final double WALL_TOLERANCE = (boardWidth + boardHeight) / 15.0;
        final double WALL_WEIGHT = 15.0;

        final double CORNER_TOLERANCE = (boardWidth + boardHeight) / 10.0;
        final double CORNER_WEIGHT = 30.0;

        final double ENEMY_WEIGHT = 10.0;

        Point leftWall = new Point(0, current.y);
        Point rightWall = new Point(boardWidth - 1, current.y);
        Point topWall = new Point(current.x, 0);
        Point bottomWall = new Point(current.x, boardHeight - 1);
        Point topLeftCorner = new Point(0, 0);
        Point topRightCorner = new Point(boardWidth - 1, 0);
        Point bottomLeftCorner = new Point(0, boardHeight - 1);
        Point bottomRightCorner = new Point(boardWidth - 1, boardHeight - 1);
        Vector[] wallForces = { new Vector(leftWall, current), new Vector(rightWall, current), new Vector(topWall, current), new Vector(bottomWall, current), };
        Vector[] cornerForces = { new Vector(topLeftCorner, current), new Vector(topRightCorner, current), new Vector(bottomLeftCorner, current),
                new Vector(bottomRightCorner, current) };
        Vector enemyForce = new Vector(enemyPredicted, current);
        Vector resultant = new Vector();

        enemyForce.mult(ENEMY_WEIGHT / enemyForce.length());
        resultant.add(enemyForce);

        for (Vector force : wallForces) {
            double len = force.length();
            if (len > 0 && len < WALL_TOLERANCE) {
                force.mult(WALL_WEIGHT / len);
                resultant.add(force);
            }
        }

        for (Vector force : cornerForces) {
            double len = force.length();
            if (len > 0 && len < CORNER_TOLERANCE) {
                force.mult(CORNER_WEIGHT / len);
                resultant.add(force);
            }
        }

        Point target = new Point((int) (current.x + resultant.x), (int) (current.y + resultant.y));

        /*
         * ArrayList<Point> candidates = new ArrayList<Point>();
         * 
         * for (int dx = -1; dx <= 1; dx++) { for (int dy = -1; dy <= 1; dy++) {
         * int newX = current.x + dx; int newY = current.y + dy; if (newX >= 0
         * && newY >= 0 && newX < BoardView.BOARD_WIDTH && newY <
         * BoardView.BOARD_HEIGHT) { Point candidate = new Point(current.x + dx,
         * current.y + dy); candidates.add(candidate); } } }
         * 
         * double largestDist = Double.NEGATIVE_INFINITY; Point target =
         * current;
         * 
         * for (Point candidate : candidates) { double dist =
         * getDistance(candidate, enemyPredicted); if (dist >= largestDist) {
         * largestDist = dist; target = candidate; } }
         */
        return target;
    }

    private Action getAction(Point start, Point end) {
        int dx = end.x - start.x;
        int dy = end.y - start.y;

        if (dx == 0 && dy == 0) {
            return Action.WAIT;
        } else if (dx == 0) {
            if (dy > 0) {
                return Action.NORTH;
            } else {
                return Action.SOUTH;
            }
        } else if (dy == 0) {
            if (dx > 0) {
                return Action.EAST;
            } else {
                return Action.WEST;
            }
        } else if (dx > 0) {
            if (dy > 0) {
                return Action.NORTHEAST;
            } else {
                return Action.SOUTHEAST;
            }
        } else if (dx < 0) {
            if (dy > 0) {
                return Action.NORTHWEST;
            } else {
                return Action.SOUTHWEST;
            }
        } else
            return Action.WAIT;
    }

    private Point predictEnemy(Point current, Point prev, double distance, double prevDistance) {
        ArrayList<Point> currentCandidates = new ArrayList<Point>();
        ArrayList<Point> prevCandidates = new ArrayList<Point>();

        for (int x = 0; x < BoardView.BOARD_WIDTH; x++) {
            for (int y = 0; y < BoardView.BOARD_HEIGHT; y++) {
                Point potential = new Point(x, y);
                double d = getDistance(current, potential);
                double prev_d = getDistance(prev, potential);

                if (compareDouble(distance, d)) {
                    currentCandidates.add(potential);
                }
                if (compareDouble(prevDistance, prev_d)) {
                    prevCandidates.add(potential);
                }
            }
        }

        ArrayList<Point> potentials = new ArrayList<Point>();

        for (Point p : currentCandidates) {
            for (Point pp : prevCandidates) {
                if (this.nearby(p, pp)) {
                    potentials.add(p);
                }
            }
        }

        if (potentials.size() > 0) {
            if (prevEnemyLocation == null) {
                return potentials.get(this.random(0, potentials.size() - 1));
            }
            for (Point p : potentials) {
                if (nearby(p, prevEnemyLocation)) {
                    return p;
                }
            }
            return potentials.get(this.random(0, potentials.size() - 1));
        } else if (currentCandidates.size() > 0) {
            return currentCandidates.get(this.random(0, currentCandidates.size() - 1));
        } else
            return current;
    }

    private double getDistance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private boolean compareDouble(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

    private boolean nearby(Point a, Point b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);

        return dx <= 1 && dy <= 1;
    }

    class Vector {
        double x, y;

        public Vector() {
            this.x = 0;
            this.y = 0;

        }

        public Vector(Point start, Point end) {
            this.x = end.x - start.x;
            this.y = end.y - start.y;
        }

        public void add(Vector that) {
            this.x += that.x;
            this.y += that.y;
        }

        public void mult(double d) {
            this.x *= d;
            this.y *= d;
        }

        public double length() {
            return Math.sqrt(x * x + y * y);
        }
    }
}
