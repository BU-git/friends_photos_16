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
		return this.getPhotos(ownerId);
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
		return this.getPhotoIds(ownerId);
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
		Long userId = this.methodSecurityService.getUserId();
		return this.getPhotos(userId);
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
		Long userId = this.methodSecurityService.getUserId();
		return this.getPhotoIds(userId);
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
		body.setPhotos(this.photoService.getPhotosByEvent(eventId).stream().parallel()
				.map(Photo::getId).collect(toList()));
		return body;
	}


	//***************************************
	//                 @POST
	//***************************************


	/**
	 * Saves a photo file to filesystem
	 * and save photo info to DB.
	 *
	 * @param file the file
	 * @param eventId the event id
	 * @param name the photo name
	 * @param description the photo description
	 * @return a photo
	 */
	@RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(CREATED)
	@ResponseBody
	public PhotoInfo createPhoto(@RequestParam(PHOTO.FILE) final MultipartFile file,
								 @RequestParam(EVENT.ID) final Long eventId,
								 @RequestParam(value = PHOTO.NAME, required = false) final String name,
								 @RequestParam(value = PHOTO.DESCRIPTION, required = false) final String description) throws IOException {
		Long userId = this.methodSecurityService.getUserId();
		Photo photo = this.photoService.saveToFileSystem(eventId, userId, file, name);
		return new PhotoInfo(photo);
	}

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


	private EntityInfoLists getPhotos(final Long accountId) {
		List<Photo> photos = this.photoService.getPhotosByOwnerId(accountId);
		EntityInfoLists body = new EntityInfoLists();
		body.setPhotos(photos.stream().parallel()
				.map(PhotoInfo::new)
				.collect(Collectors.toList()));
		return body;
	}

	private IdLists getPhotoIds(final Long accountId) {
		List<Long> photos = this.photoService.getPhotoIdsByOwnerId(accountId);
		IdLists body = new IdLists();
		body.setPhotos(photos);
		return body;
	}
}
