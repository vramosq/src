package com.ice.sgpr.implementor;

import java.util.List;

import com.ice.sgpr.dataaccess.CantonesDataAccess;
import com.ice.sgpr.entidades.Parametro;

/**
 * Implementor para la consulta de datos relacionados con Cantones.
 * @author eperaza
 * fecha de creacion: 09/07/2014.
 */
public class CantonesImplementor {
private CantonesDataAccess _dataAccess;
private static CantonesImplementor _instancia;
	
	private CantonesImplementor()
	{
		_dataAccess = new CantonesDataAccess();
	}
	
	public static CantonesImplementor getInstance(){
		if(_instancia == null)
			_instancia = new CantonesImplementor();
		return _instancia;
	}
	
	/**
	 * Se retorna la lista de cantones guardados en la BD, de acuerdo a la provincia indicada.
	 * @return Lista de cantones
	 */
	public List<Parametro> obtenerListaCantones(int pCodigoProvincia){
		_dataAccess.openForReading();
		List<Parametro> lCantones = _dataAccess.obtenerCantones(pCodigoProvincia);
		_dataAccess.close();
		return lCantones;
	}
	
	/**
	 * Se obtiene la provincia a la que pertenece un canton.
	 * @param pCodigoCanton
	 * @return
	 */
	public int obtenerCodigoProvincia(int pCodigoCanton){
		_dataAccess.openForReading();
		int codigoProvincia = _dataAccess.obtenerCodigoProvincia(pCodigoCanton);
		_dataAccess.close();
		return codigoProvincia;
	}
}