package simulation.view;

import simulation.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BasicRenderer implements Renderer {

    @Override
    public void render(GraphicsContext gc, World world, Camera camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.save();
        camera.apply(gc);

        int ts = world.getTileSize();
        for (int i = 0; i < world.getGridW(); i++) {
            for (int j = 0; j < world.getGridH(); j++) {
                gc.setFill(terrainColor(world.getTile(i, j)));
                gc.fillRect(i * ts, j * ts, ts, ts);
            }
        }

        for (Entity e : world.getEntities()) {
            if (!e.isAlive()) {
                continue;
            }
            drawEntity(gc, e);
        }

        gc.restore();
    }

    private void drawEntity(GraphicsContext gc, Entity e) {
        if (e instanceof Grass) {
            gc.setFill(Color.DARKGREEN);
            gc.fillRect(e.getX() - 3, e.getY() - 3, 6, 6);
        } else if (e instanceof FruitTree) {
            gc.setFill(Color.FORESTGREEN);
            gc.fillOval(e.getX() - 8, e.getY() - 8, 16, 16);
            gc.setFill(Color.RED);
            gc.fillOval(e.getX() - 3, e.getY() - 3, 3, 3);
            gc.fillOval(e.getX() + 1, e.getY() - 2, 3, 3);
        } else if (e instanceof Fish) {
            gc.setFill(Color.SILVER);
            gc.fillOval(e.getX() - 4, e.getY() - 2, 8, 4);
        } else if (e instanceof Duck) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(e.getX() - 5, e.getY() - 4, 10, 8);
        } else if (e instanceof Rabbit) {
            gc.setFill(Color.WHITE);
            gc.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
        } else if (e instanceof Deer) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillOval(e.getX() - 8, e.getY() - 8, 16, 16);
        } else if (e instanceof Wolf) {
            gc.setFill(Color.DARKRED);
            gc.fillOval(e.getX() - 7, e.getY() - 7, 14, 14);
        } else if (e instanceof Tiger) {
            gc.setFill(Color.ORANGE);
            gc.fillOval(e.getX() - 9, e.getY() - 9, 18, 18);
        } else if (e instanceof Elephant) {
            gc.setFill(Color.DARKGRAY);
            gc.fillOval(e.getX() - 12, e.getY() - 12, 24, 24);
        }
    }

    private Color terrainColor(Terrain t) {
        switch (t) {
            case GRASS:
                return Color.LIGHTGREEN;
            case MUD:
                return Color.SADDLEBROWN;
            case FOREST:
                return Color.DARKGREEN;
            case BUSH:
                return Color.DARKOLIVEGREEN;
            case WATER:
                return Color.LIGHTBLUE;
            case ROCK:
                return Color.GRAY;
            default:
                return Color.GRAY;
        }
    }
}
