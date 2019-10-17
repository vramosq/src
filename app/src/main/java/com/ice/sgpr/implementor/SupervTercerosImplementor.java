package com.ice.sgpr.implementor;

import com.ice.sgpr.dataaccess.SupervTercerosDataAccess;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Implementor para la consulta de datos relacionados con Parametros.
 * @author eperaza
 * fecha de creacion: 26.05.16
 */
public class SupervTercerosImplementor {
private SupervTercerosDataAccess _dataAccess;
private static SupervTercerosImplementor _instancia;

	private SupervTercerosImplementor()
	{
		_dataAccess = new SupervTercerosDataAccess();
	}
	
	public static SupervTercerosImplementor getInstance(){
		if(_instancia == null)
			_instancia = new SupervTercerosImplementor();
		return _instancia;
	}
	
	/**
	 * Se insertan las superviciones en la BD
	 */
	public void insertarNuevaSupervision(long pIdNegocio, String pSupervisionInfo, int pCodigoUsuario, String pTipo){
		_dataAccess.openForWriting();
		_dataAccess.insertarNuevaSupervision(pIdNegocio, pSupervisionInfo, pCodigoUsuario, pTipo);
		_dataAccess.close();
	}
	
	/**
	 * Se retorna el JSON con los datos
	 */
	public String obtenerJsonInfo(long pNegocioId, String pTipo){
		_dataAccess.openForWriting();
		String jsonInfo = _dataAccess.obtenerJsonInfo(pNegocioId, pTipo);
		_dataAccess.close();
		return jsonInfo;
	}
	
	/**
	 * Borra todos los parametros.
	 */
	public void actualizarSuperviciones(String pUserId, String pType){
		_dataAccess.openForWriting();
		_dataAccess.actualizarSupervisiones(pUserId, pType);
		_dataAccess.close();
	}

	/**
	 * Se retorna el JSON con los datos
	 */
	public JSONArray obtenerAtencionVersionamiento(String pUserId, String pType) throws JSONException {
		_dataAccess.openForWriting();
		JSONArray jsonInfo = _dataAccess.obtenerAtencionVersionamiento(pUserId, pType);
		_dataAccess.close();
		return jsonInfo;
	}

	/**
	 * Se borran los registros de un usuario.
	 */
	public void borrarSupervicionesUsuario(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarSupervicionesUsuario(sCodigoUsuario);
		_dataAccess.close();
	}

	/**
	 * Se borran los registros de un tipo de formulario.
	 */
	public void borrarSuperviciones(long negocioId, String pType){
		_dataAccess.openForReading();
		_dataAccess.borrarSupervisiones(negocioId, pType);
		_dataAccess.close();
	}
}