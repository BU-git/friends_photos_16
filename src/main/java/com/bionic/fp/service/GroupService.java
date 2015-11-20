package com.bionic.fp.service;

import com.bionic.fp.dao.GroupDAO;
import com.bionic.fp.domain.Group;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;

/**
 * Entry point to perform operations over group entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class GroupService {

    @Inject
    private GroupDAO groupDAO;

    public GroupService() {}

    public Long addGroup(final Group group) {
        if(group == null) {
            return null;
        }
        group.setDate(LocalDateTime.now());
        return this.groupDAO.create(group);
    }

    public void removeById(final Long id) {
        if(id != null) {
            this.groupDAO.delete(id);
        }
    }

    public Group getById(final Long id) {
        return id == null ? null : this.groupDAO.read(id);
    }

    public Group update(final Group group) {
        return group == null ? null : this.groupDAO.update(group);
    }

    public Group getByIdWithOwner(final Long id) {
        return id == null ? null : this.groupDAO.readWithOwner(id);
    }
}
