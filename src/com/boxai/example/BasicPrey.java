package com.boxai.example;

import com.boxai.Action;
import com.boxai.Prey;

public class BasicPrey extends Prey {
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
