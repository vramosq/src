package com.ice.sgpr.implementor;

import java.util.List;

import com.ice.sgpr.dataaccess.VersionamientoDataAccess;
import com.ice.sgpr.entidades.Versionamiento;

public class VersionamientoImplementor {
private VersionamientoDataAccess _dataAccess;
private static VersionamientoImplementor _instancia;
	
	private VersionamientoImplementor()
	{
		_dataAccess = new VersionamientoDataAccess();
	}
	
	public static VersionamientoImplementor getInstance(){
		if(_instancia == null)
			_instancia = new VersionamientoImplementor();
		return _instancia;
	}
	
	/**
	 * Crea una nueva tabla para el versionamiento.
	 */
	public void insertarTablaVersionamiento(){
		_dataAccess.openForWriting();
		String sUsuarioId = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.insertarTablaVersionamiento(sUsuarioId);
		_dataAccess.close();
	}
	
	/**
	 * Borra la tabla de versionamiento.
	 */
	public void borrarTablaVersionamiento(){
		_dataAccess.openForWriting();
		String sUsuarioId = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarTablaVersionamiento(sUsuarioId);
		_dataAccess.close();
	}
	
	/**
	 * Obtiene los datos del versionamiento.
	 */
	public List<Versionamiento> obtenerVersionamiento(){
		_dataAccess.openForWriting();
		String sUsuarioId = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Versionamiento> listaVersionamiento = _dataAccess.obtenerTablaVersionamiento(sUsuarioId);
		_dataAccess.close();
		return listaVersionamiento;
	}
	
	/**
	 * Actualiza los datos de un proceso si ha sido versionado correctamente.
	 * @param pProceso
	 */
	public void actualizarProcesoVersionamiento(int pProceso){
		_dataAccess.openForWriting();
		String sUsuarioId = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.actualizarDatosVersionamiento(sUsuarioId, pProceso, 1);
		_dataAccess.close();
	}
}
