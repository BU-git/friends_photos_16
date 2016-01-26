package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * This is an implementation of {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventDaoImpl extends GenericDaoJpaImpl<Event, Long> implements EventDAO {

    public EventDaoImpl() {}

    @Override
    public Event getWithAccounts(final Long eventId) {
        EntityGraph graph = this.em.getEntityGraph("Event.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(Event.class, eventId, hints);
    }

    @Override
    public List<Event> get(final String name, final String description) {
        if(isNotEmpty(name) && isNotEmpty(description)) {
            return this.em.createNamedQuery(Event.FIND_BY_NAME_AND_DESCRIPTION, Event.class)
                    .setParameter("name", "%"+name+"%")
                    .setParameter("description", "%"+description+"%")
                    .getResultList();
        }
        if(isNotEmpty(name)) {
            return this.em.createNamedQuery(Event.FIND_BY_NAME, Event.class)
                    .setParameter("name", "%"+name+"%")
                    .getResultList();
        }
        if(isNotEmpty(description)) {
            return this.em.createNamedQuery(Event.FIND_BY_DESCRIPTION, Event.class)
                    .setParameter("description", "%"+description+"%")
                    .getResultList();
        }
        return this.em.createNamedQuery(Event.FIND_ALL, Event.class).getResultList();
    }
}
