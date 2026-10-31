package remotecare.model.clock;

/**
 * Listener interface for components that need to synchronize their internal state 
 * or simulation updates with the system clock pulses.
 */
@FunctionalInterface
public interface TickListener {
    void onTick(long tick);
}