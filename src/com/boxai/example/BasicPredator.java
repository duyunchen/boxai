package com.boxai.example;

import com.boxai.Action;
import com.boxai.Predator;

public class BasicPredator extends Predator {

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
