package com.bionic.fp.dao.impl;

import com.bionic.fp.exception.logic.critical.NonUniqueResultException;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by Sergey on 16.01.2016.
 */
public class AbstractQueryHelper {

	protected <T> T getSingleResult(final TypedQuery<T> query) throws NonUniqueResultException {
		try {
			return query.getSingleResult();
		} catch (NoResultException ignored) {
			return null;
		} catch (javax.persistence.NonUniqueResultException e) {
			throw new NonUniqueResultException(e.getMessage(), e);
		}
	}

}
