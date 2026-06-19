package simulation.model;

public abstract class Entity {
    protected double x;
    protected double y;
    protected boolean alive = true;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update(double dt, World world);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }
}
