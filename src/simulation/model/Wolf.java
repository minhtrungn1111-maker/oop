package simulation.model;

import simulation.strategy.*;

public class Wolf extends Carnivore {
    public Wolf(double x, double y) {
        super(x, y, 58);
        strategy = new HunterStrategy(200);
    }

    @Override
    protected Carnivore createChild(double x, double y) {
        return new Wolf(x, y);
    }
}
