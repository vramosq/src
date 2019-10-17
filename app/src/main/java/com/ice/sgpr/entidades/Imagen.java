package com.ice.sgpr.entidades;

/**
 * Clase para una imagen
 * @author eperaza
 * Fecha de creacion: 25/09/2013.
 */
public class Imagen {
	private int _nCodigoNegocio;
	private byte[] _bImagen;
	private int _nImagenId;
	
	public Imagen(int pCodigoNegocio, byte[] pImagen, int pImagenId){
		this._nCodigoNegocio = pCodigoNegocio;
		this._bImagen = pImagen;
		this._nImagenId = pImagenId;
	}
	
	/** GET **/
	public int getCodigoNegocio(){
		return this._nCodigoNegocio;
	}
	public byte[] getImagen(){
		return this._bImagen;
	}
	public int getImagenId(){
		return this._nImagenId;
	}
}
