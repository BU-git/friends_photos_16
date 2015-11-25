package com.bionic.fp.service;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventService}
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class EventServiceIT {

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCreateEventSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);

        assertNotNull(ownerId);

        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("testCreateEventSuccess");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);

        Long eventId = this.eventService.createEvent(ownerId, event);

        assertNotNull(eventId);

        Event actual = this.eventService.getByIdWithOwner(eventId);

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
        assertEquals(actual.getOwner().getId(), event.getOwner().getId());
        assertEquals(actual.getOwner().getEmail(), event.getOwner().getEmail());
        assertEquals(actual.getOwner().getUserName(), event.getOwner().getUserName());
        assertEquals(actual.getOwner().getPassword(), event.getOwner().getPassword());
        assertEquals(actual.getOwner().getId(), owner.getId());
        assertEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertEquals(actual.getOwner().getPassword(), owner.getPassword());
    }

    @Test
    public void testCreateEventEventInvalidFailure() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);

        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        Event event = new Event();

        // without name, description, type
        event.setName(null);
        event.setDescription(null);
        event.setEventType(null);

        assertNull(this.eventService.createEvent(ownerId, event));

        // without description, type
        event.setName("NY 2016");
        event.setDescription(null);
        event.setEventType(null);

        assertNull(this.eventService.createEvent(ownerId, event));

        // without name, description
        event.setName(null);
        event.setDescription(null);
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(ownerId, event));

        // without name, type
        event.setName(null);
        event.setDescription("Happy New Year!");
        event.setEventType(null);

        assertNull(this.eventService.createEvent(ownerId, event));

        // without name
        event.setName(null);
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(ownerId, event));

        // without description
        event.setName("NY 2016");
        event.setDescription(null);
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(ownerId, event));

        // without event type
        event.setName("NY 2016");
        event.setDescription("Happy New Year!");
        event.setEventType(null);

        assertNull(this.eventService.createEvent(ownerId, event));
    }

    @Test
    public void testCreateEventOwnerIdNullFailure() {
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        // valid event
        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(null, event));
    }

    @Test
    public void testCreateEventOwnerIdNotFoundFailure() {
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        // valid event
        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);

        assertNull(this.eventService.createEvent(Long.MAX_VALUE, event));
    }

    @Test
    public void testRemoveByIdSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);

        assertNotNull(ownerId);

        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("testRemoveByIdSuccess");
        event.setEventType(privateEvent);
        assertFalse(event.isDeleted());

        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);
        assertFalse(event.isDeleted());
        Event actual = this.eventService.getById(eventId);
        assertNotNull(actual);
        assertFalse(actual.isDeleted());

        assertTrue(this.eventService.removeById(eventId));

        assertFalse(event.isDeleted());
        actual = this.eventService.getById(eventId);
        assertNotNull(actual);
        assertTrue(actual.isDeleted());
    }

    @Test
    public void testRemoveByIdEventIdNullFailure() {
        assertFalse(this.eventService.removeById(null));
    }

    @Test
    public void testRemoveByIdEventIdNotFoundFailure() {
        assertFalse(this.eventService.removeById(Long.MAX_VALUE));
    }

    @Test
    public void testRemoveByIdPhysicallySuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);

        assertNotNull(ownerId);

        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        Event event = new Event();
        event.setName("NY 2018");
        event.setDescription("testRemoveByIdPhysicallySuccess");
        event.setEventType(privateEvent);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getById(eventId);
        assertNotNull(actual);

        this.eventService.removeByIdPhysically(eventId);

        actual = this.eventService.getById(eventId);
        assertNull(actual);
    }

    @Test
    public void testUpdateByIdUsingLazyOwnerSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("-Failure!-");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getById(eventId);
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

        actual.setName("NY 2018");
        actual.setDescription("testUpdateByIdSuccess");
        System.out.println(actual.getEventType());
        actual.setVisible(false);
        actual.setLatitude(15.5);
        actual.setLongitude(25.3);
        actual.setRadius(0.2f);
        actual.setGeoServicesEnabled(false);

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

        actual = this.eventService.getById(eventId);
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
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("-Failure!-");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getByIdWithOwner(eventId);
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

        actual.setName("NY 2018");
        actual.setDescription("testUpdateByIdSuccess");
        System.out.println(actual.getEventType());
        actual.setVisible(false);
        actual.setLatitude(15.5);
        actual.setLongitude(25.3);
        actual.setRadius(0.2f);
        actual.setGeoServicesEnabled(false);

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

        actual = this.eventService.getById(eventId);
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
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("-Failure!-");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event newEvent = new Event();

        newEvent.setId(eventId);
        newEvent.setName("NY 2018");
        newEvent.setDescription("testUpdateByIdSuccess");
        newEvent.setEventType(privateEvent);
        newEvent.setVisible(false);
        newEvent.setLatitude(15.5);
        newEvent.setLongitude(25.3);
        newEvent.setRadius(0.2f);
        newEvent.setGeoServicesEnabled(false);
        newEvent.setOwner(owner);

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

        newEvent = this.eventService.getById(eventId);
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

    @Test
    public void testUpdateByIdEventNullFailure() {
        assertNull(this.eventService.update(null));
    }

    @Test
    public void testUpdateByIdChangeOwnerFailure() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        Account newOwner = new Account("yaya2@gmail.com", "Yaya2", "yaya2");
        Long newOwnerId = this.accountService.addAccount(newOwner);
        assertNotNull(newOwnerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("testUpdateByIdOwnerFailure");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getByIdWithOwner(eventId);
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

        assertEquals(actual.getOwner().getId(), event.getOwner().getId());
        assertEquals(actual.getOwner().getEmail(), event.getOwner().getEmail());
        assertEquals(actual.getOwner().getUserName(), event.getOwner().getUserName());
        assertEquals(actual.getOwner().getPassword(), event.getOwner().getPassword());
        assertEquals(actual.getOwner().getId(), owner.getId());
        assertEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertEquals(actual.getOwner().getPassword(), owner.getPassword());

        actual.setName("NY 2018");
        actual.setDescription("testUpdateByIdSuccess");
        actual.setVisible(false);
        actual.setLatitude(15.5);
        actual.setLongitude(25.3);
        actual.setRadius(0.2f);
        actual.setGeoServicesEnabled(false);
        actual.setOwner(newOwner);

        assertEquals(actual.getId(), event.getId());
        assertNotEquals(actual.getName(), event.getName());
        assertNotEquals(actual.getDescription(), event.getDescription());
        assertNotEquals(actual.getLatitude(), event.getLatitude());
        assertNotEquals(actual.getLongitude(), event.getLongitude());
        assertNotEquals(actual.getRadius(), event.getRadius());
        assertNotEquals(actual.isVisible(), event.isVisible());
        assertNotEquals(actual.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        assertNotEquals(actual.getOwner().getId(), event.getOwner().getId());
        assertNotEquals(actual.getOwner().getEmail(), event.getOwner().getEmail());
        assertNotEquals(actual.getOwner().getUserName(), event.getOwner().getUserName());
        assertNotEquals(actual.getOwner().getPassword(), event.getOwner().getPassword());
        assertNotEquals(actual.getOwner().getId(), owner.getId());
        assertNotEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertNotEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertNotEquals(actual.getOwner().getPassword(), owner.getPassword());

        assertNull(this.eventService.update(actual));
    }

    @Test
    public void testUpdateByIdChangeOwnerUsingNewEventFailure() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        Account newOwner = new Account("yaya2@gmail.com", "Yaya2", "yaya2");
        Long newOwnerId = this.accountService.addAccount(newOwner);
        assertNotNull(newOwnerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("testUpdateByIdOwnerFailure");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event newEvent = new Event();

        newEvent.setId(eventId);
        newEvent.setName("NY 2018");
        newEvent.setDescription("testUpdateByIdSuccess");
        newEvent.setEventType(privateEvent);
        newEvent.setVisible(false);
        newEvent.setLatitude(15.5);
        newEvent.setLongitude(25.3);
        newEvent.setRadius(0.2f);
        newEvent.setGeoServicesEnabled(false);
        newEvent.setOwner(newOwner);

        assertEquals(newEvent.getId(), event.getId());
        assertNotEquals(newEvent.getName(), event.getName());
        assertNotEquals(newEvent.getDescription(), event.getDescription());
        assertNotEquals(newEvent.getLatitude(), event.getLatitude());
        assertNotEquals(newEvent.getLongitude(), event.getLongitude());
        assertNotEquals(newEvent.getRadius(), event.getRadius());
        assertNotEquals(newEvent.isVisible(), event.isVisible());
        assertNotEquals(newEvent.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        assertNotEquals(newEvent.getOwner().getId(), event.getOwner().getId());
        assertNotEquals(newEvent.getOwner().getEmail(), event.getOwner().getEmail());
        assertNotEquals(newEvent.getOwner().getUserName(), event.getOwner().getUserName());
        assertNotEquals(newEvent.getOwner().getPassword(), event.getOwner().getPassword());
        assertNotEquals(newEvent.getOwner().getId(), owner.getId());
        assertNotEquals(newEvent.getOwner().getEmail(), owner.getEmail());
        assertNotEquals(newEvent.getOwner().getUserName(), owner.getUserName());
        assertNotEquals(newEvent.getOwner().getPassword(), owner.getPassword());

        assertNull(this.eventService.update(newEvent));
    }

    @Test
    public void testUpdateByIdUsingNewEventOwnerNullFailure() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("-Failure!-");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event newEvent = new Event();

        newEvent.setId(eventId);
        newEvent.setName("NY 2018");
        newEvent.setDescription("testUpdateByIdSuccess");
        newEvent.setEventType(privateEvent);
        newEvent.setVisible(false);
        newEvent.setLatitude(15.5);
        newEvent.setLongitude(25.3);
        newEvent.setRadius(0.2f);
        newEvent.setGeoServicesEnabled(false);

        assertEquals(newEvent.getId(), event.getId());
        assertNotEquals(newEvent.getName(), event.getName());
        assertNotEquals(newEvent.getDescription(), event.getDescription());
        assertNotEquals(newEvent.getLatitude(), event.getLatitude());
        assertNotEquals(newEvent.getLongitude(), event.getLongitude());
        assertNotEquals(newEvent.getRadius(), event.getRadius());
        assertNotEquals(newEvent.isVisible(), event.isVisible());
        assertNotEquals(newEvent.isGeoServicesEnabled(), event.isGeoServicesEnabled());

        assertNull(this.eventService.update(newEvent));
    }

    @Test
    public void testGetByIdSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2019");
        event.setDescription("testGetByIdSuccess");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);

        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getById(eventId);
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
    public void testGetByIdWithOwnerSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2020");
        event.setDescription("testGetByIdWithOwnerSuccess");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);

        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getByIdWithOwner(eventId);
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

        assertEquals(actual.getOwner().getId(), event.getOwner().getId());
        assertEquals(actual.getOwner().getEmail(), event.getOwner().getEmail());
        assertEquals(actual.getOwner().getUserName(), event.getOwner().getUserName());
        assertEquals(actual.getOwner().getPassword(), event.getOwner().getPassword());
        assertEquals(actual.getOwner().getId(), owner.getId());
        assertEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertEquals(actual.getOwner().getPassword(), owner.getPassword());
    }

    @Test
    public void testGetByIdWithOwnerAndAccountsSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2021");
        event.setDescription("testGetByIdWithOwnerAndAccountsSuccess");
        event.setEventType(privateEvent);
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(true);

        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getByIdWithOwnerAndAccounts(eventId);
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

        assertEquals(actual.getOwner().getId(), event.getOwner().getId());
        assertEquals(actual.getOwner().getEmail(), event.getOwner().getEmail());
        assertEquals(actual.getOwner().getUserName(), event.getOwner().getUserName());
        assertEquals(actual.getOwner().getPassword(), event.getOwner().getPassword());
        assertEquals(actual.getOwner().getId(), owner.getId());
        assertEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertEquals(actual.getOwner().getPassword(), owner.getPassword());

        assertFalse(actual.getAccounts().isEmpty());
        assertFalse(event.getAccounts().isEmpty());
        assertEquals(actual.getAccounts().size(), event.getAccounts().size());
    }
}