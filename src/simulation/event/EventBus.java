package simulation.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private final Map<EventType, List<EventListener>> listeners = new EnumMap<>(EventType.class);

    public void subscribe(EventType type, EventListener l) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(l);
    }

    public void publish(EventType type) {
        List<EventListener> ls = listeners.get(type);
        if (ls == null) {
            return;
        }
        for (EventListener l : ls) {
            l.onEvent(type);
        }
    }
}
