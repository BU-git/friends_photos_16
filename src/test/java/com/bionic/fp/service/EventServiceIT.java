package com.bionic.fp.service;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * This is an integration test that verifies {@link EventService}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateful-spring-security.xml"})
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
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());
        assertFalse(actual.isDeleted());

        assertCustomEqualsDate(event.getCreated(), actual.getCreated());
        assertNull(actual.getModified());
        assertNull(event.getModified());
        assertNull(actual.getExpireDate());
        assertNull(event.getExpireDate());

        Account actualOwner = getEventOwner(actual.getId());
        assertEquals(actualOwner.getId(), owner.getId());
        assertEquals(actualOwner.getEmail(), owner.getEmail());
        assertEquals(actualOwner.getUserName(), owner.getUserName());

        AccountEvent accountEvent = this.accountEventService.get(owner.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
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

    @Test(expected = EntityNotFoundException.class)
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
    public void testSoftDeleteByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);
        assertFalse(actual.isDeleted());
        assertNull(actual.getModified());

        this.eventService.softDelete(event.getId());

        assertFalse(event.isDeleted());
        assertNull(this.eventService.get(event.getId()));
    }

    @Test(expected = InvalidParameterException.class)
    public void testSoftDeleteByIdEventIdNullFailure() {
        this.eventService.softDelete(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testSoftDeleteByIdEventIdNotFoundFailure() {
        this.eventService.softDelete(Long.MAX_VALUE);
    }

    @Test
    public void testDeleteByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);

        this.eventService.delete(event.getId());

        actual = this.eventService.get(event.getId());
        assertNull(actual);
    }

    @Test
    public void testUpdateByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        Event actual = this.eventService.get(event.getId());
        assertNotNull(actual);
        assertNull(actual.getModified());

        assertEquals(actual.getId(), event.getId());
        assertEquals(actual.getName(), event.getName());
        assertEquals(actual.getDescription(), event.getDescription());
        assertEquals(actual.getEventType(), event.getEventType());
        assertEquals(actual.getLatitude(), event.getLatitude());
        assertEquals(actual.getLongitude(), event.getLongitude());
        assertEquals(actual.getRadius(), event.getRadius());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());
        assertCustomEqualsDate(event.getCreated(), actual.getCreated());

        updateEvent(actual);

        assertEquals(actual.getId(), event.getId());
        assertNotEquals(actual.getName(), event.getName());
        assertNotEquals(actual.getDescription(), event.getDescription());
        assertNotEquals(actual.getLatitude(), event.getLatitude());
        assertNotEquals(actual.getLongitude(), event.getLongitude());
        assertNotEquals(actual.getRadius(), event.getRadius());
        assertNotEquals(actual.isVisible(), event.isVisible());
        assertNotEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());
        assertCustomEqualsDate(event.getCreated(), actual.getCreated());

        Event updated = this.eventService.update(actual);
        assertNotNull(updated);
