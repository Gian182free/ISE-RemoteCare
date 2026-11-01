package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import remotecare.model.ambulance.AmbulanceInfo;
import remotecare.model.field.GridNavigator;
import remotecare.model.field.GridWorld;
import remotecare.model.general.Position;

/**
 * Unit tests for GridNavigator movement logic and GridWorld fleet coordination.
 */
class GridWorldTest {

    private GridWorld gridWorld;

    @BeforeEach
    void setUp() {
        final List<GridWorld.AmbulanceConfig> initialFleet = List.of(
            new GridWorld.AmbulanceConfig("amb-1", 0, 0),
            new GridWorld.AmbulanceConfig("amb-2", 10, 10)
        );
        // Map size 50, initialized with two ambulances
        gridWorld = new GridWorld(50, initialFleet);
    }

    @Test
    void testManhattanDistanceAndNearestAmbulance() {
        final Position target = new Position(2, 2);
        
        // "amb-1" is at (0,0), distance = 2 + 2 = 4
        // "amb-2" is at (10,10), distance = 8 + 8 = 16
        final Optional<AmbulanceInfo> nearest = gridWorld.findNearestFreeAmbulance(target);

        assertTrue(nearest.isPresent());
        assertEquals("amb-1", nearest.get().getId());
    }

    @Test
    void testAmbulanceArrivalStatus() {
        final AmbulanceInfo ambulance = gridWorld.getAmbulance("amb-1").get();
        
        // Set destination at (0, 0) which is the current position
        ambulance.setDestination(new Position(0, 0));
        assertTrue(ambulance.isArrived());

        // Set a new destination at (1, 0)
        ambulance.setDestination(new Position(1, 0));
        assertFalse(ambulance.isArrived());

        // Move the ambulance to the destination
        GridNavigator.advance(ambulance);
        assertEquals(new Position(1, 0), ambulance.getPosition());
        assertTrue(ambulance.isArrived());
    }

    @Test
    void testGridNavigatorStepByStepMovement() {
        final AmbulanceInfo ambulance = gridWorld.getAmbulance("amb-1").get();
        
        // Set destination at (2, 1)
        ambulance.setDestination(new Position(2, 1));
        assertFalse(ambulance.isArrived());

        // Tick 1: from (0,0) -> moves along X (deltaX = 2) -> (1,0)
        GridNavigator.advance(ambulance);
        assertEquals(new Position(1, 0), ambulance.getPosition());
        assertFalse(ambulance.isArrived());

        // Tick 2: from (1,0) -> moves along X (deltaX = 1) -> (2,0)
        GridNavigator.advance(ambulance);
        assertEquals(new Position(2, 0), ambulance.getPosition());
        assertFalse(ambulance.isArrived());

        // Tick 3: from (2,0) -> X matches, moves along Y (deltaY = 1) -> (2,1)
        GridNavigator.advance(ambulance);
        assertEquals(new Position(2, 1), ambulance.getPosition());
        assertTrue(ambulance.isArrived());
    }

    @Test
    void testFindNearestFreeAmbulanceIgnoresBusyUnits() {
        final Position target = new Position(1, 1);
        
        // "amb-1" is closer to target (distance = 2), but we make it busy/unavailable
        final AmbulanceInfo amb1 = gridWorld.getAmbulance("amb-1").get();
        amb1.setDestination(new Position(5, 5)); // This sets available to false
        
        // Now find the nearest free ambulance
        final Optional<AmbulanceInfo> nearest = gridWorld.findNearestFreeAmbulance(target);

        assertTrue(nearest.isPresent());
        // Should skip "amb-1" and pick "amb-2" (even though amb-2 is further away)
        assertEquals("amb-2", nearest.get().getId());
    }
}