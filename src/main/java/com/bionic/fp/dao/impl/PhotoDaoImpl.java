package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.PhotoDAO;
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
    private EntityManager entityManager;

    public PhotoDaoImpl() {}

    @Override
    public Long create(Photo newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Photo read(Long id) {
        return entityManager.find(Photo.class, id);
    }

    @Override
    public Photo update(Photo photo) {
        entityManager.merge(photo);
        return photo;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }

	@Override
	public List<Photo> getPhotosByEvent(Event event) {
		Query query = entityManager.createQuery("from Photo where event = :event");
		query.setParameter("event", event);
		return query.getResultList();
	}

//    @Override
//    public Photo getSingleInfoByHash(String hash){
//        Query query = entityManager.createQuery("from Photo where hash = :md5");
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
