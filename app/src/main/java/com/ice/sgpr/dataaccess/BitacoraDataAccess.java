package com.ice.sgpr.dataaccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaBitacora;
import com.ice.sgpr.database.TablaNegocios;
import com.ice.sgpr.entidades.Bitacora;
/**
 * Acceso a datos de la bitácora.
 * @author eperaza
 * Fecha de creación: 08/08/2013.
 */
public class BitacoraDataAccess extends AbstractDataAccess {
	private String _sConsultaObtenerBitacora = "SELECT b."+ TablaBitacora.COL_NEGOCIO_ID +", b."+ TablaBitacora.COL_FECHA_INI 
			+ ", b." + TablaBitacora.COL_FECHA_FIN + ", n." + TablaNegocios.COL_NOMBRE + ", b." + TablaBitacora.COL_LATITUD + ", b." 
			+ TablaBitacora.COL_LONGITUD + ", b." + TablaBitacora.COL_ACTUALIZADA
			+ " FROM " + TablaBitacora.NOMBRE_TABLA +" b, "+ TablaNegocios.NOMBRE_TABLA +" n WHERE n."	+ TablaNegocios.COL_ID +" = b."
			+ TablaBitacora.COL_NEGOCIO_ID +" AND b."+ TablaBitacora.COL_RUTA_ID +" = ";
	private String _sConsultaModificarBitacora = "SELECT "+TablaBitacora.COL_BITACORA_ID+" FROM " + TablaBitacora.NOMBRE_TABLA + " WHERE " 
			+ TablaBitacora.COL_NEGOCIO_ID + " = N* ORDER BY "+ TablaBitacora.COL_BITACORA_ID +" DESC LIMIT 1";
	private String _sConsultaBitacoraParaVersionamiento = "SELECT * FROM " + TablaBitacora.NOMBRE_TABLA + " WHERE " + TablaBitacora.COL_ACTUALIZADA 
			+ " = 1 AND " + TablaBitacora.COL_USUARIO_ID + " = ";
	private String _sConsultaFechaHoraNegocio = "SELECT MAX(" + TablaBitacora.COL_FECHA_INI + ") FROM " + TablaBitacora.NOMBRE_TABLA + " WHERE " + TablaBitacora.COL_NEGOCIO_ID
			+ " = ";
	
	public BitacoraDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se inserta una nueva bitácora en la tabla.
	 * Al ser una nueva bitácora que no ha sido versionada, el valor de "Actualizada" se coloca desde el inicio en 1.
	 * @param pIdRuta
	 * @param pIdNegocio
	 * @param pFechaInicio
	 */
	public void insertarNuevaBitacora(int pIdRuta, int pIdNegocio, String pFechaInicio, String pLatitud, String pLongitud, String pCodigoUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaBitacora.COL_RUTA_ID, pIdRuta);
		values.put(TablaBitacora.COL_NEGOCIO_ID, pIdNegocio);
		values.put(TablaBitacora.COL_FECHA_INI, pFechaInicio);
		values.put(TablaBitacora.COL_LATITUD, pLatitud);
		values.put(TablaBitacora.COL_LONGITUD, pLongitud);
		values.put(TablaBitacora.COL_ACTUALIZADA, 1);
		values.put(TablaBitacora.COL_USUARIO_ID, pCodigoUsuario);
		
