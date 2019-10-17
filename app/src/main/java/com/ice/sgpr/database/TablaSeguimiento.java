package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "Seguimiento"
 * @author eperaza
 */
public class TablaSeguimiento implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table " + NOMBRE_TABLA + " done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table " + NOMBRE_TABLA);
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
	public static final String NOMBRE_TABLA = "SEGUIMIENTO";
	
	//Columns of the table	
	public static final String COL_ID = "SEGUIMIENTO_ID";
	public static final String COL_USER_ID = "SEG_ID_USUARIO";
	public static final String COL_FECHA_HORA = "SEG_FECHA";
	public static final String COL_LATITUD = "SEG_LATITUD";
	public static final String COL_LONGITUD = "SEG_LONGITUD";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ COL_USER_ID + " INTEGER NOT NULL, "
													+ COL_LATITUD + " TEXT NOT NULL, "
													+ COL_LONGITUD + " TEXT NOT NULL, "
													+ COL_FECHA_HORA + " TEXT NOT NULL, "
													+ "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TablaUsuarios.NOMBRE_TABLA + "(" + TablaUsuarios.COL_USUARIO + "));";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
