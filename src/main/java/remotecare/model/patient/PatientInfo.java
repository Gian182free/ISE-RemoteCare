package remotecare.model.patient;

import remotecare.model.general.Position;

/**
 * Represents the dynamic state and clinical information of a monitored patient
 * within the RemoteCare system, including vital signs, calculated risk level, and spatial position.
 */
public final class PatientInfo {

    private final String name;
    private final String pathology;
    
    private int heartRate;
    private int bloodPressure;
    private int spO2;
    private int glucose;
    
    private RiskLevel currentRisk;
    private Position position;
    
    private boolean active;
    private boolean discharged;
    private String assignedDoctor;

    public PatientInfo(final String name, final String pathology, int hr, int bp, int spO2, int glucose, int x, int y) {
        this.name = name;
        this.pathology = pathology;
        this.heartRate = hr;
        this.bloodPressure = bp;
        this.spO2 = spO2;
        this.glucose = glucose;
        this.currentRisk = RiskLevel.GREEN;
        this.position = new Position(x, y);
    }

    public String name() {
        return name;
    }

    public String pathology() {
        return pathology;
    }

    public synchronized void updateVitals(int hr, int bp, int spO2, int glucose, RiskLevel risk) {
        this.heartRate = hr;
        this.bloodPressure = bp;
        this.spO2 = spO2;
        this.glucose = glucose;
        this.currentRisk = risk;
    }

    public synchronized RiskLevel currentRisk() {
        return currentRisk;
    }

    public synchronized Position position() {
        return position;
    }

    public synchronized void updatePosition(int x, int y) {
        this.position = new Position(x, y);
    }

    public synchronized int heartRate() { return heartRate; }
    public synchronized int bloodPressure() { return bloodPressure; }
    public synchronized int spO2() { return spO2; }
    public synchronized int glucose() { return glucose; }

    public synchronized void activate() {
        this.active = true;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void assignTo(final String doctor) {
        this.assignedDoctor = doctor;
    }

    public synchronized String assignedDoctor() {
        return assignedDoctor;
    }

    public synchronized boolean isDischarged() {
        return discharged;
    }

    public synchronized void discharge() {
        this.discharged = true;
        this.assignedDoctor = null;
    }
}