package simulation;

import simulation.model.*;
import simulation.view.*;
import simulation.event.*;
import simulation.stats.*;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Main extends Application {
    private World world;
    private Canvas canvas;
    private Renderer renderer = new BasicRenderer();
    private Camera camera;
    private StatisticsCollector stats;

    @Override
    public void start(Stage stage) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double winW = bounds.getWidth();
        double winH = bounds.getHeight();
        double worldW = winW;
        double worldH = winH - 50;

        world = new World(worldW, worldH);
        camera = new Camera(worldW, worldH);

        for (int i = 0; i < 50; i++) {
            double[] p = randomNonWaterPos(worldW, worldH);
            world.add(new Grass(p[0], p[1]));
        }
        for (int i = 0; i < 15; i++) {
            double[] p = randomNonWaterPos(worldW, worldH);
            world.add(new FruitTree(p[0], p[1]));
        }
        for (int i = 0; i < 15; i++) {
            double[] p = randomWaterPos(worldW, worldH);
            world.add(new Fish(p[0], p[1]));
        }
        for (int i = 0; i < 5; i++) {
            double[] p = randomWaterPos(worldW, worldH);
            world.add(new Duck(p[0], p[1]));
        }
        for (int i = 0; i < 35; i++) {
            double[] p = randomGrassPos(worldW, worldH);
            world.add(new Rabbit(p[0], p[1]));
        }
        for (int i = 0; i < 12; i++) {
            double[] p = randomGrassPos(worldW, worldH);
            world.add(new Deer(p[0], p[1]));
        }
        for (int i = 0; i < 6; i++) {
            double[] p = randomGrassPos(worldW, worldH);
            world.add(new Wolf(p[0], p[1]));
        }
        for (int i = 0; i < 3; i++) {
            double[] p = randomGrassPos(worldW, worldH);
            world.add(new Tiger(p[0], p[1]));
        }
        for (int i = 0; i < 2; i++) {
            double[] p = randomGrassPos(worldW, worldH);
            world.add(new Elephant(p[0], p[1]));
        }

        new AudioSystem(world.getEventBus());
        stats = new StatisticsCollector(world.getEventBus());

        canvas = new Canvas(worldW, worldH);

        Button toggleBtn = new Button("Renderer: Basic");
        toggleBtn.setOnAction(e -> {
            if (renderer instanceof BasicRenderer) {
                renderer = new SpriteRenderer();
                toggleBtn.setText("Renderer: Sprite");
            } else {
                renderer = new BasicRenderer();
                toggleBtn.setText("Renderer: Basic");
            }
        });

        Button zoomInBtn = new Button("Zoom +");
        zoomInBtn.setOnAction(e -> camera.zoomIn());
        Button zoomOutBtn = new Button("Zoom -");
        zoomOutBtn.setOnAction(e -> camera.zoomOut());
        Button zoomResetBtn = new Button("Reset");
        zoomResetBtn.setOnAction(e -> camera.setZoom(1.0));

        Button seasonBtn = new Button("Season: NORMAL");
        seasonBtn.setOnAction(e -> {
            world.cycleSeason();
            seasonBtn.setText("Season: " + world.getSeason());
        });

        Button exportBtn = new Button("Export CSV");
        exportBtn.setOnAction(e -> {
            String filename = "stats-" + System.currentTimeMillis() + ".csv";
            try {
                stats.exportCsv(filename);
                System.out.println("Stats exported to " + filename);
            } catch (Exception ex) {
                System.out.println("Export failed: " + ex.getMessage());
            }
        });

        HBox controls = new HBox(8, toggleBtn, zoomInBtn, zoomOutBtn, zoomResetBtn, seasonBtn, exportBtn);
        controls.setPadding(new Insets(5));

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(canvas);
        Scene scene = new Scene(root, winW, winH);

        canvas.setOnMouseClicked(e -> {
            double[] w = camera.screenToWorld(e.getX(), e.getY());
            double cx = w[0];
            double cy = w[1];
            if (e.getButton() == MouseButton.SECONDARY) {
                world.setTileAt(cx, cy, Terrain.ROCK);
            } else if (world.getTerrainAt(cx, cy) != Terrain.WATER) {
                world.add(new Grass(cx, cy));
            }
        });

        stage.setTitle("Eco Sim");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;
                world.tick(dt);
                stats.tick(dt, world);
                renderer.render(canvas.getGraphicsContext2D(), world, camera);
            }
        };
        timer.start();
    }

    private double[] randomGrassPos(double worldW, double worldH) {
        for (int tries = 0; tries < 100; tries++) {
            double x = Math.random() * worldW;
            double y = Math.random() * worldH;
            if (world.getTerrainAt(x, y) == Terrain.GRASS) {
                return new double[] { x, y };
            }
        }
        return new double[] { worldW / 2, worldH / 2 };
    }

    private double[] randomNonWaterPos(double worldW, double worldH) {
        double x = 0, y = 0;
        for (int tries = 0; tries < 20; tries++) {
            x = Math.random() * worldW;
            y = Math.random() * worldH;
            Terrain t = world.getTerrainAt(x, y);
            if (t != Terrain.WATER && t != Terrain.ROCK) {
                return new double[] { x, y };
            }
        }
        return new double[] { x, y };
    }

    private double[] randomWaterPos(double worldW, double worldH) {
        for (int tries = 0; tries < 200; tries++) {
            double x = Math.random() * worldW;
            double y = Math.random() * worldH;
            if (world.getTerrainAt(x, y) == Terrain.WATER) {
                return new double[] { x, y };
            }
        }
        return new double[] { worldW * 0.8, worldH * 0.8 };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
