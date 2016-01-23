package com.bionic.fp.service;

import com.bionic.fp.Constants;
import com.bionic.fp.dao.PhotoDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.AppException;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.PhotoNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.Constants.FILE_SEPERATOR;
import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * Created by franky_str on 22.11.15.
 */
@Service
@Transactional
public class PhotoService {

    @Inject
    private PhotoDAO photoDAO;
	@Inject
	private EventService eventService;
    @Inject
	private AccountService accountService;

    @Value("${photo.folder}")
    private String directory;

    private SecureRandom random = new SecureRandom();

    public PhotoService(){}

	public Photo update(Photo photo) {
		return photoDAO.update(photo);
	}

    /**
     * @param id photo ID
     * @return Photo entity from database
     */
    public Photo get(Long id) {
        return photoDAO.read(id);
    }

    /**
     * Returns a photo by ID or throw exception
     *
     * @param photoId the photo ID
     * @return a photo
     * @throws InvalidParameterException if the photo ID is invalid
     * @throws PhotoNotFoundException if the photo doesn't exist
     */
    public Photo getOrThrow(final Long photoId) throws InvalidParameterException, PhotoNotFoundException {
        return ofNullable(this.get(photoId)).orElseThrow(() -> new PhotoNotFoundException(photoId));
    }

    /**
     * Returns a list of the photos by the owner ID
     *
     * @param ownerId the owner ID
     * @return a list of the photos of the owner
     */
	public List<Photo> getPhotosByOwnerId(final Long ownerId) {
		return ownerId == null ? Collections.emptyList() : this.photoDAO.getPhotosByOwnerId(ownerId);
	}

    /**
     * Returns an ID list of the photos by the owner ID
     *
     * @param ownerId the owner ID
     * @return an ID list of the photos of the owner
     */
    public List<Long> getPhotoIdsByOwnerId(final Long ownerId) {
        return this.getPhotosByOwnerId(ownerId).stream().parallel().map(Photo::getId).collect(Collectors.toList());
    }

    public Photo saveToFileSystem(final Event event, final Account owner, final MultipartFile file, final String name)
                                                                        throws InvalidParameterException, IOException {
        check(owner != null, "The owner should not be null");
        check(owner.getId() != null, "The owner doesn't exist yet");
        check(event != null, "The event should not be null");
        check(event.getId() != null, "The event doesn't exist yet");
        check(file != null, "The file should not be null");
        check(!file.isEmpty(), "The file should not be empty");

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = this.generateFileName(fileExtension);
        File target = this.generatePathFile(event.getId().toString(), owner.getId().toString(), fileName);

        file.transferTo(target);

        Photo photo = new Photo();
        photo.setName(name == null ? file.getOriginalFilename() : name);
        photo.setUrl(target.getAbsolutePath());
        photo.setOwner(owner);
        photo.setEvent(event);

        this.photoDAO.create(photo);

        return photo;
    }

    public Photo saveToFileSystem(final Long eventId, final Long ownerId, final MultipartFile file, final String name)
                                                throws InvalidParameterException, EntityNotFoundException, IOException {
        Event event = this.eventService.getOrThrow(eventId);
        Account owner = this.accountService.getOrThrow(ownerId);
        return this.saveToFileSystem(event, owner, file, name);
    }

    private String generateFileName(final String fileExtension) {
        String fileName = new BigInteger(130, random).toString(32);
        if (StringUtils.isNotEmpty(fileExtension)) {
            fileName = String.join(".", fileName, fileExtension);
        }
        return fileName;
    }

    private File generatePathFile(final String event, final String owner, final String fileName) throws IOException {
        String path = String.join("/", this.directory, event, owner);
        File dir = new File(path);
        if(!dir.exists() && !dir.mkdirs()) {
            throw new IOException("can't create dir! " + path);
        }
        return new File(String.join("/", path, fileName));
    }

    /**
     * Pulls out a photo from the file system
     *
     * @param photo Photo entity
     * @return File object for REST
     * */
    public File getSingleFile(Photo photo) {
        return new File(directory + photo.getEvent().getId()
                + FILE_SEPERATOR + photo.getOwner().getId()
                + FILE_SEPERATOR + photo.getName());
    }

    /**
     * @param hash photo md5
     * @return Photo entity from database
     */
//    public Photo getSingleInfo(String hash) {
//        return photoDAO.getSingleInfoByHash(hash);
//    }



    /**
     * todo: update(photo)?!? fixme using commentDao! and photo => photoId
     * Add comment to photo
     * @param photo - photo entity
     * @param comment - comment entity
     */
    public void addComment(Photo photo, Comment comment) {
        if (photo.getComments() == null) {
            throw new AppException("Invalid photo entity");
        }

        if(comment.getText().isEmpty()) {
            throw new AppException("Comment is empty");
        }

        photo.getComments().add(comment);
        update(photo);
    }

}
