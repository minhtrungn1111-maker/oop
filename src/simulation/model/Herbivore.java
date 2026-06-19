package simulation.model;

public abstract class Herbivore extends Animal {
    protected double breedTimer = 0;
    protected double breedInterval = 15.0;

    public Herbivore(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    public boolean canEat(Entity e) {
        return e instanceof Plant;
    }

    @Override
    public void update(double dt, World world) {
        super.update(dt, world);
        if (!alive) {
            return;
        }
        Season s = world.getSeason();
        if (s == Season.DROUGHT) {
            return;
        }
        double effInterval = (s == Season.BREEDING) ? breedInterval : breedInterval * 3;
        if (hunger < 0.4) {
            breedTimer += dt;
            if (breedTimer >= effInterval) {
                breedTimer = 0;
                double ox = (Math.random() - 0.5) * 20;
                double oy = (Math.random() - 0.5) * 20;
                Herbivore child = createChild(x + ox, y + oy);
                if (child != null) {
                    world.add(child);
                }
            }
        }
    }

    protected abstract Herbivore createChild(double x, double y);
}
