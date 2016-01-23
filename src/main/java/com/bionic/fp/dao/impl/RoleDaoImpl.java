package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Optional.ofNullable;


/**
 * Created by Yevhenii on 11/16/2015.
 */
@Repository
public class RoleDaoImpl extends GenericDaoJpaImpl<Role, Long> implements RoleDAO {

    public static final String SELECT_ALL_ROLES = "SELECT r FROM Role r";

    public RoleDaoImpl() {}

    @Override
    public Role getOwner() throws RoleNotFoundException  {
        return this.getOrThrow(1L);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> allRolesQuery = em.createQuery(SELECT_ALL_ROLES, Role.class);
        return allRolesQuery.getResultList();
    }

//    private Role getOrThrow(final Long roleId) throws RoleNotFoundException {
//        return ofNullable(this.read(roleId)).orElseThrow(() -> new RoleNotFoundException(roleId));
//    }
}
