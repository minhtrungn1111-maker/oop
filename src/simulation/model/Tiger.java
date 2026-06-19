package simulation.model;

import simulation.strategy.*;

public class Tiger extends Carnivore {
    public Tiger(double x, double y) {
        super(x, y, 65);
        strategy = new HunterStrategy(250);
    }

    @Override
    protected Carnivore createChild(double x, double y) {
        return new Tiger(x, y);
    }
}
