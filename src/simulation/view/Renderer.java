package simulation.view;

import simulation.model.*;
import javafx.scene.canvas.GraphicsContext;

public interface Renderer {
    void render(GraphicsContext gc, World world, Camera camera);
}
