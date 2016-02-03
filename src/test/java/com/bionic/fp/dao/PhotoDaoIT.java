package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link PhotoDAO}
 *
 * @author Sergiy Gabriel
 */
public class PhotoDaoIT extends AbstractDaoIT {

    @Test
    public void testGetPhotosByEventSuccess() {
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event1 = getSavedEventMax(user1);
        Event event2 = getSavedEventMax(user2);
        getSavedAccountEvent(user2, event1, getRoleMember());
        Photo photo1 = getSavedPhoto(event1, user1);
        Photo photo2 = getSavedPhoto(event1, user2);
        Photo photo3 = getSavedPhoto(event2, user2);

        List<Photo> photos = this.photoDAO.getPhotosByEvent(event1.getId());

        assertEquals(2, photos.size());
        assertTrue(photos.contains(photo1));
        assertTrue(photos.contains(photo2));

        photos = this.photoDAO.getPhotosByEvent(event2.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo3));
        assertEquals(photo3.getId(), photos.get(0).getId());
    }

    @Test
    public void testGetPhotosByAccountSuccess() {
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event1 = getSavedEventMax(user1);
        Event event2 = getSavedEventMax(user2);
        getSavedAccountEvent(user2, event1, getRoleMember());
        Photo photo1 = getSavedPhoto(event1, user1);
        Photo photo2 = getSavedPhoto(event1, user2);
        Photo photo3 = getSavedPhoto(event2, user2);

        List<Photo> photos = this.photoDAO.getPhotosByOwner(user2.getId());

        assertEquals(2, photos.size());
        assertTrue(photos.contains(photo2));
        assertTrue(photos.contains(photo3));

        photos = this.photoDAO.getPhotosByOwner(user1.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo1));
        assertEquals(photo1.getId(), photos.get(0).getId());
    }

    @Test
    public void testGetPhotosByAccountInEventSuccess() {
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event1 = getSavedEventMax(user1);
        Event event2 = getSavedEventMax(user2);
        getSavedAccountEvent(user2, event1, getRoleMember());
        Photo photo1 = getSavedPhoto(event1, user1);
        Photo photo2 = getSavedPhoto(event1, user2);
        Photo photo3 = getSavedPhoto(event2, user2);

        List<Photo> photos = this.photoDAO.getPhotosByAccountInEvent(user2.getId(), event1.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo2));
        assertEqualsEntity(photo2, photos.get(0));

        photos = this.photoDAO.getPhotosByAccountInEvent(user2.getId(), event2.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo3));
        assertEqualsEntity(photo3, photos.get(0));

        photos = this.photoDAO.getPhotosByAccountInEvent(user1.getId(), event1.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo1));
        assertEqualsEntity(photo1, photos.get(0));

        assertTrue(this.photoDAO.getPhotosByAccountInEvent(user1.getId(), event2.getId()).isEmpty());
    }

    @Test
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account account = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Event another = getSavedEventMax(account);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment = getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner, comment);

        this.photoDAO.setDeleted(photo.getId(), true);

        assertPhotoIsDeleted(photo, event, owner, comment);

        Photo photo1 = getSavedPhoto(event, owner);
        Photo photo2 = getSavedPhoto(event, account);
        Photo photo3 = getSavedPhoto(another, account);
        Photo photo4 = getSavedPhoto(another, owner);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(event.getId()), photo1, photo2);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(another.getId()), photo3, photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(owner.getId()), photo1, photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(account.getId()), photo2, photo3);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()), photo1);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), another.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()), photo2);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), another.getId()), photo3);

        this.photoDAO.setDeleted(photo1.getId(), true);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(event.getId()), photo2);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(another.getId()), photo3, photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(owner.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(account.getId()), photo2, photo3);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), another.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()), photo2);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), another.getId()), photo3);

        this.photoDAO.setDeleted(photo2.getId(), true);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(another.getId()), photo3, photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(owner.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(account.getId()), photo3);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), another.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), another.getId()), photo3);

        this.photoDAO.setDeleted(photo3.getId(), true);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(another.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(owner.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(account.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), another.getId()), photo4);
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), another.getId()));

        this.photoDAO.setDeleted(photo4.getId(), true);
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByEvent(another.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(owner.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByOwner(account.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), another.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), event.getId()));
        assertEqualsEntities(this.photoDAO.getPhotosByAccountInEvent(account.getId(), another.getId()));
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment = getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner, comment);

        this.photoDAO.setDeleted(photo.getId(), true);

        assertPhotoIsDeleted(photo, event, owner, comment);

        this.photoDAO.setDeleted(photo.getId(), false);

        assertPhotoIsNotDeleted(photo, event, owner, comment);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment = getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner, comment);

        this.photoDAO.delete(photo.getId());

        assertPhotoIsDeleted(photo, event, owner, comment);

        this.photoDAO.delete(photo.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment = getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner, comment);

        this.photoDAO.setDeleted(photo.getId(), true);

        assertPhotoIsDeleted(photo, event, owner, comment);

        this.photoDAO.delete(photo.getId());

        assertPhotoIsDeleted(photo, event, owner, comment);

        this.photoDAO.delete(photo.getId());
    }

    private void assertPhotoIsNotDeleted(Photo photo, Event event, Account owner, Comment comment) {
        assertEqualsEntity(photo, this.photoDAO.read(photo.getId()));
        assertEqualsEntity(photo, this.photoDAO.getOrThrow(photo.getId()));

        if(event != null) {
            assertFalse(this.photoDAO.getPhotosByEvent(event.getId()).isEmpty());
        }
        if(owner != null) {
            assertFalse(this.photoDAO.getPhotosByOwner(owner.getId()).isEmpty());
        }
        if(event != null && owner != null) {
            assertFalse(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()).isEmpty());
        }
        if(comment != null) {
            assertFalse(this.commentDAO.getCommentsByPhoto(photo.getId()).isEmpty());
        }
    }

    private void assertPhotoIsDeleted(Photo photo, Event event, Account owner, Comment comment) {
        assertNull(this.photoDAO.read(photo.getId()));
        try {
            this.photoDAO.getOrThrow(photo.getId());
            fail();
        } catch (EntityNotFoundException ignored){}

        if(event != null) {
            assertTrue(this.photoDAO.getPhotosByEvent(event.getId()).isEmpty());
        }
        if(owner != null) {
            assertTrue(this.photoDAO.getPhotosByOwner(owner.getId()).isEmpty());
        }
        if(event != null && owner != null) {
            assertTrue(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()).isEmpty());
        }
        if(comment != null) {
            assertTrue(this.commentDAO.getCommentsByPhoto(photo.getId()).isEmpty());
        }
    }
}