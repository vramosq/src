package com.ice.sgpr.dataaccess;

import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaDistritos;
import com.ice.sgpr.entidades.Parametro;
/**
 * Acceso a datos de cantones.
 * @author eperaza
 * Fecha de creaci�n: 09/07/2014.
 */
public class DistritosDataAccess extends AbstractDataAccess {
	private String _sConsultaDistritos = "SELECT " + TablaDistritos.COL_CODIGO_DISTRITO + ", " + TablaDistritos.COL_NOMBRE_DISTRITO + " FROM " + 
			TablaDistritos.NOMBRE_TABLA + " WHERE " + TablaDistritos.COL_CODIGO_CANTON + " = ";
	
	private String _sConsultaCanton = "SELECT " + TablaDistritos.COL_CODIGO_CANTON  + " FROM " + 
			TablaDistritos.NOMBRE_TABLA + " WHERE " + TablaDistritos.COL_CODIGO_DISTRITO + " = ";
	
	public DistritosDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Arma una lista con los distritos, según el cantón seleccionado
	 * @return Lista de distritos.
	 */
	public List<Parametro> obtenerDistritos(int pCodigoCanton){
		List<Parametro> lParametros = new LinkedList<Parametro>();
		
		Cursor cursorParametros = _database.rawQuery(_sConsultaDistritos + pCodigoCanton, null);
		Parametro parametro;
		if (cursorParametros.moveToFirst()){
		     //Se recorre el cursor, registro por registro.
		     do {
		          int nCodigo = cursorParametros.getInt(0);
		          String sNombre = cursorParametros.getString(1);
		          
		          parametro = new Parametro(nCodigo, sNombre, "");
		          lParametros.add(parametro);
		     } while(cursorParametros.moveToNext());
		}
		return lParametros;
	}
	
	/**
	 * Se obtiene el codigo de canton de un distrito.
	 * @param pCodigoDistrito
	 * @return
	 */
	public int obtenerCodigoCanton(int pCodigoDistrito){
		Cursor cursorParametros = _database.rawQuery(_sConsultaCanton + pCodigoDistrito, null);
		int nCodigo = 0;
		if (cursorParametros.moveToFirst()){
			 nCodigo = cursorParametros.getInt(0);
		}
		return nCodigo;		
	}
}
