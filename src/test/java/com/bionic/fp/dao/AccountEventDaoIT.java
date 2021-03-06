package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountEventDAO}
 *
 * @author Sergiy Gabriel
 */
public class AccountEventDaoIT extends AbstractDaoIT {

    @Test @Ignore // Unsupported soft delete for account-event
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        AccountEvent accountEvent = this.accountEventDAO.get(owner.getId(), event.getId());
        Role role = accountEvent.getRole();

        assertAccountEventIsNotDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.setDeleted(accountEvent.getId(), true);

        assertAccountEventIsDeleted(accountEvent, owner, event, role);
    }

    @Test @Ignore // Unsupported soft delete for account-event
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        AccountEvent accountEvent = this.accountEventDAO.get(owner.getId(), event.getId());
        Role role = accountEvent.getRole();

        assertAccountEventIsNotDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.setDeleted(accountEvent.getId(), true);

        assertAccountEventIsDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.setDeleted(accountEvent.getId(), false);

        assertAccountEventIsNotDeleted(accountEvent, owner, event, role);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        AccountEvent accountEvent = this.accountEventDAO.get(owner.getId(), event.getId());
        Role role = accountEvent.getRole();

        assertAccountEventIsNotDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.delete(accountEvent.getId());

        assertAccountEventIsDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.delete(accountEvent.getId());
    }

    @Test
    public void testDeleteByAccountAndEventSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        AccountEvent accountEvent = this.accountEventDAO.get(owner.getId(), event.getId());
        Role role = accountEvent.getRole();

        assertAccountEventIsNotDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.delete(owner.getId(), event.getId());

        assertAccountEventIsDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.delete(owner.getId(), event.getId());
    }

    @Test
    @Ignore // Unsupported soft delete for account-event
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        AccountEvent accountEvent = this.accountEventDAO.get(owner.getId(), event.getId());
        Role role = accountEvent.getRole();

        assertAccountEventIsNotDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.setDeleted(accountEvent.getId(), true);

        assertAccountEventIsDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.delete(accountEvent.getId());

        assertAccountEventIsDeleted(accountEvent, owner, event, role);

        this.accountEventDAO.delete(accountEvent.getId());
    }

    private void assertAccountEventIsNotDeleted(final AccountEvent accountEvent, final Account owner, final Event event, final Role role) {
        assertNotNull(this.accountEventDAO.read(accountEvent.getId()));
        assertNotNull(this.accountEventDAO.getOrThrow(accountEvent.getId()));

        assertNotNull(this.accountEventDAO.get(owner.getId(), event.getId()));
        assertNotNull(this.accountEventDAO.getRole(owner.getId(), event.getId()));
        assertNotNull(this.accountEventDAO.getWithAccountEvent(owner.getId(), event.getId()));

        assertFalse(this.accountEventDAO.getAccounts(event.getId()).isEmpty());
        assertFalse(this.accountEventDAO.getAccounts(event.getId(), role.getId()).isEmpty());
        assertFalse(this.accountEventDAO.getByEventAndRole(event.getId(), role.getId()).isEmpty());

        assertFalse(this.accountEventDAO.getEvents(owner.getId()).isEmpty());
        assertFalse(this.accountEventDAO.getEvents(owner.getId(), role.getId()).isEmpty());
        assertFalse(this.accountEventDAO.getByAccountAndRole(owner.getId(), role.getId()).isEmpty());
    }

    private void assertAccountEventIsDeleted(final AccountEvent accountEvent, final Account owner, final Event event, final Role role) {
        assertNull(this.accountEventDAO.read(accountEvent.getId()));
        try {
            this.accountEventDAO.getOrThrow(accountEvent.getId());
            fail();
        } catch (EntityNotFoundException ignored){}

        assertNull(this.accountEventDAO.get(owner.getId(), event.getId()));
        assertNull(this.accountEventDAO.getRole(owner.getId(), event.getId()));
        assertNull(this.accountEventDAO.getWithAccountEvent(owner.getId(), event.getId()));

        assertTrue(this.accountEventDAO.getAccounts(event.getId()).isEmpty());
        assertTrue(this.accountEventDAO.getAccounts(event.getId(), role.getId()).isEmpty());
        assertTrue(this.accountEventDAO.getByEventAndRole(event.getId(), role.getId()).isEmpty());

        assertTrue(this.accountEventDAO.getEvents(owner.getId()).isEmpty());
        assertTrue(this.accountEventDAO.getEvents(owner.getId(), role.getId()).isEmpty());
        assertTrue(this.accountEventDAO.getByAccountAndRole(owner.getId(), role.getId()).isEmpty());
    }

}