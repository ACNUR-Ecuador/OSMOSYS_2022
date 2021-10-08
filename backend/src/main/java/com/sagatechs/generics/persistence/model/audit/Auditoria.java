package com.sagatechs.generics.persistence.model.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@SuppressWarnings("JpaDataSourceORMInspection")
@Embeddable
public class Auditoria {

	@Column(name = "fecha_ingreso", nullable = false)
	private LocalDateTime fechaIngreso;

	@Column(name = "usuario_ingreso", length = 50, nullable = false)
	private String usuarioCreacion;

	@Column(name = "fecha_actualizacion")
	private LocalDateTime fechaActualizacion;

	@Column(name = "usuario_actualizacion", length = 50)
	private String usuarioActualizacion;

	public LocalDateTime getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDateTime fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public String getUsuarioActualizacion() {
		return usuarioActualizacion;
	}

	public void setUsuarioActualizacion(String usuarioActualizacion) {
		this.usuarioActualizacion = usuarioActualizacion;
	}

}
