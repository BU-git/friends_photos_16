package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.permission.PermissionsDeniedException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.AccountEventService;
import com.bionic.fp.web.rest.dto.CommentDTO;
import com.bionic.fp.web.rest.dto.PhotoInfoDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.PhotoService;
import com.bionic.fp.web.security.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

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
@RequestMapping(API+PHOTOS)
public class PhotoController {

	@Autowired
	private PhotoService photoService;
	@Autowired
	private EventService eventService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountEventService accountEventService;

	private SecureRandom random = new SecureRandom();


	//***************************************
	//                 @GET
	//***************************************


	@RequestMapping(value = PHOTO_ID, method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public PhotoInfoDTO getPhotoInfo(@PathVariable(PHOTO.ID) final Long photoId) {
		Photo photo = ofNullable(photoService.get(photoId)).orElseThrow(() -> new NotFoundException(photoId));
		return new PhotoInfoDTO(photo);
	}

	/**
	 * Get photo image.
	 *
	 * @param photoId id of the photo
	 * @return file
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


	//***************************************
	//                 @POST
	//***************************************


	/**
	 * Save photo image to filesystem
	 * and save photo info to DB.
	 *
	 * @param file
	 * @param eventId
	 * @param name
	 * @param description
	 * @return
	 */
	@RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(CREATED)
	@ResponseBody
	public PhotoInfoDTO createPhoto(@RequestParam(PHOTO.FILE) final MultipartFile file,
									@RequestParam(EVENT.ID) final Long eventId,
									@RequestParam(value = PHOTO.NAME, required = false) final String name,
									@RequestParam(value = PHOTO.DESCRIPTION, required = false) final String description,
									final HttpServletRequest servletRequest) throws IOException {
		Long userId = SessionUtils.getUserId(servletRequest.getSession(false));
		Photo photo = this.photoService.saveToFileSystem(eventId, userId, file, name);
		return new PhotoInfoDTO(photo);
	}

	@RequestMapping(value = PHOTO_ID+ADD_COMMENT , method = POST, consumes = APPLICATION_JSON_VALUE)
	@ResponseStatus(CREATED)
	public void addComment(@PathVariable(PHOTO.ID) final Long photoId,
						   @RequestBody final CommentDTO commentDTO,
						   final HttpServletRequest servletRequest) {
		Long userId = SessionUtils.getUserId(servletRequest.getSession(false));
		Photo photo = photoService.get(photoId);
		Role role = accountEventService.get(userId, photo.getEvent().getId()).getRole();
		if(!role.isCanAddComments()) {
			throw new PermissionsDeniedException();
		}
		Comment comment = new Comment();
		comment.setAuthor(accountService.get(userId));
		comment.setText(commentDTO.getCommentText());
		photoService.addComment(photo, comment);
	}


	//***************************************
	//                 @PUT
	//***************************************


	/**
	 * Update photo info
	 *
	 * @param name
	 * @return
	 */
	@RequestMapping(value = PHOTO_ID, method = PUT, produces = APPLICATION_JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public PhotoInfoDTO updatePhoto(@PathVariable(PHOTO.ID) final Long photoId,
									@RequestParam(value = PHOTO.NAME) final String name) {
		check(StringUtils.isNotEmpty(name), "Param 'name' is null or empty string");
		Photo photo = photoService.getOrThrow(photoId);
		photo.setName(name);
		photo = photoService.update(photo);
		return new PhotoInfoDTO(photo);
	}


    /*
	@RequestMapping(value = "owner/{owner_id:[\\d]+}/event/{event_id:[\\d]+}/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<IdInfo> handleFileUpload(@ModelAttribute("upload_form") , @PathVariable("owner_id") Long ownerId, @PathVariable("event_id") Long eventId ) throws IOException {

        Event event = eventService.get(eventId);
        Account account = accountService.get(ownerId);

        if (event == null || account == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Photo photo = new Photo(file.getOriginalFilename(), event, account);

        Long id = photoService.saveSinglePhoto(photo, file.getInputStream());
        return new ResponseEntity<IdInfo>(new IdInfo(id), HttpStatus.CREATED);
    }
*/
//        String name = multipartFileRef.getOriginalFilename();
//        multipartFileRef.getInputStream();
//        Photo photo = new Photo();
//        photo.setName(name);
//
//
//        long eventId = 1;
//        long ownerId = 1;
////        photo.setName(photoDto.getName());
//        Event event = eventService.get(eventId /*photoDto.getEventId()*/);
//        Account account = accountService.get(ownerId /*photoDto.getOwnerId()*/);
//
////        if (event == null || account == null){
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        photo.setEvent(event);
////        photo.setOwner(account);
//
//        photo.setOwner(account);
//        photo.setEvent(event);
//
//        Long aLong = photoService.saveSinglePhoto(photo, multipartFileRef.getInputStream());
//
//        if (aLong == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//}

}
