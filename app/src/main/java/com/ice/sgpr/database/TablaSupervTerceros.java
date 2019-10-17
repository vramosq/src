package com.ice.sgpr.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ice.sgpr.comun.Constantes;

/**
 * Estructura de la tabla "Productos disponibles" (TERCEROS)
 * @author eperaza
 * fecha de creacion: 19.05.16
 */
public class TablaSupervTerceros implements IDatabaseTable{

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
			Log.e(Constantes.LOG_TAG, "Error create table  " + NOMBRE_TABLA + " Disponibles report.");
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
	public static final String NOMBRE_TABLA = "SUPERV_TERC";
	
	//Columns of the table
	public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
	public static final String COL_JSON_OPCIONES = "JSON_OPCIONES";
	public static final String COL_ACTUALIZADO = "ACTUALIZADO";
    public static final String COL_CODIGO_USUARIO = "COD_USUARIO";
	public static final String COL_TIPO = "TIPO";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
                                                    + COL_NEGOCIO_ID + " INTEGER,"
                                                    + COL_ACTUALIZADO + " INTEGER,"
                                                    + COL_CODIGO_USUARIO + " INTEGER, "
													+ COL_TIPO + " TEXT, "
													+ COL_JSON_OPCIONES + " TEXT);";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";

}
