package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.AccountGroupConnectionDAO;
import com.bionic.fp.dao.GroupDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountGroupConnection;
import com.bionic.fp.domain.Group;
import com.bionic.fp.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;

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
    private AccountGroupConnectionDAO accountGroupConnectionDAO;

    public GroupService() {}

//    public Long addGroup(final Group group) {
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
        AccountGroupConnection conn = this.accountGroupConnectionDAO.readByAccountAndGroupId(accountId, groupId);
        if(conn != null) {
            conn.setRole(role);
            this.accountGroupConnectionDAO.update(conn);
        } else {
            Account account = this.accountDAO.readWithGroups(accountId);
            Group group = this.groupDAO.readWithAccounts(groupId);
            if(account != null && group != null) {
                conn = new AccountGroupConnection();
                conn.setAccount(account);
                conn.setGroup(group);
                conn.setRole(role);

                account.addGroupConnection(conn);
                group.addAccountConnection(conn);

                this.accountDAO.update(account);
                this.groupDAO.update(group);
            }
        }
    }

    public Long createGroup(final Long ownerId, final Group group) {
        if(ownerId == null || group == null || group.getId() != null) {
            return null;
        }
        Account owner = this.accountDAO.readWithGroups(ownerId);
        if(owner == null) {
            return null;
        }

        AccountGroupConnection conn = new AccountGroupConnection();
        conn.setAccount(owner);
        conn.setGroup(group);
        conn.setRole(Role.OWNER);

        owner.addGroupConnection(conn);
        group.addAccountConnection(conn);

        group.setOwner(owner);
        group.setDate(LocalDateTime.now());

        Long groupId = this.groupDAO.create(group);
        this.accountDAO.update(owner);
        return groupId;
    }

    public void removeById(final Long id) {
        if(id != null) {
            this.groupDAO.delete(id);
        }
    }

    public Group getById(final Long id) {
        return id == null ? null : this.groupDAO.read(id);
    }

    public Group getByIdWithOwner(final Long id) {
        return id == null ? null : this.groupDAO.readWithOwner(id);
    }

    public Group getByIdWithOwnerAndAccounts(final Long id) {
        return id == null ? null : this.groupDAO.readWithOwnerAndAccounts(id);
    }

    public Group update(final Group group) {
        return group == null ? null : this.groupDAO.update(group);
    }
}
