package com.boxai.example;

import java.util.ArrayList;

import android.graphics.Point;

import com.boxai.Action;
import com.boxai.BoardView;
import com.boxai.Predator;

public class SmartPredator extends Predator {
    Integer prevX = null;
    Integer prevY = null;
    Action prevAction = null;
    Double prevDistance = null;
    Point prevEnemyLocation = null;

    @Override
    public Action move() {
        Action result;
        double distance = this.getEnemyDistance();

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
        return result;
    }

    private Point computeTarget(Point current, Point enemyPredicted) {
        ArrayList<Point> candidates = new ArrayList<Point>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = current.x + dx;
                int newY = current.y + dy;
                if (newX >= 0 && newY >= 0 && newX < BoardView.BOARD_WIDTH && newY < BoardView.BOARD_HEIGHT) {
                    Point candidate = new Point(current.x + dx, current.y + dy);
                    candidates.add(candidate);
                }
            }
        }

        double smallestDist = Double.POSITIVE_INFINITY;
        Point target = current;

        for (Point candidate : candidates) {
            double dist = getDistance(candidate, enemyPredicted);
            if (dist <= smallestDist) {
                smallestDist = dist;
                target = candidate;
            }
        }
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
}
