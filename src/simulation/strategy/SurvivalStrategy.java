package simulation.strategy;

import simulation.model.*;

public interface SurvivalStrategy {
    void act(Animal self, World world, double dt);
}
