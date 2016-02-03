package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
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

    public EventDaoImpl() {}

    @Override
    public List<Event> get(final String name, final String description) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);

        Root<Event> event = query.from(Event.class);

        Predicate predicate = cb.conjunction();
        predicate = cb.and(predicate, isNotDeleted(event));
        predicate = cb.and(predicate, cb.isTrue(event.get(VISIBLE)));

        if(isNotEmpty(name)) {
            predicate = cb.and(predicate, cb.like(event.get(NAME), "%"+name+"%"));
        }
        if(isNotEmpty(description)) {
            predicate = cb.and(predicate, cb.like(event.get(DESCRIPTION), "%"+description+"%"));
        }

        return this.em.createQuery(query.where(predicate)).getResultList();
    }
}
