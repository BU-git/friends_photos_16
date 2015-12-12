package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.web.rest.dto.PhotoInfoDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.PhotoService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by franky_str on 26.11.15.
 */

@Controller
@RequestMapping("/photo")
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
	@RequestMapping(value = "/{photo_id:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PhotoInfoDTO> getPhotoInfo(@PathVariable("photo_id") Long photoId) {
		Photo photo = photoService.getById(photoId);
		if (photo == null) {
			return new ResponseEntity<PhotoInfoDTO>(HttpStatus.NOT_FOUND);
		}
		PhotoInfoDTO photoInfoDTO = new PhotoInfoDTO();
		photoInfoDTO.setName(photo.getName());
		photoInfoDTO.setOwnerID(photo.getOwner() == null ? null : photo.getOwner().getId());
		photoInfoDTO.setUrl(photo.getUrl());

		return new ResponseEntity<PhotoInfoDTO>(photoInfoDTO, HttpStatus.OK);
	}

	/**
	 * Get photo image.
	 *
	 * @param photoId id of the photo
	 * @return file
	 */
	@RequestMapping(
			value = "/file/{photo_id:[\\d]+}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<FileSystemResource> getPhotoFile(@PathVariable("photo_id") Long photoId) {
		Photo photo = photoService.getById(photoId);
		if (photo == null) {
			return new ResponseEntity<FileSystemResource>(HttpStatus.NOT_FOUND);
		}
		String url = photo.getUrl();
		File photoFile = new File(url);
		if (!photoFile.exists() || photoFile.isDirectory()) {
			return new ResponseEntity<FileSystemResource>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<FileSystemResource>(new FileSystemResource(url), HttpStatus.OK);
	}


	@RequestMapping(value = "/event/{event_id:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<PhotoInfoDTO>> getPhotosInfoByEvent(@PathVariable("event_id") Long eventId) {

		Event event = eventService.get(eventId);
		if (event == null) {
			// TODO add message 'event was not found'
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Photo> photos = photoService.getPhotosByEvent(event);
		if (photos != null) {
			List<PhotoInfoDTO> photosDto = photos.stream().parallel().map(photo -> {
				PhotoInfoDTO dto = new PhotoInfoDTO();
				dto.setName(photo.getName());
				dto.setOwnerID(photo.getOwner().getId());
				dto.setUrl(photo.getUrl());
				return dto;
			}).collect(Collectors.toList());
			return new ResponseEntity<>(photosDto, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<PhotoInfoDTO>> getPhotoList(
			@RequestParam("owner") Long ownerId) {

		Account owner = accountService.get(ownerId);
		List<Photo> photos = photoService.getPhotosList(owner);
		if (photos != null) {
			List<PhotoInfoDTO> photosDto = photos.stream().parallel().map(photo -> {
				PhotoInfoDTO dto = new PhotoInfoDTO();
				dto.setName(photo.getName());
				dto.setOwnerID(photo.getOwner().getId());
				dto.setUrl(photo.getUrl());
				return dto;
			}).collect(Collectors.toList());
			return new ResponseEntity<>(photosDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//***************************************
	//                 @POST
	//***************************************
	/**
	 * Save photo image to filesystem
	 * and save photo info to DB.
	 *
	 * @param file
	 * @param ownerId
	 * @param name
	 * @param description
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<PhotoInfoDTO> createPhoto(
			@RequestParam("file") MultipartFile file,
			@RequestParam("owner_id") Long ownerId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "description", required = false) String description
	) {
		// FIXME add messages to errors
		if (file.isEmpty()) {
			return new ResponseEntity<PhotoInfoDTO>(HttpStatus.BAD_REQUEST);
		}
		Account owner = accountService.get(ownerId);
		if (owner == null) {
			return new ResponseEntity<PhotoInfoDTO>(HttpStatus.BAD_REQUEST);
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
			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File("/friendsphotos/" + resultFileName)));
			stream.write(bytes);
			stream.close();

			Photo photo = new Photo();
			photo.setName(name == null ? file.getOriginalFilename() : name);
			photo.setUrl("/friendsphotos/" + resultFileName);
			photo.setOwner(owner);
			photo = photoService.create(photo);
			PhotoInfoDTO photoInfoDTO = new PhotoInfoDTO();
			photoInfoDTO.setName(photo.getName());
			photoInfoDTO.setOwnerID(photo.getOwner().getId());
			photoInfoDTO.setUrl(photo.getUrl());
			return new ResponseEntity<PhotoInfoDTO>(photoInfoDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<PhotoInfoDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
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
	@RequestMapping(value = "/{photo_id:[\\d]+}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<PhotoInfoDTO> updatePhoto(
			@PathVariable("photo_id") Long photoId,
			@RequestParam(value = "name") String name
	) {
		if (StringUtils.isEmpty(name)) {
			throw new InvalidParameterException("Param 'name' is null or empty string");
		}
		Photo photo = photoService.getById(photoId);
		if (photo == null) {
			throw new EntityNotFoundException("photo", photoId);
		}
		photo.setName(name);
		photo = photoService.update(photo);
		PhotoInfoDTO photoInfoDTO = new PhotoInfoDTO();
		photoInfoDTO.setName(photo.getName());
		photoInfoDTO.setOwnerID(photo.getOwner().getId());
		photoInfoDTO.setUrl(photo.getUrl());
		return new ResponseEntity<PhotoInfoDTO>(photoInfoDTO, HttpStatus.OK);
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
