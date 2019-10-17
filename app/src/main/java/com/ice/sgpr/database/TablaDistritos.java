package com.ice.sgpr.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TablaDistritos implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		try 
		{
			pDatabase.execSQL(SQL_CREATE_TABLE);
			Log.i(Constantes.LOG_TAG, "Create Table "+ NOMBRE_TABLA +" done...");
			
			try 
			{
				InputStream is = SgprApplication.getContext().getAssets().open("data/distritos.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName(Constantes.DEFAULT_ENCODING)));
				String line;
				String[] data;
				reader.readLine();
		        while ((line = reader.readLine()) != null) 
		        {
		             data = line.split("[,]");	 
		             pDatabase.execSQL(SQL_INSERT, new Object[]{data[0], data[1], data[2]});
		        }
		        
		        is.close();
		        
		        Log.i(Constantes.LOG_TAG, "Done loading "+ NOMBRE_TABLA +" table data.");
			}
			catch (Exception e) 
			{
				Log.e(Constantes.LOG_TAG, "Error create table "+ NOMBRE_TABLA +".");
				e.printStackTrace();
			}

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table "+ NOMBRE_TABLA +".");
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
		public static final String NOMBRE_TABLA = "DISTRITOS";
		
		//Columns of the table
		public static final String COL_CODIGO_DISTRITO = "DISTRITO_ID";
		public static final String COL_CODIGO_CANTON = "CANTON_ID";
		public static final String COL_NOMBRE_DISTRITO = "NOMBRE";

		//Creation of the table 
		private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
														+ COL_CODIGO_DISTRITO + " INTEGER NOT NULL, "
														+ COL_CODIGO_CANTON + " INTEGER NOT NULL,"
														+ COL_NOMBRE_DISTRITO + " TEXT NOT NULL"
														+ ");";
		
		
		private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
		
	// Inserts
	private static final String SQL_INSERT = "INSERT INTO " + NOMBRE_TABLA
			+ " ( " + COL_CODIGO_DISTRITO + " , " + COL_CODIGO_CANTON + " , "
			+ COL_NOMBRE_DISTRITO + " ) VALUES (?,?,?)";
}	