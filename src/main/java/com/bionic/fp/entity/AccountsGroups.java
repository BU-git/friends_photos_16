package com.bionic.fp.entity;

import javax.persistence.*;

/**
 * Created by Yevhenii on 11/17/2015.
 */
@Entity
@Table(name="accounts_groups")
//@IdClass(AccountsGroups.class)
public class AccountsGroups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "account_id")
    @Id
    private Long accountId;

    @Column(name = "group_id")
    @Id
    private Long groupId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public AccountsGroups() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
