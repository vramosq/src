package com.ice.sgpr.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaVersionamiento;
import com.ice.sgpr.entidades.Versionamiento;
/**
 * Acceso a datos de versionamiento.
 * @author eperaza
 * Fecha de creacion: 26/09/2013.
 */
public class VersionamientoDataAccess extends AbstractDataAccess {
	private String _sConsultaObtenerVersionamiento = "SELECT * FROM " + TablaVersionamiento.NOMBRE_TABLA + " WHERE " 
			+ TablaVersionamiento.COL_CODIGO_USUARIO + " = "; 
	
	public VersionamientoDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}	
	
	/**
	 * Crea una nueva tabla para versionamiento.
	 * @param pUsuario
	 */
	public void insertarTablaVersionamiento(String pUsuario){
		ContentValues values = new ContentValues();
		for(int cont = 0; cont < Constantes.VERSION_AMOUNT; cont ++){
			values.put(TablaVersionamiento.COL_CODIGO_USUARIO, pUsuario);
			values.put(TablaVersionamiento.COL_PROCESO, cont);
			values.put(TablaVersionamiento.COL_ESTADO, 0);
			_database.insert(TablaVersionamiento.NOMBRE_TABLA, null, values);
		}
	}
	
	/**
	 * Elimina la tabla de Versionamiento.
	 */
	public void borrarTablaVersionamiento(String pUsuario){
		String sCondicion = TablaVersionamiento.COL_CODIGO_USUARIO + " = " + pUsuario;
		_database.delete(TablaVersionamiento.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Obtiene los datos del versionamiento. (Para revisar si se ha versionado toda la información).
	 * @param pUsuario
	 * @return
	 */
	public List<Versionamiento> obtenerTablaVersionamiento(String pUsuario){
		Cursor cVersionamiento = _database.rawQuery(_sConsultaObtenerVersionamiento + pUsuario, null);
		List<Versionamiento> listaVersionamiento = new ArrayList<Versionamiento>();
		if(cVersionamiento.moveToFirst()){
			do{
				Versionamiento versionamiento = new Versionamiento(
						cVersionamiento.getString(0),
						cVersionamiento.getInt(1), 
						cVersionamiento.getInt(2));
				listaVersionamiento.add(versionamiento);
			}while(cVersionamiento.moveToNext());
		}
		return listaVersionamiento;
	}
	
	/**
	 * Actualiza los datos de un proceso que ha sido versionado.
	 * @param pUsuario
	 * @param pEstado
	 */
	public void actualizarDatosVersionamiento(String pUsuario, int pProceso, int pEstado){
		Cursor cVersionamiento = _database.rawQuery(_sConsultaObtenerVersionamiento + pUsuario, null);
		if(cVersionamiento.moveToFirst()){
			ContentValues values = new ContentValues();
			values.put(TablaVersionamiento.COL_CODIGO_USUARIO, pUsuario);
			values.put(TablaVersionamiento.COL_PROCESO, pProceso);
			values.put(TablaVersionamiento.COL_ESTADO, pEstado);
			
			String sCondicion = TablaVersionamiento.COL_PROCESO + " = " + pProceso;
			_database.update(TablaVersionamiento.NOMBRE_TABLA, values, sCondicion, null);
		}
	}
}
