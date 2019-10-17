package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "Versionamiento"
 * @author eperaza
 * fecha de creacion: 17/09/13
 */
public class TablaVersionamiento implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table VERSIONAMIENTO done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table Versionamiento report.");
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
	public static final String NOMBRE_TABLA = "VERSIONAMIENTO";
	
	//Columns of the table
	public static final String COL_CODIGO_USUARIO = "VERSIONAMIENTO_CODIGO_USUARIO";
	public static final String COL_PROCESO = "VERSIONAMIENTO_VALOR";
	public static final String COL_ESTADO = "VERSIONAMIENTO_ESTADO";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_CODIGO_USUARIO + " TEXT NOT NULL, "
													+ COL_PROCESO + " INTEGER NOT NULL,"
													+ COL_ESTADO + " INTEGER NOT NULL);";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
