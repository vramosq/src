package com.ice.sgpr.implementor;
import com.ice.sgpr.comun.Utils;
import com.ice.sgpr.dataaccess.UsuariosDataAccess;
/**
 * Implementor para la consulta de datos relacionados con Negocios.
 * @author eperaza
 * fecha de creacion: 07/08/2013.
 */
public class UsuariosImplementor {
private UsuariosDataAccess _dataAccess;
private static UsuariosImplementor _instancia;
	
	private UsuariosImplementor()
	{
		_dataAccess = new UsuariosDataAccess();
	}
	
	public static UsuariosImplementor getInstance(){
		if(_instancia == null)
			_instancia = new UsuariosImplementor();
		return _instancia;
	}
	
	/**
	 * Se registra el usuario que inicio sesion.
	 * @param pUsuario
	 */
	public void insertarUsuario(String pUsuario, String pNombreUsuario, int pContasena, String pRolUsuario){
		_dataAccess.openForWriting();
		_dataAccess.insertarUsuario(pUsuario, pNombreUsuario, pContasena, pRolUsuario);
		_dataAccess.close();
	}
		
	/**
	 * Se verifica si el usuario existe.
	 * @return
	 */
	public String validarUsuarioLogueado(String pUsuario, int pContrasena){
		_dataAccess.openForReading();
		String sCodigoUsuario = _dataAccess.validarDatos(pUsuario, pContrasena);
		_dataAccess.close();
		return sCodigoUsuario;
	}
	
	public void desactivarUsuario(){
		_dataAccess.openForWriting();
		String sCodigoUsuario = _dataAccess.obtenerUsuarioLogueado()[0];
		_dataAccess.activarYdesactivarUsuario(sCodigoUsuario, 0);
		_dataAccess.close();
	}
	
	/**
	 * Se obtiene el codigo y nombre de usuario logueado.
	 * @return
	 */
	public String[] obtenerUsuarioLogueado(){
		_dataAccess.openForReading();
		String[] sDatosUsuario = _dataAccess.obtenerUsuarioLogueado();
		_dataAccess.close();
		return sDatosUsuario;
	}
	
	/**
	 * Se obtiene el codigo y nombre de usuario logueado.
	 * @return
	 */
	public String obtenerRolUsuarioLogueado(){
		_dataAccess.openForReading();
		String sRol = _dataAccess.obtenerRolUsuarioLogueado();
		_dataAccess.close();
		return sRol;
	}
	
	/**
	 * Si hay algun usuario que dej{o la sesion abierta, le coloca el estado de inactivo y activa el
	 * nuevo usuario.
	 * @param pCodigoUsuario
	 */
	public void cambiarDeUsuario(String pCodigoUsuario){
		_dataAccess.openForWriting();
		_dataAccess.cambiarUsuarioActivo(pCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Retorna la fecha de la ultima sincronizacion por parte de un usuario.
	 * @return
	 */
	public String obtenerUltimaSincronizacion(){
		_dataAccess.openForWriting();
		String sCodigoUsuario = _dataAccess.obtenerUsuarioLogueado()[0];
		String sFecha = _dataAccess.obtenerFechaUltimaSincronizacion(sCodigoUsuario);
		_dataAccess.close();
		return sFecha;
	}
	
	/**
	 * Actualiza la fecha de la ultima sincronizacion que hace un usuario.
	 */
	public void actualizarFechaSinc(){
		_dataAccess.openForWriting();
		String sFechaActual = Utils.obtenerFechaActual();
		String sCodigoUsuario = _dataAccess.obtenerUsuarioLogueado()[0];
		_dataAccess.actualizarFechaSinc(sFechaActual, sCodigoUsuario);
		_dataAccess.close();
	}
	
	public int aceptaSeguimiento(){
		_dataAccess.openForWriting();
		String sCodigoUsuario = _dataAccess.obtenerUsuarioLogueado()[0];
		int acepta = _dataAccess.aceptaSeguimiento(sCodigoUsuario);
		_dataAccess.close();
		return acepta;
	}
	
	/**
	 * Actualiza la fecha de la ultima sincronizacion que hace un usuario.
	 */
	public void actualizarPermisoSeguimiento(int acepta){
		_dataAccess.openForWriting();
		String sCodigoUsuario = _dataAccess.obtenerUsuarioLogueado()[0];
		_dataAccess.actualizarPermisoSeguimiento(sCodigoUsuario, acepta);
		_dataAccess.close();
	}
}