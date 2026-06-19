package simulation.model;

public abstract class WaterAnimal extends Animal {
    public WaterAnimal(double x, double y, double speed) {
        super(x, y, speed);
        hungerRate = 0;
        thirstRate = 0;
    }

    @Override
    protected boolean canEnter(Terrain t) {
        return t == Terrain.WATER;
    }
}
