package simulation.model;

import simulation.strategy.SurvivalStrategy;
import simulation.strategy.PassiveStrategy;

public abstract class Carnivore extends Animal {
    protected double stamina = 1.0;
    protected double maxStamina = 1.0;
    protected double sprintMultiplier = 1.35;
    protected double breedTimer = 0;
    protected double breedInterval = 90.0;
    private final SurvivalStrategy passiveStrategy = new PassiveStrategy();

    public Carnivore(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    public boolean canEat(Entity e) {
        return e instanceof Herbivore;
    }

    @Override
    protected boolean canEnter(Terrain t) {
        return super.canEnter(t) && t != Terrain.BUSH;
    }

    @Override
    protected SurvivalStrategy pickBrain() {
        if (thirst > 0.7) return thirstyStrategy;
        if (hunger > 0.7) return aggressiveStrategy;
        if (hunger < 0.4) return passiveStrategy; // Only hunt when hungry
        return strategy;
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
        if (hunger < 0.3) {
            breedTimer += dt;
            if (breedTimer >= effInterval) {
                breedTimer = 0;
                double ox = (Math.random() - 0.5) * 30;
                double oy = (Math.random() - 0.5) * 30;
                Carnivore child = createChild(x + ox, y + oy);
                if (child != null) {
                    world.add(child);
                }
            }
        }
    }

    protected abstract Carnivore createChild(double x, double y);

    @Override
    protected void move(double dt, World world) {
        double sprintFactor = 1.0;
        if (sprinting && stamina > 0) {
            sprintFactor = sprintMultiplier;
            stamina = Math.max(0, stamina - dt * 0.3);
        } else {
            stamina = Math.min(maxStamina, stamina + dt * 0.2);
        }
        double terrainFactor = world.getTerrainAt(x, y).speedFactor;
        double seasonFactor = world.getSeason() == Season.DROUGHT ? 0.6 : 1.0;
        double factor = terrainFactor * seasonFactor * sprintFactor;
        double newX = x + Math.cos(direction) * speed * factor * dt;
        double newY = y + Math.sin(direction) * speed * factor * dt;
        if (canEnter(world.getTerrainAt(newX, newY))) {
            x = newX;
            y = newY;
        }
    }

    public double getStamina() {
        return stamina;
    }
}
