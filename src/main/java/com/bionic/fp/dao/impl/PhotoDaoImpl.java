package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.PhotoDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */
@Repository
public class PhotoDaoImpl implements PhotoDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;

    public PhotoDaoImpl() {}

    @Override
    public Long create(Photo newInstance) {
        em.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Photo read(Long id) {
        return em.find(Photo.class, id);
    }

    @Override
    public Photo update(Photo photo) {
        em.merge(photo);
        return photo;
    }

    @Override
    public void delete(Long persistentObjectID) {
        em.remove(read(persistentObjectID));
    }

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
//    @Override
//    public Photo getSingleInfoByHash(String hash){
//        Query query = em.createQuery("from Photo where hash = :md5");
//        query.setParameter("md5", hash);
//        List<Photo> list  = query.getResultList();
//
//        Photo foundEntity = null;
//        if (!list.isEmpty()) {
//            foundEntity = list.get(0);
//        }
//        return foundEntity;
//    }
}
