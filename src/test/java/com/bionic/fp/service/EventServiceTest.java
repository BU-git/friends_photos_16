package com.bionic.fp.service;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
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

import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;
import static org.junit.Assert.*;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountEventService accountEventService;

    @Autowired
    private RoleService roleService;

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

        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("testCreateEventSuccess");
        event.setEventType(this.eventTypeService.getPrivate());
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

        System.err.println(actual.getDate().format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.err.println(event.getDate().format(DateTimeFormatter.ISO_LOCAL_TIME));
        // sometimes failure 16:51:53 == 16:51:53.213 but 16:51:53 != 16:51:53.599
        assertEquals(actual.getDate().format(LOCAL_DATE_TIME), event.getDate().format(LOCAL_DATE_TIME));
        assertEquals(actual.getExpireDate(), event.getExpireDate());
        assertEquals(actual.isVisible(), event.isVisible());
        assertEquals(actual.isGeolocationServicesEnabled(), event.isGeolocationServicesEnabled());
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
    public void testRemoveByIdSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);

        assertNotNull(ownerId);

        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("testRemoveByIdSuccess");
        event.setEventType(this.eventTypeService.getPrivate());
        assertFalse(event.isDeleted());

        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);
        assertFalse(event.isDeleted());
        Event actual = this.eventService.getById(eventId);
        assertNotNull(actual);
        assertFalse(actual.isDeleted());

        this.eventService.removeById(eventId);

        assertFalse(event.isDeleted());
        actual = this.eventService.getById(eventId);
        assertNotNull(actual);
        assertTrue(actual.isDeleted());
    }

    @Test
    public void testRemoveByIdPhysicallySuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);

        assertNotNull(ownerId);

        Event event = new Event();
        event.setName("NY 2018");
        event.setDescription("testRemoveByIdPhysicallySuccess");
        event.setEventType(this.eventTypeService.getPrivate());
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        Event actual = this.eventService.getById(eventId);
        assertNotNull(actual);

        this.eventService.removeByIdPhysically(eventId);

        actual = this.eventService.getById(eventId);
        assertNull(actual);
    }

    @Test
    public void testUpdateByIdSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("-Failure!-");
        event.setEventType(this.eventTypeService.getPrivate());
        event.setVisible(true);
        event.setLatitude(15.0);
        event.setLongitude(25.0);
        event.setRadius(0.1f);
        event.setGeolocationServicesEnabled(true);

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
        assertEquals(actual.isGeolocationServicesEnabled(), event.isGeolocationServicesEnabled());

        actual.setName("NY 2018");
        actual.setDescription("testUpdateByIdSuccess");
        actual.setVisible(false);
        actual.setLatitude(15.5);
        actual.setLongitude(25.3);
        actual.setRadius(0.2f);
        actual.setGeolocationServicesEnabled(false);

        assertEquals(actual.getId(), event.getId());
        assertNotEquals(actual.getName(), event.getName());
        assertNotEquals(actual.getDescription(), event.getDescription());
        assertNotEquals(actual.getLatitude(), event.getLatitude());
        assertNotEquals(actual.getLongitude(), event.getLongitude());
        assertNotEquals(actual.getRadius(), event.getRadius());
        assertNotEquals(actual.isVisible(), event.isVisible());
        assertNotEquals(actual.isGeolocationServicesEnabled(), event.isGeolocationServicesEnabled());

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
        assertEquals(actual.isGeolocationServicesEnabled(), updated.isGeolocationServicesEnabled());

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
        assertEquals(actual.isGeolocationServicesEnabled(), updated.isGeolocationServicesEnabled());
    }
}