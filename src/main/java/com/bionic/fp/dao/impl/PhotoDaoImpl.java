package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.PhotoDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of {@link PhotoDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
@Transactional
public class PhotoDaoImpl extends GenericDaoJpaImpl<Photo, Long> implements PhotoDAO {

	private static final String OWNER = "owner";
	private static final String EVENT = "event";

	public PhotoDaoImpl() {}

	@Override
	public List<Photo> getPhotosByEvent(final Long eventId) {
		return this.em.createQuery(this.getQuery((Long) null, eventId)).getResultList();
	}

	@Override
	public List<Photo> getPhotosByOwner(final Long ownerId) {
		return this.em.createQuery(this.getQuery(ownerId, null)).getResultList();
	}

	@Override
	public List<Photo> getPhotosByAccountInEvent(final Long accountId, final Long eventId) {
		return this.em.createQuery(this.getQuery(accountId, eventId)).getResultList();
	}

	private CriteriaQuery<Photo> getQuery(final Long ownerId, final Long eventId) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Photo> query = cb.createQuery(Photo.class);

		Root<Photo> photo = query.from(Photo.class);
		Join<Photo, Event> event = photo.join(EVENT);
		Join<Photo, Account> owner = photo.join(OWNER);

		Predicate predicate = cb.conjunction();
		predicate = cb.and(predicate, isNotDeleted(photo));

		if(ownerId != null) {
			predicate = cb.and(predicate, equalId(owner, ownerId));
			predicate = cb.and(predicate, isNotDeleted(owner));
		}
		if(eventId != null) {
			predicate = cb.and(predicate, equalId(event, eventId));
			predicate = cb.and(predicate, isNotDeleted(event));
		}

		return query.where(predicate);
	}

}