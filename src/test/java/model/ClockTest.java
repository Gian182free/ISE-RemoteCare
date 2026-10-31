package model;

import org.junit.jupiter.api.Test;

import remotecare.model.clock.Clock;
import remotecare.model.clock.TickListener;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClockTest {

    @Test
    void pulseNotifiesListenersInRegistrationOrder() {
        final Clock clock = new Clock();
        final List<String> log = new ArrayList<>();

        clock.register(t -> log.add("first@" + t));
        clock.register(t -> log.add("second@" + t));

        clock.pulse();
        clock.pulse();

        assertThat(clock.getCurrentTick()).isEqualTo(2);
        assertThat(log).containsExactly("first@1", "second@1", "first@2", "second@2");
    }

    @Test
    void unregisterStopsListenerNotifications() {
        final Clock clock = new Clock();
        final List<Long> ticks = new ArrayList<>();
        TickListener listener = ticks::add;

        clock.register(listener);
        clock.pulse();
        
        clock.unregister(listener);
        clock.pulse();

        assertThat(ticks).containsExactly(1L);
    }
}