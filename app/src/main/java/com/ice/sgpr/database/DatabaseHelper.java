package com.ice.sgpr.database;

import java.util.ArrayList;
import java.util.List;

import com.ice.sgpr.SgprApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Custom implementation of the SQLOpenHelper to handle the data access
 * @author mgomez
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * Tables of the database
	 */
	private List<IDatabaseTable> _tables;
	/**
	 * Unique instance of the class (Singleton)
	 */
	private static DatabaseHelper _instance;
	
	/***
	 * Private Class constructor (Singleton)
	 * @param pContext Context of the application
	 */
	public DatabaseHelper(Context pContext) 
	{
		super(pContext, DataConstants.DATABASE_NAME, null, DataConstants.DATABASE_VERSION);
		
		_tables = new ArrayList<IDatabaseTable>();
		
		//Add the required tables to the list
		_tables.add(new TablaRuta());
		_tables.add(new TablaNegocios());
		_tables.add(new TablaBitacora());
		_tables.add(new TablaPreguntas());
		_tables.add(new TablaPreguntasAuditor());
		_tables.add(new TablaImagenes());
		_tables.add(new TablaParametros());
		_tables.add(new TablaPaginacionRuta());
		_tables.add(new TablaVersionamiento());
		_tables.add(new TablaUsuarios());
		_tables.add(new TablaPreguntasPresenciaMarca());
		_tables.add(new TablaCantones());
		_tables.add(new TablaDistritos());
		_tables.add(new TablaSeguimiento());
		_tables.add(new TablaAtencion());
		_tables.add(new TablaSupervTerceros());
		_tables.add(new TablaUsuariosTerceros());
		_tables.add(new TablaTiposSupervision());
	}
	
	/**
	 * Called when the database is created. 
	 */
	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		
		//Create the database tables
		for(IDatabaseTable table : _tables)
		{
			table.onCreate(pDatabase);
		}
		
		//Create additional structures via SQL
	}

	/**
	 * Called when the database is upgraded. 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion) 
	{
		//Update each of the tables
		for(IDatabaseTable table : _tables)
		{
			table.onUpgrade(pDatabase, pOldVersion, pNewVersion);
		}
	}
	
	/**
	 * Returns the unique instance of the class
	 * @return Unique instance of DatabaseHelper
	 */
	public static DatabaseHelper getInstance()
	{
		if (_instance == null)
			_instance = new DatabaseHelper(SgprApplication.getContext());
		return _instance;
	}
}
