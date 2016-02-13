package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventTypeDAO;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * This is an implementation of {@link EventTypeDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
@Transactional
public class EventTypeDaoImpl extends GenericDaoJpaImpl<EventType, Long> implements EventTypeDAO {

    public EventTypeDaoImpl() {}

    @Override
    @Transactional(readOnly = true)
    public EventType getPrivate() throws EventTypeNotFoundException {
        return this.getOrThrow(1L);
    }
}