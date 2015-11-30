package com.bionic.fp.service;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountService}
 *
 * @author Sergiy Gabriel
 */
public class AccountServiceIT extends AbstractIT {

    @Test
    public void testGetAccountEventsSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event1 = getSavedEventMax(owner);

        List<Event> events = this.accountService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(1, events.size());
        events.forEach(event -> {
            assertEquals(event.getId(), event1.getId());
            assertEquals(event.getName(), event1.getName());
            assertEquals(event.getDescription(), event1.getDescription());
            assertEquals(event.getEventType(), event1.getEventType());
            assertEquals(event.getLongitude(), event1.getLongitude());
            assertEquals(event.getLatitude(), event1.getLatitude());
            assertEquals(event.getRadius(), event1.getRadius());
        });

        Event event2 = getSavedEventMax(owner);

        events = this.accountService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));

        Event event3 = getSavedEventMax(owner);

        events = this.accountService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
    }

}