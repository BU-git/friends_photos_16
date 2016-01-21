package com.bionic.fp;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class AbstractHelperTest {

    protected Event getNewEventMin() {
        Event event = new Event();
        LocalDateTime now = LocalDateTime.now();
        event.setName("Nano is " + now.getNano());
        event.setDescription("Today is " + now);
        event.setEventType(getPrivateEventType());

        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    protected EventType getPrivateEventType() {
        EventType eventType = new EventType();
        eventType.setId(1L);
        eventType.setTypeName("PRIVATE");
        return eventType;
    }

    protected Event getNewEventMax() {
        Random random = new Random();
        Event event = getNewEventMin();
        event.setVisible(true);
        event.setLatitude(random.nextDouble());
        event.setLongitude(random.nextDouble());
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(false);

        assertFalse(event.isDeleted());

        return event;
    }
}
