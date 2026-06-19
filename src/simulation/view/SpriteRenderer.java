package simulation.view;

import simulation.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class SpriteRenderer implements Renderer {

    @Override
    public void render(GraphicsContext gc, World world, Camera camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.save();
        camera.apply(gc);

        int ts = world.getTileSize();
        for (int i = 0; i < world.getGridW(); i++) {
            for (int j = 0; j < world.getGridH(); j++) {
                Terrain t = world.getTile(i, j);
                Image img = getTerrainImage(t);
                if (img != null) {
                    gc.drawImage(img, i * ts, j * ts, ts, ts);
                } else {
                    gc.setFill(terrainColor(t));
                    gc.fillRect(i * ts, j * ts, ts, ts);
                }
            }
        }
        drawTerrainEdges(gc, world);

        for (Entity e : world.getEntities()) {
            if (!e.isAlive()) continue;
            drawEntity(gc, e);
        }

        gc.restore();
 

        Season currentSeason = world.getSeason();

        
        double mapWidth = 3000;  
        double mapHeight = 3000; 

        if (currentSeason == Season.DROUGHT) {
            gc.setFill(javafx.scene.paint.Color.rgb(255, 140, 0, 0.15));
            gc.fillRect(0, 0, mapWidth, mapHeight);
        } else if (currentSeason == Season.BREEDING) {
            gc.setFill(javafx.scene.paint.Color.rgb(255, 182, 193, 0.15));
            gc.fillRect(0, 0, mapWidth, mapHeight);
        }
    }

    private Image getTerrainImage(Terrain t) {
        switch (t) {
            case GRASS:
                return ImageLoader.getImage("/resources/images/co_tuoi.png");
            case FOREST:
                return ImageLoader.getImage("/resources/images/co_dam.png");
            case WATER:
                return ImageLoader.getImage("/resources/images/waterreal.png");
            case ROCK:
                return ImageLoader.getImage("/resources/images/rock.png");
            default:
                return null; // BUSH and MUD fall back to color
        }
    }

    private void drawTerrainEdges(GraphicsContext gc, World world) {
        int ts = world.getTileSize();
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.SQUARE);

        for (int i = 0; i < world.getGridW(); i++) {
            for (int j = 0; j < world.getGridH(); j++) {
                Terrain current = world.getTile(i, j);
                double x = i * ts;
                double y = j * ts;

                if (i + 1 < world.getGridW()) {
                    Terrain right = world.getTile(i + 1, j);
                    if (current != right) {
                        strokeTerrainEdge(gc, current, right);
                        gc.strokeLine(x + ts, y, x + ts, y + ts);
                    }
                }

                if (j + 1 < world.getGridH()) {
                    Terrain bottom = world.getTile(i, j + 1);
                    if (current != bottom) {
                        strokeTerrainEdge(gc, current, bottom);
                        gc.strokeLine(x, y + ts, x + ts, y + ts);
                    }
                }
            }
        }
    }

    private void strokeTerrainEdge(GraphicsContext gc, Terrain a, Terrain b) {
        if (a == Terrain.WATER || b == Terrain.WATER) {
            gc.setStroke(Color.rgb(38, 103, 141, 0.85));
            gc.setLineWidth(2.0);
        } else if (a == Terrain.ROCK || b == Terrain.ROCK) {
            gc.setStroke(Color.rgb(45, 45, 45, 0.75));
            gc.setLineWidth(1.6);
        } else if (a == Terrain.FOREST || b == Terrain.FOREST || a == Terrain.BUSH || b == Terrain.BUSH) {
            gc.setStroke(Color.rgb(35, 79, 31, 0.55));
            gc.setLineWidth(1.2);
        } else {
            gc.setStroke(Color.rgb(70, 70, 55, 0.35));
            gc.setLineWidth(1.0);
        }
    }

    private void drawEntity(GraphicsContext gc, Entity e) {
        double x = e.getX();
        double y = e.getY();

        if (e instanceof Grass) {
            gc.setFill(Color.LIMEGREEN);
            gc.fillOval(x - 4, y - 4, 8, 8);
            gc.setStroke(Color.DARKGREEN);
            gc.strokeOval(x - 4, y - 4, 8, 8);
        } else if (e instanceof FruitTree) {
            Image img = ImageLoader.getImage("/resources/images/fruit_trees.png");
            if (img != null) {
                // Crop the fruit tree (x=0, y=0, w=96, h=128)
                gc.drawImage(img, 0, 0, 96, 128, x - 24, y - 56, 48, 64);
            } else {
                gc.setFill(Color.SADDLEBROWN);
                gc.fillRect(x - 2, y, 4, 12);
                gc.setFill(Color.FORESTGREEN);
                gc.fillOval(x - 10, y - 12, 20, 20);
                gc.setFill(Color.RED);
                gc.fillOval(x - 5, y - 8, 3, 3);
                gc.fillOval(x + 2, y - 6, 3, 3);
                gc.fillOval(x - 3, y - 3, 3, 3);
            }
        } else if (e instanceof Fish) {
            Image img = ImageLoader.getImage("/resources/images/fish.png");
            if (drawSprite(gc, img, x, y, 38, 38)) return;
            gc.setFill(Color.SILVER);
            gc.fillOval(x - 5, y - 2, 10, 5);
            gc.setFill(Color.DARKGRAY);
            gc.fillPolygon(new double[] { x + 5, x + 8, x + 8 }, new double[] { y, y - 3, y + 3 }, 3);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 3, y - 1, 1.5, 1.5);
        } else if (e instanceof Duck) {
            Image img = ImageLoader.getImage("/resources/images/duck.png");
            if (drawSprite(gc, img, x, y, 40, 40)) return;
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 6, y - 4, 12, 8);
            gc.setFill(Color.YELLOW);
            gc.fillOval(x + 3, y - 6, 5, 5);
            gc.setFill(Color.ORANGE);
            gc.fillRect(x + 6, y - 5, 3, 2);
        } else if (e instanceof Rabbit) {
            Image img = ImageLoader.getImage("/resources/images/rabbit.png");
            if (drawSprite(gc, img, x, y, 28, 28)) return;
            drawAnimal(gc, x, y, 12, Color.WHITESMOKE, Color.GRAY);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 2, y - 3, 2, 2);
            gc.fillOval(x + 1, y - 3, 2, 2);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(x - 4, y - 9, 3, 6);
            gc.fillOval(x + 1, y - 9, 3, 6);
        } else if (e instanceof Deer) {
            Image img = ImageLoader.getImage("/resources/images/deer.png");
            if (drawSprite(gc, img, x, y, 44, 44)) return;
            drawAnimal(gc, x, y, 18, Color.PERU, Color.SADDLEBROWN);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 3, y - 4, 2, 2);
            gc.fillOval(x + 1, y - 4, 2, 2);
        } else if (e instanceof Wolf) {
            Image img = ImageLoader.getImage("/resources/images/wolf.png");
            if (drawSprite(gc, img, x, y, 42, 42)) return;
            drawAnimal(gc, x, y, 16, Color.INDIANRED, Color.DARKRED);
            gc.setFill(Color.YELLOW);
            gc.fillOval(x - 3, y - 3, 2, 2);
            gc.fillOval(x + 1, y - 3, 2, 2);
        } else if (e instanceof Tiger) {
            Image img = ImageLoader.getImage("/resources/images/tiger.png");
            if (drawSprite(gc, img, x, y, 42, 42)) return;
            drawAnimal(gc, x, y, 20, Color.ORANGE, Color.DARKORANGE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.5);
            gc.strokeLine(x - 8, y - 4, x + 8, y - 4);
            gc.strokeLine(x - 8, y + 2, x + 8, y + 2);
        } else if (e instanceof Elephant) {
            Image img = ImageLoader.getImage("/resources/images/elephant.png");
            if (drawSprite(gc, img, x, y, 58, 58)) return;
            drawAnimal(gc, x, y, 28, Color.LIGHTGRAY, Color.DIMGRAY);
            gc.setFill(Color.DIMGRAY);
            gc.fillRect(x - 2, y + 6, 4, 10);
        }
    }

    private boolean drawSprite(GraphicsContext gc, Image img, double x, double y, double width, double height) {
        if (img == null) return false;
        gc.drawImage(img, x - width / 2, y - height / 2, width, height);
        return true;
    }

    private void drawAnimal(GraphicsContext gc, double x, double y, double size, Color fill, Color outline) {
        gc.setFill(fill);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setStroke(outline);
        gc.setLineWidth(2);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);
    }

    private Color terrainColor(Terrain t) {
        switch (t) {
            case GRASS:
                return Color.web("#7cbc6c");
            case MUD:
                return Color.web("#6b4423");
            case FOREST:
                return Color.web("#2d5016");
            case BUSH:
                return Color.web("#3e6b1e");
            case WATER:
                return Color.web("#3498db");
            case ROCK:
                return Color.web("#7f7f7f");
            default:
                return Color.GRAY;
        }
    }
    
}
 
