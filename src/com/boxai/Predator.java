package com.boxai;

public class Predator implements Entity {
    private int x, y;
    private int tileIndex;

    public Predator(int x, int y, int tileIndex) {
        this.x = x;
        this.y = y;
        this.tileIndex = tileIndex;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getTileIndex() {
        return this.tileIndex;
    }

    @Override
    public void update() {

    }

    public void move() {

    }

}
