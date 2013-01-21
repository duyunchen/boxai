package com.boxai;

public interface Entity {
    int getX();

    int getY();

    void setX(int x);

    void setY(int y);

    int getTileIndex();

    void setTileIndex(int tileIndex);

    void update(double enemyDistance);
}
