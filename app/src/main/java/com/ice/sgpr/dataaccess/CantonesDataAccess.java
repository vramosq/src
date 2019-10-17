package com.ice.sgpr.dataaccess;

import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaCantones;
import com.ice.sgpr.entidades.Parametro;
/**
 * Acceso a datos de cantones.
 * @author eperaza
 * Fecha de creación: 09/07/2014.
 */
public class CantonesDataAccess extends AbstractDataAccess {
	private String _sConsultaCantones = "SELECT " + TablaCantones.COL_CODIGO_CANTON + ", " + TablaCantones.COL_NOMBRE_CANTON + " FROM " + 
			TablaCantones.NOMBRE_TABLA + " WHERE " + TablaCantones.COL_CODIGO_PROVINCIA + " = ";
	
	private String _sConsultaProvincia = "SELECT " + TablaCantones.COL_CODIGO_PROVINCIA + " FROM " + 
			TablaCantones.NOMBRE_TABLA + " WHERE " + TablaCantones.COL_CODIGO_CANTON + " = ";
	
	public CantonesDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Arma una lista con los cantones, seg�n la provincia seleccionada
	 * @return Lista de cantones.
	 */
	public List<Parametro> obtenerCantones(int pCodigoProvincia){
		List<Parametro> lParametros = new LinkedList<Parametro>();
		
		Cursor cursorParametros = _database.rawQuery(_sConsultaCantones + pCodigoProvincia, null);
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
	 * Obtiene el c�digo de provincia de un canton.
	 * @param pCodigoCanton
	 * @return
	 */
	public int obtenerCodigoProvincia(int pCodigoCanton){
		Cursor cursorParametros = _database.rawQuery(_sConsultaProvincia + pCodigoCanton, null);
		int nCodigo = 0;
		if (cursorParametros.moveToFirst()){
			 nCodigo = cursorParametros.getInt(0);
		}
		return nCodigo;
	}
}
