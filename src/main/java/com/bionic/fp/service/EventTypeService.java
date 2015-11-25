package com.bionic.fp.service;

import com.bionic.fp.dao.EventTypeDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Entry point to perform operations over event type entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class EventTypeService {

    @Inject
    private EventTypeDAO eventTypeDAO;

    public EventType getPrivate() {
        return this.eventTypeDAO.getPrivate();
    }

    public EventType getById(final Integer id) {
        return id == null ? null : this.eventTypeDAO.read(id);
    }
}
