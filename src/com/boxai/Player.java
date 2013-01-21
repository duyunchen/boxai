package com.boxai;

public abstract class Player implements Entity {
    private int x, y;
    private int tileIndex;
    private double enemyDistance;

    public Player() {
        this.x = 0;
        this.y = 0;
        this.tileIndex = 0;
    }

    public Player(int x, int y, int tileIndex) {
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
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    @Override
    public int getTileIndex() {
        return this.tileIndex;
    }

    public double getEnemyDistance() {
        return this.enemyDistance;
    }

    @Override
    public void update(double enemyDistance) {
        this.enemyDistance = enemyDistance;
        Action action = this.move();
        switch (action) {
        case NORTH:
            this.y++;
            break;
        case SOUTH:
            this.y--;
            break;
        case EAST:
            this.x++;
            break;
        case WEST:
            this.x--;
            break;
        case NORTHEAST:
            this.x++;
            this.y++;
            break;
        case NORTHWEST:
            this.x--;
            this.y++;
            break;
        case SOUTHEAST:
            this.x++;
            this.y--;
            break;
        case SOUTHWEST:
            this.x--;
            this.y--;
            break;
        }
        this.checkBounds();
    }

    /**
     * check if out of bounds
     */
    private void checkBounds() {
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

    public abstract Action move();

    protected Action getOpposite(Action action) {
        switch (action) {
        case NORTH:
            return Action.SOUTH;
        case SOUTH:
            return Action.NORTH;
        case EAST:
            return Action.WEST;
        case WEST:
            return Action.EAST;
        case NORTHEAST:
            return Action.SOUTHWEST;
        case NORTHWEST:
            return Action.SOUTHEAST;
        case SOUTHEAST:
            return Action.NORTHWEST;
        case SOUTHWEST:
            return Action.NORTHEAST;
        default:
            return Action.WAIT;
        }
    }

    protected Action getRandomAction() {
        int rand = random(0, 7);
        switch (rand) {
        case 0:
            return Action.SOUTH;
        case 1:
            return Action.NORTH;
        case 2:
            return Action.WEST;
        case 3:
            return Action.EAST;
        case 4:
            return Action.SOUTHWEST;
        case 5:
            return Action.SOUTHEAST;
        case 6:
            return Action.NORTHWEST;
        case 7:
            return Action.NORTHEAST;
        default:
            return Action.WAIT;
        }
    }

    protected final int random(int low, int high) {
        return (int) Math.floor((high - low + 1) * Math.random() + low);
    }
}
