package com.ice.sgpr.implementor;

import java.util.List;
import com.ice.sgpr.dataaccess.ParametrosDataAccess;
import com.ice.sgpr.entidades.Parametro;

/**
 * Implementor para la consulta de datos relacionados con Parametros.
 * @author eperaza
 * fecha de creacion: 23/09/2013.
 */
public class ParametrosImplementor {
private ParametrosDataAccess _dataAccess;
private static ParametrosImplementor _instancia;
	
	private ParametrosImplementor()
	{
		_dataAccess = new ParametrosDataAccess();
	}
	
	public static ParametrosImplementor getInstance(){
		if(_instancia == null)
			_instancia = new ParametrosImplementor();
		return _instancia;
	}
	
	/**
	 * Se insertan los parametros en a BD.
	 * @param pListaParametros
	 */
	public void insertarParametros(List<Parametro> pListaParametros){
		_dataAccess.openForWriting();
		_dataAccess.insertarParametro(pListaParametros);
		_dataAccess.close();
	}
	
	/**
	 * Se retorna la lista de categorias guardadas en la BD
	 * @param pCategoria: 0 Para los parametros de Tipo de Comercio, 1 para los de Prioridad.
	 * @return Lista de parametros
	 */
	public List<Parametro> obtenerListaParametros(int pCategoria){
		_dataAccess.openForWriting();
		List<Parametro> lParametros = _dataAccess.obtenerParametros(pCategoria);
		_dataAccess.close();
		return lParametros;
	}
	
	/**
	 * Borra todos los parametros.
	 */
	public void borrarParametros(){
		_dataAccess.openForWriting();
		_dataAccess.borrarParametros();
		_dataAccess.close();
	}
}