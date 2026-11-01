package remotecare.model.ambulance;

import remotecare.model.general.Position;

/**
 * Represents the physical state and spatial tracking of an ambulance unit on the grid.
 */
public final class AmbulanceInfo {

    private final String id;
    private Position position;
    private Position destination;
    private boolean arrived;
    private boolean available;

    /**
     * Creates a new ambulance info record with an initial position.
     * 
     * @param id the unique ambulance identifier
     * @param initialPosition the starting position on the grid
     */
    public AmbulanceInfo(final String id, final Position initialPosition) {
        this.id = id;
        this.position = initialPosition;
        this.destination = null;
        this.arrived = false;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public synchronized int getX() {
        return position.x();
    }

    public synchronized int getY() {
        return position.y();
    }

    public synchronized Position getPosition() {
        return position;
    }

    public synchronized void updatePosition(final Position newPosition) {
        this.position = newPosition;
    }


    public synchronized Position getDestination() {
        return destination;
    }

    public synchronized boolean hasDestination() {
        return destination != null;
    }

    public synchronized boolean isArrived() {
        return arrived;
    }

    public synchronized void setArrived(final boolean arrived) {
        this.arrived = arrived;
    }

    public synchronized void setDestination(final Position target) {
        this.destination = target;
        this.arrived = (position.x() == target.x() && position.y() == target.y());
        this.available = false;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    public synchronized void setAvailable(final boolean available) {
        this.available = available;
    }
}