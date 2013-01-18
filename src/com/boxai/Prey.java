package com.boxai;

public class Prey implements Entity {
    private int x, y;
    private int tileIndex;

    public Prey(int x, int y, int tileIndex) {
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
        move();
        // check if out of bounds
        if (this.x > BoardView.BOARD_WIDTH - 1) {
            this.x = BoardView.BOARD_WIDTH - 1;
        } else if (this.x < 0) {
            this.x = 0;
        }

        if (this.y > BoardView.BOARD_HEIGHT - 1) {
            this.y = BoardView.BOARD_HEIGHT - 1;
        } else if (this.y < 0) {
            this.y = 0;
        }
    }

    public void move() {
        x++;
    }

}
