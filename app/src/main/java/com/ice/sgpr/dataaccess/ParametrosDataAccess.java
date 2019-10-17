package com.ice.sgpr.dataaccess;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaParametros;
import com.ice.sgpr.entidades.Parametro;
/**
 * Acceso a datos de parametros.
 * @author eperaza
 * Fecha de creacion: 23/09/2013.
 */
public class ParametrosDataAccess extends AbstractDataAccess {
	private String _sTipoComercio = "'TCOMER'";
	private String _sPrioridad = "'PRI'";
	private String _sOperador = "'OPER'";
	private String _sTipoIncidencia = "'T_INCI'";
	private String _sSocios = "'SOC'";

	private String _sConsultaTiposComercio = "SELECT " + TablaParametros.COL_VALOR + ", " + TablaParametros.COL_CODIGO + " FROM " + 
			TablaParametros.NOMBRE_TABLA + " WHERE " + TablaParametros.COL_CATEGORIA + " = C* ORDER BY " + TablaParametros.COL_VALOR + " ASC";
	
	public ParametrosDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se insertan un los parametros en la tabla.
	 */
	public void insertarParametro(List<Parametro> pParametros){
		for(Parametro paramActual:pParametros){
			ContentValues values = new ContentValues();
			values.put(TablaParametros.COL_CODIGO, paramActual.getCodigo());
			values.put(TablaParametros.COL_VALOR, paramActual.getValor());
			values.put(TablaParametros.COL_CATEGORIA, paramActual.getCategoria());
			_database.insert(TablaParametros.NOMBRE_TABLA, null, values);
		}
	}
	
	/**
	 * Arma una lista con los parametros, segun el tipo de parametro a cargar
	 * @param pCategoria: 0 Para los parametros de Tipo de Comercio, 1 para los de Prioridad,
	 * 2 para operadores, 3 para tipos de incidencia y 4 para socios.
	 * @return Lista de parametros.
	 */
	public List<Parametro> obtenerParametros(int pCategoria){
		List<Parametro> lParametros = new LinkedList<Parametro>();
		String sCategoriaSeleccionada = "";

		switch (pCategoria){
			case 0 :
				sCategoriaSeleccionada = _sTipoComercio;
				break;
			case 1:
				sCategoriaSeleccionada = _sPrioridad;
				break;
			case 2:
				sCategoriaSeleccionada = _sOperador;
				break;
			case 3:
				sCategoriaSeleccionada = _sTipoIncidencia;
				break;
			case 4:
				sCategoriaSeleccionada = _sSocios;
				break;
		}

		String query = _sConsultaTiposComercio.replace("C*", sCategoriaSeleccionada);
		
		Cursor cursorParametros = _database.rawQuery(query, null);
		Parametro parametro;
		if (cursorParametros.moveToFirst()){
		     //Se recorre el cursor, registro por registro.
		     do {
		          String sValor = cursorParametros.getString(0);
		          int nCodigo = cursorParametros.getInt(1);
		          
		          parametro = new Parametro(nCodigo, sValor, sCategoriaSeleccionada);
		          lParametros.add(parametro);
		     } while(cursorParametros.moveToNext());
		}
		return lParametros;
	}
	
	/**
	 * Borra los datos de la tabla "Parametros".
	 */
	public void borrarParametros() 
	{
		_database.delete(TablaParametros.NOMBRE_TABLA, null, null);
	}
}