		_database.insert(TablaBitacora.NOMBRE_TABLA, null, values);
	}
	
	/**
	 * Obtener la lista de bit�coras almacenadas en la BD para mostrarlas en la info de ruta, invierte la lista para que los registros
	 * m�s recientes queden arriba.
	 * @return
	 */
	public List<Bitacora> obtenerBitacora(int pRutaId){
		List<Bitacora> lBitacora = new ArrayList<Bitacora>();
		Cursor cursorBitacora = _database.rawQuery(_sConsultaObtenerBitacora + pRutaId, null);
		if (cursorBitacora.moveToFirst()){
			do {
		          int nIdNegocio = cursorBitacora.getInt(0);
		          String sFechaInicio = cursorBitacora.getString(1);
		          String sFechaFin = cursorBitacora.getString(2);
		          String sNombreNegocio = cursorBitacora.getString(3);
		          String sLatitud = cursorBitacora.getString(4);
		          String sLongitud = cursorBitacora.getString(5);
		          int sIndicador = cursorBitacora.getInt(6);
		          
		          Bitacora bitacora = new Bitacora(pRutaId, nIdNegocio, sFechaInicio, sFechaFin, sNombreNegocio, sLatitud, sLongitud, sIndicador, 0);
		          lBitacora.add(bitacora);
		     } while(cursorBitacora.moveToNext());
		}
		Collections.reverse(lBitacora);
		return lBitacora;
	}
	
	/**
	 * Actualiza la bit�cora de un negocio, agregando al registro la hora de Fin de la visita.
	 * @param pHoraFinNegocio
	 */
	public void actualizarBitacora(int pNegocioId, String pHoraFinNegocio, String pLatitud, String pLongitud){
		ContentValues values = new ContentValues();
		values.put(TablaBitacora.COL_FECHA_FIN, pHoraFinNegocio);
		values.put(TablaBitacora.COL_LATITUD, pLatitud);
		values.put(TablaBitacora.COL_LONGITUD, pLongitud);
		
		String sConsulta = _sConsultaModificarBitacora.replace("N*", Integer.toString(pNegocioId));
		Cursor mcursor = _database.rawQuery(sConsulta, null);
		
		if(mcursor.moveToFirst()){
			String sCondicion = TablaBitacora.COL_BITACORA_ID + " = " + mcursor.getInt(0);
			_database.update(TablaBitacora.NOMBRE_TABLA, values, sCondicion, null);
		}
	}
	
	/**
	 * Obtiene todas las bit�coras para versionamiento.
	 * @return
	 */
	public List<Bitacora> obtenerBitacorasVersionamiento(String pCodigoUsuario){
		List<Bitacora> lBitacora = new ArrayList<Bitacora>();
		Cursor cursorBitacora = _database.rawQuery(_sConsultaBitacoraParaVersionamiento + pCodigoUsuario, null);
		if (cursorBitacora.moveToFirst()){
			do {
				int nIdRuta = cursorBitacora.getInt(0);
		          int nIdNegocio = cursorBitacora.getInt(1);
		          String sFechaInicio = cursorBitacora.getString(2);
		          String sFechaFin = cursorBitacora.getString(3);
		          int nActualizada = cursorBitacora.getInt(4);
		          String sLatitud = cursorBitacora.getString(5);
		          String sLongitud = cursorBitacora.getString(6);
		          int nId = cursorBitacora.getInt(7);
		          Bitacora bitacora = new Bitacora(nIdRuta, nIdNegocio, sFechaInicio, sFechaFin, "", sLatitud, sLongitud, nActualizada, nId);
		          lBitacora.add(bitacora);
		     } while(cursorBitacora.moveToNext());
		}
		return lBitacora;
	}
	
	/**
	 * Actualiza el estado de la bit�cora a 0, indicando que ya fue versionada.
	 * @param pBitacoraId
	 */
	public void actualizarEstadoBitacoraVersionamiento(int pBitacoraId){
		ContentValues values = new ContentValues();
		values.put(TablaBitacora.COL_ACTUALIZADA, 0);
		String sCondicion = TablaBitacora.COL_BITACORA_ID + " = " + pBitacoraId;
		_database.update(TablaBitacora.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Actualiza el c�digo asignado por la app por el obtenido del SP. (Negocios nuevos)
	 * @param pCodigoAnterior
	 * @param pCodigoNuevo
	 */
	public void actualizarIdBitacoraDespuesVersionamiento(int pCodigoAnterior, int pCodigoNuevo){
		ContentValues values = new ContentValues();
		values.put(TablaBitacora.COL_NEGOCIO_ID, pCodigoNuevo);
		String sCondicion = TablaBitacora.COL_NEGOCIO_ID + " = " + pCodigoAnterior;
		_database.update(TablaBitacora.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Borra las bit�coras de un determinado usuario.
	 * @param pCodigoUsuario
	 * @param pIdNegocio Negocio a borrar (-1 = todos).
	 */
	public void borrarBitacoraUsuario(String pCodigoUsuario, int pIdNegocio){
		String sCondicion = TablaBitacora.COL_USUARIO_ID + " = " + pCodigoUsuario;
		if(pIdNegocio != -1)
			sCondicion +=" AND " + TablaBitacora.COL_NEGOCIO_ID + " = " + pIdNegocio;
		_database.delete(TablaBitacora.NOMBRE_TABLA, sCondicion, null);
	}

	public Date obtenerFechaNegocio(int negocioId){
		Cursor cursorBitacora = _database.rawQuery(_sConsultaFechaHoraNegocio + negocioId, null);
		if (cursorBitacora.moveToFirst()){
			String fechaHora = cursorBitacora.getString(0);

			try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				return formatter.parse(fechaHora);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
