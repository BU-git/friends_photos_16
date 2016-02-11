package com.bionic.fp.web.rest.v1;

import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.*;
import com.bionic.fp.web.rest.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by franky_str on 26.11.15.
 */
@Controller
@RequestMapping(API+V1+PHOTOS)
public class PhotoController {

	@Autowired private PhotoService photoService;
	@Autowired private CommentService commentService;
	@Autowired private MethodSecurityService methodSecurityService;


	//***************************************
	//                 @GET
	//***************************************


	/**
	 * Returns a photo
	 *
	 * @param photoId the photo id
	 * @return a photo
	 */
	@RequestMapping(value = PHOTO_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public PhotoInfo getPhoto(@PathVariable(PHOTO.ID) final Long photoId) {
		Photo photo = ofNullable(photoService.get(photoId)).orElseThrow(() -> new NotFoundException(photoId));
		return new PhotoInfo(photo);
	}

	/**
	 * Returns a photo file.
	 *
	 * @param photoId the photo id
	 * @return a photo file
	 */
	@RequestMapping(value = PHOTO_ID+FILE, method = GET, produces = APPLICATION_OCTET_STREAM_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public FileSystemResource getPhotoFile(@PathVariable(PHOTO.ID) final Long photoId) {
		Photo photo = ofNullable(photoService.get(photoId)).orElseThrow(() -> new NotFoundException(photoId));
		String url = photo.getUrl();
		File photoFile = new File(url);
		if (!photoFile.exists() || photoFile.isDirectory()) {
			throw new NotFoundException(photoId);
		}
		return new FileSystemResource(url);
	}

	/**
	 * Returns a list of photos of the owner
	 *
	 * @param ownerId the owner id
	 * @return a list of photos
	 */
	@RequestMapping(value = ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public final EntityInfoLists getAccountPhotos(@PathVariable(ACCOUNT.ID) final Long ownerId) {
		EntityInfoLists body = new EntityInfoLists();
		body.setPhotos(this.photoService.getPhotosByAccount(ownerId)
				.stream().parallel().map(PhotoInfo::new).collect(toList()));
		return body;
	}

	/**
	 * Returns a list of photo ids of the owner
	 *
	 * @param ownerId the owner id
	 * @return a list of photo ids
	 */
	@RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public final IdLists getAccountPhotoIds(@PathVariable(ACCOUNT.ID) final Long ownerId) {
		IdLists body = new IdLists();
		body.setPhotos(this.photoService.getPhotoIdsByAccount(ownerId));
		return body;
	}

	/**
	 * Returns a list of photos of the user.
	 * The user must be authenticated
	 *
	 * @return a list of photos
	 */
	@RequestMapping(value = ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public final EntityInfoLists getUserPhotos() {
		EntityInfoLists body = new EntityInfoLists();
		body.setPhotos(this.photoService.getPhotosByAccount(this.methodSecurityService.getUserId())
				.stream().parallel().map(PhotoInfo::new).collect(toList()));
		return body;
	}

	/**
	 * Returns a list of photo ids of the user.
	 * The user must be authenticated
	 *
	 * @return a list of photo ids
	 */
	@RequestMapping(value = ID+ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public final IdLists getUserPhotoIds() {
		IdLists body = new IdLists();
		body.setPhotos(this.photoService.getPhotoIdsByAccount(this.methodSecurityService.getUserId()));
		return body;
	}

	/**
	 * Returns a list of photos of the event
	 *
	 * @param eventId the event id
	 * @return a list of photos
	 */
	@RequestMapping(value = EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getEventPhotos(@PathVariable(EVENT.ID) final Long eventId) {
		EntityInfoLists body = new EntityInfoLists();
		body.setPhotos(this.photoService.getPhotosByEvent(eventId).stream().parallel()
				.map(PhotoInfo::new).collect(toList()));
		return body;
	}

	/**
	 * Returns a list of photo ids of the event
	 *
	 * @param eventId the event id
	 * @return a list of photo ids
	 */
	@RequestMapping(value = ID+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getEventPhotoIds(@PathVariable(EVENT.ID) final Long eventId) {
		IdLists body = new IdLists();
		body.setPhotos(this.photoService.getPhotoIdsByEvent(eventId));
		return body;
	}

	@RequestMapping(value = EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getPhotosByAccountInEvent(@PathVariable(EVENT.ID) final Long eventId,
													 @PathVariable(ACCOUNT.ID) final Long accountId) {
		EntityInfoLists body = new EntityInfoLists();
		body.setPhotos(this.photoService.getPhotosByAccountInEvent(accountId, eventId)
				.stream().parallel().map(PhotoInfo::new).collect(toList()));
		return body;
	}

	@RequestMapping(value = ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getPhotoIdsByAccountInEvent(@PathVariable(EVENT.ID) final Long eventId,
											   @PathVariable(ACCOUNT.ID) final Long accountId) {
		IdLists body = new IdLists();
		body.setPhotos(this.photoService.getPhotoIdsByAccountInEvent(accountId, eventId));
		return body;
	}

	@RequestMapping(value = EVENTS+EVENT_ID+ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public EntityInfoLists getPhotosByUserInEvent(@PathVariable(EVENT.ID) final Long eventId) {
		EntityInfoLists body = new EntityInfoLists();
		body.setPhotos(this.photoService.getPhotosByAccountInEvent(this.methodSecurityService.getUserId(), eventId)
				.stream().parallel().map(PhotoInfo::new).collect(toList()));
		return body;
	}

	@RequestMapping(value = ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public IdLists getPhotoIdsByUserInEvent(@PathVariable(EVENT.ID) final Long eventId) {
		IdLists body = new IdLists();
		body.setPhotos(this.photoService.getPhotoIdsByAccountInEvent(this.methodSecurityService.getUserId(), eventId));
		return body;
	}


	//***************************************
	//                 @POST
	//***************************************


	/**
	 * Adds a comment to the photo
	 *
	 * @param photoId the photo id
	 * @param commentDTO the comment
	 */
	@RequestMapping(value = PHOTO_ID+COMMENTS, method = POST, consumes = APPLICATION_JSON_VALUE)
	@ResponseStatus(CREATED)
	public void addComment(@PathVariable(PHOTO.ID) final Long photoId,
						   @RequestBody final CommentDTO commentDTO) {
		Photo photo = photoService.getOrThrow(photoId);
		this.methodSecurityService.checkPermission(photo.getEvent().getId(), Role::isCanAddComments);
		Comment comment = new Comment();
		comment.setAuthor(this.methodSecurityService.getUser());
		comment.setText(commentDTO.getCommentText());
		commentService.addCommentToPhoto(photoId, comment);
	}


	//***************************************
	//                 @PUT
	//***************************************


	/**
	 * Update a photo info
	 *
	 * @param name the photo name
	 * @return a photo
	 */
	@RequestMapping(value = PHOTO_ID, method = PUT, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public PhotoInfo updatePhoto(@PathVariable(PHOTO.ID) final Long photoId,
								 @RequestParam(value = PHOTO.NAME) final String name) {
		check(StringUtils.isNotEmpty(name), "Param 'name' is null or empty string");
		Photo photo = photoService.getOrThrow(photoId); // todo: 400 => 404?
		photo.setName(name);
		photo = photoService.update(photo);
		return new PhotoInfo(photo);
	}


	//***************************************
	//                 PRIVATE
	//***************************************


//	private EntityInfoLists getPhotos(final Long accountId) {
//		List<Photo> photos = this.photoService.getPhotosByOwnerId(accountId);
//		EntityInfoLists body = new EntityInfoLists();
//		body.setPhotos(photos.stream().parallel()
//				.map(PhotoInfo::new)
//				.collect(toList()));
//		return body;
//	}
}
