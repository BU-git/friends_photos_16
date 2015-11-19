package com.bionic.fp.service;

import com.bionic.fp.dao.GroupDAO;
import com.bionic.fp.domain.Group;
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
public class GroupService {

    @Inject
    private GroupDAO groupDAO;

    public GroupService() {}

    public Long addGroup(final Group group) {
        if(group == null) {
            return null;
        }
        return this.groupDAO.create(group);
    }

//    public void clear() {
//        this.groupDAO.fi
//    }
}
