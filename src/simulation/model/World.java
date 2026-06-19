package simulation.model;

import simulation.event.EventBus;
import simulation.event.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private final double width;
    private final double height;
    private final int tileSize = 32;
    private final int gridW;
    private final int gridH;
    private final Terrain[][] grid;
    private List<Entity> entities = new ArrayList<>();
    private final EventBus eventBus = new EventBus();
    private Season season = Season.NORMAL;

    public EventBus getEventBus() {
        return eventBus;
    }

    public Season getSeason() {
        return season;
    }

    public void cycleSeason() {
        Season[] vals = Season.values();
        season = vals[(season.ordinal() + 1) % vals.length];
    }

    public World(double width, double height) {
        this.width = width;
        this.height = height;
        this.gridW = (int) (width / tileSize);
        this.gridH = (int) (height / tileSize);
        this.grid = new Terrain[gridW][gridH];
        generateTerrain();
    }

    private void generateTerrain() {
        Random rng = new Random();
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                grid[i][j] = Terrain.GRASS;
            }
        }

        boolean[][] water = scatterBlobs(rng, 3 + rng.nextInt(2), 6, 10, 0.70);
        water = smoothCa(water, 4);
        int maxWater = (int) (gridW * gridH * 0.35);
        if (countTrue(water) > maxWater) {
            water = smoothCa(water, 2);
        }
        overlay(water, Terrain.WATER, false);

        boolean[][] forest = scatterBlobs(rng, 5 + rng.nextInt(3), 8, 15, 0.65);
        forest = smoothCa(forest, 4);
        overlay(forest, Terrain.FOREST, true);

        boolean[][] bush = new boolean[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                bush[i][j] = rng.nextDouble() < 0.18;
            }
        }
        bush = smoothCa(bush, 3);
        overlay(bush, Terrain.BUSH, true);

        boolean[][] mud = new boolean[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                mud[i][j] = rng.nextDouble() < 0.08;
            }
        }
        mud = smoothCa(mud, 2);
        overlay(mud, Terrain.MUD, true);
    }

    private boolean[][] scatterBlobs(Random rng, int count, int minR, int maxR, double density) {
        boolean[][] g = new boolean[gridW][gridH];
        for (int k = 0; k < count; k++) {
            int margin = maxR;
            int cx = margin + rng.nextInt(Math.max(1, gridW - 2 * margin));
            int cy = margin + rng.nextInt(Math.max(1, gridH - 2 * margin));
            int r = minR + rng.nextInt(maxR - minR + 1);
            for (int i = 0; i < gridW; i++) {
                for (int j = 0; j < gridH; j++) {
                    double dx = i - cx;
                    double dy = j - cy;
                    if (dx * dx + dy * dy < r * r && rng.nextDouble() < density) {
                        g[i][j] = true;
                    }
                }
            }
        }
        return g;
    }

    private int countTrue(boolean[][] g) {
        int n = 0;
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                if (g[i][j]) n++;
            }
        }
        return n;
    }

    private boolean[][] smoothCa(boolean[][] g, int iters) {
        for (int iter = 0; iter < iters; iter++) {
            boolean[][] next = new boolean[gridW][gridH];
            for (int i = 0; i < gridW; i++) {
                for (int j = 0; j < gridH; j++) {
                    int n = neighborCount(g, i, j);
                    if (g[i][j]) {
                        next[i][j] = n >= 4;
                    } else {
                        next[i][j] = n >= 5;
                    }
                }
            }
            g = next;
        }
        return g;
    }

    private int neighborCount(boolean[][] g, int x, int y) {
        int n = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < gridW && ny >= 0 && ny < gridH && g[nx][ny]) {
                    n++;
                }
            }
        }
        return n;
    }

    private void overlay(boolean[][] g, Terrain t, boolean onlyOnGrass) {
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                if (!g[i][j]) {
                    continue;
                }
                if (onlyOnGrass && grid[i][j] != Terrain.GRASS) {
                    continue;
                }
                grid[i][j] = t;
            }
        }
    }

    public Terrain getTerrainAt(double x, double y) {
        int i = (int) (x / tileSize);
        int j = (int) (y / tileSize);
        if (i < 0 || i >= gridW || j < 0 || j >= gridH) {
            return Terrain.GRASS;
        }
        return grid[i][j];
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getGridW() {
        return gridW;
    }

    public int getGridH() {
        return gridH;
    }

    public Terrain getTile(int i, int j) {
        return grid[i][j];
    }

    public double[] findNearestWaterTile(double x, double y) {
        double bestDist = Double.MAX_VALUE;
        int bestI = -1, bestJ = -1;
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                if (grid[i][j] != Terrain.WATER) continue;
                double cx = (i + 0.5) * tileSize;
                double cy = (j + 0.5) * tileSize;
                double d = (cx - x) * (cx - x) + (cy - y) * (cy - y);
                if (d < bestDist) {
                    bestDist = d;
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        if (bestI < 0) return null;
        return new double[] { (bestI + 0.5) * tileSize, (bestJ + 0.5) * tileSize };
    }

    public void setTileAt(double x, double y, Terrain t) {
        int i = (int) (x / tileSize);
        int j = (int) (y / tileSize);
        if (i >= 0 && i < gridW && j >= 0 && j < gridH) {
            grid[i][j] = t;
        }
    }

    public void add(Entity e) {
        entities.add(e);
    }

    private double cleanupTimer = 0;
    private double birdTimer = 0;
    private double birdInterval = 10;
    private double rustleTimer = 0;

    public void tick(double dt) {
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            Entity e = entities.get(i);
            if (e.isAlive()) {
                e.update(dt, this);
                if (e.x < 0) e.x = 0;
                if (e.x > width) e.x = width;
                if (e.y < 0) e.y = 0;
                if (e.y > height) e.y = height;
            }
        }
        cleanupTimer += dt;
        if (cleanupTimer > 1.0) {
            cleanupTimer = 0;
            entities.removeIf(e -> !e.isAlive());
        }
        birdTimer += dt;
        if (birdTimer >= birdInterval) {
            birdTimer = 0;
            birdInterval = 8 + Math.random() * 8;
            eventBus.publish(EventType.BIRD_CHIRP);
        }
        rustleTimer += dt;
        if (rustleTimer > 0.7) {
            rustleTimer = 0;
            for (Entity e : entities) {
                if (!e.isAlive() || !(e instanceof Animal) || e instanceof WaterAnimal) continue;
                if (getTerrainAt(e.getX(), e.getY()) == Terrain.FOREST) {
                    eventBus.publish(EventType.LEAVES_RUSTLE);
                    break;
                }
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
