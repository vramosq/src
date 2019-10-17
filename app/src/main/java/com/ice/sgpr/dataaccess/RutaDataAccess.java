package com.ice.sgpr.dataaccess;

import java.util.ArrayList;
import java.util.List;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaRuta;
import com.ice.sgpr.entidades.Ruta;

import android.content.ContentValues;
import android.database.Cursor;


public class RutaDataAccess extends AbstractDataAccess {
	private String _sConsultaRutas = "SELECT * FROM " + TablaRuta.NOMBRE_TABLA + " WHERE " + TablaRuta.COL_CODIGO_USUARIO + " = '";
	private String _sConsultaInfoRuta = "SELECT * FROM " + TablaRuta.NOMBRE_TABLA + " WHERE " + TablaRuta.COL_RUTA_SELECCIONADA + " = 1 AND "
			+ TablaRuta.COL_CODIGO_USUARIO + " = ";
	private String _sConsultaIdRutaActiva = "SELECT " + TablaRuta.COL_RUTA_ID + " FROM " + TablaRuta.NOMBRE_TABLA + " WHERE " + TablaRuta.COL_RUTA_SELECCIONADA
			+ " = 1 AND " + TablaRuta.COL_CODIGO_USUARIO + " = ";

	public RutaDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
		
	/**
	 * Consulta la lista de rutas almacenadas en SQLite.
	 * @return cursor: Lista de rutas.
	 */
	public List<Ruta> obtenerRutas(String pCodigoUsuario){
		List<Ruta> lRutas = new ArrayList<Ruta>();
		Cursor cursorRutas = _database.rawQuery(_sConsultaRutas + pCodigoUsuario + "'", null);
		Ruta ruta;
		if (cursorRutas.moveToFirst()){
		     //Se recorre el cursor, registro por registro.
		     do {
		          int nIdRuta = cursorRutas.getInt(0);
		          String sNombre = cursorRutas.getString(1);
		          String sDescripcion = cursorRutas.getString(2);
		          String sFecha = cursorRutas.getString(3);
		          String sFrecuencia = cursorRutas.getString(4);
		          int nPendiente = cursorRutas.getInt(5);
		          int nSeleccionada = cursorRutas.getInt(9);
		          
		          ruta = new Ruta(nIdRuta, sNombre, sDescripcion, sFrecuencia, sFecha, nPendiente, "", "", nSeleccionada);
		          lRutas.add(ruta);
		     } while(cursorRutas.moveToNext());
		}
		return lRutas;
	}
	
	/**
	 * Retorna la informacion de ruta seleccionada en la lista de rutas.
	 * @return
	 */
	public Ruta obtenerInformacionRuta(String pCodigoUsuario){
		Ruta ruta = null;
		Cursor cInfoRuta = _database.rawQuery(_sConsultaInfoRuta + pCodigoUsuario, null);
		if(cInfoRuta.moveToFirst()){
			ruta = new Ruta(cInfoRuta.getInt(0), cInfoRuta.getString(1), cInfoRuta.getString(2), cInfoRuta.getString(4), 
					cInfoRuta.getString(3), cInfoRuta.getInt(5), cInfoRuta.getString(7), cInfoRuta.getString(8), cInfoRuta.getInt(9));
		}
		return ruta;
	}
	
	/**
	 * Se insertan valores en la "TablaRuta".
	 * @param pRutaId
	 * @param pRutaDescripcion
	 * @param pRutaFechaUltVisita
	 * @param pRutaFrecuencia
	 * @param pPendiente
	 */
	public void insertarRuta(int pRutaId, String pRutaNombre, String pRutaDescripcion, String pRutaFechaUltVisita, 
			String pRutaFrecuencia, int pPendiente, String pCodigoUsuario, String pLatitud, String pLongitud, int pSeleccionada){
		ContentValues values = new ContentValues();
		values.put(TablaRuta.COL_RUTA_ID, pRutaId);
		values.put(TablaRuta.COL_RUTA_NOMBRE, pRutaNombre);
		values.put(TablaRuta.COL_RUTA_DESC, pRutaDescripcion);
		values.put(TablaRuta.COL_RUTA_FECHA, pRutaFechaUltVisita);
		values.put(TablaRuta.COL_RUTA_FREC, pRutaFrecuencia);
		values.put(TablaRuta.COL_RUTA_PEND, pPendiente);
		values.put(TablaRuta.COL_CODIGO_USUARIO, pCodigoUsuario);
		values.put(TablaRuta.COL_RUTA_LATITUD, pLatitud);
		values.put(TablaRuta.COL_RUTA_LONGITUD, pLongitud);
		values.put(TablaRuta.COL_RUTA_SELECCIONADA, pSeleccionada);

		_database.insert(TablaRuta.NOMBRE_TABLA, null, values);
	}
	
	/**
	 * Borra los datos de la tabla "TablaRuta".
	 */
	public void borrarRutasUsuario(String pCodigoUsuario) 
	{
		String sCondicion = TablaRuta.COL_CODIGO_USUARIO + " = " + pCodigoUsuario;
		_database.delete(TablaRuta.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Se obtiene el id de la ruta seleccionada.
	 * @param pCodigoUsuario
	 * @return
	 */
	public int obtenerIdRutaSeleccionada(String pCodigoUsuario){
		Cursor cIdRuta = _database.rawQuery(_sConsultaIdRutaActiva + pCodigoUsuario, null);
		int nIdRuta = 0;
		if(cIdRuta.moveToFirst()){
			nIdRuta = cIdRuta.getInt(0);
		}
		return nIdRuta;
	}
	
	/**
	 * Activa o desactiva una ruta (Iniciar o Finalizar)
	 * @param pIdRuta
	 */
	public void activarDesactivarRuta(int pIdRuta, int pEstado, String pCodigoUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaRuta.COL_RUTA_SELECCIONADA, pEstado);
		String sCondicion = TablaRuta.COL_CODIGO_USUARIO + " = " + pCodigoUsuario + " AND " + TablaRuta.COL_RUTA_ID + " = " + pIdRuta;
		_database.update(TablaRuta.NOMBRE_TABLA, values, sCondicion, null);
	}
}
