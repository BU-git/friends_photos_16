package com.bionic.fp;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.security.SessionUtils;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
import com.bionic.fp.service.RoleService;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This is a general integration test
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public abstract class AbstractIT {

    @Autowired
    protected EventService eventService;

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected EventTypeService eventTypeService;

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    protected Account getSavedAccount() {
        String s = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        Account account = new Account("yaya@gmail.com" + s, "Yaya" + s, "yaya" + s);
        Long accountId = this.accountService.addAccount(account);
        assertNotNull(accountId);
        return account;
    }

    protected EventType getPrivateEventType() {
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        return privateEvent;
    }

    protected Event getNewEventMin() {
        Event event = new Event();
        LocalDateTime now = LocalDateTime.now();
        event.setName("Nano is " + now.getNano());
        event.setDescription("Today is " + now);
        event.setEventType(getPrivateEventType());

        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    protected Event getNewEventMax() {
        Random random = new Random();
        Event event = getNewEventMin();
        event.setVisible(true);
        event.setLatitude(random.nextDouble());
        event.setLongitude(random.nextDouble());
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(false);

        assertFalse(event.isDeleted());

        return event;
    }

    protected Event setPrivate(final Event event) {
        event.setIsPrivate(true);
        event.setPassword("secret");
        return event;
    }

    protected Event getSavedEventMin(final Account owner) {
        Event event = getNewEventMin();

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    protected Event getSavedEventMax(final Account owner) {
        Event event = getNewEventMax();

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    protected Event getSaved(final Event event, final Account owner) {
        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    protected Event getSavedEventMaxReverse(final Account owner) {
        Event event = updateEvent(getNewEventMax());

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    protected Event updateEvent(final Event event) {
        event.setName(event.getName() + "_up");
        event.setDescription(event.getDescription() + "_up");
        event.setLatitude(event.getLatitude() == null ? 0 : event.getLatitude() + 1);
        event.setLongitude(event.getLongitude() == null ? 0 : event.getLongitude() + 1);
        event.setRadius(event.getRadius() == null ? 0 : event.getRadius() + 1);
        event.setVisible(!event.isVisible());
        event.setGeoServicesEnabled(!event.isGeoServicesEnabled());

        return event;
    }

    protected Filter getFilter(final Long accountId) {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {}

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                SessionUtils.setUserId(((HttpServletRequest) request).getSession(), accountId);
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {}
        };
    }
}
