package com.ice.sgpr.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaNegocios;
import com.ice.sgpr.entidades.Negocio;

/**
 * 
 * @author eperaza
 * Fecha de creacion: 07/08/2013.
 */
public class NegociosDataAccess extends AbstractDataAccess {

	private String _consultaNegocios = "SELECT * FROM "+ TablaNegocios.NOMBRE_TABLA +" WHERE "+ TablaNegocios.COL_RUTA_ID +" = ";
	private String _consultaIdNegocio = "SELECT * FROM "+ TablaNegocios.NOMBRE_TABLA +" WHERE "+ TablaNegocios.COL_NEGOCIO_ACTIVO 
			+" = " + Constantes.ESTADO_NEGOCIO_ACTIVO + " AND " + TablaNegocios.COL_USUARIO_ID + " = ";
	private String _sConsultaExisteNegocio = "SELECT " + TablaNegocios.COL_ID + " FROM " + TablaNegocios.NOMBRE_TABLA + " WHERE " + TablaNegocios.COL_ID + " = ";
	private String _sConsultaNegociosVersionamiento = "SELECT * FROM " + TablaNegocios.NOMBRE_TABLA +" WHERE "+ TablaNegocios.COL_ACTUALIZADO + " = 1 AND "
			+ TablaNegocios.COL_USUARIO_ID +" = ";
	private String _sConsultaNuevosNegociosVersionamiento = "SELECT * FROM " + TablaNegocios.NOMBRE_TABLA +" WHERE "+ TablaNegocios.COL_ACTUALIZADO + " = 1 AND "
			+ TablaNegocios.COL_RUTA_ID + " = "+ Constantes.RUTA_ID_NUEVOS_NEGOCIOS +" AND "+ TablaNegocios.COL_USUARIO_ID +" = ";
	private String _sConsultaBuscarNegocio = "SELECT " + TablaNegocios.COL_ID + " FROM " + TablaNegocios.NOMBRE_TABLA + " WHERE " + TablaNegocios.COL_ID + " = ";
	private String _sConsultaObservaciones = "SELECT " + TablaNegocios.COL_OBSERVACIONES + " FROM " + TablaNegocios.NOMBRE_TABLA +" WHERE "+ TablaNegocios.COL_ID + " = ";
	private String _consultaNegociosPorUsusario = "SELECT * FROM "+ TablaNegocios.NOMBRE_TABLA +" WHERE "+ TablaNegocios.COL_USUARIO_ID +" = ";
	
	public NegociosDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Obtiene la lista de negocios desde la BD.
	 * @param pIdRuta
	 */
	public List<Negocio> obtenerListaNegocios(int pIdRuta){
		List<Negocio> lNegocios = new ArrayList<Negocio>();
		Cursor cursorNegocios = _database.rawQuery(_consultaNegocios + pIdRuta, null);
		Negocio negocio;
		if (cursorNegocios.moveToFirst()){
		     //Se recorre el cursor, registro por registro.
		     do {
		          int nIdNegocio = cursorNegocios.getInt(0);
		          String sLatitud = cursorNegocios.getString(2);
		          String sLongitud = cursorNegocios.getString(3);
		          int nEstado = cursorNegocios.getInt(4);
		          String sNombreNegocio = cursorNegocios.getString(5);
		          int nActivo = cursorNegocios.getInt(7);
		          negocio = new Negocio(pIdRuta, nIdNegocio, sLatitud, sLongitud, nEstado, nActivo, sNombreNegocio);
		          lNegocios.add(negocio);
		     } while(cursorNegocios.moveToNext());
		}
		return lNegocios;
	}
	
