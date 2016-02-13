package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Coordinate;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.util.GeoUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RoleConstants.MEMBER;
import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static com.bionic.fp.util.GeoUtils.DistanceUnitPerDegree.KM;
import static com.bionic.fp.util.GeoUtils.getDistance;
import static java.util.stream.Collectors.toList;
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

    @Test
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

    @Test
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
        Stream.of(event1, event2, event3).forEach(event -> save(owner, event));

        List<Event> events = this.eventDAO.get(true, "at " + now, null);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        events = this.eventDAO.get(true, "d event starts at " + now, null);

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
        Stream.of(event1, event2, event3).forEach(event -> save(owner, event));

        List<Event> events = this.eventDAO.get(true, null, "at " + now);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        events = this.eventDAO.get(true, null, "d event starts at " + now);

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
        Stream.of(event1, event2, event3).forEach(event -> save(owner, event));

        List<Event> events = this.eventDAO.get(true, "at " + now, "at " + now);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        events = this.eventDAO.get(true, "second", "d event starts at " + now);

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
        Stream.of(event1, event2, event3).forEach(event -> save(owner, event));

        List<Event> events = this.eventDAO.get(true, "at " + now, "at " + now);

        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        event1.setVisible(false);
        this.eventDAO.update(event1);
        assertNotNull(event1.getModified());

        events = this.eventDAO.get(true, "at " + now, "at " + now);

        assertEquals(2, events.size());
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));

        event3.setVisible(false);
        this.eventDAO.update(event3);
        assertNotNull(event3.getModified());

        events = this.eventDAO.get(true, "at " + now, "at " + now);

        assertEquals(1, events.size());
        assertTrue(events.contains(event2));
    }

    @Test
    public void testGetByCoordinatesSuccess() throws Exception {
        // delete existed events
        this.eventDAO.get(null, new Coordinate(50, 30), new Coordinate(51, 31))
                .stream().unordered().forEach(event -> this.eventDAO.delete(event.getId()));

        float  e = 0.003f;    // 3m
        Account owner = getSavedAccount();
        List<Event> events = Stream.of(
                new Coordinate(50.445385, 30.501502),   // ~0
                new Coordinate(50.445173, 30.502908),   // ~100m
                new Coordinate(50.444961, 30.504249),   // ~200m
                new Coordinate(50.444727, 30.505630),   // ~300m
                new Coordinate(50.444507, 30.507001)    // ~400m
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());

        assertEqualsEntities(eventDAO.get(true, events.get(2).getLocation(), (0.2f+e), KM), events.toArray(new Event[events.size()]));
        assertEqualsEntities(eventDAO.get(true, events.get(2).getLocation(), (0.1f+e), KM), events.get(1), events.get(2), events.get(3));
        assertEqualsEntities(eventDAO.get(true, events.get(1).getLocation(), (0.1f+e), KM), events.get(0), events.get(1), events.get(2));
        assertEqualsEntities(eventDAO.get(true, events.get(1).getLocation(), (0.2f+e), KM), events.get(0), events.get(1), events.get(2), events.get(3));
    }

    @Test
    public void testGetByCoordinatesVisibleSuccess() throws Exception {
        // delete existed events
        this.eventDAO.get(null, new Coordinate(50, 30), new Coordinate(51, 31))
                .stream().unordered().forEach(event -> this.eventDAO.delete(event.getId()));

        float  e = 0.003f;    // 3m
        Account owner = getSavedAccount();
        List<Event> events = Stream.of(
                new Coordinate(50.445385, 30.501502),   // ~0
                new Coordinate(50.445173, 30.502908),   // ~100m
                new Coordinate(50.444961, 30.504249),   // ~200m
                new Coordinate(50.444727, 30.505630),   // ~300m
                new Coordinate(50.444507, 30.507001)    // ~400m
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());

        Stream.of(events.get(1), events.get(3)).unordered().forEach(event -> {
            event.setVisible(false);
            this.eventDAO.update(event);
        });

        assertEqualsEntities(eventDAO.get(null, events.get(2).getLocation(), (0.2f+e), KM), events.toArray(new Event[events.size()]));
        assertEqualsEntities(eventDAO.get(true, events.get(2).getLocation(), (0.2f+e), KM), events.get(0), events.get(2), events.get(4));
        assertEqualsEntities(eventDAO.get(false,events.get(2).getLocation(), (0.2f+e), KM), events.get(1), events.get(3));

        assertEqualsEntities(eventDAO.get(null, events.get(2).getLocation(), (0.1f+e), KM), events.get(1), events.get(2), events.get(3));
        assertEqualsEntities(eventDAO.get(true, events.get(2).getLocation(), (0.1f+e), KM), events.get(2));
        assertEqualsEntities(eventDAO.get(false,events.get(2).getLocation(), (0.1f+e), KM), events.get(1), events.get(3));

        assertEqualsEntities(eventDAO.get(null, events.get(1).getLocation(), (0.1f+e), KM), events.get(0), events.get(1), events.get(2));
        assertEqualsEntities(eventDAO.get(true, events.get(1).getLocation(), (0.1f+e), KM), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(false,events.get(1).getLocation(), (0.1f+e), KM), events.get(1));

        assertEqualsEntities(eventDAO.get(null, events.get(1).getLocation(), (0.2f+e), KM), events.get(0), events.get(1), events.get(2), events.get(3));
        assertEqualsEntities(eventDAO.get(true, events.get(1).getLocation(), (0.2f+e), KM), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(false,events.get(1).getLocation(), (0.2f+e), KM), events.get(1), events.get(3));
    }

    @Test
    @Ignore  // business logic doesn't support this case
    public void testGetByCoordinatesVisibleForMemberEventSuccess() throws Exception {
        // delete existed events
        this.eventDAO.get(true, new Coordinate(50, 30), (float) getDistance(50, 30, 51, 31, KM), KM)
                .stream().unordered().map(Event::getId).forEach(id -> this.eventDAO.delete(id));

        float  e = 0.003f;    // 3m

        List<Event> events = Stream.of(
                new Coordinate(50.445385, 30.501502),   // ~0
                new Coordinate(50.445173, 30.502908),   // ~100m
                new Coordinate(50.444961, 30.504249),   // ~200m
                new Coordinate(50.444727, 30.505630),   // ~300m
                new Coordinate(50.444507, 30.507001)    // ~400m
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return event;
        }).collect(toList());

        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();

        save(owner1, events.get(0));
        save(owner1, events.get(1));
        save(owner1, events.get(2)); getSavedAccountEvent(owner2, events.get(2), getRoleMember());
        save(owner2, events.get(3));
        save(owner2, events.get(4));

        Stream.of(events.get(1), events.get(3)).unordered().forEach(event -> {
            event.setVisible(false);
            this.eventDAO.update(event);
        });

        // todo
    }

    @Test
    public void testGetByCoordinatesDeletedSuccess() throws Exception {
        // delete existed events
        this.eventDAO.get(null, new Coordinate(50, 30), new Coordinate(51, 31))
                .stream().unordered().forEach(event -> this.eventDAO.delete(event.getId()));

        float  e = 0.003f;    // 3m
        Account owner = getSavedAccount();
        List<Event> events = Stream.of(
                new Coordinate(50.445385, 30.501502),   // ~0
                new Coordinate(50.445173, 30.502908),   // ~100m
                new Coordinate(50.444961, 30.504249),   // ~200m
                new Coordinate(50.444727, 30.505630),   // ~300m
                new Coordinate(50.444507, 30.507001)    // ~400m
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());

        Stream.of(events.get(1), events.get(3)).unordered().map(Event::getId)
                .forEach(id -> this.eventDAO.setDeleted(id, true));

        assertEqualsEntities(eventDAO.get(null, events.get(2).getLocation(), (0.2f+e), KM), events.get(0), events.get(2), events.get(4));
        assertEqualsEntities(eventDAO.get(true, events.get(2).getLocation(), (0.2f+e), KM), events.get(0), events.get(2), events.get(4));
        assertEqualsEntities(eventDAO.get(false,events.get(2).getLocation(), (0.2f+e), KM));

        assertEqualsEntities(eventDAO.get(null, events.get(2).getLocation(), (0.1f+e), KM), events.get(2));
        assertEqualsEntities(eventDAO.get(true, events.get(2).getLocation(), (0.1f+e), KM), events.get(2));
        assertEqualsEntities(eventDAO.get(false,events.get(2).getLocation(), (0.1f+e), KM));

        assertEqualsEntities(eventDAO.get(null, events.get(1).getLocation(), (0.1f+e), KM), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(true, events.get(1).getLocation(), (0.1f+e), KM), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(false,events.get(1).getLocation(), (0.1f+e), KM));

        assertEqualsEntities(eventDAO.get(null, events.get(1).getLocation(), (0.2f+e), KM), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(true, events.get(1).getLocation(), (0.2f+e), KM), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(false,events.get(1).getLocation(), (0.2f+e), KM));
    }

    @Test
    public void testGetByRangeSuccess() throws Exception {
        // delete existed events
        this.eventDAO.get(null, new Coordinate(50, 30), new Coordinate(51, 31))
                .stream().unordered().forEach(event -> this.eventDAO.delete(event.getId()));

        Account owner = getSavedAccount();
        List<Event> events = Stream.of(
                new Coordinate(50.01, 30.06),
                new Coordinate(50.02, 30.07),
                new Coordinate(50.03, 30.08),
                new Coordinate(50.04, 30.09),
                new Coordinate(50.05, 30.10)
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());

        assertEqualsEntities(this.eventDAO.get(true, new Coordinate(50.01, 30.06), new Coordinate(50.05, 30.10)), events.toArray(new Event[events.size()]));
        assertEqualsEntities(this.eventDAO.get(true, new Coordinate(50.02, 30.07), new Coordinate(50.04, 30.09)), events.get(1), events.get(2), events.get(3));
        assertEqualsEntities(this.eventDAO.get(true, new Coordinate(50.01, 30.06), new Coordinate(50.03, 30.08)), events.get(0), events.get(1), events.get(2));
        assertEqualsEntities(this.eventDAO.get(true, new Coordinate(50.01, 30.06), new Coordinate(50.04, 30.09)), events.get(0), events.get(1), events.get(2), events.get(3));
    }

    @Test
    public void testGetByRangeVisibleSuccess() throws Exception {
        // delete existed events
        this.eventDAO.get(null, new Coordinate(50, 30), new Coordinate(51, 31))
                .stream().unordered().forEach(event -> this.eventDAO.delete(event.getId()));

        Account owner = getSavedAccount();
        List<Event> events = Stream.of(
                new Coordinate(50.01, 30.06),
                new Coordinate(50.02, 30.07),
                new Coordinate(50.03, 30.08),
                new Coordinate(50.04, 30.09),
                new Coordinate(50.05, 30.10)
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());

        Stream.of(events.get(1), events.get(3)).unordered().forEach(event -> {
            event.setVisible(false);
            this.eventDAO.update(event);
        });

        assertEqualsEntities(eventDAO.get(null, new Coordinate(50.01, 30.06), new Coordinate(50.05, 30.10)), events.toArray(new Event[events.size()]));
        assertEqualsEntities(eventDAO.get(true, new Coordinate(50.01, 30.06), new Coordinate(50.05, 30.10)), events.get(0), events.get(2), events.get(4));
        assertEqualsEntities(eventDAO.get(false,new Coordinate(50.01, 30.06), new Coordinate(50.05, 30.10)), events.get(1), events.get(3));

        assertEqualsEntities(eventDAO.get(null, new Coordinate(50.02, 30.07), new Coordinate(50.04, 30.09)), events.get(1), events.get(2), events.get(3));
        assertEqualsEntities(eventDAO.get(true, new Coordinate(50.02, 30.07), new Coordinate(50.04, 30.09)), events.get(2));
        assertEqualsEntities(eventDAO.get(false,new Coordinate(50.02, 30.07), new Coordinate(50.04, 30.09)), events.get(1), events.get(3));

        assertEqualsEntities(eventDAO.get(null, new Coordinate(50.01, 30.06), new Coordinate(50.03, 30.08)), events.get(0), events.get(1), events.get(2));
        assertEqualsEntities(eventDAO.get(true, new Coordinate(50.01, 30.06), new Coordinate(50.03, 30.08)), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(false,new Coordinate(50.01, 30.06), new Coordinate(50.03, 30.08)), events.get(1));

        assertEqualsEntities(eventDAO.get(null, new Coordinate(50.01, 30.06), new Coordinate(50.04, 30.09)), events.get(0), events.get(1), events.get(2), events.get(3));
        assertEqualsEntities(eventDAO.get(true, new Coordinate(50.01, 30.06), new Coordinate(50.04, 30.09)), events.get(0), events.get(2));
        assertEqualsEntities(eventDAO.get(false,new Coordinate(50.01, 30.06), new Coordinate(50.04, 30.09)), events.get(1), events.get(3));
    }

    private void assertEventIsNotDeleted(final Long accountId, final Event event) {
        assertEqualsEntity(event, this.eventDAO.read(event.getId()));
        assertEventIsNotDeleted(accountId, event.getId());
        assertFalse(this.eventDAO.get(true, event.getName(), null).isEmpty());
        assertFalse(this.eventDAO.get(true, null, event.getDescription()).isEmpty());
        assertFalse(this.eventDAO.get(true, event.getName(), event.getDescription()).isEmpty());
    }

    private void assertEventIsDeleted(final Long accountId, final Event event) {
        assertEventIsDeleted(accountId, event.getId());
        assertTrue(this.eventDAO.get(true, event.getName(), null).isEmpty());
        assertTrue(this.eventDAO.get(true, null, event.getDescription()).isEmpty());
        assertTrue(this.eventDAO.get(true, event.getName(), event.getDescription()).isEmpty());
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