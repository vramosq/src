package com.ice.sgpr.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaUsuarios;
/**
 * Acceso a datos de la tabla de usuarios.
 * @author eperaza
 * Fecha de creacion: 01/10/2013.
 */
public class UsuariosDataAccess extends AbstractDataAccess {
	private String _sConsultaObtenerUsuario = "SELECT * FROM " + TablaUsuarios.NOMBRE_TABLA + " WHERE " + TablaUsuarios.COL_NOMBRE_USUARIO 
			+ " = 'U*' AND " + TablaUsuarios.COL_CONTRASENA_USUARIO + " = 'C*'";
	private String _sConsultaObtenerUsuarioLogueado = "SELECT "+ TablaUsuarios.COL_USUARIO + ", " + TablaUsuarios.COL_NOMBRE_USUARIO +" FROM " 
			+ TablaUsuarios.NOMBRE_TABLA + " WHERE " + TablaUsuarios.COL_ACTIVO + " = 1";
	private String _sConsultaFechaSincronizacion = "SELECT " + TablaUsuarios.COL_FECHA_SINC + " FROM " + TablaUsuarios.NOMBRE_TABLA + " WHERE "
			+ TablaUsuarios.COL_USUARIO + " = ";
	private String _sConsultaObtenerRolUsuarioLogueado = "SELECT "+ TablaUsuarios.COL_ROL + " FROM " 
			+ TablaUsuarios.NOMBRE_TABLA + " WHERE " + TablaUsuarios.COL_ACTIVO + " = 1";
	private String _sConsultaAceptaSeguimiento = "SELECT "+ TablaUsuarios.COL_ACEPTA_SEGUIMIENTO + " FROM " 
			+ TablaUsuarios.NOMBRE_TABLA + " WHERE " + TablaUsuarios.COL_USUARIO + " = ";
	
	public UsuariosDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se registra el usuario que ha iniciado sesion.
	 * @param pUsuario
	 */
	public void insertarUsuario(String pUsuario, String pNombreUsuario, int pContrasena, String pRolUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaUsuarios.COL_USUARIO, pUsuario);
		values.put(TablaUsuarios.COL_NOMBRE_USUARIO, pNombreUsuario);
		values.put(TablaUsuarios.COL_CONTRASENA_USUARIO, pContrasena);
		values.put(TablaUsuarios.COL_ROL, pRolUsuario);
		values.put(TablaUsuarios.COL_ACEPTA_SEGUIMIENTO, 0);
		_database.insert(TablaUsuarios.NOMBRE_TABLA, null, values);
		cambiarUsuarioActivo(pUsuario);
	}
		
	/**
	 * Valida que un usuario esta guardado en la BD.
	 * @param pUsuario
	 * @param pContrasena
	 * @return: Codigo del usuario, para iniciar sesion en la app.
	 */
	public String validarDatos(String pUsuario, int pContrasena){		
		String sConsulta = _sConsultaObtenerUsuario.replace("U*", pUsuario);
		sConsulta = sConsulta.replace("C*", Integer.toString(pContrasena));
		
		Cursor mcursor = _database.rawQuery(sConsulta, null);
		if(mcursor.moveToFirst())
			return mcursor.getString(0);
		return null;
	}
	
	/**
	 * Retorna el usuario que est� logueado (C�digo y nombre en un array).
	 * @return
	 */
	public String[] obtenerUsuarioLogueado(){
		Cursor cUsuario = _database.rawQuery(_sConsultaObtenerUsuarioLogueado, null);
		if(cUsuario.moveToFirst()){
			String[] sDatosUsuario = {cUsuario.getString(0),cUsuario.getString(1)};
			return sDatosUsuario;
		}
		else
			return null;
	}
	
	/**
	 * Retorna el usuario que est� logueado (C�digo y nombre en un array).
	 * @return
	 */
	public String obtenerRolUsuarioLogueado(){
		Cursor cUsuario = _database.rawQuery(_sConsultaObtenerRolUsuarioLogueado, null);
		if(cUsuario.moveToFirst()){
			String sRol = cUsuario.getString(0);
			return sRol;
		}
		else
			return null;
	}
	
	/**
	 * Cambia el estado "Activo" al usuario que acaba de loguearse.
	 */
	public void cambiarUsuarioActivo(String pUsuario){
		Cursor cUsuario = _database.rawQuery(_sConsultaObtenerUsuarioLogueado, null);
		if(cUsuario.moveToFirst()){
			String usuarioLogueado = cUsuario.getString(0);
			activarYdesactivarUsuario(usuarioLogueado, 0);
		}
		activarYdesactivarUsuario(pUsuario, 1);
	}
	
	/**
	 * Cambia el estado de un usuario a "desactivado" (0).
	 * @param pUsuario
	 */
	public void activarYdesactivarUsuario(String pUsuario, int pEstado){
		ContentValues values = new ContentValues();
		values.put(TablaUsuarios.COL_ACTIVO, pEstado);
		String sCondicion = TablaUsuarios.COL_USUARIO + " = " + pUsuario;
		_database.update(TablaUsuarios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Obtener la fecha de la �ltima sincronizaci�n hecha por un usuario
	 * @return
	 */
	public String obtenerFechaUltimaSincronizacion(String pUsuario){
		Cursor cFechaSinc = _database.rawQuery(_sConsultaFechaSincronizacion + pUsuario, null);
		cFechaSinc.moveToFirst();
		return cFechaSinc.getString(0);
	}
	
	/**
	 * Se insertan los datos de la aplicaci�n
	 * @param pFechaSincronizacion: Fecha de la �ltima vez que se sincroniz�.
	 */
	public void actualizarFechaSinc(String pFechaSincronizacion, String pCodigoUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaUsuarios.COL_FECHA_SINC, pFechaSincronizacion);
		String sCondicion = TablaUsuarios.COL_USUARIO + " = " + pCodigoUsuario; 
		_database.update(TablaUsuarios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Verifica si el usuario ha aceptado el seguimiento de GPS
	 * @param userId
	 * @return 0 = No se le ha preguntado, 1 = Acept�, 2 = No acept�
	 */
	public int aceptaSeguimiento(String userId){
		Cursor cUsuario = _database.rawQuery(_sConsultaAceptaSeguimiento + userId, null);
		if(cUsuario.moveToFirst()){
			int nAcepta = cUsuario.getInt(0);
			return nAcepta;
		}
		else
			return 0;
	}
	
	/**
	 * Se actualiza el estado de si acepta o no seguimiento
	 * @param pCodigoUsuario
	 * @param acepta: 1= Acept�, 2 = No acept�
	 */
	public void actualizarPermisoSeguimiento(String pCodigoUsuario, int acepta){
		ContentValues values = new ContentValues();
		values.put(TablaUsuarios.COL_ACEPTA_SEGUIMIENTO, acepta);
		String sCondicion = TablaUsuarios.COL_USUARIO + " = " + pCodigoUsuario; 
		_database.update(TablaUsuarios.NOMBRE_TABLA, values, sCondicion, null);
	}
}
