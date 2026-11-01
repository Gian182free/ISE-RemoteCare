package remotecare.model.general;

/**
 * Represents a 2D spatial coordinate on the territorial map of the RemoteCare system.
 * Used for distance calculations and entity localization (patients, ambulances, and facilities).
 */
public record Position(int x, int y) {
    
    /**
     * Calculates the Euclidean distance between this position and another target position.
     * 
     * @param other The other position to measure distance to
     * @return The straight-line Euclidean distance as a double value
     */
    public double distanceTo(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the Manhattan distance between this position and another target position.
     * 
     * @param other The other position to measure distance to
     * @return The Manhattan distance as an integer value
     */
    public int manhattanDistanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
}