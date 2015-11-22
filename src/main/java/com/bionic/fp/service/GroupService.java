package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.GroupDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entry point to perform operations over group entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class GroupService {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private GroupDAO groupDAO;

    @Inject
    private AccountEventDAO accountEventDAO;

    public GroupService() {}

//    public Long addGroup(final Event group) {
//        if(group == null) {
//            return null;
//        }
//        group.setDate(LocalDateTime.now());
//        return this.groupDAO.create(group);
//    }

    public void addOrUpdateAccountToGroup(final Long accountId, final Long groupId, final Role role) {
        if(accountId == null || groupId == null || role == null) {
            return;
        }
        AccountEvent conn = this.accountEventDAO.readByAccountAndGroupId(accountId, groupId);
        if(conn != null) {
            conn.setRole(role);
            this.accountEventDAO.update(conn);
        } else {
            Account account = this.accountDAO.readWithGroups(accountId);
            Event event = this.groupDAO.readWithAccounts(groupId);
            if(account != null && event != null) {
                conn = new AccountEvent();
                conn.setAccount(account);
                conn.setEvent(event);
                conn.setRole(role);

//                account.addGroupConnection(conn);
//                event.addAccountConnection(conn);

                this.accountDAO.update(account);
                this.groupDAO.update(event);
            }
        }
    }

    public Long createGroup(final Long ownerId, final Event event) {
        if(ownerId == null || event == null || event.getId() != null) {
            return null;
        }
        Account owner = this.accountDAO.readWithGroups(ownerId);
        if(owner == null) {
            return null;
        }

        AccountEvent conn = new AccountEvent();
        conn.setAccount(owner);
        conn.setEvent(event);
//        conn.setRole(Role.OWNER);

//        owner.addGroupConnection(conn);
//        event.addAccountConnection(conn);
//		List<Event> events = owner.getEvents();
//		events.add(event);
//		owner.setEvents(events);

        event.setOwner(owner);
        event.setDate(LocalDateTime.now());

        Long groupId = this.groupDAO.create(event);
        this.accountDAO.update(owner);
        return groupId;
    }

    public void removeById(final Long id) {
        if(id != null) {
            this.groupDAO.delete(id);
        }
    }

    public Event getById(final Long id) {
        return id == null ? null : this.groupDAO.read(id);
    }

    public Event getByIdWithOwner(final Long id) {
        return id == null ? null : this.groupDAO.readWithOwner(id);
    }

    public Event getByIdWithOwnerAndAccounts(final Long id) {
        return id == null ? null : this.groupDAO.readWithOwnerAndAccounts(id);
    }

    public Event update(final Event event) {
        return event == null ? null : this.groupDAO.update(event);
    }
}
