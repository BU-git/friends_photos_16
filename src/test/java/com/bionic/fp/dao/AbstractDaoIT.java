package com.bionic.fp.dao;

import com.bionic.fp.AbstractHelperTest;
import com.bionic.fp.dao.*;
import com.bionic.fp.domain.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.bionic.fp.Constants.RoleConstants.ADMIN;
import static com.bionic.fp.Constants.RoleConstants.MEMBER;
import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static org.junit.Assert.*;

/**
 * This is a general integration DAO test
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/test-dao-root-context.xml")
@Transactional
public abstract class AbstractDaoIT extends AbstractHelperTest {

    @Autowired protected EventDAO eventDAO;
    @Autowired protected AccountDAO accountDAO;
    @Autowired protected AccountEventDAO accountEventDAO;
    @Autowired protected CommentDAO commentDAO;
    @Autowired protected PhotoDAO photoDAO;
    @Autowired protected RoleDAO roleDAO;


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

    protected Account getNewEmailAccount() {
        return new Account(generateEmail(), generateUsername(), generatePassword());
    }

    protected Account fb(final Account account) {
        account.setFbId("fb" + String.valueOf(System.currentTimeMillis()));
        account.setProfileImageUrl(String.format("https://www.facebook.com/%s.jpg", account.getFbId()));
        account.setFbProfileUrl(String.format("https://www.facebook.com/%s", account.getFbId()));
        account.setFbToken(String.format("T#%s", account.getFbId()));
        return account;
    }

    protected Account vk(final Account account) {
        account.setVkId("vk" + String.valueOf(System.currentTimeMillis()));
        account.setProfileImageUrl(String.format("https://www.vk.com/%s.jpg", account.getVkId()));
        account.setVkProfileUrl(String.format("https://www.vk.com/%s", account.getVkId()));
        account.setVkToken(String.format("T#%s", account.getVkId()));
        return account;
    }

    protected String generateEmail() {
        return String.format("yaya%d@gmail.com", System.currentTimeMillis());
    }

    protected String generateUsername() {
        return String.format("yaya%d", System.currentTimeMillis());
    }

    protected String generatePassword() {
        return String.format("secret%d", System.currentTimeMillis());
    }


    //////////////////////////////////////////////
    //                  EVENT                   //
    //////////////////////////////////////////////


    protected Event save(final Account owner, final Event event) {
        assertNull(event.getId());

        Role role = this.roleDAO.getOwner();

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


    //////////////////////////////////////////////
    //              ACCOUNT-EVENT               //
    //////////////////////////////////////////////


    protected AccountEvent getSavedAccountEvent(final Account account, final Event event, Role role) {
        assertNotNull(event.getId());
        assertNotNull(account.getId());
        if(role != null) {
            assertNotNull(role.getId());
        } else {
            role = this.roleDAO.getOwner();
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
        Photo photo = new Photo();
        photo.setName("photo" + System.currentTimeMillis());
        photo.setUrl("/fp/" + photo.getName().hashCode());
        photo.setEvent(event);
        photo.setOwner(owner);
        return this.photoDAO.create(photo);
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


    protected void assertEqualsDate(final LocalDateTime expected, final LocalDateTime actual) {
        assertTrue(Duration.between(expected, actual).getSeconds() < 1L);
    }
}
