package com.bionic.fp.dao;

import com.bionic.fp.dao.interfaces.RoleDAO;
import com.bionic.fp.entity.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * Created by Yevhenii on 11/16/2015.
 */
@Repository
public class RoleDAOImpl implements RoleDAO {

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
}
