package com.ice.sgpr.entidades;

import java.util.Date;

/**
 * Entidad con las propiedades de un seguimiento.
 * @author eperaza
 * Fecha de creacion: 08/09/2015.
 */
public class Seguimiento {
	private int idUsuario;
	private String fecha;
	private String _sLatitud;
	private String _sLongitud;
	
	
	public Seguimiento(int idUsuario, String fecha, String _sLatitud,
			String _sLongitud) {
		super();
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this._sLatitud = _sLatitud;
		this._sLongitud = _sLongitud;
	}
	/**
	 * Getters & setters
	 * @return
	 */
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String get_sLatitud() {
		return _sLatitud;
	}
	public void set_sLatitud(String _sLatitud) {
		this._sLatitud = _sLatitud;
	}
	public String get_sLongitud() {
		return _sLongitud;
	}
	public void set_sLongitud(String _sLongitud) {
		this._sLongitud = _sLongitud;
	}
}