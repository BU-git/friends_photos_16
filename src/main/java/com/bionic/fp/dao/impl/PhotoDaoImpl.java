package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.PhotoDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is an implementation of {@link PhotoDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class PhotoDaoImpl extends GenericDaoJpaImpl<Photo, Long> implements PhotoDAO {

	public PhotoDaoImpl() {}

	@Override
	public List<Photo> getPhotosByEvent(final Long eventId) {
		return this.em.createNamedQuery(Photo.FIND_BY_EVENT_ID, Photo.class)
				.setParameter("eventId", eventId)
				.getResultList();
	}

	@Override
	public List<Photo> getPhotosByOwner(final Long ownerId) {
		return this.em.createNamedQuery(Photo.FIND_BY_OWNER_ID, Photo.class)
				.setParameter("ownerId", ownerId)
				.getResultList();
	}

}