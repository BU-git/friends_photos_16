package com.bionic.fp.domain;

import javax.persistence.*;

/**
 * Created by Yevhenii on 11/17/2015.
 */
@Entity
@Table(name="accounts_events")
@NamedQuery(
        name="findConnByAccount&Event",
        query="SELECT ae FROM AccountEvent ae WHERE ae.account.id = :accountId AND ae.event.id = :eventId"
)
public class AccountEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name="account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @OneToOne(fetch = FetchType.EAGER)
    private Role role;

    public AccountEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
