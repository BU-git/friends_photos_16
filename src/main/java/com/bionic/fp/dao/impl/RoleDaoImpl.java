package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Yevhenii on 11/16/2015.
 */
@Repository
@Transactional
public class RoleDaoImpl extends GenericDaoJpaImpl<Role, Long> implements RoleDAO {

    public RoleDaoImpl() {}

    @Override
    public List<Role> getAllRoles() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Role> query = cb.createQuery(Role.class);
        Root<Role> role = query.from(Role.class);
        return this.em.createQuery(query.where(isNotDeleted(role))).getResultList();
    }

}