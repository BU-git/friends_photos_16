package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * Created by Yevhenii on 11/16/2015.
 */
@Repository
public class RoleDaoImpl implements RoleDAO {

    public static final String SELECT_ALL_ROLES = "SELECT r FROM Role r";

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;


    @Override
    public Integer create(Role role) {
        entityManager.persist(role);
        return role.getId();
    }

    @Override
    public Role read(Integer id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role update(Role role) {
        entityManager.merge(role);
        return role;
    }

    @Override
    public void delete(Integer id) {
        entityManager.remove(read(id));
    }

    @Override
    public Role getOwner() {
        // todo: delete this block
//        Role role = new Role();
//        role.setRole("OWNER");
//        this.entityManager.persist(role);
//        return this.entityManager.find(Role.class, role.getId());

        // todo: use it
        return this.entityManager.find(Role.class, 1);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> allRolesQuery = entityManager.createQuery(SELECT_ALL_ROLES, Role.class);
        return allRolesQuery.getResultList();
    }
}
