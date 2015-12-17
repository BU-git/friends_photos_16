package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;

import java.util.List;

/**
 * Represents data access object of the photo
 *
 * @author Sergiy Gabriel
 */
public interface PhotoDAO extends GenericDAO<Photo, Long> {
	/**
	 *
	 * @param hash unique identifier
	 * @return all info about photo
	 */
//    Photo getSingleInfoByHash(String hash);

	/**
	 *
	 * @param eventId the event id  we want to get photos for.
	 * @return list of all photo objects for current event.
	 */
	List<Photo> getPhotosByEventId(Long eventId);

	List<Photo> getPhotosByOwnerId(Long ownerId);

}
