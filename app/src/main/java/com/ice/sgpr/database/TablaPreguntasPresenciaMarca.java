package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TablaPreguntasPresenciaMarca implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table PREGUNTAS PRESENCIA MARCA done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table PREGUNTAS PRESENCIA MARCA.");
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion,
			int pNewVersion) {
		pDatabase.execSQL(SQL_UPGRADE_TABLE);
		onCreate(pDatabase);
	}
		//Table Name
		public static final String NOMBRE_TABLA = "PRESENCIA_MARCA";
		
		//Columns of the table
		public static final String COL_PREGUNTA_PM_ID = "PREGUNTA_PM_ID";
		public static final String COL_CODIGO_USUARIO = "COD_USUARIO";
		public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
		public static final String COL_NUM_PREGUNTA = "NUM_PREGUNTA";
		public static final String COL_CODIGO_PREGUNTA = "COD_PREGUNTA";
		public static final String COL_NUM_RESPUESTA = "NUM_RESPUESTA";
		public static final String COL_CODIGO_RESPUESTA = "COD_RESPUESTA";
		public static final String COL_ACTUALIZADO = "ACTUALIZADO";

		//Creation of the table 
		private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
														+ COL_PREGUNTA_PM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
														+ COL_CODIGO_USUARIO + " INTEGER NOT NULL, "
														+ COL_NEGOCIO_ID + " INTEGER NOT NULL,"
														+ COL_NUM_PREGUNTA + " INTEGER NOT NULL, "
														+ COL_CODIGO_PREGUNTA + " INTEGER, "
														+ COL_NUM_RESPUESTA + " INTEGER, "
														+ COL_CODIGO_RESPUESTA + " TEXT, "
														+ COL_ACTUALIZADO + " INTEGER);";
		
		
		private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}	