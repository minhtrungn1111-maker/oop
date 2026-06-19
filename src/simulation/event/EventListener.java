package simulation.event;

@FunctionalInterface
public interface EventListener {
    void onEvent(EventType type);
}
