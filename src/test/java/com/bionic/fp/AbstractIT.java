package com.bionic.fp;

import com.bionic.fp.dao.AbstractDaoIT;
import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.service.*;
import com.bionic.fp.web.security.spring.infrastructure.User;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This is a general integration test
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public abstract class AbstractIT extends AbstractDaoIT {

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

    protected Account getNewEmailAccount(final Long accountId) {
        Account account = getNewEmailAccount();
        account.setId(accountId);
        return account;
    }

    protected Event setPrivate(final Event event) {
        event.setPrivate(true);
        event.setPassword("secret");
        return event;
    }

    protected Event getSavedEventMaxReverse(final Account owner) {
        Event event = update(getNewEventMax());

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    protected Event update(final Event event) {
        Event result = new Event();
        result.setId(event.getId());
        result.setVisible(event.isVisible());
        result.setEventType(event.getEventType());  // todo: when there will be more types
        result.setPrivate(event.isPrivate());
        result.setPassword(event.getPassword());
        result.setCreated(event.getCreated());
        result.setModified(event.getModified());
        result.setExpireDate(event.getExpireDate());
        result.setDeleted(event.isDeleted());

        result.setName(event.getName() + "_up");
        result.setDescription(event.getDescription() + "_up");
        result.setLocation(new Coordinate(event.getLocation() == null ? 0 : event.getLocation().getLatitude() + 1,
                event.getLocation() == null ? 0 : event.getLocation().getLongitude() + 1));
        result.setGeoServicesEnabled(!event.isGeoServicesEnabled());

        return result;
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

    protected Photo getSavedPhoto(final Event event, final Account owner, final File file) throws IOException {
//        DiskFileItem diskFileItem = new DiskFileItem("file", "image/jpeg", false, file.getName(), (int) file.length(), file.getParentFile());
//        diskFileItem.getOutputStream(); // because it will throw NPE otherwise. It is black magic of java
//        MultipartFile multipartFile = new CommonsMultipartFile(diskFileItem);

        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), null, IOUtils.toByteArray(new FileInputStream(file)));
//                file.getName(), "image/jpeg", IOUtils.toByteArray(new FileInputStream(file)));

        Photo photo =  this.photoService.saveToFileSystem(event, owner, multipartFile, "photo" + System.currentTimeMillis());

        assertNotNull(photo.getCreated());
        assertNull(photo.getModified());
        assertNotNull(photo.getId());
        assertFalse(photo.isDeleted());

        return photo;
    }
}
