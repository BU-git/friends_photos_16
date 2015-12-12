package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static java.util.Optional.ofNullable;


/**
 * Created by Yevhenii on 11/16/2015.
 */
@Repository
public class RoleDaoImpl implements RoleDAO {

    public static final String SELECT_ALL_ROLES = "SELECT r FROM Role r";

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;


    @Override
    public Integer create(Role role) {
        em.persist(role);
        return role.getId();
    }

    @Override
    public Role read(Integer id) {
        return em.find(Role.class, id);
    }

    @Override
    public Role update(Role role) {
        em.merge(role);
        return role;
    }

    @Override
    public void delete(Integer roleId) throws RoleNotFoundException {
        this.em.remove(this.getOrThrow(roleId));
    }

    @Override
    public Role getOwner() throws RoleNotFoundException  {
        return this.getOrThrow(1);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> allRolesQuery = em.createQuery(SELECT_ALL_ROLES, Role.class);
        return allRolesQuery.getResultList();
    }

    private Role getOrThrow(final Integer roleId) throws RoleNotFoundException {
        return ofNullable(this.read(roleId)).orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}
