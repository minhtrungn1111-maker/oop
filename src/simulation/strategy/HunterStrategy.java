package simulation.strategy;

import simulation.model.*;

public class HunterStrategy implements SurvivalStrategy {
    private final double sightRange;

    public HunterStrategy(double sightRange) {
        this.sightRange = sightRange;
    }

    @Override

    public void act(Animal self, World world, double dt) {
        
        Season currentSeason = world.getSeason();

      
        if (currentSeason == Season.DROUGHT) {
            
            double[] nearestWaterPos = world.findNearestWaterTile(self.getX(), self.getY());
            
            if (nearestWaterPos != null) {
              
                double waterX = nearestWaterPos[0];
                double waterY = nearestWaterPos[1];
                
                double dwx = waterX - self.getX();
                double dwy = waterY - self.getY();
                double distToWater = Math.sqrt(dwx * dwx + dwy * dwy);
                
       
                if (distToWater > 100) {
                    self.direction = Math.atan2(dwy, dwx);
                    self.sprinting = false;
                    return; 
                } 
               
                else {
                    Entity prey = findNearestPrey(self, world);
                    if (prey != null) {
                        double dx = prey.getX() - self.getX();
                        double dy = prey.getY() - self.getY();
                        double distToPrey = Math.sqrt(dx * dx + dy * dy);
                        
                       
                        if (distToPrey < 150) {
                            self.direction = Math.atan2(dy, dx);
                            self.sprinting = true; 
                        }
                    }
                    return; 
                }
            }
        }

       
        Entity preyTarget = findNearestPrey(self, world);
        
       
        if (preyTarget == null) {
            return;
        }
        
       
        double dx = preyTarget.getX() - self.getX();
        double dy = preyTarget.getY() - self.getY();
        self.direction = Math.atan2(dy, dx);
    
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 100) {
            self.sprinting = true;
        }
    }
    private Entity findNearestPrey(Animal self, World world) {
        Entity nearest = null;
        double bestDist = sightRange;
        for (Entity e : world.getEntities()) {
            if (e == self || !e.isAlive()) {
                continue;
            }
            if (!(e instanceof Herbivore)) {
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
