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
 * Created by boubdyk on 11.11.2015.
 */
@Repository
public class PhotoDaoImpl extends GenericDaoJpaImpl<Photo, Long> implements PhotoDAO {

    public PhotoDaoImpl() {}

	@Override
	public List<Photo> getPhotosByEventId(final Long eventId) {
        return this.em.createNamedQuery(Photo.FIND_BY_EVENT_ID, Photo.class)
                .setParameter("eventId", eventId)
                .getResultList();
	}

    @Override
	public List<Photo> getPhotosByOwnerId(final Long ownerId) {
        return this.em.createNamedQuery(Photo.FIND_BY_OWNER_ID, Photo.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
	}

}
