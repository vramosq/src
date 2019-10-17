package com.ice.sgpr.implementor;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ice.sgpr.dataaccess.SeguimientoDataAccess;
import com.ice.sgpr.entidades.Seguimiento;
import com.ice.sgpr.service.JSONHelper;
/**
 * Implementor para la consulta de datos relacionados con Negocios.
 * @author eperaza
 * @since 08/09/2015.
 */
public class SeguimientoImplementor {
private SeguimientoDataAccess _dataAccess;
private static SeguimientoImplementor _instancia;
	
	private SeguimientoImplementor()
	{
		_dataAccess = new SeguimientoDataAccess();
	}
	
	public static SeguimientoImplementor getInstance(){
		if(_instancia == null)
			_instancia = new SeguimientoImplementor();
		return _instancia;
	}
	
	/**
	 * Se envia el ID de la ruta para obtener la lista de seguimientos.
	 * @return
	 */
	public ArrayList<Seguimiento> obtenerListaSeguimiento(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		ArrayList<Seguimiento> lSeguimientos = _dataAccess.obtenerListaSeguimientos(sCodigoUsuario);
		_dataAccess.close();
		return lSeguimientos;
	}
	
	/**
	 * Se obtiene la informacion del negocio activo.
	 * @return: Negocio seleccionado en el mapa.
	 */
	public void borrarSeguimientosUsuario(){
		_dataAccess.openForReading();
		_dataAccess.borrarSeguimientosUsuario();
		_dataAccess.close();
	}
	
	/**
	 * Se agrega un seguimiento
	 */
	public void agregarSeguimiento(String pFecha, Double pLatitud, Double pLongitud){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.agregarSeguimiento(sCodigoUsuario, pFecha, pLatitud, pLongitud);
		_dataAccess.close();
	}
	
	/**
	 * Obtiene la lista de seguimientos en un objeto json
	 * @return
	 */
	public JSONObject obtenerSeguimientosParaEnviar(){
		JSONArray arraySeguimiento = new JSONArray();
		
		ArrayList<Seguimiento> listaSeguimientos = obtenerListaSeguimiento();
		for(Seguimiento seg : listaSeguimientos) {
		    	 try {
		    		 JSONObject jsonDatosFormulario = JSONHelper.getInstance().obtenerJsonInformacionSeguimiento(Integer.toString(seg.getIdUsuario()), seg.getFecha(), seg.get_sLatitud(), seg.get_sLongitud());
		    		 arraySeguimiento.put(jsonDatosFormulario);
		    		 //actualizarEstadoActualizadoPreguntaPM(mCursor.getInt(0), 1);
				}
		    	 catch (JSONException e) {
					Log.i("ERROR JSON DAT FORMUL", e.toString());
		    	}
		     }
		JSONObject jsonSeguimientosParaEnviar = null;
		try {
			jsonSeguimientosParaEnviar = JSONHelper.getInstance().obtenerJsonSeguimientosParaEnviar(arraySeguimiento);
		}
		catch (JSONException e) {
			Log.i("ERROR JSON DATOS FORMUL", e.toString());
		}
		return jsonSeguimientosParaEnviar;
	}
}
