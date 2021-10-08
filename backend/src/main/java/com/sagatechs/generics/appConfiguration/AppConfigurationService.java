package com.sagatechs.generics.appConfiguration;

import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

@Singleton
@Startup
@DependsOn("AppDataConfigLlenadoAutomatico")
public class AppConfigurationService {
	private static final Logger LOGGER = Logger.getLogger(AppConfigurationService.class);

	@Inject
	AppConfigurationDao appConfigurationDao;

	private EnumMap<AppConfigurationKey, AppConfiguration> appConfigurationCache = new EnumMap<>(
			AppConfigurationKey.class);

	@PostConstruct
	public void init() {
		// Arracar el sistema
		LOGGER.info("Cargando configuración del sistema");
		llenarAppConfigurationCache();
		LOGGER.info("Terminado Cargando configuración del sistema");

	}

	private void llenarAppConfigurationCache() {
		appConfigurationCache.clear();

		List<AppConfiguration> appConfs = appConfigurationDao.findAll();
		if (CollectionUtils.isNotEmpty(appConfs)) {
			for (AppConfiguration appConfiguration : appConfs) {
				appConfigurationCache.put(appConfiguration.getClave(), appConfiguration);
			}
		}
	}

	@SuppressWarnings("WeakerAccess")
	public void updateAppConfiguration(Long id, String value) {
		AppConfiguration appConfiguration = appConfigurationDao.find(id);
		appConfiguration.setValor(value);
		this.appConfigurationDao.update(appConfiguration);
		this.llenarAppConfigurationCache();

	}

	@SuppressWarnings("WeakerAccess")
	public List<AppConfiguration> findAll() {
		Collection<AppConfiguration> cache = this.appConfigurationCache.values();
		return new ArrayList<>(cache);

	}

	@SuppressWarnings("unused")
	public String findValorByClave(AppConfigurationKey clave) {
		AppConfiguration appconfig = appConfigurationCache.get(clave);
		if (appconfig == null) {
			return null;
		} else {
			return appconfig.getValor();
		}
	}

	@SuppressWarnings("unused")
	private String crearMensajeProblemaValorConfiguracion(AppConfigurationKey clave, String valor) {
		StringBuilder sb = new StringBuilder();
		sb.append("El valor de configuración ");

		sb.append(clave);
		sb.append(" de la aplicación no es correcto. (").append(valor).append(")");
		LOGGER.error(sb.toString());
		return sb.toString();

	}
}
