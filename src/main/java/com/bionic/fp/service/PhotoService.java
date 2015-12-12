package com.bionic.fp.service;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.dao.PhotoDAO;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by franky_str on 22.11.15.
 */
@Named
@Transactional
public class PhotoService {

    private static final String DIRECTORY = System.getProperty("file.separator") + "photo_bank" + System.clearProperty("file.separator");

    @Inject
    private PhotoDAO photoDAO;
	@Inject
	private EventDAO eventDAO;

    public PhotoService(){}

	public Photo create(Photo photo) {
		Long newPhotoId = photoDAO.create(photo);
		return photoDAO.read(newPhotoId);
	}

	public Photo update(Photo photo) {
		Long id = photoDAO.create(photo);
		return photoDAO.read(id);
	}
















//    public Long saveSinglePhoto(Photo photo, InputStream uploadedInputStream) {
//        return photoDAO.create(saveToFileSystemStream(photo, uploadedInputStream));
//        //photoDao.create(saveToFileSystem(photo, uploadedInputStream));
//    }

    /**
     * Pulls out a photo from the file system
     *
     * @param photo Photo entity
     * @return File object for REST
     * */
//    public File getSingleFile(Photo photo) {
//        return new File(DIRECTORY + photo.getEvent().getId() + System.getProperty("file.separator") + photo.getName());
//    }

	// TODO refactor this
//    public List<Photo> getPhotos(Long eventId){
//         return photoDAO.getPhotosByEvent(eventId);
//    }
    /**
     * @param hash photo md5
     * @return Photo entity from database
     */
//    public Photo getSingleInfo(String hash) {
//        return photoDAO.getSingleInfoByHash(hash);
//    }

    /**
     * @param id photo ID
     * @return Photo entity from database
     */
    public Photo getById(Long id) {
        return photoDAO.read(id);
    }


    /**
     * The method allows to find out which files belong to the event
     *
     * @return List that contains the entities with all the photos in this event
     */
	// TODO refactor this method!
//    public List<Photo> getEventInfo(Event event) {
//        return event == null ? Collections.emptyList() : photoDAO.getPhotosByEvent(event);
//    }

    /* Private methods */

    private Photo saveToFileSystemStream(Photo photo, InputStream uploadedInputStream) {
        File dir = new File(DIRECTORY + photo.getEvent().getId() + System.getProperty("file.separator"));
        if (!dir.exists()) { dir.mkdirs(); }
        File file = new File(dir, photo.getName());

        if (file.exists()) {
            int counter = 1;
            String newName = photo.getName();
            do {
                newName = changeName(newName, counter++);
                file = new File(DIRECTORY + photo.getEvent().getId() + System.getProperty("file.separator") + newName);
            } while (file.exists());
            photo.setName(newName);
        }

        try (OutputStream out = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];


            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error I/O");
            e.printStackTrace();
        }

        return photo;
    }


    public List<Photo> getPhotosByEvent(Event event){
		if (event == null) {
			throw new NullPointerException("Event param is null");
		}
        return photoDAO.getPhotosByEvent(event);
	}

	public List<Photo> getPhotosByEventId(Long eventId){
		Event event = eventDAO.read(eventId);
		if (event == null) {
			throw new EntityNotFoundException("event", eventId);
		}
		return photoDAO.getPhotosByEvent(event);
	}

    /**
     * Add counter to the beginning of the name
     *
     * @param name
     * @param counter
     * @return new name
     */
    private String changeName(String name, int counter) {
        String newPhotoName;
        if(name.startsWith("(")) {
            String [] separatedName = name.split("\\)", 2);
            newPhotoName = "(" + (counter++) + ")";
            for (int i = 1; i < separatedName.length ; i++) {
                newPhotoName += separatedName[i];
            }
        } else {
            newPhotoName = "(" + (counter++) + ") " + name;
        }
        return newPhotoName;
    }
}
