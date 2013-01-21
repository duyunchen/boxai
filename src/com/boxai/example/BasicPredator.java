package com.boxai.example;

import com.boxai.Action;
import com.boxai.Predator;

public class BasicPredator extends Predator {
    Action prevAction = null;
    Double prevDistance = null;

    @Override
    public Action move() {
        double distance = this.getEnemyDistance();

        Action result;
        if (prevAction == null) {
            result = this.getRandomAction();
        } else if (prevDistance <= distance)
            result = this.getRandomAction();
        else
            result = prevAction;

        prevDistance = distance;
        prevAction = result;

        return result;
    }

}
