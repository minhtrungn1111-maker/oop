package simulation.strategy;

import simulation.model.*;

public class AggressiveStrategy implements SurvivalStrategy {
    private final SurvivalStrategy fallback = new PassiveStrategy();

    @Override
    public void act(Animal self, World world, double dt) {
        Entity food = findNearestFood(self, world);
        if (food == null) {
            fallback.act(self, world, dt);
            return;
        }
        double dx = food.getX() - self.getX();
        double dy = food.getY() - self.getY();
        self.direction = Math.atan2(dy, dx);
    }

    private Entity findNearestFood(Animal self, World world) {
        Entity nearest = null;
        double bestDist = Double.MAX_VALUE;
        for (Entity e : world.getEntities()) {
            if (e == self || !e.isAlive()) {
                continue;
            }
            if (!self.canEat(e)) {
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
