package com.bionic.fp.service;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.web.rest.dto.AuthenticationSocialRequest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountService}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateful-spring-security.xml"})
public class AccountServiceIT extends AbstractIT {

    @Test
    public void testGetAccountEventsSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event1 = getSavedEventMax(owner);

        List<Event> events = this.accountEventService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(1, events.size());
        events.forEach(event -> {
            assertEquals(event.getId(), event1.getId());
            assertEquals(event.getName(), event1.getName());
            assertEquals(event.getDescription(), event1.getDescription());
            assertEquals(event.getEventType(), event1.getEventType());
            assertEquals(event.getLocation(), event1.getLocation());
        });

        Event event2 = getSavedEventMax(owner);

        events = this.accountEventService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));

        Event event3 = getSavedEventMax(owner);

        events = this.accountEventService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
    }

    @Test
    public void testCreateAccountViaFacebookSuccess() throws Exception {
        AuthenticationSocialRequest socialRequest = new AuthenticationSocialRequest();

        socialRequest.setSocialId("fb" + System.currentTimeMillis());
        socialRequest.setToken("some token");
        socialRequest.setEmail(generateEmail());
        socialRequest.setUsername(generateUsername());
        socialRequest.setFirstName("First");
        socialRequest.setLastName("Last");
        socialRequest.setImage("http://image.jpg");

        Account account = this.accountService.getOrCreateFbAccount(socialRequest);

        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(socialRequest.getSocialId(), account.getFbId());
//        assertEquals(socialRequest.getToken(), account.getFbToken());
        assertEquals(socialRequest.getEmail(), account.getEmail());
        assertEquals(socialRequest.getUsername(), account.getUserName());
        assertEquals(socialRequest.getImage(), account.getProfileImageUrl());
    }

    @Test(expected = InvalidParameterException.class)
    public void testCreateAccountViaFacebookWithoutEmailFailure() throws Exception {
        AuthenticationSocialRequest socialRequest = new AuthenticationSocialRequest();

        socialRequest.setSocialId("fb" + System.currentTimeMillis());
        socialRequest.setToken("some token");
        socialRequest.setEmail(null);
        socialRequest.setUsername(generateUsername());
        socialRequest.setFirstName("First");
        socialRequest.setLastName("Last");
        socialRequest.setImage("http://image.jpg");

        this.accountService.getOrCreateFbAccount(socialRequest);
    }

    @Test
    public void testAddFacebookInfoToExistingEmailAccountSuccess() throws Exception {
        Account emailAccount = getSavedAccount();

        AuthenticationSocialRequest socialRequest = new AuthenticationSocialRequest();
        socialRequest.setSocialId("fb" + System.currentTimeMillis());
        socialRequest.setToken("some token");
        socialRequest.setEmail(emailAccount.getEmail());
        socialRequest.setUsername(generateUsername());
        socialRequest.setFirstName("First");
        socialRequest.setLastName("Last");
        socialRequest.setImage("http://image.jpg");

        Account account = this.accountService.getOrCreateFbAccount(socialRequest);

        assertNotNull(account);
        assertEquals(emailAccount.getId(), account.getId());
        assertEquals(socialRequest.getSocialId(), account.getFbId());
//        assertEquals(socialRequest.getToken(), account.getFbToken());
        assertEquals(emailAccount.getEmail(), account.getEmail());
        assertEquals(emailAccount.getUserName(), account.getUserName());
        assertEquals(socialRequest.getImage(), account.getProfileImageUrl());
    }

    @Test(expected = InvalidParameterException.class)
    public void testAddFacebookInfoToExistingEmailAccountWithoutFacebookIdFailure() throws Exception {
        Account emailAccount = getSavedAccount();

        AuthenticationSocialRequest socialRequest = new AuthenticationSocialRequest();
        socialRequest.setSocialId(null);
        socialRequest.setToken("some token");
        socialRequest.setEmail(emailAccount.getEmail());
        socialRequest.setUsername(generateUsername());
        socialRequest.setFirstName("First");
        socialRequest.setLastName("Last");
        socialRequest.setImage("http://image.jpg");

        this.accountService.getOrCreateFbAccount(socialRequest);
    }

}