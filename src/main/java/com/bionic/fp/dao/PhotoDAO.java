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
	 * Returns a list of photos by the event
	 *
	 * @param eventId the event ID
	 * @return a list if photos
     */
	List<Photo> getPhotosByEvent(Long eventId);

	/**
	 * Returns a list of photos by the account
	 *
	 * @param ownerId the account ID
	 * @return a list if photos
	 */
	List<Photo> getPhotosByOwner(Long ownerId);

	/**
	 * Returns a list of photos by the account in the event
	 *
	 * @param accountId the account ID
	 * @param eventId the event ID
     * @return a list of photos
     */
	List<Photo> getPhotosByAccountInEvent(Long accountId, Long eventId);
}
