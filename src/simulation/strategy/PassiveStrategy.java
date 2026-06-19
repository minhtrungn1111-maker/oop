package simulation.strategy;

import simulation.model.*;

public class PassiveStrategy implements SurvivalStrategy {
    private double directionTimer = 0;

    @Override
    public void act(Animal self, World world, double dt) {
        directionTimer += dt;
        if (directionTimer > 2.0) {
            directionTimer = 0;
            self.direction = Math.random() * Math.PI * 2;
        }
    }
}
