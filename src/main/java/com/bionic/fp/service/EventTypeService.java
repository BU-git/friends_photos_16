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
 * todo: comment
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

}
