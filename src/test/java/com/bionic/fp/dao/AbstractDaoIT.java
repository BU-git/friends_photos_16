package com.bionic.fp.dao;

import com.bionic.fp.AbstractHelperTest;
import com.bionic.fp.domain.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bionic.fp.Constants.RoleConstants.ADMIN;
import static com.bionic.fp.Constants.RoleConstants.MEMBER;
import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.junit.Assert.*;

/**
 * This is a general integration DAO test
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/test-dao-root-context.xml")
//@Transactional
public abstract class AbstractDaoIT extends AbstractHelperTest {

    @Autowired protected EventDAO eventDAO;
    @Autowired protected AccountDAO accountDAO;
    @Autowired protected AccountEventDAO accountEventDAO;
    @Autowired protected CommentDAO commentDAO;
    @Autowired protected PhotoDAO photoDAO;
    @Autowired protected RoleDAO roleDAO;
    @PersistenceContext private EntityManager em;
    @Autowired private PlatformTransactionManager transactionManager;

    //////////////////////////////////////////////
    //                 ACCOUNT                  //
    //////////////////////////////////////////////


    protected Account getSavedAccount() {
        Account account = this.getNewEmailAccount();
        return save(account);
    }

    protected Account save(final Account account) {
        assertNull(account.getId());
        assertNull(account.getCreated());

        Account actual = this.accountDAO.create(account);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreated());
        assertFalse(actual.isDeleted());

        return actual;
    }


    //////////////////////////////////////////////
    //                  EVENT                   //
    //////////////////////////////////////////////


    protected Event save(final Account owner, final Event event) {
        assertNull(event.getId());

        Role role = this.roleDAO.getOrThrow(OWNER);

        Event actual = this.eventDAO.create(event);

        assertNotNull(actual);
        assertNotNull(event.getId());
        assertNotNull(event.getCreated());
        assertFalse(event.isDeleted());

        AccountEvent accountEvent = this.accountEventDAO.create(new AccountEvent(event, owner, role));

        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getId());
        assertNotNull(accountEvent.getCreated());
        assertFalse(accountEvent.isDeleted());

        return event;
    }

    protected Event getSavedEventMin(final Account owner) {
        Event event = getNewEventMin();
        return save(owner, event);
    }

    protected Event getSavedEventMax(final Account owner) {
        Event event = getNewEventMax();
        return save(owner, event);
    }

    protected List<Event> getSavedFiveEventsWithStep100m(Account owner) {
        return of(
                new Coordinate(50.445385, 30.501502),   // ~0
                new Coordinate(50.445173, 30.502908),   // ~100m
                new Coordinate(50.444961, 30.504249),   // ~200m
                new Coordinate(50.444727, 30.505630),   // ~300m
                new Coordinate(50.444507, 30.507001)    // ~400m
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());
    }


    //////////////////////////////////////////////
    //              ACCOUNT-EVENT               //
    //////////////////////////////////////////////


    protected AccountEvent getSavedAccountEvent(final Account account, final Event event, Role role) {
        assertNotNull(event.getId());
        assertNotNull(account.getId());
        if(role != null) {
            assertNotNull(role.getId());
        } else {
            role = this.roleDAO.getOrThrow(OWNER);
        }
        assertNotNull(account.getCreated());
        assertNotNull(event.getCreated());
        assertNotNull(role.getCreated());

        AccountEvent accountEvent = this.accountEventDAO.create(new AccountEvent(event, account, role));
        assertNotNull(accountEvent);
        assertNotNull(accountEvent.getId());
        assertNotNull(accountEvent.getCreated());
        assertFalse(accountEvent.isDeleted());
        return accountEvent;
    }


    //////////////////////////////////////////////
    //                  PHOTO                   //
    //////////////////////////////////////////////


    protected Photo getSavedPhoto(final Event event, final Account owner) {
        Photo photo = this.photoDAO.create(getNewPhoto(event, owner));

        assertNotNull(photo.getCreated());
        assertNull(photo.getModified());
        assertNotNull(photo.getId());
        assertFalse(photo.isDeleted());

        return photo;
    }


    //////////////////////////////////////////////
    //                 COMMENT                  //
    //////////////////////////////////////////////


    protected Comment getSavedComment(final Event event, final Account author) {
        Comment comment = getNewComment(author);
        this.commentDAO.createEventComment(event.getId(), comment);
        return comment;
    }

    protected Comment getSavedComment(final Photo photo, final Account author) {
        Comment comment = getNewComment(author);
        this.commentDAO.createPhotoComment(photo.getId(), comment);
        return comment;
    }


    //////////////////////////////////////////////
    //                  ROLE                    //
    //////////////////////////////////////////////


    protected Role getRoleOwner() {
        return this.roleDAO.getOrThrow(OWNER);
    }

    protected Role getRoleAdmin() {
        return this.roleDAO.getOrThrow(ADMIN);
    }

    protected Role getRoleMember() {
        return this.roleDAO.getOrThrow(MEMBER);
    }


    //////////////////////////////////////////////
    //                  OTHER                   //
    //////////////////////////////////////////////


    protected void clearAllTables() {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        this.em.createNativeQuery("DELETE FROM photos_comments").executeUpdate();
        this.em.createNativeQuery("DELETE FROM events_comments").executeUpdate();
        this.em.createNativeQuery("DELETE FROM comments").executeUpdate();
        this.em.createNativeQuery("DELETE FROM photos").executeUpdate();
        this.em.createNativeQuery("DELETE FROM accounts_events").executeUpdate();
        this.em.createNativeQuery("DELETE FROM events").executeUpdate();
        this.em.createNativeQuery("DELETE FROM accounts").executeUpdate();
        transactionManager.commit(transaction);
    }

    protected void softDeleteAllEvents() {
        String string = null;
        eventDAO.get(null, string, string).stream().unordered().forEach(event -> {
            event.setDeleted(true);
            this.eventDAO.update(event);
        });
    }
}
