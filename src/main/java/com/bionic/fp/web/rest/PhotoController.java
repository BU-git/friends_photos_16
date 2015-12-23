package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.PhotoNotFoundException;
import com.bionic.fp.web.rest.dto.PhotoInfoDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.PhotoService;
import com.bionic.fp.web.security.SessionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import static com.bionic.fp.web.rest.RestConstants.*;
import static com.bionic.fp.web.rest.RestConstants.PATH.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by franky_str on 26.11.15.
 */

@Controller
@RequestMapping(PHOTOS)
public class PhotoController {

	@Autowired
	private PhotoService photoService;
	@Autowired
	private EventService eventService;
	@Autowired
	private AccountService accountService;

	private SecureRandom random = new SecureRandom();


	//***************************************
	//                 @GET
	//***************************************


	@RequestMapping(value = PHOTO_ID, method = GET)
	@ResponseBody
	public ResponseEntity<PhotoInfoDTO> getPhotoInfo(@PathVariable(PHOTO.ID) Long photoId) {
		Photo photo = photoService.getById(photoId);
		if (photo == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		PhotoInfoDTO photoInfoDTO = new PhotoInfoDTO();
		photoInfoDTO.setName(photo.getName());
		photoInfoDTO.setOwnerId(photo.getOwner() == null ? null : photo.getOwner().getId());
		photoInfoDTO.setUrl(photo.getUrl());

		return new ResponseEntity<>(photoInfoDTO, OK);
	}

	/**
	 * Get photo image.
	 *
	 * @param photoId id of the photo
	 * @return file
	 */
	@RequestMapping(value = PHOTO_ID+FILE, method = GET, produces = APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<FileSystemResource> getPhotoFile(@PathVariable(PHOTO.ID) Long photoId) {
		Photo photo = photoService.getById(photoId);
		if (photo == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		String url = photo.getUrl();
		File photoFile = new File(url);
		if (!photoFile.exists() || photoFile.isDirectory()) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		return new ResponseEntity<>(new FileSystemResource(url), OK);
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
	@RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<PhotoInfoDTO> createPhoto(
			@RequestParam(PHOTO.FILE) MultipartFile file,
			@RequestParam(EVENT.ID) Long eventId,
			@RequestParam(value = PHOTO.NAME, required = false) String name,
			@RequestParam(value = PHOTO.DESCRIPTION, required = false) String description,
			HttpServletRequest servletRequest
	) {
		// FIXME add messages to errors
		HttpSession session = servletRequest.getSession(false);
		if (session == null) {
			return new ResponseEntity<>(UNAUTHORIZED);
		}
		Long ownerId = SessionUtils.getUserId(session);
		if (file.isEmpty() || ownerId == null) {
			return new ResponseEntity<>(BAD_REQUEST);
		}
		Account owner = accountService.get(ownerId);
		Event event = eventService.get(eventId);
		if (owner == null || event == null) {
			return new ResponseEntity<>(BAD_REQUEST);
		}
		try {
			String fileName = file.getOriginalFilename();
			String fileExtension = FilenameUtils.getExtension(fileName);

			String resultFileName = new BigInteger(130, random).toString(32);
			if (StringUtils.isNotEmpty(fileExtension)) {
				resultFileName = new StringBuilder(resultFileName)
						.append('.')
						.append(fileExtension)
						.toString();
			}

			byte[] bytes = file.getBytes();
			// FIXME set correct path to file
//			String fullPath = new StringBuilder("D:\\Projects\\fp-server\\photos_cloud\\")
//					.append(eventId.toString())
//					.append('\\')
//					.append(ownerId.toString())
//					.toString();
			String fullPath = new StringBuilder("/home/artem/ff/")
					.append(eventId.toString())
					.append('/')
					.append(ownerId.toString())
					.toString();

			// FIXME temporary testing fix
			new File(fullPath).mkdirs();
//			fullPath += '\\' + resultFileName;
			fullPath += '/' + resultFileName;

			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(fullPath)));
			stream.write(bytes);
			stream.close();

			Photo photo = new Photo();
			photo.setName(name == null ? file.getOriginalFilename() : name);
			photo.setUrl(fullPath);
			photo.setOwner(owner);
			photo.setEvent(event);
			photo = photoService.create(photo);
			PhotoInfoDTO photoInfoDTO = new PhotoInfoDTO();
			photoInfoDTO.setName(photo.getName());
			photoInfoDTO.setOwnerId(photo.getOwner().getId());
			photoInfoDTO.setEventId(photo.getEvent().getId());
			return new ResponseEntity<>(photoInfoDTO, CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
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
	@RequestMapping(value = PHOTO_ID, method = PUT)
	@ResponseBody
	public ResponseEntity<PhotoInfoDTO> updatePhoto(
			@PathVariable(PHOTO.ID) Long photoId,
			@RequestParam(value = PHOTO.NAME) String name
	) {
		if (StringUtils.isEmpty(name)) {
			throw new InvalidParameterException("Param 'name' is null or empty string");
		}
		Photo photo = photoService.getById(photoId);
		if (photo == null) {
			throw new PhotoNotFoundException(photoId);
		}
		photo.setName(name);
		photo = photoService.update(photo);
		PhotoInfoDTO photoInfoDTO = new PhotoInfoDTO();
		photoInfoDTO.setName(photo.getName());
		photoInfoDTO.setOwnerId(photo.getOwner().getId());
		photoInfoDTO.setUrl(photo.getUrl());
		return new ResponseEntity<>(photoInfoDTO, OK);
	}


    /*
	@RequestMapping(value = "owner/{owner_id:[\\d]+}/event/{event_id:[\\d]+}/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<IdInfoDTO> handleFileUpload(@ModelAttribute("upload_form") , @PathVariable("owner_id") Long ownerId, @PathVariable("event_id") Long eventId ) throws IOException {

        Event event = eventService.getById(eventId);
        Account account = accountService.getById(ownerId);

        if (event == null || account == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Photo photo = new Photo(file.getOriginalFilename(), event, account);

        Long id = photoService.saveSinglePhoto(photo, file.getInputStream());
        return new ResponseEntity<IdInfoDTO>(new IdInfoDTO(id), HttpStatus.CREATED);
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
//        Event event = eventService.getById(eventId /*photoDto.getEventId()*/);
//        Account account = accountService.getById(ownerId /*photoDto.getOwnerId()*/);
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
