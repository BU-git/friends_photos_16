package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.util.GeoUtils.DistanceUnitPerDegree;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * This is an implementation of {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
@Transactional
public class EventDaoImpl extends GenericDaoJpaImpl<Event, Long> implements EventDAO {

    private static final String VISIBLE = "visible";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String GEO = "geoServicesEnabled";

    public EventDaoImpl() {}

    @Override
    @Transactional(readOnly = true)
    public List<Event> get(final Boolean visible, final String name, final String description) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);

        Root<Event> event = query.from(Event.class);

        Predicate predicate = cb.conjunction();
        predicate = cb.and(predicate, isNotDeleted(event));
        if(visible != null) {
            predicate = cb.and(predicate, cb.equal(event.get(VISIBLE), visible));
        }
        if(isNotEmpty(name)) {
            predicate = cb.and(predicate, cb.like(cb.lower(event.get(NAME)), "%"+name.toLowerCase()+"%"));
        }
        if(isNotEmpty(description)) {
            predicate = cb.and(predicate, cb.like(cb.lower(event.get(DESCRIPTION)), "%"+description.toLowerCase()+"%"));
        }

        return this.em.createQuery(query.where(predicate)).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> get(final Boolean visible, final Coordinate coordinate,
                           final float radius, final DistanceUnitPerDegree distanceUnit) {
        return this.em.createNamedQuery(Event.FIND_BY_RADIUS, Event.class)
                .setParameter("latitude", coordinate.getLatitude())
                .setParameter("longitude", coordinate.getLongitude())
                .setParameter("radius", radius)
                .setParameter("visible", visible)
                .setParameter("distance_unit", distanceUnit.getDistance())
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> get(final Boolean visible, final Coordinate sw, final Coordinate ne) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);

        Predicate predicate = cb.conjunction();
        predicate = cb.and(predicate, isNotDeleted(event));
        if(visible != null) {
            predicate = cb.and(predicate, cb.equal(event.get(VISIBLE), visible));
        }
        predicate = cb.and(predicate, cb.isTrue(event.get(GEO)));
        predicate = cb.and(predicate, cb.between(event.get(LOCATION).get(LATITUDE), sw.getLatitude(), ne.getLatitude()));
        predicate = cb.and(predicate, cb.between(event.get(LOCATION).get(LONGITUDE), sw.getLongitude(), ne.getLongitude()));

        return this.em.createQuery(query.where(predicate)).getResultList();
    }
}
