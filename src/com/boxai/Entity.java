package com.boxai;

public interface Entity {
    int getX();

    int getY();

    int getTileIndex();

    void update();

    void move();
}
