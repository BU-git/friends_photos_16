package com.bionic.fp;

import com.bionic.fp.domain.*;

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
        event.setName("Name is " + System.currentTimeMillis());
        event.setDescription("Today is " + System.currentTimeMillis());
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
        Event event = getNewEventMin();
        event.setVisible(true);
        event.setLocation(getNewCoordinate());
        event.setGeoServicesEnabled(true);

        assertFalse(event.isDeleted());

        return event;
    }

    protected Photo getNewPhoto() {
        Photo photo = new Photo();
        Event event = new Event();
        event.setId(System.currentTimeMillis());
        Account owner = new Account();
        owner.setId(System.currentTimeMillis());
        LocalDateTime now = LocalDateTime.now();
        photo.setName("photo" + now.getNano());
        photo.setUrl("http://" + now.getNano());
        photo.setEvent(event);
        photo.setOwner(owner);
        photo.setCreated(now);
        return photo;
    }

    protected Coordinate getNewCoordinate() {
        Random random = new Random();
        return new Coordinate(random.nextDouble(), random.nextDouble());
    }
}
