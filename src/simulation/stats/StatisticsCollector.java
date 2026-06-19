package simulation.stats;

import simulation.model.*;
import simulation.event.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class StatisticsCollector implements EventListener {
    private final List<Snapshot> snapshots = new ArrayList<>();
    private double timeSinceLast = 0;
    private double totalTime = 0;
    private int attacks = 0;
    private int eats = 0;
    private int deaths = 0;

    public StatisticsCollector(EventBus bus) {
        for (EventType t : EventType.values()) {
            bus.subscribe(t, this);
        }
    }

    public void tick(double dt, World world) {
        totalTime += dt;
        timeSinceLast += dt;
        if (timeSinceLast >= 1.0) {
            timeSinceLast = 0;
            snapshots.add(snapshot(world));
        }
    }

    private Snapshot snapshot(World world) {
        int rabbits = 0, deer = 0, wolves = 0, tigers = 0, elephants = 0, grass = 0;
        for (Entity e : world.getEntities()) {
            if (!e.isAlive()) {
                continue;
            }
            if (e instanceof Rabbit) rabbits++;
            else if (e instanceof Deer) deer++;
            else if (e instanceof Wolf) wolves++;
            else if (e instanceof Tiger) tigers++;
            else if (e instanceof Elephant) elephants++;
            else if (e instanceof Grass) grass++;
        }
        return new Snapshot(totalTime, rabbits, deer, wolves, tigers, elephants, grass, attacks, eats, deaths);
    }

    public void exportCsv(String path) throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("time,rabbits,deer,wolves,tigers,elephants,grass,attacks,eats,deaths");
            for (Snapshot s : snapshots) {
                pw.printf("%.1f,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                        s.time, s.rabbits, s.deer, s.wolves, s.tigers, s.elephants, s.grass,
                        s.attacks, s.eats, s.deaths);
            }
        }
    }

    @Override
    public void onEvent(EventType type) {
        switch (type) {
            case ATTACK:
                attacks++;
                break;
            case EAT:
                eats++;
                break;
            case DEATH:
                deaths++;
                break;
        }
    }

    private static class Snapshot {
        final double time;
        final int rabbits, deer, wolves, tigers, elephants, grass;
        final int attacks, eats, deaths;

        Snapshot(double time, int rabbits, int deer, int wolves, int tigers, int elephants,
                 int grass, int attacks, int eats, int deaths) {
            this.time = time;
            this.rabbits = rabbits;
            this.deer = deer;
            this.wolves = wolves;
            this.tigers = tigers;
            this.elephants = elephants;
            this.grass = grass;
            this.attacks = attacks;
            this.eats = eats;
            this.deaths = deaths;
        }
    }
}
