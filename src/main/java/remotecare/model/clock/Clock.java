package remotecare.model.clock;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the simulation time and pulse ticks, notifying registered components 
 * and listeners at each time step update within the RemoteCare system.
 */
public class Clock {
    private long currentTick = 0;
    private final List<TickListener> listeners = new ArrayList<>();
    private boolean running = false;

    public synchronized void register(TickListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public synchronized void unregister(TickListener listener) {
        listeners.remove(listener);
    }

    public synchronized void pulse() {
        currentTick++;
        for (TickListener listener : listeners) {
            listener.onTick(currentTick);
        }
    }

    public synchronized long getCurrentTick() {
        return currentTick;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }
}