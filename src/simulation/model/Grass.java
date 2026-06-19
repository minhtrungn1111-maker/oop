package simulation.model;

public class Grass extends Plant {
    public Grass(double x, double y) {
        super(x, y);
    }

    @Override
    protected Plant createChild(double x, double y) {
        return new Grass(x, y);
    }
}
