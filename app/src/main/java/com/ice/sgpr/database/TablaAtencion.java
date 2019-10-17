package com.ice.sgpr.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ice.sgpr.comun.Constantes;

/**
 * Estructura de la tabla "Atención" (TERCEROS)
 * @author eperaza
 * fecha de creacion: 18.05.16
 */
public class TablaAtencion implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table Atencion done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table Atencion report.");
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
	public static final String NOMBRE_TABLA = "ATENCION";
	
	//Columns of the table
	public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
	public static final String COL_FECHA = "FECHA";
	public static final String COL_HORA_INICIO = "H_INICIO";
	public static final String COL_HORA_FIN = "H_FIN";
	public static final String COL_SUPERVISA = "SUPERVISA";
	public static final String COL_PENDIENTE = "PENDIENTE";
	public static final String COL_ASIGNAR_A = "ASIGNAR_A";
	public static final String COL_OBSERVACIONES = "OBSERVACIONES";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_NEGOCIO_ID + " INTEGER, "
													+ COL_FECHA + " TEXT, "
													+ COL_HORA_INICIO + " TEXT,"
													+ COL_HORA_FIN + " TEXT,"
													+ COL_SUPERVISA + " INTEGER,"
													+ COL_PENDIENTE + " INTEGER,"
													+ COL_ASIGNAR_A + " INTEGER,"
													+ COL_OBSERVACIONES + " TEXT);";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
