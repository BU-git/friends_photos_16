package com.bionic.fp.domain;

import javax.persistence.*;

@Entity
@Table(name = "account_group")
@NamedEntityGraph(name = "AccountGroupConnection.account&group", attributeNodes = {
        @NamedAttributeNode("account"),
        @NamedAttributeNode("group")}
)
@NamedQuery(
        name="findConnByAccount&Group",
        query="SELECT conn FROM AccountGroupConnection conn WHERE conn.account.id = :accountId AND conn.group.id = :groupId"
)
public class AccountGroupConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private Account account;
    @OneToOne(fetch = FetchType.LAZY)
    private Group group;
    @Enumerated(EnumType.STRING)
    private Role role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
