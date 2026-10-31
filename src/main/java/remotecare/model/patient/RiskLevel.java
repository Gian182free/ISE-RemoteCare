package remotecare.model.patient;

/**
 * Enumeration representing the clinical risk tiers for patient triage and emergency routing,
 * providing conversion utilities for tuProlog integration.
 */
public enum RiskLevel {
    
    GREEN(1),
    YELLOW(2),
    RED(3);

    private final int risk;

    RiskLevel(final int risk) {
        this.risk = risk;
    }

    public int risk() {
        return this.risk;
    }

    public static RiskLevel fromAtom(final String atom) {
        return switch (atom.toLowerCase()) {
            case "green" -> GREEN;
            case "yellow" -> YELLOW;
            case "red" -> RED;
            default -> throw new IllegalArgumentException("Unknown risk atom: " + atom);
        };
    }

    public String atom() {
        return this.name().toLowerCase();
    }
}
