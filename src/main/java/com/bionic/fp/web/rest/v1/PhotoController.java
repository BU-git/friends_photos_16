package com.bionic.fp.web.rest.v1;

import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.*;
import com.bionic.fp.web.rest.dto.CommentDTO;
import com.bionic.fp.web.rest.dto.CommentInfo;
import com.bionic.fp.web.rest.dto.PhotoInfo;
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
	 * Returns photo's comments list
	 * by given photo id.
	 *
	 * @param photoId the photo id.
	 * @return list of photo's comments.
	 */
	@RequestMapping(value = PHOTO_ID+COMMENTS, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public List<CommentInfo> getCommentsByPhoto(@PathVariable(PHOTO.ID) final Long photoId) {
		Photo photo = ofNullable(photoService.get(photoId)).orElseThrow(() -> new NotFoundException(photoId));
		List<Comment> commentList = photo.getComments();
		List<CommentInfo> commentInfoList = commentList.stream().parallel().map(CommentInfo::new).collect(Collectors.toList());
		return commentInfoList;
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
		Photo photo = photoService.get(photoId);
		this.methodSecurityService.checkPermission(photo.getEvent().getId(), Role::isCanAddComments);
		Comment comment = new Comment();
		comment.setAuthor(this.methodSecurityService.getUser());
		comment.setText(commentDTO.getCommentText());
		photoService.addComment(photo, comment);
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
		Photo photo = photoService.getOrThrow(photoId);
		photo.setName(name);
		photo = photoService.update(photo);
		return new PhotoInfo(photo);
	}
}
