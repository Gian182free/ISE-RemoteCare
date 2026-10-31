package remotecare.model.patient;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe directory and repository managing the active collection of monitored patients
 * within the RemoteCare system environment.
 */
public final class PatientDirectory {

    private final Map<String, PatientInfo> patients = new ConcurrentHashMap<>();

    public void register(final PatientInfo patient) {
        if (patient != null) {
            patients.put(patient.name(), patient);
        }
    }

    public Optional<PatientInfo> find(final String name) {
        return Optional.ofNullable(patients.get(name));
    }

    public Collection<PatientInfo> all() {
        return patients.values();
    }
}