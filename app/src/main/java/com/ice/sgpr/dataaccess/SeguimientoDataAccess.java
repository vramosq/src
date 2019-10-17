package com.ice.sgpr.dataaccess;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaSeguimiento;
import com.ice.sgpr.entidades.Seguimiento;

/**
 * @author eperaza
 * Fecha de creacion: 08/09/2015.
 */
@SuppressLint("SimpleDateFormat")
public class SeguimientoDataAccess extends AbstractDataAccess {

	private String _consultaSeguimientos = "SELECT " + TablaSeguimiento.COL_USER_ID + ", " + TablaSeguimiento.COL_FECHA_HORA + ", " 
			+TablaSeguimiento.COL_LATITUD + ", " + TablaSeguimiento.COL_LONGITUD
			+ " FROM "+ TablaSeguimiento.NOMBRE_TABLA +" WHERE "+ TablaSeguimiento.COL_USER_ID +" = ";

	public SeguimientoDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Obtiene la lista de negocios desde la BD.
	 */
	public ArrayList<Seguimiento> obtenerListaSeguimientos(String pIdUser){
		ArrayList<Seguimiento> lSeguimientos = new ArrayList<Seguimiento>();
		Cursor cursorNegocios = _database.rawQuery(_consultaSeguimientos + pIdUser, null);
		Seguimiento seguimiento;
		if (cursorNegocios.moveToFirst()){
		     //Se recorre el cursor, registro por registro.
		     do {
				/*try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");*/
					int nIdUsuario = cursorNegocios.getInt(0);
					String dfecha = cursorNegocios.getString(1);
					String sLatitud = cursorNegocios.getString(2);
					String sLongitud = cursorNegocios.getString(3);
			          
			        seguimiento = new Seguimiento(nIdUsuario, dfecha, sLatitud, sLongitud);
			        lSeguimientos.add(seguimiento);
				/*} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
		     } while(cursorNegocios.moveToNext());
		}
		return lSeguimientos;
	}

	/**
	 * Borra los seguimientos correspondientes a un determinado usuario.
	 */
	public void borrarSeguimientosUsuario(){
		_database.delete(TablaSeguimiento.NOMBRE_TABLA, null, null);
	}
	
	/**
	 * Guarda un seguimiento que no puedo ser enviado.
	 */
	public void agregarSeguimiento(String pCodigoUsuario, String pFecha, Double pLatitud, Double pLongitud){
		ContentValues values = new ContentValues();
		values.put(TablaSeguimiento.COL_USER_ID, pCodigoUsuario);
		values.put(TablaSeguimiento.COL_FECHA_HORA, pFecha);
		values.put(TablaSeguimiento.COL_LATITUD, pLatitud);
		values.put(TablaSeguimiento.COL_LONGITUD, pLongitud);
		_database.insert(TablaSeguimiento.NOMBRE_TABLA, null, values);
	}
}
