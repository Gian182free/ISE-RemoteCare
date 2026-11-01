package remotecare.model.ambulance;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import remotecare.model.general.Position;

/**
 * Manages the complete collection of ambulance units in the RemoteCare system,
 * handling lookup, status tracking, and spatial queries.
 */
public final class AmbulanceDirectory {

    private final Map<String, AmbulanceInfo> ambulances = new LinkedHashMap<>();

    /**
     * Registers a new ambulance unit into the directory.
     * 
     * @param ambulance the ambulance unit to register
     */
    public synchronized void registerAmbulance(final AmbulanceInfo ambulance) {
        ambulances.put(ambulance.getId(), ambulance);
    }

    /**
     * Retrieves an ambulance unit by its unique identifier.
     * 
     * @param id the ambulance identifier
     * @return an Optional containing the ambulance unit if found
     */
    public synchronized Optional<AmbulanceInfo> getAmbulance(final String id) {
        return Optional.ofNullable(ambulances.get(id));
    }

    /**
     * Returns an immutable list of all registered ambulance units.
     * 
     * @return list of all ambulances
     */
    public synchronized List<AmbulanceInfo> getAllAmbulances() {
        return List.copyOf(ambulances.values());
    }

    /**
     * Finds the nearest available ambulance to a given target position using Manhattan distance.
     * 
     * @param targetPosition the target position (e.g., patient location)
     * @return an Optional containing the nearest available ambulance unit, if any exists
     */
    public synchronized Optional<AmbulanceInfo> findNearestAvailable(final Position targetPosition) {
        return ambulances.values().stream()
                .filter(AmbulanceInfo::isAvailable)
                .min(Comparator.comparingInt(ambulance -> 
                    ambulance.getPosition().manhattanDistanceTo(targetPosition)
                ));
    }
}