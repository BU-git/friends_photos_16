package com.bionic.fp.dao;

import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link PhotoDAO}
 *
 * @author Sergiy Gabriel
 */
public class CommentDaoIT extends AbstractDaoIT {

    @Test
    public void testGetCommentsByEventSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        List<Comment> comments = this.commentDAO.getCommentsByEvent(event.getId());
        assertTrue(comments.isEmpty());

        Comment comment1 = getSavedComment(event, owner);

        comments = this.commentDAO.getCommentsByEvent(event.getId());

        assertEquals(1, comments.size());
        assertEqualsEntity(comment1, comments.get(0));

        Comment comment2 = getSavedComment(event, owner);

        comments = this.commentDAO.getCommentsByEvent(event.getId());

        assertEquals(2, comments.size());
        assertEqualsEntities(comments, comment1, comment2);
    }

    @Test
    public void testGetCommentsByPhotoSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        List<Comment> comments = this.commentDAO.getCommentsByPhoto(photo.getId());
        assertTrue(comments.isEmpty());

        Comment comment1 = getSavedComment(photo, owner);

        comments = this.commentDAO.getCommentsByPhoto(photo.getId());

        assertEquals(1, comments.size());
        assertEqualsEntity(comment1, comments.get(0));

        Comment comment2 = getSavedComment(photo, owner);

        comments = this.commentDAO.getCommentsByPhoto(photo.getId());

        assertEquals(2, comments.size());
        assertEqualsEntities(comments, comment1, comment2);
    }

    @Test
    public void testGetCommentsByAuthorSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);

        List<Comment> comments = this.commentDAO.getCommentsByAuthor(owner.getId());
        assertTrue(comments.isEmpty());

        Comment comment1 = getSavedComment(photo, owner);

        comments = this.commentDAO.getCommentsByAuthor(owner.getId());

        assertEquals(1, comments.size());
        assertEqualsEntity(comment1, comments.get(0));

        Comment comment2 = getSavedComment(event, owner);

        comments = this.commentDAO.getCommentsByAuthor(owner.getId());

        assertEquals(2, comments.size());
        assertEqualsEntities(comments, comment1, comment2);
    }

    @Test
    public void testSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment1 = getSavedComment(event, owner);
        Comment comment2 = getSavedComment(photo, owner);

        assertCommentIsNotDeleted(comment1, owner, event, null);
        assertCommentIsNotDeleted(comment2, owner, null, photo);

        this.commentDAO.setDeleted(comment1.getId(), true);
        this.commentDAO.setDeleted(comment2.getId(), true);

        assertCommentIsDeleted(comment1, owner, event, null);
        assertCommentIsDeleted(comment2, owner, null, photo);

        Comment comment3 = getSavedComment(event, owner);
        Comment comment4 = getSavedComment(event, owner);
        Comment comment5 = getSavedComment(photo, owner);
        Comment comment6 = getSavedComment(photo, owner);
        assertEqualsEntities(this.commentDAO.getCommentsByAuthor(owner.getId()), comment3, comment4, comment5,comment6);
        assertEqualsEntities(this.commentDAO.getCommentsByEvent(event.getId()), comment3, comment4);
        assertEqualsEntities(this.commentDAO.getCommentsByPhoto(photo.getId()), comment5, comment6);

        this.commentDAO.setDeleted(comment3.getId(), true);
        assertEqualsEntities(this.commentDAO.getCommentsByAuthor(owner.getId()), comment4, comment5,comment6);
        assertEqualsEntities(this.commentDAO.getCommentsByEvent(event.getId()), comment4);
        assertEqualsEntities(this.commentDAO.getCommentsByPhoto(photo.getId()), comment5, comment6);

        this.commentDAO.setDeleted(comment4.getId(), true);
        assertEqualsEntities(this.commentDAO.getCommentsByAuthor(owner.getId()), comment5,comment6);
        assertEqualsEntities(this.commentDAO.getCommentsByEvent(event.getId()));
        assertEqualsEntities(this.commentDAO.getCommentsByPhoto(photo.getId()), comment5, comment6);

        this.commentDAO.setDeleted(comment5.getId(), true);
        assertEqualsEntities(this.commentDAO.getCommentsByAuthor(owner.getId()),comment6);
        assertEqualsEntities(this.commentDAO.getCommentsByEvent(event.getId()));
        assertEqualsEntities(this.commentDAO.getCommentsByPhoto(photo.getId()), comment6);

        this.commentDAO.setDeleted(comment6.getId(), true);
        assertEqualsEntities(this.commentDAO.getCommentsByAuthor(owner.getId()));
        assertEqualsEntities(this.commentDAO.getCommentsByEvent(event.getId()));
        assertEqualsEntities(this.commentDAO.getCommentsByPhoto(photo.getId()));
    }

    @Test
    public void testSoftDeleteAndRecoverSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment1 = getSavedComment(event, owner);
        Comment comment2 = getSavedComment(photo, owner);

        assertCommentIsNotDeleted(comment1, owner, event, null);
        assertCommentIsNotDeleted(comment2, owner, null, photo);

        this.commentDAO.setDeleted(comment1.getId(), true);
        this.commentDAO.setDeleted(comment2.getId(), true);

        assertCommentIsDeleted(comment1, owner, event, null);
        assertCommentIsDeleted(comment2, owner, null, photo);

        this.commentDAO.setDeleted(comment1.getId(), false);
        this.commentDAO.setDeleted(comment2.getId(), false);

        assertCommentIsNotDeleted(comment1, owner, event, null);
        assertCommentIsNotDeleted(comment2, owner, null, photo);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment1 = getSavedComment(event, owner);
        Comment comment2 = getSavedComment(photo, owner);

        assertCommentIsNotDeleted(comment1, owner, event, null);
        assertCommentIsNotDeleted(comment2, owner, null, photo);

        this.commentDAO.delete(comment1.getId());
        this.commentDAO.delete(comment2.getId());

        assertCommentIsDeleted(comment1, owner, event, null);
        assertCommentIsDeleted(comment2, owner, null, photo);

        this.commentDAO.delete(comment1.getId());
        this.commentDAO.delete(comment2.getId());
    }

    @Test
    public void testDeleteAfterSoftDeleteSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment1 = getSavedComment(event, owner);
        Comment comment2 = getSavedComment(photo, owner);

        assertCommentIsNotDeleted(comment1, owner, event, null);
        assertCommentIsNotDeleted(comment2, owner, null, photo);

        this.commentDAO.setDeleted(comment1.getId(), true);
        this.commentDAO.setDeleted(comment2.getId(), true);

        assertCommentIsDeleted(comment1, owner, event, null);
        assertCommentIsDeleted(comment2, owner, null, photo);

        this.commentDAO.delete(comment1.getId());
        this.commentDAO.delete(comment2.getId());

        assertCommentIsDeleted(comment1, owner, event, null);
        assertCommentIsDeleted(comment2, owner, null, photo);

        this.commentDAO.delete(comment1.getId());
        this.commentDAO.delete(comment2.getId());
    }

    @Test
    public void testGetPhotoOfSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment1 = getSavedComment(event, owner);
        Comment comment2 = getSavedComment(photo, owner);

        Photo actual = this.commentDAO.getPhotoOf(comment2.getId());

        assertEquals(photo, actual);
        assertNull(this.commentDAO.getPhotoOf(comment1.getId()));
    }

    @Test
    public void testGetEventOfSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);
        Photo photo = getSavedPhoto(event, owner);
        Comment comment1 = getSavedComment(event, owner);
        Comment comment2 = getSavedComment(photo, owner);

        Event actual = this.commentDAO.getEventOf(comment1.getId());

        assertEquals(event, actual);
        assertNull(this.commentDAO.getEventOf(comment2.getId()));
    }

    private void assertCommentIsNotDeleted(final Comment comment, final Account owner, final Event event, final Photo photo) {
        Comment actual = this.commentDAO.read(comment.getId());
        assertNotNull(actual);
        assertEqualsEntity(comment, actual);
        assertNotNull(this.commentDAO.getOrThrow(comment.getId()));
        assertFalse(this.commentDAO.getCommentsByAuthor(owner.getId()).isEmpty());
        if(event != null) {
            assertFalse(this.commentDAO.getCommentsByEvent(event.getId()).isEmpty());
        }
        if(photo != null) {
            assertFalse(this.commentDAO.getCommentsByPhoto(photo.getId()).isEmpty());
        }
    }

    private void assertCommentIsDeleted(final Comment comment, final Account owner, final Event event, final Photo photo) {
        assertNull(this.commentDAO.read(comment.getId()));
        try {
            this.commentDAO.getOrThrow(comment.getId());
            fail();
        } catch (EntityNotFoundException ignored){}
        assertTrue(this.commentDAO.getCommentsByAuthor(owner.getId()).isEmpty());
        if(event != null) {
            assertTrue(this.commentDAO.getCommentsByEvent(event.getId()).isEmpty());
        }
        if(photo != null) {
            assertTrue(this.commentDAO.getCommentsByPhoto(photo.getId()).isEmpty());
        }
    }

}