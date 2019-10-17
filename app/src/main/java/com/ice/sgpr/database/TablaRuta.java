package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "RUTAS"
 * @author eperaza
 *
 */
public class TablaRuta implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table RUTAS done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table RUTAS.");
			e.printStackTrace();
		}	
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion) 
	{
		pDatabase.execSQL(SQL_UPGRADE_TABLE);
		onCreate(pDatabase);
	}
	
	
	//Table Name
	public static final String NOMBRE_TABLA = "RUTAS";
	
	//Columns of the table
	public static final String COL_RUTA_ID = "RUTA_ID";
	public static final String COL_RUTA_NOMBRE = "RUTA_NOMBRE";
	public static final String COL_RUTA_DESC = "RUTA_DESC";
	public static final String COL_RUTA_FECHA = "RUTA_FECHA";
	public static final String COL_RUTA_FREC = "RUTA_FREC";
	public static final String COL_RUTA_PEND = "RUTA_PEND";
	public static final String COL_CODIGO_USUARIO = "DATOS_CODIGO_USUARIO";
	public static final String COL_RUTA_LATITUD = "RUTA_LATITUD";
	public static final String COL_RUTA_LONGITUD = "RUTA_LONGITUD";
	public static final String COL_RUTA_SELECCIONADA = "RUTA_SELECCIONADA";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_RUTA_ID + " INTEGER PRIMARY KEY, "
													+ COL_RUTA_NOMBRE + " TEXT NOT NULL, "
													+ COL_RUTA_DESC + " TEXT NOT NULL, "
													+ COL_RUTA_FECHA + " DATE NOT NULL,"
													+ COL_RUTA_FREC + " TEXT NOT NULL, "
													+ COL_RUTA_PEND + " INTEGER NOT NULL, "
													+ COL_CODIGO_USUARIO + " TEXT NOT NULL, "
													+ COL_RUTA_LATITUD + " TEXT, "
													+ COL_RUTA_LONGITUD + " TEXT, "
													+ COL_RUTA_SELECCIONADA + " INTEGER);";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
