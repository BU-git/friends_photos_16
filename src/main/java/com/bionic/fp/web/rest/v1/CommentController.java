package com.bionic.fp.web.rest.v1;

import com.bionic.fp.domain.Comment;
import com.bionic.fp.service.CommentService;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.web.rest.dto.CommentInfo;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.rest.dto.IdLists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.bionic.fp.Constants.RestConstants.EVENT;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.Constants.RestConstants.PHOTO;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
				.map(CommentInfo::new).collect(toList()));
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
		body.setComments(this.commentService.getCommentsByEvent(eventId).stream().parallel()
				.map(Comment::getId).collect(toList()));
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
				.map(CommentInfo::new).collect(toList()));
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
		body.setComments(this.commentService.getCommentsByPhoto(photoId).stream().parallel()
				.map(Comment::getId).collect(toList()));
		return body;
	}
}
