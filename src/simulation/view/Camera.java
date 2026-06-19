package simulation.view;

import javafx.scene.canvas.GraphicsContext;

public class Camera {
    private double zoom = 1.0;
    private final double canvasW;
    private final double canvasH;

    public Camera(double canvasW, double canvasH) {
        this.canvasW = canvasW;
        this.canvasH = canvasH;
    }

    public void apply(GraphicsContext gc) {
        gc.translate(canvasW / 2, canvasH / 2);
        gc.scale(zoom, zoom);
        gc.translate(-canvasW / 2, -canvasH / 2);
    }

    public double[] screenToWorld(double sx, double sy) {
        double wx = (sx - canvasW / 2) / zoom + canvasW / 2;
        double wy = (sy - canvasH / 2) / zoom + canvasH / 2;
        return new double[] { wx, wy };
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double z) {
        zoom = Math.max(0.5, Math.min(4, z));
    }

    public void zoomIn() {
        setZoom(zoom * 1.25);
    }

    public void zoomOut() {
        setZoom(zoom / 1.25);
    }
}
