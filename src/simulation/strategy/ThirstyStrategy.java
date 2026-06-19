package simulation.strategy;

import simulation.model.*;

public class ThirstyStrategy implements SurvivalStrategy {
    @Override
    public void act(Animal self, World world, double dt) {
        double[] target = world.findNearestWaterTile(self.getX(), self.getY());
        if (target == null) {
            return;
        }
        double dx = target[0] - self.getX();
        double dy = target[1] - self.getY();
        self.direction = Math.atan2(dy, dx);
    }
}
