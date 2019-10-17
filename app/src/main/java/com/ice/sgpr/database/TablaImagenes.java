package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TablaImagenes implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table IMAGENES done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table IMAGENES.");
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
		public static final String NOMBRE_TABLA = "IMAGENES";
		
		//Columns of the table
		public static final String COL_IMAGEN_ID = "IMAGEN_ID";
		public static final String COL_IMAGEN = "IMAGEN";
		public static final String COL_NEGOCIO_ID = "NEGOCIO_ID";
		public static final String COL_ACTUALIZADO = "IMAGEN_ACTUALIZADA";
		public static final String COL_USUARIO_ID = "USUARIO_ID";

		//Creation of the table 
		private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
														+ COL_IMAGEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
														+ COL_IMAGEN + " BLOB, "
														+ COL_NEGOCIO_ID + " INTEGER NOT NULL, "
														+ COL_ACTUALIZADO + " INTEGER, "
														+ COL_USUARIO_ID + " TEXT, "
														+ "FOREIGN KEY ("+ COL_USUARIO_ID +") REFERENCES " + TablaUsuarios.NOMBRE_TABLA +"("+TablaUsuarios.COL_USUARIO+"));";
		
		
		private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
