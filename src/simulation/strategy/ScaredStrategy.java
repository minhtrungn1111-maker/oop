package simulation.strategy;

import simulation.model.*;

public class ScaredStrategy implements SurvivalStrategy {
    private final double fearRange;
    private final SurvivalStrategy fallback;

    public ScaredStrategy(double fearRange) {
        this.fearRange = fearRange;
        this.fallback = new PassiveStrategy();
    }

    @Override
    public void act(Animal self, World world, double dt) {
        Entity threat = findNearestThreat(self, world);
        if (threat == null) {
            fallback.act(self, world, dt);
            return;
        }
        double dx = self.getX() - threat.getX();
        double dy = self.getY() - threat.getY();
        self.direction = Math.atan2(dy, dx);
    }

    private Entity findNearestThreat(Animal self, World world) {
        Entity nearest = null;
        double bestDist = fearRange;
        for (Entity e : world.getEntities()) {
            if (!e.isAlive() || e == self) {
                continue;
            }
            if (!(e instanceof Carnivore)) {
                continue;
            }
            double dx = e.getX() - self.getX();
            double dy = e.getY() - self.getY();
            double d = Math.sqrt(dx * dx + dy * dy);
            if (d < bestDist) {
                bestDist = d;
                nearest = e;
            }
        }
        return nearest;
    }
}
