package simulation.model;

import simulation.strategy.*;

public class Elephant extends ApexEntity {
    public Elephant(double x, double y) {
        super(x, y, 30);
        strategy = new ApexStrategy();
    }
}
