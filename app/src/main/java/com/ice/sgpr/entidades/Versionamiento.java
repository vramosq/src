package com.ice.sgpr.entidades;

/**
 * Clase para versionamiento.
 * @author eperaza
 * Fecha de creacion: 26/09/2013.
 */
public class Versionamiento{
	private String _sCodigoUsuario;
	private int _nProceso;
	private int _nEstado;
	
	public Versionamiento(String pCodigoUsuario, int pProceso, int pEstado){
		this._sCodigoUsuario = pCodigoUsuario;
		this._nProceso = pProceso;
		this._nEstado = pEstado;
	}
	
	/** GET **/
	public String getCodigoUsuario(){
		return this._sCodigoUsuario;
	}
	
	public int getProceso(){
		return this._nProceso;
	}
	
	public int getEstado(){
		return this._nEstado;
	}
}
