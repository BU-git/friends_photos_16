package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * This is an implementation of {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventDaoImpl extends GenericDaoJpaImpl<Event, Long> implements EventDAO {

    private static final String VISIBLE = "visible";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    public EventDaoImpl() {}

    @Override
    @Deprecated
    public Event getWithAccounts(final Long eventId) {
        EntityGraph graph = this.em.getEntityGraph("Event.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(Event.class, eventId, hints);
    }

    @Override
    public List<Event> get(final String name, final String description) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);

        Root<Event> event = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(isNotDeleted(event));
        predicates.add(cb.isTrue(event.get(VISIBLE)));

        if(isNotEmpty(name)) {
            predicates.add(cb.like(event.get(NAME), "%"+name+"%"));
        }
        if(isNotEmpty(description)) {
            predicates.add(cb.like(event.get(DESCRIPTION), "%"+description+"%"));
        }

        return this.em.createQuery(query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))))
                .getResultList();
    }
}
