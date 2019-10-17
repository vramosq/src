package com.ice.sgpr.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaPaginacionRuta;
/**
 * Acceso a datos de la Paginacion.
 * @author eperaza
 * Fecha de creacion: 17/09/2013.
 */
public class PaginacionDataAccess extends AbstractDataAccess {
	private String _sObtenerInfoPaginacion = "SELECT * FROM " + TablaPaginacionRuta.NOMBRE_TABLA + " WHERE " 
			+ TablaPaginacionRuta.COL_RUTA_ID + " = ";
	private String _sObtenerPagina = "SELECT "+ TablaPaginacionRuta.COL_PAGINA_ACTUAL +" FROM " + TablaPaginacionRuta.NOMBRE_TABLA + 
			" WHERE " + TablaPaginacionRuta.COL_RUTA_ID + " = ";
	
	public PaginacionDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se guarda el Id de la ruta actual, junton con el numero de pagina (con los negocios) que se esta cargando.
	 * @param pPagina
	 */
	public void actualizarPaginacion(int pRutaId, int pPagina, String pCodigoUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaPaginacionRuta.COL_RUTA_ID, pRutaId);
		values.put(TablaPaginacionRuta.COL_PAGINA_ACTUAL, pPagina);
		values.put(TablaPaginacionRuta.COL_CODIGO_USUARIO, pCodigoUsuario);
		
		Cursor cPaginacion = _database.rawQuery(_sObtenerInfoPaginacion + pRutaId, null);
		if(cPaginacion.moveToFirst()){
			String sCondicion = TablaPaginacionRuta.COL_RUTA_ID + " = " + pRutaId;
			_database.update(TablaPaginacionRuta.NOMBRE_TABLA, values, sCondicion, null);
		}
		else
			_database.insert(TablaPaginacionRuta.NOMBRE_TABLA, null, values);
	}
	
	/**
	 * Obtiene la p�gina por la que ha cargado rutas.
	 * @param pRutaId
	 * @return
	 */
	public int obtenerPaginaActual(int pRutaId){
		Cursor cPaginaActual = _database.rawQuery(_sObtenerPagina + pRutaId, null);
		if(cPaginaActual.moveToFirst())
			return cPaginaActual.getInt(0);
		return 0;
	}
	
	/**
	 * Borra la tabla de paginaci�n para un usuario.
	 * @param pCodigoUsuario
	 */
	public void borrarPaginacionUsuario(String pCodigoUsuario){
		String sCondicion = TablaPaginacionRuta.COL_CODIGO_USUARIO + " = " + pCodigoUsuario;
		_database.delete(TablaPaginacionRuta.NOMBRE_TABLA, sCondicion, null);
	}
}
