package com.ice.sgpr.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ice.sgpr.comun.Constantes;

/**
 * Estructura de la tabla "Usuarios Terceros" Esta es la lista de usuarios disponibles
 * para los spinner de los formularios para terceros.
 * @author eperaza
 * fecha de creacion: 08.06.16
 */
public class TablaUsuariosTerceros implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table " + NOMBRE_TABLA + " Disponibles done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table " + NOMBRE_TABLA + " Disponibles report.");
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
	public static final String NOMBRE_TABLA = "USERS_TERC";
	
	//Columns of the table
	public static final String COL_USER_ID = "USER_ID";
	public static final String COL_USER_NAME = "USER_NAME";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
                                                    + COL_USER_ID + " INTEGER,"
                                                    + COL_USER_NAME + " TEXT);";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
