package com.ice.sgpr.entidades;

/**
 * Entidad con las propiedades de una bitacora.
 * @author eperaza
 * Fecha de creacion: 08/08/2013.
 */
public class Bitacora {
	private int _nRutaId;
	private int _nNegocioId;
	private String _sNombreNegocio;
	private String _sFechaInicio;
	private String _sFechaFin;
	private String _sLatitud;
	private String _sLongitud;
	private int _nActualizada;
	private int _nBitacoraId;

	
	public Bitacora(int pRutaId, int pNegocioId, String pFechaInicio, String pFechaFin, String pNombreNegocio, String pLatitud, 
			String pLongitud, int pActualizada, int pBitacoraId){
		this._nRutaId = pRutaId;
		this._nNegocioId = pNegocioId;
		this._sFechaInicio = pFechaInicio;
		this._sFechaFin = pFechaFin;
		this._sNombreNegocio = pNombreNegocio;
		this._sLatitud = pLatitud;
		this._sLongitud = pLongitud;
		this._nActualizada = pActualizada;
		this._nBitacoraId = pBitacoraId;
	}
	
	/* GET */
	public int getRutaId(){
		return _nRutaId;
	}
	public int getNegocioId(){
		return _nNegocioId;
	}
	public String getFechaInicio(){
		return _sFechaInicio;
	}
	public String getFechaFin(){
		return _sFechaFin;
	}
	public String getNombreNegocio(){
		return _sNombreNegocio;
	}
	public String getLatitud(){
		return _sLatitud;
	}
	public String getLongitud(){
		return _sLongitud;
	}
	public int getActualizada(){
		return _nActualizada;
	}
	public int getId(){
		return _nBitacoraId;
	}
	
	/* SET */
	public void setRutaId(int pRutaId){
		_nRutaId = pRutaId;
	}
	public void setNegocioId(int pRutaId){
		_nRutaId = pRutaId;
	}
	public void setFechaInicio(String pFechaInicio){
		_sFechaInicio = pFechaInicio;
	}
	public void setFechaFin(String pFechaFin){
		_sFechaFin = pFechaFin;
	}
	public void setNombreNegocio(String pFechaFin){
		_sFechaFin = pFechaFin;
	}
}