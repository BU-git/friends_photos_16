package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.MEMBER;
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
        assertEqualsEntity(event, actual);
    }

    @Test
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account another = getSavedAccount();
        Event event = getSavedEventMax(owner);
        getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), true);

        assertEventIsDeleted(owner.getId(), event);

        Event event1 = getSavedEventMax(owner);
        Event event2 = getSavedEventMax(another);
        getSavedAccountEvent(owner, event2, getRoleMember());
        getSavedAccountEvent(another, event1, getRoleMember());
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId()), event1, event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId()), event2, event1);
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId(), OWNER), event1);
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId(), MEMBER), event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId(), OWNER), event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId(), MEMBER), event1);

        this.eventDAO.setDeleted(event1.getId(), true);
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId()), event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId()), event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId(), OWNER));
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId(), MEMBER), event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId(), OWNER), event2);
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId(), MEMBER));

        this.eventDAO.setDeleted(event2.getId(), true);
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId()));
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId()));
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId(), OWNER));
        assertEqualsEntities(this.accountEventDAO.getEvents(owner.getId(), MEMBER));
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId(), OWNER));
        assertEqualsEntities(this.accountEventDAO.getEvents(another.getId(), MEMBER));
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), true);

        assertEventIsDeleted(owner.getId(), event);

        this.eventDAO.setDeleted(event.getId(), false);

        assertEventIsNotDeleted(owner.getId(), event);
    }

    @Test(expected = EntityNotFoundException.class) //@Ignore //todo: fix physically delete comment
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);

        assertEventIsNotDeleted(owner.getId(), event);

        this.eventDAO.delete(event.getId());

        assertEventIsDeleted(owner.getId(), event);

        this.eventDAO.delete(event.getId());
    }

    @Test(expected = EntityNotFoundException.class) //@Ignore //todo: fix physically delete comment
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);

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

    private void assertEventIsNotDeleted(final Long accountId, final Event event) {
        assertEqualsEntity(event, this.eventDAO.read(event.getId()));
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

        assertTrue(this.commentDAO.getCommentsByEvent(eventId).isEmpty());
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

        assertFalse(this.commentDAO.getCommentsByEvent(eventId).isEmpty());
    }
}