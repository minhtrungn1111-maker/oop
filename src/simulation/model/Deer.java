package simulation.model;

import simulation.strategy.*;

public class Deer extends Herbivore {
    public Deer(double x, double y) {
        super(x, y, 40);
        strategy = new ScaredStrategy(180);
    }

    @Override
    protected Herbivore createChild(double x, double y) {
        return new Deer(x, y);
    }
}
