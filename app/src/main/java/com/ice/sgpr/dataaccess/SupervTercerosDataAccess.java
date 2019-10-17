package com.ice.sgpr.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaSupervTerceros;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Acceso a datos de las superviciones de distribuidores.
 * @author eperaza
 * fecha de creacion: 26.05.16
 */
public class SupervTercerosDataAccess extends AbstractDataAccess {
	private String _sConsultaSupervDist = "SELECT " + TablaSupervTerceros.COL_JSON_OPCIONES + " FROM "
			+ TablaSupervTerceros.NOMBRE_TABLA + " WHERE " + TablaSupervTerceros.COL_NEGOCIO_ID + " = *N AND "
			+ TablaSupervTerceros.COL_TIPO + " = '*T' AND " + TablaSupervTerceros.COL_ACTUALIZADO + " IS NULL";

	private String _sConsultaAtenciones = "SELECT " + TablaSupervTerceros.COL_JSON_OPCIONES + ", "
			+ TablaSupervTerceros.COL_NEGOCIO_ID + " FROM "
			+ TablaSupervTerceros.NOMBRE_TABLA + " WHERE " + TablaSupervTerceros.COL_CODIGO_USUARIO + " = *U AND "
			+TablaSupervTerceros.COL_TIPO + " = ";

	public SupervTercerosDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se inserta el json con las opciones de supervisión de distribuidores
	 */
	public void insertarNuevaSupervision(long pIdNegocio, String pSupervisionInfo, int pCodigoUsuario, String pTipo){
		
		borrarSupervisiones(pIdNegocio, pTipo);
		
		ContentValues values = new ContentValues();
		values.put(TablaSupervTerceros.COL_NEGOCIO_ID, pIdNegocio);
		values.put(TablaSupervTerceros.COL_JSON_OPCIONES, pSupervisionInfo);
        values.put(TablaSupervTerceros.COL_CODIGO_USUARIO, pCodigoUsuario);
		values.put(TablaSupervTerceros.COL_TIPO, pTipo);

		_database.insert(TablaSupervTerceros.NOMBRE_TABLA, null, values);
	}

	/**
	 * Retorna el json con la info de la supervición
	 * @param pNegocioId
	 * @param pTipo: Tipo a consultar, SD: Supervición distribuidores, SM: Superv. Marca
	 * @return
	 */
    public String obtenerJsonInfo(long pNegocioId, String pTipo){
		String query = _sConsultaSupervDist.replace("*N", Long.toString(pNegocioId));
		query = query.replace("*T", pTipo);
        Cursor cUsuario = _database.rawQuery(query, null);
        if(cUsuario.moveToFirst()){
            String sInfo = cUsuario.getString(0);
            return sInfo;
        }
        else
            return null;
    }
	
	/**
	 * Borra los datos de la tabla.
	 */
	public void borrarSupervisiones(long pIdNegocio, String pType)
	{
		String sCondicion = TablaSupervTerceros.COL_NEGOCIO_ID + " = " + pIdNegocio + " AND "
				+ TablaSupervTerceros.COL_TIPO + " = '" + pType + "'";
		_database.delete(TablaSupervTerceros.NOMBRE_TABLA, sCondicion, null);
	}

	/**
	 * Retorna el json con la info de la atencion
	 * @return
	 */
	public JSONArray obtenerAtencionVersionamiento(String pUserId, String pType) throws JSONException {
		String query = _sConsultaAtenciones.replace("*U", "'" + pUserId + "'");
		query += "'" + pType + "'";
		Cursor cUsuario = _database.rawQuery(query, null);
		JSONArray arrayAtenciones = new JSONArray();

		if (cUsuario.moveToFirst()){
			do {
				String sDetails = cUsuario.getString(0);
				int negocioId = cUsuario.getInt(1);
				JSONObject objectInfo = new JSONObject();
				JSONObject objectDetails = new JSONObject(sDetails);
				objectInfo.put("PV", negocioId);

				JSONArray arrayInfo = new JSONArray();
				arrayInfo.put(objectInfo);
				arrayInfo.put(objectDetails);
				arrayAtenciones.put(arrayInfo);
			} while(cUsuario.moveToNext());
		}
		else
			return null;

		return arrayAtenciones;
	}

	/**
	 * Borra los registros de un usuario
	 * @param pCodigoUsuario
	 */
	public void borrarSupervicionesUsuario(String pCodigoUsuario){
		String sCondicion = TablaSupervTerceros.COL_CODIGO_USUARIO + " = " + pCodigoUsuario;
		_database.delete(TablaSupervTerceros.NOMBRE_TABLA, sCondicion, null);
	}

	/**
	 * Actualiza el estado "versionado" de los registros asociados al tipo por usuario
	 */
	public void actualizarSupervisiones(String pUserId, String pType)
	{
		String sCondicion = TablaSupervTerceros.COL_CODIGO_USUARIO + " = " + pUserId + " AND "
				+ TablaSupervTerceros.COL_TIPO + " = '" + pType + "'";
		ContentValues values = new ContentValues();
		values.put(TablaSupervTerceros.COL_ACTUALIZADO, 1);
		_database.update(TablaSupervTerceros.NOMBRE_TABLA, values, sCondicion, null);
	}
}
