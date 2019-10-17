package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "USUARIOS"
 * @author eperaza
 *
 */
public class TablaUsuarios implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table USUARIOS done...");

		}
		catch (Exception e)
		{
			Log.e(Constantes.LOG_TAG, "Error create table USUARIOS.");
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
	public static final String NOMBRE_TABLA = "USUARIOS";
	
	//Columnas
	public static final String COL_USUARIO = "USUARIOS_CODIGO_USUARIO";
	public static final String COL_NOMBRE_USUARIO = "USUARIOS_NOMBRE_USUARIO";
	public static final String COL_CONTRASENA_USUARIO = "USUARIOS_CONTRASENA_USUARIO";
	public static final String COL_ROL = "USUARIOS_ROL";
	public static final String COL_ACTIVO = "USUARIOS_ACTIVO";
	public static final String COL_FECHA_SINC = "USUARIOS_FECHA_SINC";
	public static final String COL_ACEPTA_SEGUIMIENTO = "ACEPTA_SEGUIMIENTO";

	//Creacion de la tabla
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_USUARIO + " TEXT PRIMARY KEY, "
													+ COL_NOMBRE_USUARIO + " TEXT, "
													+ COL_CONTRASENA_USUARIO + " INTEGER, "
													+ COL_ROL + " TEXT, "
													+ COL_ACTIVO + " INTEGER, "
													+ COL_FECHA_SINC + " TEXT,"
													+ COL_ACEPTA_SEGUIMIENTO + " INTEGER"
													+ ");";
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
