package com.ice.sgpr.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Represents a table within an SQLLite Database
 * @author keylorchaves
 *
 */
public interface IDatabaseTable {
	/**
	 * Method to be executed when the database is created. Should create the table
	 * with all the constraints 
	 * @param pDatabase SQLLite Databse
	 */
	public void onCreate(SQLiteDatabase pDatabase);
	
	/**
	 * Method to be executed when the database is updated. Should update the table to a new version (if necessary)
	 * @param pDatabase SQLLite Database
	 * @param pOldVersion Old version of the database
	 * @param pNewVersion New version of the database
	 */
	public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion);
}
