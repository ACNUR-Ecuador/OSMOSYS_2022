package com.sagatechs.generics.security.dao;


import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleType;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
public class RoleDao extends GenericDaoJpa<Role, Long> {

	public RoleDao() {
		super(Role.class, Long.class);

	}

	public Role findByRoleType(RoleType roleType) {

		String jpql = "SELECT DISTINCT o FROM Role o WHERE o.roleType = :roleType";
		Query q = getEntityManager().createQuery(jpql, Role.class);
		q.setParameter("roleType", roleType);

		try {
			return (Role) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
