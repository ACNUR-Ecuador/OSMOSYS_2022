package com.sagatechs.generics.security.dao;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.security.model.RoleAssigment;

import javax.ejb.Stateless;

@Stateless
public class RoleAssigmentDao extends GenericDaoJpa<RoleAssigment, Long> {

	public RoleAssigmentDao() {
		super(RoleAssigment.class, Long.class);

	}


    public void saveOrUpdate(RoleAssigment userRoleAssigment) {
		if(userRoleAssigment.getId()==null){
			this.entityManager.persist(userRoleAssigment);
		}else{
			this.update(userRoleAssigment);
		}
    }
}
