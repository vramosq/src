package com.ice.sgpr.database;

import com.ice.sgpr.comun.Constantes;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Estructura de la tabla "Negocios"
 * @author eperaza
 */
public class TablaNegocios implements IDatabaseTable{

	@Override
	public void onCreate(SQLiteDatabase pDatabase) 
	{
		try 
		{
		   pDatabase.execSQL(SQL_CREATE_TABLE);
	       Log.i(Constantes.LOG_TAG, "Create Table NEGOCIOS done...");

		}
		catch (Exception e) 
		{
			Log.e(Constantes.LOG_TAG, "Error create table NEGOCIOS");
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
	public static final String NOMBRE_TABLA = "NEGOCIOS";
	
	//Columns of the table	
	public static final String COL_ID = "NEGOCIO_ID";
	public static final String COL_NOMBRE = "NEGOCIO_NOMBRE";
	public static final String COL_DESCRIPCION = "NEGOCIO_DESCRIPCION";
	public static final String COL_TIPO_NEGOCIO = "NEGOCIO_TIPO_COMERCIO";
	public static final String COL_PRIORIDAD = "NEGOCIO_PRIORIDAD";
	public static final String COL_TELEFONO = "NEGOCIO_TELEFONO";
	public static final String COL_NOMBRE_CONTACTO = "NEGOCIO_NOMBRE_CONTACTO";
	public static final String COL_TELEFONO_CONTACTO = "NEGOCIO_TEL_CONTACTO";
	public static final String COL_ULTIMA_VISITA = "NEGOCIO_ULT_VISITA";	
	public static final String COL_DIRECCION = "NEGOCIO_DIR";
	public static final String COL_LATITUD = "NEGOCIO_LAT";
	public static final String COL_LONGITUD = "NEGOCIO_LONG";	
	public static final String COL_RUTA_ID = "RUTA_ID";
	public static final String COL_ESTADO = "NEGOCIO_ESTADO";
	public static final String COL_NEGOCIO_ACTIVO = "NEGOCIO_ACTIVO";
	public static final String COL_ACTUALIZADO = "NEGOCIO_ACTUALIZADO";
	public static final String COL_CELULAR_CONTACTO = "NEGOCIO_CELULAR_CONTACTO";
	public static final String COL_USUARIO_ID = "USUARIO_ID";
	public static final String COL_HABILITADO = "HABILITADO";
	public static final String COL_COD_DISTRITO = "DISTRITO";
	public static final String COL_COD_CANTON = "CANTON";
	public static final String COL_COD_PROVINCIA = "PROVINCIA";
	public static final String COL_OBSERVACIONES = "OBSERVACIONES";
	public static final String COL_FECHA_OBSERVACIONES = "FECHA_OBSERVACIONES";
	public static final String COL_USR_MOD_OBS = "USR_MODIF_OBS";
	public static final String COL_EMAIL = "EMAIL";

	//Creation of the table 
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NOMBRE_TABLA + "( "
													+ COL_ID + " INTEGER PRIMARY KEY, "
													+ COL_RUTA_ID + " INTEGER NOT NULL, "
													+ COL_LATITUD + " TEXT NOT NULL, "
													+ COL_LONGITUD + " TEXT NOT NULL, "
													+ COL_ESTADO + " INTEGER NOT NULL,"
													+ COL_NOMBRE+ " TEXT NOT NULL, "
													+ COL_DIRECCION + " TEXT NOT NULL,"
													+ COL_NEGOCIO_ACTIVO + " INTEGER, "
													+ COL_DESCRIPCION + " TEXT, "
													+ COL_TIPO_NEGOCIO + " INTEGER, "
													+ COL_PRIORIDAD + " INTEGER, "
													+ COL_TELEFONO + " TEXT, "
													+ COL_NOMBRE_CONTACTO + " TEXT, "
													+ COL_TELEFONO_CONTACTO + " TEXT, "
													+ COL_ULTIMA_VISITA + " TEXT, "
													+ COL_ACTUALIZADO + " INTEGER, "
													+ COL_CELULAR_CONTACTO + " TEXT, "
													+ COL_USUARIO_ID + " TEXT, "
													+ COL_HABILITADO + " INTEGER, "
													+ COL_COD_DISTRITO + " INTEGER NOT NULL, "
													+ COL_COD_CANTON + " INTEGER NOT NULL, "
													+ COL_COD_PROVINCIA + " INTEGER NOT NULL, "
													+ COL_OBSERVACIONES + " TEXT, "
													+ COL_FECHA_OBSERVACIONES + " DATETIME, "
													+ COL_USR_MOD_OBS + " INTEGER, "
													+ COL_EMAIL + " TEXT, "
													+ "FOREIGN KEY ("+ COL_USUARIO_ID +") REFERENCES " + TablaUsuarios.NOMBRE_TABLA +"("+TablaUsuarios.COL_USUARIO+"));";
	
	
	private static final String SQL_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";";
}
