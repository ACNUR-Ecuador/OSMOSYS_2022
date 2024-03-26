package com.sagatechs.generics.appConfiguration;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Startup
@DependsOn("AppDataConfigLlenadoAutomatico")
public class AppConfigurationService {
    private static final Logger LOGGER = Logger.getLogger(AppConfigurationService.class);

    @Inject
    AppConfigurationDao appConfigurationDao;

    private final EnumMap<AppConfigurationKey, AppConfiguration> appConfigurationCache = new EnumMap<>(
            AppConfigurationKey.class);

    @PostConstruct
    public void init() {
        // Arracar el sistema
        LOGGER.info("Cargando configuración del sistema");
        llenarAppConfigurationCache();
        LOGGER.info("Terminado Cargando configuración del sistema");

    }

    public void llenarAppConfigurationCache() {
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
    public String crearMensajeProblemaValorConfiguracion(AppConfigurationKey clave, String valor) {

        String sb = "El valor de configuración " +
                clave +
                " de la aplicación no es correcto. (" + valor + ")";
        return sb;

    }


    public Integer getReportLimitDay() {
        String valusS = this.findValorByClave(AppConfigurationKey.REPORT_LIMIT);

        if (StringUtils.isBlank(valusS)) {
            return null;
        } else {
            return Integer.parseInt(valusS);
        }
    }
    public String getAppUrl() {
        return this.findValorByClave(AppConfigurationKey.APP_URL);


    }

    public List<Integer> getReminderDays() {
        String valusS = this.findValorByClave(AppConfigurationKey.REMINDER_DAYS);

        if (StringUtils.isBlank(valusS)) {
            return null;
        } else {
            try {
                return Arrays.stream(valusS.split(",")).map(String::trim).map(Integer::parseInt).sorted().collect(Collectors.toList());
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getRootCause(e));
                return new ArrayList<>();
            }
        }
    }

    private List<Integer> getLisOfNumberFromString(String stringList) {
        List<Integer> result = new ArrayList<>();
        if (StringUtils.isBlank(stringList)) {
            return result;
        }
        String[] valuesString = StringUtils.split(",");
        if (valuesString == null || valuesString.length < 1) {
            return result;
        }
        return Arrays.stream(valuesString).map(Integer::parseInt).collect(Collectors.toList());
    }
}
