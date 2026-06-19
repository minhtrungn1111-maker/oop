package simulation.model;

import simulation.strategy.*;

public class Rabbit extends Herbivore {
    public Rabbit(double x, double y) {
        super(x, y, 50);
        breedInterval = 40.0;
        strategy = new ScaredStrategy(200);
    }

    @Override
    protected Herbivore createChild(double x, double y) {
        return new Rabbit(x, y);
    }
}
