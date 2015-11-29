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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountService}
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class AccountServiceIT {

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

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

    private Account getSavedAccount() {
        String s = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        Account account = new Account("yaya@gmail.com" + s, "Yaya" + s, "yaya" + s);
        Long accountId = this.accountService.addAccount(account);
        assertNotNull(accountId);
        return account;
    }

    private EventType getPrivateEventType() {
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        return privateEvent;
    }

    private Event getSavedEventMin(final Account owner) {
        Event event = new Event();
        event.setName("NY 2016");
        event.setDescription("testRemoveByIdSuccess");
        event.setEventType(getPrivateEventType());

        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    private Event getNewEventMax() {
        Random random = new Random();

        Event event = new Event();
        LocalDateTime now = LocalDateTime.now();
        event.setName("Nano is " + now.getNano());
        event.setDescription("Today is " + now);
        event.setEventType(getPrivateEventType());
        event.setVisible(true);
        event.setLatitude(random.nextDouble());
        event.setLongitude(random.nextDouble());
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(false);

        assertFalse(event.isDeleted());

        return event;
    }

    private Event getSavedEventMax(final Account owner) {
        Event event = getNewEventMax();

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    private Event getSavedEventMaxReverse(final Account owner) {
        Event event = updateEvent(getNewEventMax());

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    private Event updateEvent(final Event event) {
        event.setName(event.getName() + "_up");
        event.setDescription(event.getDescription() + "_up");
        event.setLatitude(event.getLatitude() + 1);
        event.setLongitude(event.getLongitude() + 1);
        event.setRadius(event.getRadius() + 1);
        event.setVisible(!event.isVisible());
        event.setGeoServicesEnabled(!event.isGeoServicesEnabled());

        return event;
    }
}