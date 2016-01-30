package com.bionic.fp.dao;

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
	 * @param eventId the event id  we want to get photos for.
	 * @return list of all photo objects for current event.
	 */
	List<Photo> getPhotosByEvent(Long eventId);

	List<Photo> getPhotosByOwner(Long ownerId);

	List<Photo> getPhotosByAccountInEvent(Long accountId, Long eventId);
}
