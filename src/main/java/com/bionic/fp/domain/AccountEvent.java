package com.bionic.fp.domain;

import javax.persistence.*;

/**
 * Created by Yevhenii on 11/17/2015.
 */
@Entity
@Table(name="accounts_events")
@NamedQuery(
        name= AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID,
        query="SELECT ae FROM AccountEvent ae WHERE ae.account.id = :accountId AND ae.event.id = :eventId"
)
@NamedEntityGraph(name = "AccountEvent.full", attributeNodes={
                @NamedAttributeNode("account"),
                @NamedAttributeNode("event"),
                @NamedAttributeNode("role")}
)
public class AccountEvent {
    @Transient
    public static final String GET_BY_ACCOUNT_AND_EVENT_ID = "AccountEvent.getByAccountAndEventId";

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
