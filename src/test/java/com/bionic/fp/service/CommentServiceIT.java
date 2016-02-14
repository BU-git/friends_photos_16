//package com.bionic.fp.service;
//
//import com.bionic.fp.AbstractIT;
//import com.bionic.fp.domain.Account;
//import com.bionic.fp.domain.Comment;
//import com.bionic.fp.domain.Event;
//import com.bionic.fp.domain.Photo;
//import org.junit.Test;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
///**
// * This is an integration test that verifies {@link CommentService}
// *
// * @author Sergiy Gabriel
// */
//@ContextConfiguration(value = {
//        "classpath:spring/test-root-context.xml",
//        "classpath:spring/test-stateful-spring-security.xml"})
//public class CommentServiceIT extends AbstractIT {
//
//    @Test
//    public void testAddCommentToEventSuccess() throws Exception {
//        Account owner = getRegularUser();
//        Event event = getSavedEventMin(owner);
//
//        List<Comment> comments = this.commentService.getCommentsByEvent(event.getId());
//        assertTrue(comments.isEmpty());
//
//        Comment comment = getNewComment(owner);
//        assertNull(comment.getCreated());
//
//        this.commentService.addCommentToEvent(event.getId(), comment);
//
//        comments = this.commentService.getCommentsByEvent(event.getId());
//        assertEquals(1, comments.size());
//        Comment actual = comments.get(0);
//        assertNotNull(actual.getCreated());
//        assertEquals(comment.getAuthor().getId(), actual.getAuthor().getId());
//        assertEquals(comment.getText(), actual.getText());
//
//        Comment comment2 = getNewComment(owner);
//        assertNull(comment2.getCreated());
//
//        this.commentService.addCommentToEvent(event.getId(), comment2);
//
//        comments = this.commentService.getCommentsByEvent(event.getId());
//        assertEquals(2, comments.size());
//    }
//
//    @Test
//    public void testAddCommentToPhotoSuccess() throws Exception {
//        Account owner = getRegularUser();
//        Event event = getSavedEventMin(owner);
//        Photo photo = getSavedPhoto(event, owner);
//
//        List<Comment> comments = this.commentService.getCommentsByPhoto(photo.getId());
//        assertTrue(comments.isEmpty());
//
//        Comment comment = getNewComment(owner);
//        assertNull(comment.getCreated());
//
//        this.commentService.addCommentToPhoto(photo.getId(), comment);
//
//        comments = this.commentService.getCommentsByPhoto(photo.getId());
//        assertEquals(1, comments.size());
//        Comment actual = comments.get(0);
//        assertNotNull(actual.getCreated());
//        assertEquals(comment.getAuthor().getId(), actual.getAuthor().getId());
//        assertEquals(comment.getText(), actual.getText());
//
//        Comment comment2 = getNewComment(owner);
//        assertNull(comment2.getCreated());
//
//        this.commentService.addCommentToPhoto(photo.getId(), comment2);
//
//        comments = this.commentService.getCommentsByPhoto(photo.getId());
//        assertEquals(2, comments.size());
//    }
//}