package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TablaPreguntasAuditor implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table PREGUNTAS AUDIT done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table PREGUNTAS AUDIT.");
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
		public static final String NOMBRE_TABLA = "PREGUNTAS_AUDIT";
		
		//Columns of the table
		public static final String COL_PREGUNTA_ID = "PREGUNTA_ID";
		public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
		public static final String COL_RESPUESTA_1 = "RESPUESTA_1";
		public static final String COL_RESPUESTA_2 = "RESPUESTA_2";
		public static final String COL_RESPUESTA_3 = "RESPUESTA_3";
		public static final String COL_CODIGO_PREGUNTA = "CODIGO_PREGUNTA";
		public static final String COL_CODIGO_USUARIO = "COD_USUARIO";
		public static final String COL_ACTUALIZADO = "ACTUALIZADO";

		//Creation of the table 
		private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
														+ COL_PREGUNTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
														+ COL_NEGOCIO_ID + " INTEGER NOT NULL,"
														+ COL_RESPUESTA_1 + " TEXT NOT NULL,"
														+ COL_RESPUESTA_2 + " TEXT,"
														+ COL_RESPUESTA_3 + " TEXT,"
														+ COL_CODIGO_PREGUNTA + " TEXT,"
														+ COL_CODIGO_USUARIO + " INTEGER, "
														+ COL_ACTUALIZADO + " INTEGER);";
		
		
		private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
