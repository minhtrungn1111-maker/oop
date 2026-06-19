package simulation.event;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class AudioSystem implements EventListener {
    private final Map<EventType, AudioClip> clips = new EnumMap<>(EventType.class);

    public AudioSystem(EventBus bus) {
        try {
            String base = "/resources/audio/";
            load(EventType.ATTACK, base + "roar.wav");
            load(EventType.EAT, base + "eat.wav");
            load(EventType.BIRD_CHIRP, base + "bird.wav");
            load(EventType.LEAVES_RUSTLE, base + "leaves.wav");
        } catch (Throwable t) {
            System.out.println("[Audio Init Error] " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
        for (EventType t : EventType.values()) {
            bus.subscribe(t, this);
        }
    }

    private void load(EventType type, String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.out.println("[Audio] not found: " + path);
                return;
            }
            clips.put(type, new AudioClip(url.toExternalForm()));
            System.out.println("[Audio] loaded: " + path);
        } catch (Throwable t) {
            System.out.println("[Audio Load Error] " + path + " - " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    @Override
    public void onEvent(EventType type) {
        AudioClip clip = clips.get(type);
        if (clip != null) {
            try {
                clip.play();
            } catch (Throwable t) {
                System.out.println("[Audio Error] " + type + " - " + t.getMessage());
            }
        } else {
            System.out.println("[Audio] " + type);
        }
    }
}
