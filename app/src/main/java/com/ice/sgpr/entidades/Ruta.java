package com.ice.sgpr.entidades;

/**
 * Entidad con las propiedades de una ruta.
 * @author eperaza
 * Fecha de creacion: 31/07/2013.
 */
public class Ruta {
	private int _nRutaId;
	private String _sRutaNombre;
	private String _sRutaDescripcion;
	private String _sRutaFrecuencia;
	private String _sRutaFecha;
	private int _nRutaPendiente;
	private String _sLatitud;
	private String _sLongitud;
	private int _nSeleccionada;
	
	public Ruta(int pRutaId, String pNombre, String pDescripcion, String pFrecuencia, String pFecha, int pPendiente, String pLatitud, 
			String pLongitud, int pSeleccionada){
		this._nRutaId = pRutaId;
		this._sRutaNombre = pNombre;
		this._sRutaDescripcion = pDescripcion;
		this._sRutaFrecuencia = pFrecuencia;
		this._sRutaFecha = pFecha;
		this._nRutaPendiente = pPendiente;
		this._sLatitud = pLatitud;
		this._sLongitud = pLongitud;
		this._nSeleccionada = pSeleccionada;
	}
	
	/* GET */
	public int getId(){
		return _nRutaId;
	}
	
	public String getNombre(){
		return _sRutaNombre;
	}
	
	public String getDescripcion(){
		return _sRutaDescripcion;
	}
	
	public String getFrecuencia(){
		return _sRutaFrecuencia;
	}
	
	public String getFecha(){
		return _sRutaFecha;
	}
	
	public int getPendiente(){
		return _nRutaPendiente;
	}
	
	public String getLatitud(){
		return _sLatitud;
	}
	
	public String getLongitud(){
		return _sLongitud;
	}
	
	public int getSeleccionada(){
		return _nSeleccionada;
	}
	/* SET */
	public void setRutaId(int pId){
		this._nRutaId = pId;
	}
	
	public void setRutaNombre(String pNombre){
		this._sRutaNombre = pNombre;
	}
	
	public void setRutaDescripcion(String pDescripcion){
		this._sRutaDescripcion = pDescripcion;
	}
	
	public void setRutaFrecuencia(String pFrecuencia){
		this._sRutaFrecuencia = pFrecuencia;
	}
	
	public void setRutaFecha(String pFecha){
		this._sRutaFecha = pFecha;
	}
	
	public void setPendiente(int pPendiente){
		this._nRutaPendiente = pPendiente;
	}
}