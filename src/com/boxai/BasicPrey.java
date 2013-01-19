package com.boxai;

public class BasicPrey extends Prey {

    public BasicPrey(int x, int y, int tileIndex) {
        super(x, y, tileIndex);
    }

    @Override
    public Action move() {
        Action action = this.getPreviousAction();

        float distance = this.getEnemyDistance();

        float prevDistance = this.getPrevEnemyDistance();

        if (prevDistance < 0.0f)
            return this.getRandomAction();
        else if (prevDistance <= distance)
            return this.getRandomAction();
        else
            return action;
    }
}
