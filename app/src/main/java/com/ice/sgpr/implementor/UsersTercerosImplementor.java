package com.ice.sgpr.implementor;

import com.ice.sgpr.dataaccess.UsersTercerosDataAccess;
import com.ice.sgpr.entidades.Parametro;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementor para la consulta de los usuarios del modulo de terceros.
 * @author eperaza
 * fecha de creacion: 08.06.16
 */
public class UsersTercerosImplementor {
private UsersTercerosDataAccess _dataAccess;
private static UsersTercerosImplementor _instancia;

	private UsersTercerosImplementor()
	{
		_dataAccess = new UsersTercerosDataAccess();
	}
	
	public static UsersTercerosImplementor getInstance(){
		if(_instancia == null)
			_instancia = new UsersTercerosImplementor();
		return _instancia;
	}
	
	/**
	 * Se insertan las superviciones en la BD
	 */
	public void insertarNuevosUsuarios(ArrayList<String[]> pUsersList){
		_dataAccess.openForWriting();
		_dataAccess.insertarNuevosUsuarios(pUsersList);
		_dataAccess.close();
	}
	
	/**
	 * Se retorna la lista de usuarios
	 */
	public List<Parametro> obtenerUsuariosTerceros(){
		_dataAccess.openForWriting();
		List<Parametro> usersList = _dataAccess.obtenerUsuariosTerceros();
		_dataAccess.close();
		return usersList;
	}
	
	/**
	 * Borra todos los parametros.
	 */
	public void borrarUsuariosTerceros(){
		_dataAccess.openForWriting();
		_dataAccess.borrarUsuarios();
		_dataAccess.close();
	}
}