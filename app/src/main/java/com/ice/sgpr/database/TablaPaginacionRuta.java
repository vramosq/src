package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "DATOS"
 * @author eperaza
 * fecha de creacion 17/09/13
 */
public class TablaPaginacionRuta implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table PAGINACION done...");

		}
		catch (Exception e)
		{
			Log.e(Constantes.LOG_TAG, "Error create table PAGINACION.");
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion)
	{
		pDatabase.execSQL(SQL_UPGRADE_TABLE);
		onCreate(pDatabase);
	}
	
	//Nombre de la tabla
	public static final String NOMBRE_TABLA = "PAGINACION";
	
	//Columnas
	public static final String COL_RUTA_ID = "RUTA_ID";
	public static final String COL_PAGINA_ACTUAL = "PAGINACION_PAG_ACTUAL";
	public static final String COL_CODIGO_USUARIO = "USUARIO_CODIGO";

	//Creaci�n de la tabla
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_RUTA_ID + " INTEGER PRIMARY KEY NOT NULL, "
													+ COL_PAGINA_ACTUAL + " INTEGER NOT NULL, "
													+ COL_CODIGO_USUARIO + " TEXT NOT NULL, "
													+ "FOREIGN KEY ("+ COL_RUTA_ID +") REFERENCES " + TablaRuta.NOMBRE_TABLA +"("+TablaRuta.COL_RUTA_ID+"));";
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
