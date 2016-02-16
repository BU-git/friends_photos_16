package com.bionic.fp.web.rest.v1;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.Constants.RestConstants.COMMENT;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.exception.AppException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.CommentService;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.web.rest.dto.CommentDTO;
import com.bionic.fp.web.rest.dto.CommentInfo;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.rest.dto.IdLists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.Constants.RestConstants.EVENT;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.Constants.RestConstants.PHOTO;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * This is REST web-service that handles comment-related requests
 *
 * @author Sergiy Gabriel
 */
@Controller
@RequestMapping(API+V1+COMMENTS)
public class CommentController {

	@Autowired private CommentService commentService;
	@Autowired private MethodSecurityService methodSecurityService;


	//***************************************
	//                 @GET
	//***************************************


	/**
	 * Returns a comment
	 *
	 * @param commentId the account id
	 * @return a comment
	 */
	@RequestMapping(value = COMMENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public CommentInfo getCommentById(@PathVariable(COMMENT.ID) final Long commentId) {
		Comment comment = ofNullable(this.commentService.get(commentId)).orElseThrow(() -> new NotFoundException(commentId));
		return getCommentInfo(comment);
	}

	/**
	 * Returns a list of comments of the account
	 *
	 * @param accountId the account id
	 * @return a list of comments
	 */
	@RequestMapping(value = ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getCommentsByAccount(@PathVariable(ACCOUNT.ID) final Long accountId) {
		List<Comment> comments = this.commentService.getCommentsByAccount(accountId);
		EntityInfoLists entityInfoLists = new EntityInfoLists();
		entityInfoLists.setComments(comments.stream().parallel().map(this::getCommentInfo).collect(Collectors.toList()));
		return entityInfoLists;
	}

	/**
	 * Returns a list of comment ids of the account
	 *
	 * @param accountId the account id
	 * @return a list of comment ids
	 */
	@RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getCommentIdsByAccount(@PathVariable(ACCOUNT.ID) final Long accountId) {
		IdLists body = new IdLists();
		body.setComments(this.commentService.getCommentIdsByAccount(accountId));
		return body;
	}

	/**
	 * Returns a list of comments of the user
	 *
	 * @return a list of comments
	 */
	@RequestMapping(value = ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getCommentsByUser() {
		Long userId = this.methodSecurityService.getUserId();
		List<Comment> comments = this.commentService.getCommentsByAccount(userId);
		EntityInfoLists entityInfoLists = new EntityInfoLists();
		entityInfoLists.setComments(comments.stream().parallel().map(this::getCommentInfo).collect(Collectors.toList()));
		return entityInfoLists;
	}

	/**
	 * Returns a list of comment ids of the user
	 *
	 * @return a list of comment ids
	 */
	@RequestMapping(value = ID+ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getCommentIdsByUser() {
		Long userId = this.methodSecurityService.getUserId();
		IdLists body = new IdLists();
		body.setComments(this.commentService.getCommentIdsByAccount(userId));
		return body;
	}

	/**
	 * Returns a list of comments of the event
	 *
	 * @param eventId the event id
	 * @return a list of comments
	 */
	@RequestMapping(value = EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getCommentsByEvent(@PathVariable(EVENT.ID) final Long eventId) {
		EntityInfoLists body = new EntityInfoLists();
		body.setComments(this.commentService.getCommentsByEvent(eventId).stream().parallel()
				.map(comment -> {
					CommentInfo commentInfo = new CommentInfo(comment);
					commentInfo.setEventId(eventId);
					return commentInfo;
				}).collect(toList()));
		return body;
	}

	/**
	 * Returns a list of comment ids of the event
	 *
	 * @param eventId the event id
	 * @return a list of comment ids
	 */
	@RequestMapping(value = ID+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getCommentIdsByEvent(@PathVariable(EVENT.ID) final Long eventId) {
		IdLists body = new IdLists();
		body.setComments(this.commentService.getCommentIdsByEvent(eventId));
		return body;
	}

	/**
	 * Returns photo's comments list by given photo id
	 *
	 * @param photoId the photo id
	 * @return list of photo's comments
	 */
	@RequestMapping(value = PHOTOS+PHOTO_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getCommentsByPhoto(@PathVariable(PHOTO.ID) final Long photoId) {
		EntityInfoLists body = new EntityInfoLists();
		body.setComments(this.commentService.getCommentsByPhoto(photoId).stream().parallel()
				.map(comment -> {
					CommentInfo commentInfo = new CommentInfo(comment);
					commentInfo.setPhotoId(photoId);
					return commentInfo;
				}).collect(toList()));
		return body;
	}

	/**
	 * Returns a list of comment ids of the photo
	 *
	 * @param photoId the photo id
	 * @return a list of comment ids
	 */
	@RequestMapping(value = ID+PHOTOS+PHOTO_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getCommentIdsByPhoto(@PathVariable(PHOTO.ID) final Long photoId) {
		IdLists body = new IdLists();
		body.setComments(this.commentService.getCommentIdsByPhoto(photoId));
		return body;
	}


	//***************************************
	//                 @PUT
	//***************************************


	/**
	 * Updates the comment
	 *
	 * @param commentDto the comment
	 */
	@RequestMapping(value = COMMENT_ID,  method = PUT, consumes = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	public void updateComment(@PathVariable(COMMENT.ID) final Long commentId, @RequestBody final CommentDTO commentDto) {
		Comment comment = ofNullable(this.commentService.get(commentId)).orElseThrow(() -> new NotFoundException(commentId));
		Long userId = this.methodSecurityService.getUserId();
		if(userId.equals(comment.getAuthor().getId())) {
			if (StringUtils.isNotEmpty(commentDto.getCommentText()) && !commentDto.getCommentText().equals(comment.getText())) {
				comment.setText(commentDto.getCommentText());
				this.commentService.update(comment);
			}
		} else {
			throw new AccessDeniedException("You don't have access to change the comment");
		}
	}


	//***************************************
	//               @DELETE
	//***************************************


	/**
	 * Deletes the comment
	 *
	 * @param commentId the comment id
	 */
	@RequestMapping(value = COMMENT_ID, method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void deleteComment(@PathVariable(COMMENT.ID) final Long commentId) {
		ofNullable(commentService.get(commentId)).ifPresent(comment -> {
			Long userId = this.methodSecurityService.getUserId();
			if(userId.equals(comment.getAuthor().getId())) {
				this.commentService.softDelete(commentId);
			} else {
				throw new AccessDeniedException("You don't have access to delete the comment");
			}
		});
	}


	//***************************************
	//                PRIVATE
	//***************************************


	private CommentInfo getCommentInfo(Comment comment) {
		CommentInfo commentInfo = new CommentInfo(comment);
		ofNullable(this.commentService.getEventOf(comment.getId())).ifPresent(
				event -> commentInfo.setEventId(event.getId()));
		if(commentInfo.getEventId() == null) {
			ofNullable(this.commentService.getPhotoOf(comment.getId())).ifPresent(
					photo -> commentInfo.setPhotoId(photo.getId()));
			if(commentInfo.getPhotoId() == null) {
				throw new AppException("The comment doesn't belong to any entity");
			}
		}
		return commentInfo;
	}
}
