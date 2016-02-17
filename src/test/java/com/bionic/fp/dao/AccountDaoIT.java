package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Test;

import static com.bionic.fp.Constants.RoleConstants.MEMBER;
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
        getSavedComment(event, owner);
        getSavedComment(photo, owner);

        assertAccountIsNotDeleted(owner, true, true, true, OWNER);
        assertAccountIsNotDeleted(owner, event, true);

        this.accountDAO.setDeleted(owner.getId(), true);

        assertAccountIsDeleted(owner, true, true, true, OWNER);
        assertAccountIsDeleted(owner, event, true);

        Account account1 = getSavedAccount();
        Account account2 = getSavedAccount();
        Event event1 = getSavedEventMax(account1);
        Event event2 = getSavedEventMax(account2);
        getSavedAccountEvent(account1, event2, getRoleMember());
        getSavedAccountEvent(account2, event1, getRoleMember());
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId()), account1, account2);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId()), account1, account2);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId(), OWNER), account1);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId(), MEMBER), account2);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId(), OWNER), account2);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId(), MEMBER), account1);

        this.accountDAO.setDeleted(account2.getId(), true);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId()), account1);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId()), account1);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId(), OWNER), account1);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId(), MEMBER));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId(), OWNER));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId(), MEMBER), account1);

        this.accountDAO.setDeleted(account1.getId(), true);
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId()));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId()));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId(), OWNER));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event1.getId(), MEMBER));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId(), OWNER));
        assertEqualsEntities(this.accountEventDAO.getAccounts(event2.getId(), MEMBER));
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedComment(event, owner);
        getSavedComment(photo, owner);

        assertAccountIsNotDeleted(owner, true, true, true, OWNER);
        assertAccountIsNotDeleted(owner, event, true);

        this.accountDAO.setDeleted(owner.getId(), true);

        assertAccountIsDeleted(owner, true, true, true, OWNER);
        assertAccountIsDeleted(owner, event, true);

        this.accountDAO.setDeleted(owner.getId(), false);

        assertAccountIsNotDeleted(owner, true, true, true, OWNER);
        assertAccountIsNotDeleted(owner, event, true);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        getSavedPhoto(event, owner);
//        getSavedComment(event, owner);   //fix physically delete account comments if it's necessary
//        getSavedComment(photo, owner);

        assertAccountIsNotDeleted(owner, true, true, false, OWNER);
        assertAccountIsNotDeleted(owner, event, true);

        this.accountDAO.delete(owner.getId());

        assertAccountIsDeleted(owner, true, true, false, OWNER);
        assertAccountIsDeleted(owner, event, true);

        this.accountDAO.delete(owner.getId());
    }

    @Test
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        getSavedPhoto(event, owner);
//        getSavedComment(event, owner);   //fix physically delete account comments if it's necessary
//        getSavedComment(photo, owner);

        assertAccountIsNotDeleted(owner, true, true, false, OWNER);
        assertAccountIsNotDeleted(owner, event, true);

        this.accountDAO.setDeleted(owner.getId(), true);

        assertAccountIsDeleted(owner, true, true, false, OWNER);
        assertAccountIsDeleted(owner, event, true);

        this.accountDAO.delete(owner.getId());

        assertAccountIsDeleted(owner, true, true, false, OWNER);
        assertAccountIsDeleted(owner, event, true);

        this.accountDAO.delete(owner.getId());
    }

    @Test
    public void testGetByEmailSuccess() throws Exception {
        Account account = getSavedAccount();

        Account actual = this.accountDAO.getByEmail(account.getEmail());

        assertNotNull(actual);
        assertEqualsEntity(account, actual);

        this.accountDAO.setDeleted(account.getId(), true);

        assertNull(this.accountDAO.getByEmail(account.getEmail()));
    }

    @Test
    public void testGetByFbSuccess() throws Exception {
        Account account = save(fb(getNewEmailAccount()));

        Account actual = this.accountDAO.getByFbId(account.getFbId());

        assertNotNull(actual);
        assertEqualsEntity(account, actual);

        this.accountDAO.setDeleted(account.getId(), true);

        assertNull(this.accountDAO.getByFbId(account.getFbId()));
    }

    @Test
    public void testGetByVkSuccess() throws Exception {
        Account account = save(vk(getNewEmailAccount()));

        Account actual = this.accountDAO.getByVkId(account.getVkId());

        assertNotNull(actual);
        assertEqualsEntity(account, actual);

        this.accountDAO.setDeleted(account.getId(), true);

        assertNull(this.accountDAO.getByVkId(account.getVkId()));
    }

    private void assertAccountIsNotDeleted(final Account account, boolean events, boolean photos, boolean comments, Long ... roles) {
        Account actual = this.accountDAO.read(account.getId());
        assertEqualsEntity(account, actual);
        actual = this.accountDAO.getOrThrow(account.getId());
        assertEqualsEntity(account, actual);
        if(events) {
            assertFalse(this.accountEventDAO.getEvents(account.getId()).isEmpty());
            if (roles != null) {
                for (Long role : roles) {
                    assertFalse(this.accountEventDAO.getEvents(account.getId(), role).isEmpty());
                    assertFalse(this.accountEventDAO.getByAccountAndRole(account.getId(), role).isEmpty());
                }
            }
        }
        if(photos) {
            assertFalse(this.photoDAO.getPhotosByOwner(account.getId()).isEmpty());
        }
        if(comments) {
            assertFalse(this.commentDAO.getCommentsByAuthor(account.getId()).isEmpty());
        }
    }


    private void assertAccountIsDeleted(final Account account, boolean events, boolean photos, boolean comments, Long ... roles) {
        assertNull(this.accountDAO.read(account.getId()));
        try {
            this.accountDAO.getOrThrow(account.getId());
            fail();
        } catch (EntityNotFoundException ignored){}
        if(events) {
            assertTrue(this.accountEventDAO.getEvents(account.getId()).isEmpty());
            if(roles != null) {
                for (Long role : roles) {
                    assertTrue(this.accountEventDAO.getEvents(account.getId(), role).isEmpty());
                    assertTrue(this.accountEventDAO.getByAccountAndRole(account.getId(), role).isEmpty());
                }
            }
        }
        if(photos) {
            assertTrue(this.photoDAO.getPhotosByOwner(account.getId()).isEmpty());
        }
        if(comments) {
            assertTrue(this.commentDAO.getCommentsByAuthor(account.getId()).isEmpty());
        }
    }

    private void assertAccountIsDeleted(final Account account, final Event event, boolean photos) {
        assertNull(this.accountEventDAO.get(account.getId(), event.getId()));
        assertNull(this.accountEventDAO.getRole(account.getId(), event.getId()));
        assertNull(this.accountEventDAO.getWithAccountEvent(account.getId(), event.getId()));
        if(photos) {
            assertTrue(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()).isEmpty());
        }
    }

    private void assertAccountIsNotDeleted(final Account account, final Event event, boolean photos) {
        assertNotNull(this.accountEventDAO.get(account.getId(), event.getId()));
        assertNotNull(this.accountEventDAO.getRole(account.getId(), event.getId()));
        assertNotNull(this.accountEventDAO.getWithAccountEvent(account.getId(), event.getId()));
        if(photos) {
            assertFalse(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()).isEmpty());
        }
    }
}