//        assertNotNull(updated.getModified());

        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getName(), updated.getName());
        assertEquals(actual.getDescription(), updated.getDescription());
        assertEquals(actual.getEventType(), updated.getEventType());
        assertEquals(actual.getLatitude(), updated.getLatitude());
        assertEquals(actual.getLongitude(), updated.getLongitude());
        assertEquals(actual.getRadius(), updated.getRadius());
        assertEquals(actual.isVisible(), updated.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), updated.isGeoServicesEnabled());
        assertCustomEqualsDate(event.getCreated(), actual.getCreated());

        assertNotNull(updated.getModified());

        actual = this.eventService.get(event.getId());
        assertNotNull(actual);
        assertNotNull(actual.getModified());

        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getName(), updated.getName());
        assertEquals(actual.getDescription(), updated.getDescription());
        assertEquals(actual.getEventType(), updated.getEventType());
        assertEquals(actual.getLatitude(), updated.getLatitude());
        assertEquals(actual.getLongitude(), updated.getLongitude());
        assertEquals(actual.getRadius(), updated.getRadius());
        assertEquals(actual.isVisible(), updated.isVisible());
        assertEquals(actual.isGeoServicesEnabled(), updated.isGeoServicesEnabled());
        assertCustomEqualsDate(event.getCreated(), actual.getCreated());
    }

    @Test
    public void testUpdateByIdUsingNewEventSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Event newEvent = updateEvent(getNewEventMax());

        newEvent.setId(event.getId());
        newEvent.setCreated(LocalDateTime.now());

        assertEquals(newEvent.getId(), event.getId());
        assertNotEquals(newEvent.getName(), event.getName());
        assertNotEquals(newEvent.getDescription(), event.getDescription());
        assertNotEquals(newEvent.getLatitude(), event.getLatitude());
        assertNotEquals(newEvent.getLongitude(), event.getLongitude());
        assertNotEquals(newEvent.getRadius(), event.getRadius());
        assertNotEquals(newEvent.isVisible(), event.isVisible());
        assertNotEquals(newEvent.isGeoServicesEnabled(), event.isGeoServicesEnabled());
        assertNull(event.getModified());
        assertNull(newEvent.getModified());


        Event updated = this.eventService.update(newEvent);
        assertNotNull(updated);
        assertNotNull(updated.getModified());

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
        assertNotNull(newEvent.getModified());

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
        assertCustomEqualsDate(event.getCreated(), actual.getCreated());
    }

    @Test
    public void testAddAccountToEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, null);

        AccountEvent accountEvent = this.accountEventService.get(user1.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user2.getId(), event.getId(), role, null);

        accountEvent = this.accountEventService.get(user2.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user2.getId()).size());

        Event newEvent = getSavedEventMax(user2);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(owner.getId(), newEvent.getId(), role, null);

        accountEvent = this.accountEventService.get(owner.getId(), newEvent.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(newEvent.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(2, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), newEvent.getId(), role, null);

        accountEvent = this.accountEventService.get(user1.getId(), newEvent.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(newEvent.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(3, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());
    }

    @Test
    public void testAddAccountToPrivateEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, event.getPassword());

        AccountEvent accountEvent = this.accountEventService.get(user1.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user2.getId(), event.getId(), role, event.getPassword());

        accountEvent = this.accountEventService.get(user2.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user2.getId()).size());

        Event newEvent = getSavedEventMax(user2);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(owner.getId(), newEvent.getId(), role, event.getPassword());

        accountEvent = this.accountEventService.get(owner.getId(), newEvent.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(newEvent.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(2, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), newEvent.getId(), role, event.getPassword());

        accountEvent = this.accountEventService.get(user1.getId(), newEvent.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(newEvent.getModified());
        assertNull(owner.getModified());
        assertNull(user1.getModified());
        assertNull(user2.getModified());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(3, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());
    }

    @Test(expected = InvalidParameterException.class)
    public void testAddAccountToPrivateEventPasswordNullFailure() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, null);
    }

    @Test(expected = InvalidParameterException.class)
    public void testAddAccountToPrivateEventWrongPasswordFailure() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), role, event.getPassword() + "!");
    }

    @Test
    public void testChangeAccountRoleInEventSuccess() {
        Account owner = getSavedAccount();
        Account user = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Role roleAdmin = getRoleAdmin();
        Role roleMember = getRoleMember();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), roleMember, null);

        AccountEvent accountEvent = this.accountEventService.get(user.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user.getModified());

        assertEquals(roleMember, accountEvent.getRole());

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), roleAdmin, null);

        accountEvent = this.accountEventService.get(user.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNotNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user.getModified());

        assertEquals(roleAdmin, accountEvent.getRole());

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user.getId()).size());
    }

    @Test(expected = InvalidParameterException.class)
    public void testChangeAccountRoleToOwnerInEventFailure() {
        Account owner = getSavedAccount();
        Account user = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Role roleOwner = getRoleOwner();
        Role roleMember = getRoleMember();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), roleMember, null);

        AccountEvent accountEvent = this.accountEventService.get(user.getId(), event.getId());
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getCreated());
        assertNull(accountEvent.getModified());
        assertNull(event.getModified());
        assertNull(owner.getModified());
        assertNull(user.getModified());

        assertEquals(roleMember, accountEvent.getRole());

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), roleOwner, null);
    }

    @Test
    public void testGetEventAccountsSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSavedEventMax(owner);

        List<Account> accounts = this.accountEventService.getAccounts(event.getId());
        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        accounts.forEach(account -> {
            assertEquals(account.getId(), owner.getId());
            assertEquals(account.getEmail(), owner.getEmail());
            assertEquals(account.getUserName(), owner.getUserName());
        });

        this.eventService.addOrUpdateAccountToEvent(user1.getId(), event.getId(), this.roleService.getOwner(), null);

        accounts = this.accountEventService.getAccounts(event.getId());
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(owner));
        assertTrue(accounts.contains(user1));
        assertFalse(accounts.contains(user2));

        this.eventService.addOrUpdateAccountToEvent(user2.getId(), event.getId(), this.roleService.getOwner(), null);

        accounts = this.accountEventService.getAccounts(event.getId());
        assertNotNull(accounts);
        assertEquals(3, accounts.size());
        assertTrue(accounts.contains(owner));
        assertTrue(accounts.contains(user1));
        assertTrue(accounts.contains(user2));
    }

    private void assertCustomEqualsDate(final LocalDateTime expected, final LocalDateTime actual) {
        assertTrue(Duration.between(expected, actual).getSeconds() < 1L);
    }
}