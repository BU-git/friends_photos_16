package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
public class EventDAOIT extends AbstractDaoIT {

    @Test
    public void testGetByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        Event actual = this.eventDAO.read(event.getId());
        assertNotNull(actual);
        assertEqualsEvent(event, actual);
    }

    @Test
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), true);

        assertEventIsDeleted(owner.getId(), event);
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), true);

        assertEventIsDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), false);

        assertEventIsNotDeleted(owner.getId(), event);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.delete(event.getId());

        assertEventIsDeleted(owner.getId(), event);

        this.eventDAO.delete(event.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), true);

        assertEventIsDeleted(owner.getId(), event);

        this.eventDAO.delete(event.getId());

        assertEventIsDeleted(owner.getId(), event);

        this.eventDAO.delete(event.getId());
    }

    @Test
    public void testGetByNameSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setName("The first event starts at " + now);
        event2.setName("The second event starts at " + now);
        event3.setName("The third event starts at " + now);
        event1 = save(owner, event1);
        event2 = save(owner, event2);
        event3 = save(owner, event3);

        List<Event> events = this.eventDAO.get("at " + now, null);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        events = this.eventDAO.get("d event starts at " + now, null);

        assertEquals(2, events.size());
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
    }

    @Test
    public void testGetByDescriptionSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setDescription("The first event starts at " + now);
        event2.setDescription("The second event starts at " + now);
        event3.setDescription("The third event starts at " + now);
        event1 = save(owner, event1);
        event2 = save(owner, event2);
        event3 = save(owner, event3);

        List<Event> events = this.eventDAO.get(null, "at " + now);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        events = this.eventDAO.get(null, "d event starts at " + now);

        assertEquals(2, events.size());
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
    }

    @Test
    public void testGetByNameAndDescriptionSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setName("The first event starts at " + now);
        event2.setName("The second event starts at " + now);
        event3.setName("The third event starts at " + now);
        event1.setDescription("The first event starts at " + now);
        event2.setDescription("The second event starts at " + now);
        event3.setDescription("The third event starts at " + now);
        event1 = save(owner, event1);
        event2 = save(owner, event2);
        event3 = save(owner, event3);

        List<Event> events = this.eventDAO.get("at " + now, "at " + now);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        events = this.eventDAO.get("second", "d event starts at " + now);

        assertEquals(1, events.size());
        assertTrue(events.contains(event2));
    }

    @Test
    public void testGetByNameAndDescriptionVisibleSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setName("The first event starts at " + now);
        event2.setName("The second event starts at " + now);
        event3.setName("The third event starts at " + now);
        event1.setDescription("The first event starts at " + now);
        event2.setDescription("The second event starts at " + now);
        event3.setDescription("The third event starts at " + now);
        event1 = save(owner, event1);
        event2 = save(owner, event2);
        event3 = save(owner, event3);

        List<Event> events = this.eventDAO.get("at " + now, "at " + now);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        event1.setVisible(false);
        this.eventDAO.update(event1);
        assertNotNull(event1.getModified());

        events = this.eventDAO.get("at " + now, "at " + now);

        assertEquals(2, events.size());
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        event3.setVisible(false);
        this.eventDAO.update(event3);
        assertNotNull(event3.getModified());

        events = this.eventDAO.get("at " + now, "at " + now);

        assertEquals(1, events.size());
        assertTrue(events.contains(event2));
    }

    protected void assertEqualsEvent(final Event expected, final Event actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getEventType(), actual.getEventType());
        assertEquals(expected.getLatitude(), actual.getLatitude());
        assertEquals(expected.getLongitude(), actual.getLongitude());
        assertEquals(expected.getRadius(), actual.getRadius());
        assertEquals(expected.isVisible(), actual.isVisible());
        assertEquals(expected.isGeoServicesEnabled(), actual.isGeoServicesEnabled());
    }

    private void assertEventIsNotDeleted(final Long accountId, final Event event) {
        assertEqualsEvent(event, this.eventDAO.read(event.getId()));
        assertEventIsNotDeleted(accountId, event.getId());
        assertFalse(this.eventDAO.get(event.getName(), null).isEmpty());
        assertFalse(this.eventDAO.get(null, event.getDescription()).isEmpty());
        assertFalse(this.eventDAO.get(event.getName(), event.getDescription()).isEmpty());
    }

    private void assertEventIsDeleted(final Long accountId, final Event event) {
        assertEventIsDeleted(accountId, event.getId());
        assertTrue(this.eventDAO.get(event.getName(), null).isEmpty());
        assertTrue(this.eventDAO.get(null, event.getDescription()).isEmpty());
        assertTrue(this.eventDAO.get(event.getName(), event.getDescription()).isEmpty());
    }

    private void assertEventIsDeleted(final Long ownerId, final Long eventId) {
        assertNull(this.eventDAO.read(eventId));
        try {
            this.eventDAO.getOrThrow(eventId);
            fail();
        } catch (EntityNotFoundException ignored){}

        assertNull(this.accountEventDAO.get(ownerId, eventId));
        assertNull(this.accountEventDAO.getRole(ownerId, eventId));
        assertNull(this.accountEventDAO.getWithAccountEvent(ownerId, eventId));

        assertTrue(this.accountEventDAO.getAccounts(eventId).isEmpty());
        assertTrue(this.accountEventDAO.getAccounts(eventId, OWNER).isEmpty());
        assertTrue(this.accountEventDAO.getByEventAndRole(eventId, OWNER).isEmpty());

        assertTrue(this.accountEventDAO.getEvents(ownerId).isEmpty());
        assertTrue(this.accountEventDAO.getEvents(ownerId, OWNER).isEmpty());
        assertTrue(this.accountEventDAO.getByAccountAndRole(ownerId, OWNER).isEmpty());

        assertTrue(this.photoDAO.getPhotosByEvent(eventId).isEmpty());
        assertTrue(this.photoDAO.getPhotosByAccountInEvent(ownerId, eventId).isEmpty());

//        this.commentDAO.getCommentsByEvent(eventId) todo this
    }

    private void assertEventIsNotDeleted(final Long accountId, final Long eventId) {
        assertNotNull(this.eventDAO.read(eventId));
        assertNotNull(this.eventDAO.getOrThrow(eventId));

        assertNotNull(this.accountEventDAO.get(accountId, eventId));
        assertNotNull(this.accountEventDAO.getRole(accountId, eventId));
        assertNotNull(this.accountEventDAO.getWithAccountEvent(accountId, eventId));

        assertFalse(this.accountEventDAO.getAccounts(eventId).isEmpty());
        assertFalse(this.accountEventDAO.getAccounts(eventId, OWNER).isEmpty());
        assertFalse(this.accountEventDAO.getByEventAndRole(eventId, OWNER).isEmpty());

        assertFalse(this.accountEventDAO.getEvents(accountId).isEmpty());
        assertFalse(this.accountEventDAO.getEvents(accountId, OWNER).isEmpty());
        assertFalse(this.accountEventDAO.getByAccountAndRole(accountId, OWNER).isEmpty());

        assertFalse(this.photoDAO.getPhotosByEvent(eventId).isEmpty());
        assertFalse(this.photoDAO.getPhotosByAccountInEvent(accountId, eventId).isEmpty());

//        this.commentDAO.getCommentsByEvent(eventId) todo this
    }
}