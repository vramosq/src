package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "Bitacora"
 * @author eperaza
 *
 */
public class TablaBitacora implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table BITACORA done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table report.");
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
	public static final String NOMBRE_TABLA = "BITACORA";
	
	//Columns of the table
	public static final String COL_RUTA_ID = "RUTA_ID";
	public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
	public static final String COL_FECHA_INI = "BITACORA_FECHA_INICIO";
	public static final String COL_FECHA_FIN = "BITACORA_FECHA_FIN";
	public static final String COL_ACTUALIZADA = "BITACORA_ACTUALIZADA";
	public static final String COL_LATITUD = "BITACORA_LATITUD";
	public static final String COL_LONGITUD = "BITACORA_LONGITUD";
	public static final String COL_BITACORA_ID = "BITACORA_ID";
	public static final String COL_USUARIO_ID = "USUARIO_ID";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_RUTA_ID + " INTEGER, "
													+ COL_NEGOCIO_ID + " INTEGER NOT NULL, "
													+ COL_FECHA_INI + " DATETIME,"
													+ COL_FECHA_FIN + " DATETIME,"
													+ COL_ACTUALIZADA + " INT,"
													+ COL_LATITUD + " TEXT, "
													+ COL_LONGITUD + " TEXT, "
													+ COL_BITACORA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
													+ COL_USUARIO_ID + " TEXT, "
													+ "FOREIGN KEY ("+ COL_USUARIO_ID +") REFERENCES " + TablaUsuarios.NOMBRE_TABLA +"("+TablaUsuarios.COL_USUARIO+"));";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
