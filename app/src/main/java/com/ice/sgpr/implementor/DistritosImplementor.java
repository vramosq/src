package com.ice.sgpr.implementor;

import java.util.List;

import com.ice.sgpr.dataaccess.DistritosDataAccess;
import com.ice.sgpr.entidades.Parametro;

/**
 * Implementor para la consulta de datos relacionados con Cantones.
 * @author eperaza
 * fecha de creacion: 09/07/2014.
 */
public class DistritosImplementor {
private DistritosDataAccess _dataAccess;
private static DistritosImplementor _instancia;
	
	private DistritosImplementor()
	{
		_dataAccess = new DistritosDataAccess();
	}
	
	public static DistritosImplementor getInstance(){
		if(_instancia == null)
			_instancia = new DistritosImplementor();
		return _instancia;
	}
	
	/**
	 * Se retorna la lista de distritos guardados en la BD, de acuerdo al canton indicado.
	 * @return Lista de distritos
	 */
	public List<Parametro> obtenerListaDistritos(int pCodigoCanton){
		_dataAccess.openForReading();
		List<Parametro> lCantones = _dataAccess.obtenerDistritos(pCodigoCanton);
		_dataAccess.close();
		return lCantones;
	}
	
	/**
	 * Se obtiene en codigo de canton de un distrito.
	 * @param pCodigoDistrito
	 * @return
	 */
	public int obtenerCodigoCanton(int pCodigoDistrito){
		_dataAccess.openForReading();
		int nCodigoCanton = _dataAccess.obtenerCodigoCanton(pCodigoDistrito);
		_dataAccess.close();
		return nCodigoCanton;
	}
}