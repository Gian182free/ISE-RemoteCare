package remotecare.env;

import jason.environment.Environment;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.File;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;

// Main environment handling the RemoteCare simulation space, agent interaction loops,
// and the embedded tuProlog engine for automated clinical planning.
public class RemoteCareEnvironment extends Environment {

    private static final Logger LOGGER = Logger.getLogger(RemoteCareEnvironment.class.getName());
    private static volatile RemoteCareEnvironment instance;

    private Prolog engine;

    // Clean default configuration constants
    private static final long DEFAULT_SEED = 2026L;
    private static final String DEFAULT_MODE = "telehealth";
    private static final long DEFAULT_PERIOD_MS = 500L;

    private long seed = DEFAULT_SEED;
    private String mode = DEFAULT_MODE;
    private long periodMs = DEFAULT_PERIOD_MS;
    private boolean guiRequested = false;

    private boolean stopped = false;
    

    @Override
    public void init(final String[] args) {
        for (final String arg : args == null ? new String[0] : args) {
            if (arg.startsWith("seed=")) {
                seed = Long.parseLong(arg.substring("seed=".length()));
            } else if (arg.startsWith("mode=")) {
                mode = arg.substring("mode=".length());
            } else if (arg.startsWith("period=")) {
                periodMs = Long.parseLong(arg.substring("period=".length()));
            } else if (arg.equals("gui")) {
                guiRequested = true;
            }
        }

        instance = this;

        initializePrologEngine();

        final String summary = "RemoteCareEnvironment operational: mode=" + mode + " seed=" + seed + " period=" + periodMs + "ms gui=" + guiRequested;
        LOGGER.info(summary);
    }

    @Override
    public Collection<Literal> getPercepts(final String agName) {
        return List.of();
    }

    @Override
    public boolean executeAction(final String agName, final Structure action) {
        LOGGER.info(() -> "Execution request from [" + agName + "]: " + action);
        
        boolean success = true; 
        if (success) {
            informAgsEnvironmentChanged();
        }
        return success;
    }

    @Override
    public void stop() {
        stopped = true;
        instance = null;
        super.stop();
    }

    public static RemoteCareEnvironment getInstance() {
        return instance;
    }

    public long getSeed() {
        return seed;
    }

    public String getMode() {
        return mode;
    }

    private void initializePrologEngine() {
        try {
            LOGGER.info("Initializing tuProlog engine for clinical reasoning...");
            engine = new alice.tuprolog.Prolog();
            
            File stripsFile = new java.io.File("src/main/prolog/strips.pl");
            File domainFile = new java.io.File("src/main/prolog/remotecare_domain.pl");
            
            if (stripsFile.exists()) {
                engine.addTheory(new alice.tuprolog.Theory(new java.io.FileInputStream(stripsFile)));
                LOGGER.info("Loaded strips.pl successfully.");
            } else {
                LOGGER.warning("Could not find strips.pl at: " + stripsFile.getAbsolutePath());
            }
            
            if (domainFile.exists()) {
                engine.addTheory(new alice.tuprolog.Theory(new java.io.FileInputStream(domainFile)));
                LOGGER.info("Loaded remotecare_doman.pl successfully.");
            } else {
                LOGGER.warning("Could not find remotecare_doman.pl at: " + domainFile.getAbsolutePath());
            }

        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize tuProlog theories", e);
        }
    }

    public String queryCarePlan(String patient, String pathology, String code) {
        if (engine == null) {
            return "no_engine";
        }
        try {
            String queryStr = "care_plan(" + patient + ", " + pathology + ", " + code + ", Plan).";
            SolveInfo info = engine.solve(queryStr);
            if (info.isSuccess()) {
                return info.getSolution().toString();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error solving care plan query", e);
        }
        return "fail";
    }
}