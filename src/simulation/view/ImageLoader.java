package simulation.view;

import javafx.scene.image.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getImage(String path) {
        if (!cache.containsKey(path)) {
            try {
                URL url = ImageLoader.class.getResource(path);
                if (url == null) {
                    System.out.println("[ImageLoader] Resource not found: " + path);
                    cache.put(path, null);
                } else {
                    Image img = new Image(url.toExternalForm());
                    cache.put(path, img);
                    System.out.println("[ImageLoader] Loaded: " + path + " (" + (int)img.getWidth() + "x" + (int)img.getHeight() + ")");
                }
            } catch (Exception e) {
                System.out.println("[ImageLoader] Error loading " + path + ": " + e.getMessage());
                cache.put(path, null);
            }
        }
        return cache.get(path);
    }
}
