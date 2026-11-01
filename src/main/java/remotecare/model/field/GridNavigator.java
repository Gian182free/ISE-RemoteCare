package remotecare.model.field;

import remotecare.model.ambulance.AmbulanceInfo;
import remotecare.model.general.Position;

/**
 * Handles deterministic movement: one grid cell per tick toward the target,
 * moving first along the axis with the larger residual distance.
 */
public final class GridNavigator {

    private GridNavigator() {
    }

    public static void advance(final AmbulanceInfo ambulance) {
        if (!ambulance.hasDestination() || ambulance.isArrived()) {
            return;
        }

        final Position current = ambulance.getPosition();
        final Position target = ambulance.getDestination();

        final int deltaX = target.x() - current.x();
        final int deltaY = target.y() - current.y();
        
        int nextX = current.x();
        int nextY = current.y();

        if (Math.abs(deltaX) >= Math.abs(deltaY) && deltaX != 0) {
            nextX += Integer.signum(deltaX);
        } else if (deltaY != 0) {
            nextY += Integer.signum(deltaY);
        }

        final Position nextPosition = new Position(nextX, nextY);
        ambulance.updatePosition(nextPosition);

        if (nextPosition.equals(target)) {
            ambulance.setArrived(true);
        }
    }
}