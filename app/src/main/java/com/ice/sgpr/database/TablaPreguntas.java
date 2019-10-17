package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TablaPreguntas implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table PREGUNTAS done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table PREGUNTAS.");
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
		public static final String NOMBRE_TABLA = "PREGUNTAS";
		
		//Columns of the table
		public static final String COL_PREGUNTA_ID = "PREGUNTA_ID";
		public static final String COL_PREGUNTA_NUMERO = "PREGUNTA_NUMERO";
		public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
		public static final String COL_RESPUESTA_1 = "RESPUESTA_1";
		public static final String COL_RESPUESTA_2 = "RESPUESTA_2";
		public static final String COL_VALOR = "VALOR";
		public static final String COL_OPCION_VALOR = "OPCION_VALOR";
		public static final String COL_ANIDADA_CON_VALOR = "ANIDADA_CON_VALOR";
		public static final String COL_CODIGO_PREGUNTA_PRINCIPAL = "CODIGO_PREGUNTA_PRINC";
		public static final String COL_CODIGO_PREGUNTA_SECUNDARIA = "CODIGO_PREGUNTA_SEC";
		public static final String COL_CODIGO_PREGUNTA_DEPENDENCIA = "DEPENCENCIA";
		public static final String COL_CODIGO_RESPUESTA1 = "COD_RESPUESTA1";
		public static final String COL_CODIGO_RESPUESTA2 = "COD_RESPUESTA2";
		public static final String COL_CODIGO_RESPUESTA_VALOR = "COD_RESPUESTA_VALOR";
		public static final String COL_CODIGO_USUARIO = "COD_USUARIO";
		public static final String COL_ACTUALIZADO = "ACTUALIZADO";

		//Creation of the table 
		private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
														+ COL_PREGUNTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
														+ COL_PREGUNTA_NUMERO + " INTEGER, "
														+ COL_NEGOCIO_ID + " INTEGER NOT NULL,"
														+ COL_RESPUESTA_1 + " TEXT NOT NULL,"
														+ COL_RESPUESTA_2 + " TEXT,"
														+ COL_VALOR + " TEXT,"
														+ COL_OPCION_VALOR + " INTEGER, "
														+ COL_ANIDADA_CON_VALOR + " INTEGER, "
														+ COL_CODIGO_PREGUNTA_PRINCIPAL + " INTEGER NOT NULL, "
														+ COL_CODIGO_PREGUNTA_SECUNDARIA + " INTEGER, "
														+ COL_CODIGO_PREGUNTA_DEPENDENCIA + " INTEGER, "
														+ COL_CODIGO_RESPUESTA1 + " TEXT, "
														+ COL_CODIGO_RESPUESTA2 + " TEXT, "
														+ COL_CODIGO_RESPUESTA_VALOR + " TEXT, "
														+ COL_CODIGO_USUARIO + " INTEGER, "
														+ COL_ACTUALIZADO + " INTEGER);";
		
		
		private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
