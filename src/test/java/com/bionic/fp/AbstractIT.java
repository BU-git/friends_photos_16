package com.bionic.fp;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.service.*;
import com.bionic.fp.web.security.spring.infrastructure.User;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.bionic.fp.Constants.RoleConstants.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

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
    @Autowired protected CommentService commentService;
    @Autowired protected PhotoService photoService;
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
        return new Account(generateEmail(), generateUsername(), generatePassword());
    }

    protected Account getNewEmailAccount(final Long accountId) {
        Account account = getNewEmailAccount();
        account.setId(accountId);
        return account;
    }

    @Override
    protected EventType getPrivateEventType() {
        if(PRIVATE_EVENT_TYPE == null) {
            PRIVATE_EVENT_TYPE = this.eventTypeService.getPrivate();
            assertNotNull(PRIVATE_EVENT_TYPE);
        }
        return PRIVATE_EVENT_TYPE;
    }

    protected Event setPrivate(final Event event) {
        event.setPrivate(true);
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
//        event.setLatitude(event.getLatitude() == null ? 0 : event.getLatitude() + 1);
//        event.setLongitude(event.getLongitude() == null ? 0 : event.getLongitude() + 1); // todo
//        event.setVisible(!event.isVisible()); // todo
        event.setGeoServicesEnabled(!event.isGeoServicesEnabled());
        event.setCreated(event.getCreated());

        return event;
    }

    protected Long getEventOwnerId(final Long eventId) {
        return getEventOwner(eventId).getId();
    }

    protected Account getEventOwner(final Long eventId) {
        List<Account> accounts = this.accountEventService.getAccounts(eventId, Constants.RoleConstants.OWNER);
        return accounts.get(0);
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

    protected String generateEmail() {
        return String.format("yaya%d@gmail.com", System.currentTimeMillis());
    }

    protected String generateUsername() {
        return String.format("yaya%d", System.currentTimeMillis());
    }

    protected String generatePassword() {
        return String.format("secret%d", System.currentTimeMillis());
    }

    protected Filter getPreAuthFilter(final Account owner) {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                User user = new User(owner);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {

            }
        };
    }

    protected Comment getNewComment(final Account author) {
        Comment comment = new Comment(String.format("I am %s and now %s", author.getUserName(), LocalDateTime.now()));
        comment.setAuthor(author);
        return comment;
    }

    protected Photo getSavedPhoto(final Event event, final Account owner) {
        Photo photo = getNewPhoto(event, owner);

        assertNull(photo.getCreated());
        assertNull(photo.getId());

        photo = this.photoService.create(photo);

        assertNotNull(photo.getCreated());
        assertNotNull(photo.getId());

        return photo;
    }

    protected Photo getNewPhoto(final Event event, final Account owner) {
        Photo photo = new Photo();
        String time = LocalDateTime.now().toString();
        photo.setName(time);
        photo.setUrl(String.format("http://fp/%d/%d/%s", event.getId(), owner.getId(), time));
        photo.setEvent(event);
        photo.setOwner(owner);
        return photo;
    }

    protected <PK extends Serializable> void assertEqualsId(final List<PK> actual, final IdEntity<PK> ... expected) {
        if(expected == null || expected.length == 0) {
            return;
        }
        if(actual == null || actual.isEmpty() || expected.length != actual.size()) {
            fail();
        }
        for (IdEntity<PK> entity : expected) {
            Optional<PK> optional = actual.stream().parallel().filter(a -> entity.getId().equals(a)).findFirst();
            if(!optional.isPresent()) {
                fail();
            }
        }
    }
}
