package remotecare.model.field;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import remotecare.model.ambulance.AmbulanceInfo;
import remotecare.model.general.Position;

/**
 * Field model representing a obstacle-free grid holding the active ambulance fleet.
 */
public final class GridWorld {

    private final int mapSize;
    private final Map<String, AmbulanceInfo> fleet = new LinkedHashMap<>();

    /**
     * Initializes the grid world with a custom map size and an initial fleet configuration.
     * 
     * @param mapSize the dimension of the square grid (e.g., 50)
     * @param initialFleet list of initial ambulance configurations
     */
    public GridWorld(final int mapSize, final List<AmbulanceConfig> initialFleet) {
        this.mapSize = mapSize;
        for (final AmbulanceConfig cfg : initialFleet) {
            fleet.put(cfg.id(), new AmbulanceInfo(cfg.id(), new Position(cfg.startX(), cfg.startY())));
        }
    }

    public synchronized void tick(final long currentTick) {
        for (final AmbulanceInfo ambulance : fleet.values()) {
            GridNavigator.advance(ambulance);
        }
    }

    public synchronized Optional<AmbulanceInfo> findNearestFreeAmbulance(final Position targetPosition) {
        return fleet.values().stream()
                .filter(AmbulanceInfo::isAvailable)
                .min(Comparator.comparingInt(ambulance -> 
                    ambulance.getPosition().manhattanDistanceTo(targetPosition)
                ));
    }

    public synchronized Optional<AmbulanceInfo> getAmbulance(final String id) {
        return Optional.ofNullable(fleet.get(id));
    }

    public synchronized List<AmbulanceInfo> getAllAmbulances() {
        return List.copyOf(fleet.values());
    }

    public record AmbulanceConfig(String id, int startX, int startY) {}
}