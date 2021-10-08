package com.sagatechs.generics.appConfiguration;

import com.sagatechs.generics.persistence.GenericDaoJpa;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

@LocalBean
@Stateless
public class AppConfigurationDao extends GenericDaoJpa<AppConfiguration, Long> {

	public AppConfigurationDao() {
		super(AppConfiguration.class, Long.class);
		
	}
	
	@SuppressWarnings("WeakerAccess")
	public AppConfiguration findByKey(AppConfigurationKey clave){
		
		try {
			return (AppConfiguration) entityManager.createNamedQuery(AppConfiguration.QUERY_FIND_BY_KEY).setParameter(AppConfiguration.QUERY_PARAM_KEY,clave).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	
	
	

}