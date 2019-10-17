package com.ice.sgpr.dataaccess;

import com.ice.sgpr.database.DatabaseHelper;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Abstract Data Access Object. All the specific data access objects should inherit this class. 
 * @author mgomez
 *
 */
public abstract class AbstractDataAccess {

	/**
	 * Database object
	 */
	protected SQLiteDatabase _database;
	/**
	 * Database helper
	 */
	protected DatabaseHelper _helper = DatabaseHelper.getInstance();
	
	/**
	 * Opens a database object of reading only. 
	 * @throws android.database.SQLException
	 */
	public void openForReading() throws SQLException
	{
		_database = _helper.getReadableDatabase();
	}
	
	/**
	 * Opens a database object for writing. 
	 * @throws android.database.SQLException
	 */
	public void openForWriting() throws SQLException
	{
		_database = _helper.getWritableDatabase();
	}
	
	/**
	 * Begins a transaction
	 */
	public void beginTransaction()
	{
		_database.beginTransaction();
	}
	
	/**
	 * Commits a transaction 
	 */
	public void commitTransaction()
	{
		_database.setTransactionSuccessful();
	}
	
	/**
	 * Ends a transaction (SHOULD ALWAYS BE CALLED if beginTransaction was called). It 
	 * will rollback the transaction if commitTransaction was not called 
	 */
	public void endTransaction()
	{
		_database.endTransaction();
	}
	
	
	/**
	 * Closes the database object. 
	 */
	public void close()
	{
		_helper.close();
	}
}
