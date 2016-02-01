package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Ignore;
import org.junit.Test;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountDAO}
 *
 * @author Sergiy Gabriel
 */
public class AccountDaoIT extends AbstractDaoIT {

    @Test
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);
        getSavedPhotoComment(photo, owner);

        assertAccountIsNotDeleted(owner, event);

        this.accountDAO.setDeleted(owner.getId(), true);

        assertAccountIsDeleted(owner, event);
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);
        getSavedPhotoComment(photo, owner);

        assertAccountIsNotDeleted(owner, event);

        this.accountDAO.setDeleted(owner.getId(), true);

        assertAccountIsDeleted(owner, event);

        this.accountDAO.setDeleted(owner.getId(), false);

        assertAccountIsNotDeleted(owner, event);
    }

    @Test(expected = EntityNotFoundException.class) @Ignore //todo: fix physically delete comment
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);
        getSavedPhotoComment(photo, owner);

        assertAccountIsNotDeleted(owner, event);

        this.accountDAO.delete(owner.getId());

        assertAccountIsDeleted(owner, event);

        this.accountDAO.delete(owner.getId());
    }

    @Test(expected = EntityNotFoundException.class) @Ignore //todo: fix physically delete comment
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedEventComment(event, owner);
        getSavedPhotoComment(photo, owner);

        assertAccountIsNotDeleted(owner, event);

        this.accountDAO.setDeleted(owner.getId(), true);

        assertAccountIsDeleted(owner, event);

        this.accountDAO.delete(owner.getId());

        assertAccountIsDeleted(owner, event);

        this.accountDAO.delete(owner.getId());
    }

    @Test
    public void testGetByEmailSuccess() throws Exception {
        Account account = getSavedAccount();

        Account actual = this.accountDAO.getByEmail(account.getEmail());

        assertNotNull(actual);
        assertEqualsAccount(account, actual);

        this.accountDAO.setDeleted(account.getId(), true);

        assertNull(this.accountDAO.getByEmail(account.getEmail()));
    }

    @Test
    public void testGetByFbSuccess() throws Exception {
        Account account = save(fb(getNewEmailAccount()));

        Account actual = this.accountDAO.getByFbId(account.getFbId());

        assertNotNull(actual);
        assertEqualsAccount(account, actual);

        this.accountDAO.setDeleted(account.getId(), true);

        assertNull(this.accountDAO.getByFbId(account.getFbId()));
    }

    @Test
    public void testGetByVkSuccess() throws Exception {
        Account account = save(vk(getNewEmailAccount()));

        Account actual = this.accountDAO.getByVkId(account.getVkId());

        assertNotNull(actual);
        assertEqualsAccount(account, actual);

        this.accountDAO.setDeleted(account.getId(), true);

        assertNull(this.accountDAO.getByVkId(account.getVkId()));
    }

    protected void assertEqualsAccount(final Account expected, final Account actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        if(expected.getUserName() != null) {
            assertEquals(expected.getUserName(), actual.getUserName());
        } else {
            assertNull(actual.getUserName());
        }
        if(expected.getProfileImageUrl() != null) {
            assertEquals(expected.getProfileImageUrl(), actual.getProfileImageUrl());
        } else {
            assertNull(actual.getProfileImageUrl());
        }
        if(expected.getFbId() != null) {
            assertEquals(expected.getFbId(), actual.getFbId());
        }else {
            assertNull(actual.getFbId());
        }

        if(expected.getVkId() != null) {
            assertEquals(expected.getVkId(), actual.getVkId());
        } else {
            assertNull(actual.getVkId());
        }
    }

    private void assertAccountIsNotDeleted(final Account owner, final Event event) {
        assertEqualsAccount(owner, this.accountDAO.read(owner.getId()));
        assertAccountIsNotDeleted(owner.getId(), event.getId());
    }

    private void assertAccountIsDeleted(final Account owner, final Event event) {
        assertAccountIsDeleted(owner.getId(), event.getId());
    }

    private void assertAccountIsDeleted(final Long ownerId, final Long eventId) {
        assertNull(this.accountDAO.read(ownerId));
        try {
            this.accountDAO.getOrThrow(ownerId);
            fail();
        } catch (EntityNotFoundException ignored){}

        assertNull(this.accountEventDAO.get(ownerId, eventId));
        assertNull(this.accountEventDAO.getRole(ownerId, eventId));
        assertNull(this.accountEventDAO.getWithAccountEvent(ownerId, eventId));

        assertTrue(this.accountEventDAO.getAccounts(eventId).isEmpty());
        assertTrue(this.accountEventDAO.getAccounts(eventId, OWNER).isEmpty());
        assertTrue(this.accountEventDAO.getByEventAndRole(eventId, OWNER).isEmpty());

        assertTrue(this.accountEventDAO.getEvents(ownerId).isEmpty());
        assertTrue(this.accountEventDAO.getEvents(ownerId, OWNER).isEmpty());
        assertTrue(this.accountEventDAO.getByAccountAndRole(ownerId, OWNER).isEmpty());

        assertTrue(this.photoDAO.getPhotosByOwner(ownerId).isEmpty());
        assertTrue(this.photoDAO.getPhotosByAccountInEvent(ownerId, eventId).isEmpty());

        assertTrue(this.commentDAO.getCommentsByAuthor(ownerId).isEmpty());
    }

    private void assertAccountIsNotDeleted(final Long ownerId, final Long eventId) {
        assertNotNull(this.accountDAO.read(ownerId));
        assertNotNull(this.accountDAO.getOrThrow(ownerId));

        assertNotNull(this.accountEventDAO.get(ownerId, eventId));
        assertNotNull(this.accountEventDAO.getRole(ownerId, eventId));
        assertNotNull(this.accountEventDAO.getWithAccountEvent(ownerId, eventId));

        assertFalse(this.accountEventDAO.getAccounts(eventId).isEmpty());
        assertFalse(this.accountEventDAO.getAccounts(eventId, OWNER).isEmpty());
        assertFalse(this.accountEventDAO.getByEventAndRole(eventId, OWNER).isEmpty());

        assertFalse(this.accountEventDAO.getEvents(ownerId).isEmpty());
        assertFalse(this.accountEventDAO.getEvents(ownerId, OWNER).isEmpty());
        assertFalse(this.accountEventDAO.getByAccountAndRole(ownerId, OWNER).isEmpty());

        assertFalse(this.photoDAO.getPhotosByOwner(ownerId).isEmpty());
        assertFalse(this.photoDAO.getPhotosByAccountInEvent(ownerId, eventId).isEmpty());

        assertFalse(this.commentDAO.getCommentsByAuthor(ownerId).isEmpty());
    }
}