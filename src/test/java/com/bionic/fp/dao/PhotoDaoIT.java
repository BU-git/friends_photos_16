package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
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
        assertEqualsPhoto(photo2, photos.get(0));

        photos = this.photoDAO.getPhotosByAccountInEvent(user2.getId(), event2.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo3));
        assertEqualsPhoto(photo3, photos.get(0));

        photos = this.photoDAO.getPhotosByAccountInEvent(user1.getId(), event1.getId());

        assertEquals(1, photos.size());
        assertTrue(photos.contains(photo1));
        assertEqualsPhoto(photo1, photos.get(0));

        assertTrue(this.photoDAO.getPhotosByAccountInEvent(user1.getId(), event2.getId()).isEmpty());
    }

    @Test
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner);

        this.photoDAO.setDeleted(photo.getId(), true);

        assertPhotoIsDeleted(photo, event, owner);
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner);

        this.photoDAO.setDeleted(photo.getId(), true);

        assertPhotoIsDeleted(photo, event, owner);

        this.photoDAO.setDeleted(photo.getId(), false);

        assertPhotoIsNotDeleted(photo, event, owner);
    }

    @Test(expected = EntityNotFoundException.class) //@Ignore //todo: fix physically delete comment
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner);

        this.photoDAO.delete(photo.getId());

        assertPhotoIsDeleted(photo, event, owner);

        this.photoDAO.delete(photo.getId());
    }

    @Test(expected = EntityNotFoundException.class) //@Ignore //todo: fix physically delete comment
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        getSavedPhotoComment(photo, owner);

        assertPhotoIsNotDeleted(photo, event, owner);

        this.photoDAO.setDeleted(photo.getId(), true);

        assertPhotoIsDeleted(photo, event, owner);

        this.photoDAO.delete(photo.getId());

        assertPhotoIsDeleted(photo, event, owner);

        this.photoDAO.delete(photo.getId());
    }

    private void assertEqualsPhoto(final Photo photo, final Photo actual) {
        assertEquals(photo.getId(), actual.getId());
        assertEqualsDate(photo.getCreated(), actual.getCreated());
        assertEquals(photo.getUrl(), actual.getUrl());
        if(photo.getModified() != null) assertEqualsDate(photo.getModified(), actual.getModified());
        else assertNull(actual.getModified());
        if(photo.getName() != null) assertEquals(photo.getName(), actual.getName());
        else assertNull(actual.getName());
        if(photo.getPreviewUrl() != null) assertEquals(photo.getPreviewUrl(), actual.getPreviewUrl());
        else assertNull(actual.getPreviewUrl());
    }

    private void assertPhotoIsNotDeleted(Photo photo, Event event, Account owner) {
        Photo actual = this.photoDAO.read(photo.getId());
        assertNotNull(actual);
        assertEqualsPhoto(photo, actual);
        assertNotNull(this.photoDAO.getOrThrow(photo.getId()));

        assertFalse(this.photoDAO.getPhotosByEvent(event.getId()).isEmpty());
        assertFalse(this.photoDAO.getPhotosByOwner(owner.getId()).isEmpty());
        assertFalse(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()).isEmpty());

        assertFalse(this.commentDAO.getCommentsByPhoto(photo.getId()).isEmpty());
    }

    private void assertPhotoIsDeleted(Photo photo, Event event, Account owner) {
        assertNull(this.photoDAO.read(photo.getId()));
        try {
            this.photoDAO.getOrThrow(photo.getId());
            fail();
        } catch (EntityNotFoundException ignored){}

        assertTrue(this.photoDAO.getPhotosByEvent(event.getId()).isEmpty());
        assertTrue(this.photoDAO.getPhotosByOwner(owner.getId()).isEmpty());
        assertTrue(this.photoDAO.getPhotosByAccountInEvent(owner.getId(), event.getId()).isEmpty());

        assertTrue(this.commentDAO.getCommentsByPhoto(photo.getId()).isEmpty());
    }
}