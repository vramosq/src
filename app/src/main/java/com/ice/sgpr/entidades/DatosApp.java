package com.ice.sgpr.entidades;

/**
 * Entidad con los datos de la aplicacion.
 * @author eperaza
 * Fecha de creacion: 05/08/2013.
 */
public class DatosApp {
	private int _nAppId;
	private String _sFechaUltimaSincronizacion;
	
	public DatosApp(int pAppId, String pUltimaSincronizacion){
		this._nAppId = pAppId;
		this._sFechaUltimaSincronizacion = pUltimaSincronizacion;
	}
	
	/* GET*/
	public int getAppId(){
		return _nAppId;
	}
	
	public String getUltimaSincronizacion(){
		return _sFechaUltimaSincronizacion;
	}
}