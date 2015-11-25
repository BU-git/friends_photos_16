package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
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

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class EventDAOIT {

    @Autowired
    private EventDAO eventDAO;

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

        Event actual = this.eventDAO.read(eventId);
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

        Event actual = this.eventDAO.getWithOwner(eventId);
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

        Event actual = this.eventDAO.getWithOwnerAndAccounts(eventId);
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