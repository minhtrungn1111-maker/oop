package simulation.model;

public class FruitTree extends Plant {
    public FruitTree(double x, double y) {
        super(x, y);
        reproduceInterval = 45.0;
        reproduceChance = 0.20;
    }

    @Override
    protected Plant createChild(double x, double y) {
        return new FruitTree(x, y);
    }
}
