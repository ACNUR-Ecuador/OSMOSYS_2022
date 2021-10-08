package com.sagatechs.generics.appConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AppConfigView implements Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AppConfigView.class);

	@Inject
	AppConfigurationService appConfigurationService;

	// ******************************************FIELDS*****************************************************************//

	private List<AppConfiguration> appConfigurations;

	// ******************************************METHODS*****************************************************************//

	@PostConstruct
	public void init() {
		this.appConfigurations = this.appConfigurationService.findAll();

	}

	public void onRowEdit(RowEditEvent event) {

		AppConfiguration conf = (AppConfiguration) event.getObject();
		if (StringUtils.isBlank(conf.getValor())) {
			Messages.addError(null, "El valor no puede estar vacío");
		} else {
			try {
				this.appConfigurationService.updateAppConfiguration(conf.getId(), conf.getValor());
				Messages.addInfo(null, "La configuración se guardó correctamente");
			} catch (Exception e) {
				Messages.addError(null, "Error al guardar el valor.");
			}
		}

	}

	@SuppressWarnings("unused")
	public void onRowCancel(RowEditEvent event) {
		Messages.addInfo(null, "La configuración no se guardó");
	}

	// ******************************************GETTERS/SETTERS*************************************************//

	public List<AppConfiguration> getAppConfigurations() {
		return appConfigurations;
	}

	public void setAppConfigurations(List<AppConfiguration> appConfigurations) {
		this.appConfigurations = appConfigurations;
	}

}
