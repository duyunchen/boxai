package com.boxai.example;

import com.boxai.Action;
import com.boxai.Predator;

public class SmartPredator extends Predator {
    @Override
    public Action move() {
        Action action = this.getPreviousAction();
        float prevDistance = this.getEnemyDistance();

        return convertDegreesToAction(0);
    }

    private Action convertDegreesToAction(float degrees) {
        return null;
    }

}
