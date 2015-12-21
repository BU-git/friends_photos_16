package com.bionic.fp.dao;

import com.bionic.fp.AbstractIT;
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
public class EventDAOIT extends AbstractIT {

    @Test
    public void testGetByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        Event actual = this.eventDAO.read(event.getId());
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

        Event actual = this.eventDAO.getWithAccounts(event.getId());
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
}