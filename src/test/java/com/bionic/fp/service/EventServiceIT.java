package com.bionic.fp.service;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventService}
 *
 * @author Sergiy Gabriel
 */
public class EventServiceIT extends AbstractIT {

    @Test
    public void testCreateEventSuccess() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("testCreateEventSuccess");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);

        Event actual = this.eventService.get(eventId);

        assertNotNull(actual);
        assertEquals(actual.getId(), event.getId());
        assertEquals(actual.getName(), event.getName());
        assertEquals(actual.getDescription(), event.getDescription());
        assertEquals(actual.getEventType(), event.getEventType());
        assertEquals(actual.getLatitude(), event.getLatitude());
        assertEquals(actual.getLongitude(), event.getLongitude());
        assertEquals(actual.getRadius(), event.getRadius());

        // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
        // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//        assertEquals(actual.getDate().format(LOCAL_DATE_TIME), event.getDate().format(LOCAL_DATE_TIME));
//        assertEquals(actual.getExpireDate(), event.getExpireDate());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        Account actualOwner = getEventOwner(actual.getId());
        assertEquals(actualOwner.getId(), owner.getId());
        assertEquals(actualOwner.getEmail(), owner.getEmail());
        assertEquals(actualOwner.getUserName(), owner.getUserName());
        assertEquals(actualOwner.getPassword(), owner.getPassword());
    }

    @Test(expected = InvalidParameterException.class)
    public void testCreateEventEventInvalidFailure() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        Event event = new Event();

        // without name, description, type
        event.setName(null);
        event.setDescription(null);
        event.setEventType(null);

        try {
            assertNull(this.eventService.createEvent(owner.getId(), event));
            fail();
        } catch (InvalidParameterException ignored) {}

        // without description, type
        event.setName("NY 2016");
        event.setDescription(null);
        event.setEventType(null);

        try {
            assertNull(this.eventService.createEvent(owner.getId(), event));
            fail();
        } catch (InvalidParameterException ignored) {}

        // without name, description
        event.setName(null);
        event.setDescription(null);
        event.setEventType(privateEvent);

        try {
            assertNull(this.eventService.createEvent(owner.getId(), event));
            fail();
        } catch (InvalidParameterException ignored) {}

        // without name, type
        event.setName(null);
        event.setDescription("Happy New Year!");
        event.setEventType(null);

        try {
            assertNull(this.eventService.createEvent(owner.getId(), event));
            fail();
        } catch (InvalidParameterException ignored) {}

        // without name
        event.setName(null);
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);

        try {
            assertNull(this.eventService.createEvent(owner.getId(), event));
            fail();
        } catch (InvalidParameterException ignored) {}

        // without description
        event.setName("NY 2016");
        event.setDescription(null);
        event.setEventType(privateEvent);

        try {
            assertNull(this.eventService.createEvent(owner.getId(), event));
            fail();
        } catch (InvalidParameterException ignored) {}

        // without event type
        event.setName("NY 2016");
        event.setDescription("Happy New Year!");
        event.setEventType(null);

        assertNull(this.eventService.createEvent(owner.getId(), event));
    }

    @Test(expected = InvalidParameterException.class)
    public void testCreateEventOwnerIdNullFailure() {
        EventType privateEvent = getPrivateEventType();

        // valid event
        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(null, event));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testCreateEventOwnerIdNotFoundFailure() {
        EventType privateEvent = getPrivateEventType();

        // valid event
        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(Long.MAX_VALUE, event));
    }

    @Test
    public void testRemoveByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);
        assertFalse(actual.isDeleted());

        this.eventService.remove(event.getId());

        assertFalse(event.isDeleted());
        actual = this.eventService.get(event.getId());
        assertNotNull(actual);
        assertTrue(actual.isDeleted());
    }

    @Test(expected = InvalidParameterException.class)
    public void testRemoveByIdEventIdNullFailure() {
        this.eventService.remove(null);
    }

    @Test(expected = EventNotFoundException.class)
    public void testRemoveByIdEventIdNotFoundFailure() {
        this.eventService.remove(Long.MAX_VALUE);
    }

    @Test
    public void testRemoveByIdPhysicallySuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        this.eventService.removePhysically(event.getId());

        actual = this.eventService.get(event.getId());
        assertNull(actual);
    }

    @Test
    public void testUpdateByIdUsingLazyOwnerSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        assertEquals(actual.getId(), event.getId());
        assertEquals(actual.getName(), event.getName());
        assertEquals(actual.getDescription(), event.getDescription());
        assertEquals(actual.getEventType(), event.getEventType());
        assertEquals(actual.getLatitude(), event.getLatitude());
        assertEquals(actual.getLongitude(), event.getLongitude());
        assertEquals(actual.getRadius(), event.getRadius());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        updateEvent(actual);

        assertEquals(actual.getId(), event.getId());
        assertNotEquals(actual.getName(), event.getName());
        assertNotEquals(actual.getDescription(), event.getDescription());
        assertNotEquals(actual.getLatitude(), event.getLatitude());
        assertNotEquals(actual.getLongitude(), event.getLongitude());
        assertNotEquals(actual.getRadius(), event.getRadius());
        assertNotEquals(actual.isVisible(), event.isVisible());
        assertNotEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        Event updated = this.eventService.update(actual);
        assertNotNull(updated);

        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getName(), updated.getName());
        assertEquals(actual.getDescription(), updated.getDescription());
        assertEquals(actual.getEventType(), updated.getEventType());
        assertEquals(actual.getLatitude(), updated.getLatitude());
        assertEquals(actual.getLongitude(), updated.getLongitude());
        assertEquals(actual.getRadius(), updated.getRadius());
        assertEquals(actual.isVisible(), updated.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), updated.isGeoServicesEnabled());

        actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getName(), updated.getName());
        assertEquals(actual.getDescription(), updated.getDescription());
        assertEquals(actual.getEventType(), updated.getEventType());
        assertEquals(actual.getLatitude(), updated.getLatitude());
        assertEquals(actual.getLongitude(), updated.getLongitude());
        assertEquals(actual.getRadius(), updated.getRadius());
        assertEquals(actual.isVisible(), updated.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), updated.isGeoServicesEnabled());
    }

    @Test
    public void testUpdateByIdUsingOwnerEagerSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        assertEquals(actual.getId(), event.getId());
        assertEquals(actual.getName(), event.getName());
        assertEquals(actual.getDescription(), event.getDescription());
        assertEquals(actual.getEventType(), event.getEventType());
        assertEquals(actual.getLatitude(), event.getLatitude());
        assertEquals(actual.getLongitude(), event.getLongitude());
        assertEquals(actual.getRadius(), event.getRadius());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        updateEvent(actual);

        assertEquals(actual.getId(), event.getId());
        assertNotEquals(actual.getName(), event.getName());
        assertNotEquals(actual.getDescription(), event.getDescription());
        assertNotEquals(actual.getLatitude(), event.getLatitude());
        assertNotEquals(actual.getLongitude(), event.getLongitude());
        assertNotEquals(actual.getRadius(), event.getRadius());
        assertNotEquals(actual.isVisible(), event.isVisible());
        assertNotEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        Event updated = this.eventService.update(actual);
        assertNotNull(updated);

        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getName(), updated.getName());
        assertEquals(actual.getDescription(), updated.getDescription());
        assertEquals(actual.getEventType(), updated.getEventType());
        assertEquals(actual.getLatitude(), updated.getLatitude());
        assertEquals(actual.getLongitude(), updated.getLongitude());
        assertEquals(actual.getRadius(), updated.getRadius());
        assertEquals(actual.isVisible(), updated.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), updated.isGeoServicesEnabled());

        actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getName(), updated.getName());
        assertEquals(actual.getDescription(), updated.getDescription());
        assertEquals(actual.getEventType(), updated.getEventType());
        assertEquals(actual.getLatitude(), updated.getLatitude());
        assertEquals(actual.getLongitude(), updated.getLongitude());
        assertEquals(actual.getRadius(), updated.getRadius());
        assertEquals(actual.isVisible(), updated.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), updated.isGeoServicesEnabled());
    }

    @Test
    public void testUpdateByIdUsingNewEventSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Event newEvent = updateEvent(getNewEventMax());

        newEvent.setId(event.getId());

        assertEquals(newEvent.getId(), event.getId());
        assertNotEquals(newEvent.getName(), event.getName());
        assertNotEquals(newEvent.getDescription(), event.getDescription());
        assertNotEquals(newEvent.getLatitude(), event.getLatitude());
        assertNotEquals(newEvent.getLongitude(), event.getLongitude());
        assertNotEquals(newEvent.getRadius(), event.getRadius());
        assertNotEquals(newEvent.isVisible(), event.isVisible());
        assertNotEquals(newEvent.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        Event updated = this.eventService.update(newEvent);
        assertNotNull(updated);

        assertEquals(newEvent.getId(), updated.getId());
        assertEquals(newEvent.getName(), updated.getName());
        assertEquals(newEvent.getDescription(), updated.getDescription());
        assertEquals(newEvent.getEventType(), updated.getEventType());
        assertEquals(newEvent.getLatitude(), updated.getLatitude());
        assertEquals(newEvent.getLongitude(), updated.getLongitude());
        assertEquals(newEvent.getRadius(), updated.getRadius());
        assertEquals(newEvent.isVisible(), updated.isVisible());
        assertEquals(newEvent.isGeoServicesEnabled(), updated.isGeoServicesEnabled());

        newEvent = this.eventService.get(event.getId());
        assertNotNull(newEvent);

        assertEquals(newEvent.getId(), updated.getId());
        assertEquals(newEvent.getName(), updated.getName());
        assertEquals(newEvent.getDescription(), updated.getDescription());
        assertEquals(newEvent.getEventType(), updated.getEventType());
        assertEquals(newEvent.getLatitude(), updated.getLatitude());
        assertEquals(newEvent.getLongitude(), updated.getLongitude());
        assertEquals(newEvent.getRadius(), updated.getRadius());
        assertEquals(newEvent.isVisible(), updated.isVisible());
        assertEquals(newEvent.isGeoServicesEnabled(), updated.isGeoServicesEnabled());
    }

    @Test(expected = InvalidParameterException.class)
    public void testUpdateByIdEventNullFailure() {
        assertNull(this.eventService.update(null));
    }

    @Test
    public void testGetByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        assertEquals(actual.getId(), event.getId());
        assertEquals(actual.getName(), event.getName());
        assertEquals(actual.getDescription(), event.getDescription());
        assertEquals(actual.getEventType(), event.getEventType());
        assertEquals(actual.getLatitude(), event.getLatitude());
        assertEquals(actual.getLongitude(), event.getLongitude());
        assertEquals(actual.getRadius(), event.getRadius());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());
    }

    @Test
    public void testGetByIdWithAccountsSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        Event actual = this.eventService.getWithAccounts(event.getId());
        assertNotNull(actual);

        assertEquals(actual.getId(), event.getId());
        assertEquals(actual.getName(), event.getName());
        assertEquals(actual.getDescription(), event.getDescription());
        assertEquals(actual.getEventType(), event.getEventType());
        assertEquals(actual.getLatitude(), event.getLatitude());
        assertEquals(actual.getLongitude(), event.getLongitude());
        assertEquals(actual.getRadius(), event.getRadius());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        assertFalse(actual.getAccounts().isEmpty());
        assertFalse(event.getAccounts().isEmpty());
        assertEquals(actual.getAccounts().size(), event.getAccounts().size());
    }

    @Test
    public void testAddAccountToEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, null);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user2.getId(), event.getId(), role, null);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        Event newEvent = getSavedEventMax(user2);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(owner.getId(), newEvent.getId(), role, null);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(2, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), newEvent.getId(), role, null);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(3, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());
    }

    @Test
    public void testAddAccountToPrivateEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, event.getPassword());

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user2.getId(), event.getId(), role, event.getPassword());

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        Event newEvent = getSavedEventMax(user2);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(owner.getId(), newEvent.getId(), role, event.getPassword());

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(2, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), newEvent.getId(), role, event.getPassword());

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(3, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());
    }

    @Test(expected = InvalidParameterException.class)
    public void testAddAccountToPrivateEventPasswordNullFailure() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, null);
    }

    @Test(expected = InvalidParameterException.class)
    public void testAddAccountToPrivateEventWrongPasswordFailure() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, event.getPassword() + "!");
    }

    @Test
    public void testGetEventAccountsSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSavedEventMax(owner);

        List<Account> accounts = this.eventService.getAccounts(event.getId());
        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        accounts.forEach(account -> {
            assertEquals(account.getId(), owner.getId());
            assertEquals(account.getEmail(), owner.getEmail());
            assertEquals(account.getPassword(), owner.getPassword());
            assertEquals(account.getUserName(), owner.getUserName());
        });

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), this.roleService.getOwner(), null);

        accounts = this.eventService.getAccounts(event.getId());
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(owner));
        assertTrue(accounts.contains(user1));
        assertFalse(accounts.contains(user2));

        this.eventService.addOrUpdateAccountToEvent(user2.getId(), event.getId(), this.roleService.getOwner(), null);

        accounts = this.eventService.getAccounts(event.getId());
        assertNotNull(accounts);
        assertEquals(3, accounts.size());
        assertTrue(accounts.contains(owner));
        assertTrue(accounts.contains(user1));
        assertTrue(accounts.contains(user2));
    }

}