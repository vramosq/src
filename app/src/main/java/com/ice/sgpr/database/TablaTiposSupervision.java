package com.ice.sgpr.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ice.sgpr.comun.Constantes;

/**
 * Estructura de la tabla "Tipos Supervision" (TERCEROS)
 * @author eperaza
 * fecha de creacion: 04.07.16
 */
public class TablaTiposSupervision implements IDatabaseTable{

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
			Log.e(Constantes.LOG_TAG, "Error create table " + NOMBRE_TABLA + " report.");
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
	public static final String NOMBRE_TABLA = "USUARIOS";
	
	//Columns of the table
	public static final String COL_SUPERVISION_ID = "USUARIO_ID";
	public static final String COL_NOMBRE = "NOMBRE";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_SUPERVISION_ID + " INTEGER, "
													+ COL_NOMBRE + " TEXT);";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
