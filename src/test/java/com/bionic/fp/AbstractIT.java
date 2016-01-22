package com.bionic.fp;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.domain.Role;
import com.bionic.fp.service.*;
import com.bionic.fp.web.security.spring.infrastructure.User;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static com.bionic.fp.Constants.RoleConstants.*;
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
public abstract class AbstractIT extends AbstractHelperTest {

    protected static final String TO_STRING = ".toString()";
    private static Account REGULAR_USER;
    private static EventType PRIVATE_EVENT_TYPE;

    private static Role ROLE_OWNER;
    private static Role ROLE_ADMIN;
    private static Role ROLE_MEMBER;

    @Autowired protected EventDAO eventDAO;
    @Autowired protected EventService eventService;
    @Autowired protected AccountService accountService;
    @Autowired protected AccountEventService accountEventService;
    @Autowired protected EventTypeService eventTypeService;
    @Autowired protected RoleService roleService;
    @Autowired protected WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());
    }

    protected Long save(final Account account) {
        Long accountId = this.accountService.registerByFP(account.getEmail(), account.getPassword(), account.getUserName());
        assertNotNull(accountId);
        return accountId;
    }

    protected Account getSavedAccount() {
        Account account = this.getNewEmailAccount();
        account.setId(save(account));
        return account;
    }

    protected Account getRegularUser() {
        return REGULAR_USER != null ? REGULAR_USER : (REGULAR_USER = getSavedAccount());
    }

    protected Account getNewEmailAccount() {
        String s = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        return new Account("yaya@gmail.com" + s, "Yaya" + s, "yaya" + s);
    }

//    protected String getToken(final Account account) {
//        return this.tokenUtils.generateToken(new User(account.getId(), account.getEmail()));
//    }

    @Override
    protected EventType getPrivateEventType() {
        if(PRIVATE_EVENT_TYPE == null) {
            PRIVATE_EVENT_TYPE = this.eventTypeService.getPrivate();
            assertNotNull(PRIVATE_EVENT_TYPE);
        }
        return PRIVATE_EVENT_TYPE;
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
        event.setCreated(event.getCreated());

        return event;
    }

    protected Long getEventOwnerId(final Long eventId) {
        return getEventOwner(eventId).getId();
    }

    protected Account getEventOwner(final Long eventId) {
        return this.eventService.getOwner(eventId);
    }

    protected Filter getFilter(final Long accountId) {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {}

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                com.bionic.fp.web.security.session.SessionUtils.setUserId(((HttpServletRequest) request).getSession(), accountId);
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {}
        };
    }

    protected com.jayway.restassured.response.Cookie transform(javax.servlet.http.Cookie cookie) {
        if(cookie.getName() == null || cookie.getValue() == null) {
            return null;
        }
        com.jayway.restassured.response.Cookie.Builder builder =
                new com.jayway.restassured.response.Cookie.Builder(cookie.getName(), cookie.getValue());
        if(cookie.getPath() != null) builder.setPath(cookie.getPath());
        if(cookie.getComment() != null) builder.setComment(cookie.getComment());
        if(cookie.getDomain() != null) builder.setDomain(cookie.getDomain());
//        builder.setExpiryDate();
        return builder.setHttpOnly(cookie.isHttpOnly())
                .setMaxAge(cookie.getMaxAge())
                .setVersion(cookie.getVersion())
                .setSecured(cookie.getSecure())
                .build();
    }

    protected void authenticateUser(Account owner) {
        User user = new User(owner);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected Role getRoleOwner() {
        if(ROLE_OWNER == null) {
            ROLE_OWNER = roleService.getOwner();
            assertNotNull(ROLE_OWNER);
        }
        return ROLE_OWNER;
    }

    protected Role getRoleAdmin() {
        if(ROLE_ADMIN == null) {
            ROLE_ADMIN = roleService.getRole(ADMIN);
            assertNotNull(ROLE_ADMIN);
        }
        return ROLE_ADMIN;
    }

    protected Role getRoleMember() {
        if(ROLE_MEMBER == null) {
            ROLE_MEMBER = roleService.getRole(MEMBER);
            assertNotNull(ROLE_MEMBER);
        }
        return ROLE_MEMBER;
    }
}
