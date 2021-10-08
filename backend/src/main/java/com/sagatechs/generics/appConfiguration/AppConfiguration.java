package com.sagatechs.generics.appConfiguration;


import com.sagatechs.generics.persistence.model.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "app_config", schema = "app_config")
@NamedQuery(name = AppConfiguration.QUERY_FIND_BY_KEY, query = "SELECT o FROM AppConfiguration o WHERE o.clave=:"
		+ AppConfiguration.QUERY_PARAM_KEY)
public class AppConfiguration extends BaseEntity<Long> {

	public static final String QUERY_FIND_BY_KEY = "AppConfiguration.FindByClave";
	public static final String QUERY_PARAM_KEY = "clave";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull
	@NotEmpty
	@Column(name = "nombre", unique = true)
	@Size(min = 4, max = 100)
	private String nombre;

	@NotNull
	@NotEmpty
	@Column(name = "descripcion", columnDefinition = "TEXT")
	private String descripcion;

	@NotNull
	@Column(name = "clave", unique = true)
	@Enumerated(EnumType.STRING)
	private AppConfigurationKey clave;

	@Size(max = 255)
	@Column(name = "valor")
	private String valor;

	@SuppressWarnings("WeakerAccess")
	public AppConfiguration(String nombre, String descripcion, AppConfigurationKey clave, String valor) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.clave = clave;
		this.valor = valor;
	}

	public AppConfiguration() {
		super();
	}

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public AppConfigurationKey getClave() {
		return clave;
	}

	public void setClave(AppConfigurationKey clave) {
		this.clave = clave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "AppConfiguration [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", clave=" + clave
				+ ", valor=" + valor + "]";
	}

}