	/**
	 * Se actualiza el estado del negocio
	 * @param pNegocioId
	 * @param pEstado
	 */
	public void actualizarEstadoNegocio(int pNegocioId, int pEstado, int pActivo, String pFecha){
		ContentValues values = new ContentValues();
		values.put(TablaNegocios.COL_ESTADO, pEstado);
		values.put(TablaNegocios.COL_NEGOCIO_ACTIVO, pActivo);
		if(!pFecha.equals(""))
			values.put(TablaNegocios.COL_ULTIMA_VISITA, pFecha);
		String sCondicion = TablaNegocios.COL_ID + " = " + pNegocioId;
		_database.update(TablaNegocios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Se obtiene el ID del negocio activo.
	 * @return Id del negocio
	 */
	public Negocio obtenerNegocioActivo(String pCodigoUsuario){
		Cursor cursorNegocios = _database.rawQuery(_consultaIdNegocio + pCodigoUsuario, null);
		if (cursorNegocios.moveToFirst()){			
			Negocio negocioSeleccionado = new Negocio(
					cursorNegocios.getInt(1), 
					cursorNegocios.getInt(0), 
					cursorNegocios.getString(2), 
					cursorNegocios.getString(3), 
					cursorNegocios.getInt(4), 
					cursorNegocios.getString(5), 
					cursorNegocios.getString(6), 
					cursorNegocios.getInt(7), 
					cursorNegocios.getString(8), 
					cursorNegocios.getInt(9), 
					cursorNegocios.getInt(10), 
					cursorNegocios.getString(11), 
					cursorNegocios.getString(12), 
					cursorNegocios.getString(13), 
					cursorNegocios.getString(14),
					cursorNegocios.getInt(15),
					cursorNegocios.getString(16),
					((cursorNegocios.getInt(18) == 1)? true : false),
					cursorNegocios.getInt(19),
					cursorNegocios.getInt(20),
					cursorNegocios.getInt(21),
					cursorNegocios.getString(22),
					cursorNegocios.getString(23),
					cursorNegocios.getInt(24),
					cursorNegocios.getString(25)
					);
			return negocioSeleccionado;
		}
		else
			return null;
	}
	
	/**
	 * Se insertan los valores en la tabla "Negocios". Si el negocio ya existe, actualiza la informacion.
	 * @param pRutaId
	 * @param pNegocioId
	 * @param pLatitud
	 * @param pLongitud
	 * @param pNombre
	 * @param pDireccion
	 * @param pEstado
	 * @param pNegocioActivo
	 * @param pDescripcion
	 * @param pTipoNegocio
	 * @param pPrioridad
	 * @param pTelefono
	 * @param pNombreContacto
	 * @param pTelefonoContacto
	 * @param pUltimaVisita
	 * @param pHabilitado
	 * @param pCodigoDistrito
	 */
	public void actualizarNegociosRuta(int pRutaId, int pNegocioId, String pLatitud, String pLongitud, String pNombre, String pDireccion, 
			int pEstado, int pNegocioActivo, String pDescripcion, int pTipoNegocio, int pPrioridad, String pTelefono, String pNombreContacto,
			String pTelefonoContacto, String pUltimaVisita, int pActualizado, String pCelularContacto, String pCodigoUsuario, boolean pHabilitado,
			int pCodigoDistrito, int pCodigoCanton, int pCodigoProvincia, String pObservaciones, String pFechaObservacion, int pUsuarioModObsId,
			String pEmail){
		ContentValues values = new ContentValues();
		values.put(TablaNegocios.COL_LATITUD, pLatitud);
		values.put(TablaNegocios.COL_LONGITUD, pLongitud);
		values.put(TablaNegocios.COL_ESTADO, pEstado);
		values.put(TablaNegocios.COL_NOMBRE, pNombre);
		values.put(TablaNegocios.COL_DIRECCION, pDireccion);
		values.put(TablaNegocios.COL_NEGOCIO_ACTIVO, pNegocioActivo);
		values.put(TablaNegocios.COL_DESCRIPCION, pDescripcion);
		values.put(TablaNegocios.COL_TIPO_NEGOCIO, pTipoNegocio);
		values.put(TablaNegocios.COL_PRIORIDAD, pPrioridad);
		values.put(TablaNegocios.COL_TELEFONO, pTelefono);
		values.put(TablaNegocios.COL_NOMBRE_CONTACTO, pNombreContacto);
		values.put(TablaNegocios.COL_TELEFONO_CONTACTO, pTelefonoContacto);
		values.put(TablaNegocios.COL_ULTIMA_VISITA, pUltimaVisita);
		values.put(TablaNegocios.COL_ACTUALIZADO, pActualizado);
		values.put(TablaNegocios.COL_CELULAR_CONTACTO, pCelularContacto);
		values.put(TablaNegocios.COL_USUARIO_ID, pCodigoUsuario);
		values.put(TablaNegocios.COL_HABILITADO, (pHabilitado)? 1 : 0);
		values.put(TablaNegocios.COL_COD_DISTRITO, pCodigoDistrito);
		values.put(TablaNegocios.COL_COD_CANTON, pCodigoCanton);
		values.put(TablaNegocios.COL_COD_PROVINCIA, pCodigoProvincia);
		
		//Cambios 2015
		values.put(TablaNegocios.COL_OBSERVACIONES, pObservaciones);
		values.put(TablaNegocios.COL_FECHA_OBSERVACIONES, pFechaObservacion);
		values.put(TablaNegocios.COL_USR_MOD_OBS, pUsuarioModObsId);
		values.put(TablaNegocios.COL_EMAIL, pEmail);
		
		Cursor cursorNegocio = _database.rawQuery(_sConsultaExisteNegocio + pNegocioId, null);
		if(cursorNegocio.moveToFirst()){
			String sCondicion = TablaNegocios.COL_ID + " = " + pNegocioId;
			_database.update(TablaNegocios.NOMBRE_TABLA, values, sCondicion, null);
		}
		else{
			values.put(TablaNegocios.COL_RUTA_ID, pRutaId);
			values.put(TablaNegocios.COL_ID, pNegocioId);
			_database.insert(TablaNegocios.NOMBRE_TABLA, null, values);
		}
	}
	
	/**
	 * Obtiene la lista de negocios de un usuario actualizados en la BD para enviarlos a versionamiento.
	 * @param pCodigoUsuario
	 * @return lista de negocios que han sido modificados.
	 */
	public List<Negocio> obtenerNegociosVersionamiento(String pCodigoUsuario){
		List<Negocio> lNegocios = new ArrayList<Negocio>();
		Cursor cursorNegocios = _database.rawQuery(_sConsultaNegociosVersionamiento + pCodigoUsuario, null);
		if (cursorNegocios.moveToFirst()){	
			do {
				Negocio negocio = obtenerDatosNegocio(cursorNegocios);
				lNegocios.add(negocio);
			} while(cursorNegocios.moveToNext());
		}
		return lNegocios;
	}
	
	/**
	 * Se obtiene una lista con los nuevos negocios agregados a la BD por parte de un usuario.
	 * @return
	 */
	public List<Negocio> obtenerNuevosNegociosVersionamiento(String pCodigoUsuario){
		Cursor cursorNegocios = _database.rawQuery(_sConsultaNuevosNegociosVersionamiento + pCodigoUsuario, null);
		List<Negocio> lNegocios = new ArrayList<Negocio>();
		if (cursorNegocios.moveToFirst()){	
			do {
				Negocio negocio = obtenerDatosNegocio(cursorNegocios);
				lNegocios.add(negocio);
			} while(cursorNegocios.moveToNext());
		}
		return lNegocios;
	}
	
	/**
	 * Crea un objeto "Negocio" de un determinado cursor.
	 * @param pCursorNegocios
	 * @return
	 */
	public Negocio obtenerDatosNegocio(Cursor pCursorNegocios){
		Negocio negocio = new Negocio(
				pCursorNegocios.getInt(1), 
				pCursorNegocios.getInt(0), 
				pCursorNegocios.getString(2), 
				pCursorNegocios.getString(3), 
				pCursorNegocios.getInt(4), 
				pCursorNegocios.getString(5), 
				pCursorNegocios.getString(6), 
				pCursorNegocios.getInt(7), 
				pCursorNegocios.getString(8), 
				pCursorNegocios.getInt(9), 
				pCursorNegocios.getInt(10), 
				pCursorNegocios.getString(11), 
				pCursorNegocios.getString(12), 
				pCursorNegocios.getString(13), 
				pCursorNegocios.getString(14),
				pCursorNegocios.getInt(15),
				pCursorNegocios.getString(16),
				((pCursorNegocios.getInt(18) == 1)? true : false),
				pCursorNegocios.getInt(19),
				pCursorNegocios.getInt(20),
				pCursorNegocios.getInt(21),
				pCursorNegocios.getString(22),
				pCursorNegocios.getString(23),
				pCursorNegocios.getInt(24),
				pCursorNegocios.getString(25)
				);
		return negocio;
	}
	
	/**
	 * Coloca el atributo "Actualizado" en 0 para indicar que ya fue versionado.
	 * @param pNegocioId
	 */
	public void actualizarEstadoNegocioDespuesVersionamiento(int pNegocioId){
		ContentValues values = new ContentValues();
		values.put(TablaNegocios.COL_ACTUALIZADO, 0);
		String sCondicion = TablaNegocios.COL_ID + " = " + pNegocioId;
		_database.update(TablaNegocios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Actualiza el codigo asignado por la app por el dado en el SP. (Negocios nuevos)
	 * @param pCodigoAnterior
	 * @param pCodigoNuevo
	 */
	public void actualizarIdNegocioDespuesVersionamiento(int pCodigoAnterior, int pCodigoNuevo){
		ContentValues values = new ContentValues();
		values.put(TablaNegocios.COL_ID, pCodigoNuevo);
		String sCondicion = TablaNegocios.COL_ID + " = " + pCodigoAnterior;
		_database.update(TablaNegocios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Verifica si existe un negocio seleccionado en el buscador.
	 * @param pNegocioId
	 * @return
	 */
	public boolean existeNegocio(int pNegocioId){
		Cursor cursorNegocio = _database.rawQuery(_sConsultaBuscarNegocio + pNegocioId, null);		
		if (cursorNegocio.moveToFirst()){	
			return true;
		}
		return false;
	}
	
	/**
	 * Borra los negocios correspondientes a un determinado usuario.
	 * @param pIdNegocio = Id del negocio a borrar (-1 = todos).
	 */
	public void borrarNegociosUsuario(String pCodigoUsuario, int pIdNegocio){
		String sCondicion = TablaNegocios.COL_USUARIO_ID + " = " +  pCodigoUsuario;
		if(pIdNegocio != -1)
			sCondicion += " AND " + TablaNegocios.COL_ID + " = " + pIdNegocio;
		_database.delete(TablaNegocios.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Habilita o deshabilita un negocio.
	 */
	public void habilitarDesabilitarNegocio(int pIdNegocio, int pEstado){
		ContentValues values = new ContentValues();
		values.put(TablaNegocios.COL_HABILITADO, pEstado);
		String sCondicion = TablaNegocios.COL_ID + " = " + pIdNegocio;
		_database.update(TablaNegocios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Guarda las observaciones para un negocio.
	 * @param pObservacion
	 * @param pNegocioId
	 */
	public void agregarObservacionesRuta(String pObservacion, int pNegocioId, String pFechaModificado, String pCodigoUsusario){
		ContentValues values = new ContentValues();
		values.put(TablaNegocios.COL_OBSERVACIONES, pObservacion);
		values.put(TablaNegocios.COL_FECHA_OBSERVACIONES, pFechaModificado);
		values.put(TablaNegocios.COL_USR_MOD_OBS, pCodigoUsusario);
		values.put(TablaNegocios.COL_ACTUALIZADO, 1);
		String sCondicion = TablaNegocios.COL_ID + " = " + pNegocioId;
		_database.update(TablaNegocios.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Consulta y retorna las observaciones de un negocio.
	 * @param idNegocio
	 * @return
	 */
	public String obtenerObservaciones(int idNegocio){
		Cursor cursorNegocios = _database.rawQuery(_sConsultaObservaciones + idNegocio, null);
		if (cursorNegocios.moveToFirst()){	
			do {
				return cursorNegocios.getString(0);
			} while(cursorNegocios.moveToNext());
		}
		return "";
	}
	
	/**
	 * Obtiene la lista de negocios de un usuario desde la BD.
	 */
	public List<Negocio> obtenerListaNegociosPorUsusario(String pIdUsuario){
		List<Negocio> lNegocios = new ArrayList<Negocio>();
		Cursor cursorNegocios = _database.rawQuery(_consultaNegociosPorUsusario + pIdUsuario, null);
		Negocio negocio;
		if (cursorNegocios.moveToFirst()){
		     //Se recorre el cursor, registro por registro.
		     do {
		          int nIdNegocio = cursorNegocios.getInt(0);
		          int nIdRuta = cursorNegocios.getInt(1);
		          String sLatitud = cursorNegocios.getString(2);
		          String sLongitud = cursorNegocios.getString(3);
		          int nEstado = cursorNegocios.getInt(4);
		          String sNombreNegocio = cursorNegocios.getString(5);
		          int nActivo = cursorNegocios.getInt(7);
		          negocio = new Negocio(nIdRuta, nIdNegocio, sLatitud, sLongitud, nEstado, nActivo, sNombreNegocio);
		          lNegocios.add(negocio);
		     } while(cursorNegocios.moveToNext());
		}
		return lNegocios;
	}
}
