package com.ice.sgpr.dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaNegocios;
import com.ice.sgpr.database.TablaPreguntasPresenciaMarca;
import com.ice.sgpr.service.JSONHelper;
/**
 * Acceso a datos de las preguntas depresencia de marca.
 * @author eperaza
 * Fecha de creacion: 25/06/2014.
 */
public class PresenciaMarcaDataAccess extends AbstractDataAccess {
	private String _sConsultaModificarPregunta = "SELECT "+ TablaPreguntasPresenciaMarca.COL_PREGUNTA_PM_ID 
			+ " FROM " + TablaPreguntasPresenciaMarca.NOMBRE_TABLA + " WHERE " + TablaPreguntasPresenciaMarca.COL_NUM_PREGUNTA + " = P* AND "
			+ TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID + " = N*";
	
	private String _sConsultaNumerosPregutasContestadas = "SELECT DISTINCT " + TablaPreguntasPresenciaMarca.COL_NUM_PREGUNTA 
			+ " FROM " + TablaPreguntasPresenciaMarca.NOMBRE_TABLA 
			+ " WHERE " + TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID + " = N* ORDER BY "+ TablaPreguntasPresenciaMarca.COL_NUM_PREGUNTA +" ASC";
	
	private String _sConsultaRespuestasPM = "SELECT DISTINCT R* FROM " + TablaPreguntasPresenciaMarca.NOMBRE_TABLA + " WHERE " 
			+ TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID + " = N* AND " 
			+ TablaPreguntasPresenciaMarca.COL_NUM_PREGUNTA + " = P*";
	
	private String _sConsultaNegociosConFormulario = "SELECT p."+ TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID +", n."+ TablaNegocios.COL_ULTIMA_VISITA 
			+ " FROM "+ TablaPreguntasPresenciaMarca.NOMBRE_TABLA + " p, "+ TablaNegocios.NOMBRE_TABLA +" n WHERE p." + TablaPreguntasPresenciaMarca.COL_CODIGO_USUARIO  
			+ " = U*" + " AND p." + TablaPreguntasPresenciaMarca.COL_ACTUALIZADO + " = 0 AND n." + TablaNegocios.COL_ID + " = p." + TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID  
			+" GROUP BY p."	+ TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID;// + " LIMIT 10";
	
	private String _sConsultaPreguntasPorNegocio = "SELECT "+ TablaPreguntasPresenciaMarca.COL_CODIGO_PREGUNTA + ", "
			+ TablaPreguntasPresenciaMarca.COL_CODIGO_RESPUESTA
			+ " FROM " + TablaPreguntasPresenciaMarca.NOMBRE_TABLA + " WHERE " + TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID +" = ";
	
	private Map<String, Integer> _mapRespuestas;
	/*
	private String _sConsultaModificarPreguntaAnidada = "SELECT * FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " + TablaPreguntas.COL_PREGUNTA_NUMERO + " = P* AND "
			+ TablaPreguntas.COL_NEGOCIO_ID + " = N* AND " + TablaPreguntas.COL_RESPUESTA_1 + " = R*";
	private String _sConsultaModificarPreguntaConValor = "SELECT * FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " + TablaPreguntas.COL_PREGUNTA_NUMERO + " = P* AND "
			+ TablaPreguntas.COL_NEGOCIO_ID + " = N* AND " + TablaPreguntas.COL_OPCION_VALOR + " = V*";
	private String _sConsultaPenultimaPregunta = "SELECT DISTINCT "+ TablaPreguntas.COL_RESPUESTA_1 +" FROM "+ TablaPreguntas.NOMBRE_TABLA +" WHERE "
			+ TablaPreguntas.COL_CODIGO_PREGUNTA_PRINCIPAL +" = 401 AND "+TablaPreguntas.COL_CODIGO_RESPUESTA2 +" = 809 ORDER BY "+ TablaPreguntas.COL_CODIGO_RESPUESTA1 +" ASC";
	private String _sConsultaNumeroPregunta = "SELECT "+ TablaPreguntas.COL_PREGUNTA_NUMERO + " FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " 
			+ TablaPreguntas.COL_CODIGO_PREGUNTA_PRINCIPAL + " = ";
*/
	
	public PresenciaMarcaDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se inserta la pregunta terminada en la tabla de preguntas de presencia de marca.
	 * Si la pregunta ya fue contestada la actualiza.
	 * @param pIdNegocio
	 * @param pNumeroPregunta
	 */
	public void insertarActualizarNuevaPregunta(int pIdNegocio, int pCodigoUsuario, int pNumeroPregunta, int pCodigoPregunta, String pNumeroRespuesta, 
			String pCodigoRespuesta){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID, pIdNegocio);
		values.put(TablaPreguntasPresenciaMarca.COL_CODIGO_USUARIO, pCodigoUsuario);
		values.put(TablaPreguntasPresenciaMarca.COL_NUM_PREGUNTA, pNumeroPregunta);
		values.put(TablaPreguntasPresenciaMarca.COL_CODIGO_PREGUNTA, pCodigoPregunta);
		values.put(TablaPreguntasPresenciaMarca.COL_NUM_RESPUESTA, pNumeroRespuesta);
		values.put(TablaPreguntasPresenciaMarca.COL_CODIGO_RESPUESTA, pCodigoRespuesta);
		values.put(TablaPreguntasPresenciaMarca.COL_ACTUALIZADO, 0);
		
		String sConsulta = _sConsultaModificarPregunta.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
		
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if(mCursor.moveToFirst()){
			int codigoPregunta = mCursor.getInt(0);
			String sCondicion = TablaPreguntasPresenciaMarca.COL_PREGUNTA_PM_ID + " = " + codigoPregunta;
			_database.update(TablaPreguntasPresenciaMarca.NOMBRE_TABLA, values, sCondicion, null);
		}
		else
			_database.insert(TablaPreguntasPresenciaMarca.NOMBRE_TABLA, null, values);
	}
	
	/**
	 * Obtiene una lista con los n�meros de preguntas que han sido contestadas (se encuentran en la BD).
	 * @param pIdNegocio.
	 */
	public int[] obtenerNumeroPreguntasContestadasPM(int pIdNegocio){
		String sConsulta = _sConsultaNumerosPregutasContestadas.replace("N*", Integer.toString(pIdNegocio));
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		int[] lNumeroPreguntas = null;
		int nContador = 0;
		if (mCursor.moveToFirst()){
		     lNumeroPreguntas = new int[mCursor.getCount()];
		     do {
		          lNumeroPreguntas[nContador] = mCursor.getInt(0);
		          nContador ++;
		     } while(mCursor.moveToNext());
		}
		return lNumeroPreguntas;
	}
	
	/**
	 * Obtiene las respuestas de una pregunta guardadas en la Base de Datos.
	 * @param pIdNegocio
	 * @param pNumeroPregunta
	 * @param pNumeroOpcion: N�mero de la posici�n seleccionada en el listview de la pregunta principal.
	 */
	public String obtenerRespuestas(int pIdNegocio, int pNumeroPregunta, int pNumeroOpcion){
		String sConsulta = _sConsultaRespuestasPM.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
		sConsulta = sConsulta.replace("R*", TablaPreguntasPresenciaMarca.COL_NUM_RESPUESTA);
		
		String sRespuestas = "";
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if (mCursor.moveToFirst()){
			do {
				if(sRespuestas.equals(""))
					sRespuestas = mCursor.getString(0);
				else
					sRespuestas += "#" + mCursor.getString(0);
		     } while(mCursor.moveToNext());
			
			return sRespuestas;
		}
		else
			return null;
	}
	
	/**
	 * Se obtiene el n�mero de pregunta seg�n un c�digo de la pregunta consultada.
	 * @param nCodigoPreguntaDependencia
	 * @return
	 */
	/*
	public int obtenerNumeroPregunta(int nCodigoPregunta){
		Cursor mCursor = _database.rawQuery(_sConsultaNumeroPregunta + nCodigoPregunta, null);
		int nNumeroPregunta = 0;
		if (mCursor.moveToFirst())
			nNumeroPregunta = mCursor.getInt(0);
		return nNumeroPregunta;
	}
	*/
	
	/*
	public ArrayList<int[]> obtenerRespuestasConValor(int pIdNegocio, int pNumeroPregunta, Boolean pAnidada, String pOpcionAnidada){
		String sConsulta = _sConsultaRespuestasPrincipal.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
		sConsulta = sConsulta.replace("R*", TablaPreguntas.COL_VALOR + ", "+ TablaPreguntas.COL_OPCION_VALOR);
		
		if(pAnidada){
			sConsulta += " AND "+ TablaPreguntas.COL_ANIDADA_CON_VALOR +" = 1 AND "+ TablaPreguntas.COL_RESPUESTA_1 +" = " + pOpcionAnidada;
		}
		ArrayList<int[]> arrayOpcionesValor = new ArrayList<int[]>();
		
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if (mCursor.moveToFirst()){
			do {
				int[] arrayValor = new int[2];
				arrayValor[0] = Integer.parseInt(mCursor.getString(0));
				arrayValor[1] = mCursor.getInt(1);
				arrayOpcionesValor.add(arrayValor);
		     } while(mCursor.moveToNext());
			return arrayOpcionesValor;
		}
		return null;
	}
	*/
	/**
	 * Actualiza el Id del negocio en la pregunta por el nuevo que fue asignado en el SP.
	 * @param pCodigoAnterior
	 * @param pCodigoNuevo
	 */
	public void actualizarIdPreguntaVersionamiento(int pCodigoAnterior, int pCodigoNuevo){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID, pCodigoNuevo);
		String sCondicion = TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID + " = " + pCodigoAnterior;
		_database.update(TablaPreguntasPresenciaMarca.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * VERSIONAMIENTO PREGUNTAS
	 */	
	/**
	 * Obtiene la lista de negocios que tienen formulario y los env�a a otro m�todo para crear un Json con el contenido
	 * de las preguntas, cuando lo retorna, crea el "Json Array Padre" con la dem�s informaci�n (Nombre del SP, Fecha de modificaci�n e 
	 * Id del negocio).
	 */
	public JSONObject obtenerFormulariosPM(String pCodigoUsuario, String pUid){
		JSONArray arrayFormulario = new JSONArray();
		String sConsulta = _sConsultaNegociosConFormulario.replace("U*", pCodigoUsuario);
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		//Indica si alguna de las respuestas fue seleccionada en alguna de las preguntas.
		_mapRespuestas = new HashMap<String, Integer>();
		
		if (mCursor.moveToFirst()){
		     do {
		    	 try {
		    		 JSONArray arrayPreguntas = jsonDatosPregunta(mCursor.getInt(0));
		    		 int color = obtenerColor();
		    		 JSONObject jsonDatosFormulario = JSONHelper.getInstance().obtenerJsonInformacionFormularioPM(mCursor.getInt(0), mCursor.getString(1),
		    				 arrayPreguntas, pUid, color, pCodigoUsuario);
		    		 arrayFormulario.put(jsonDatosFormulario);
		    		 //actualizarEstadoActualizadoPreguntaPM(mCursor.getInt(0), 1);
				}
		    	 catch (JSONException e) {
					Log.i("ERROR JSON DATOS FORM", e.toString());
		    	}
		     } while(mCursor.moveToNext());
		}
		else 
			return null;
		JSONObject jsonFormularioParaEnviar = null;
		try {
			jsonFormularioParaEnviar = JSONHelper.getInstance().obtenerJsonFormularioPMParaEnviar(arrayFormulario);
		}
		catch (JSONException e) {
			Log.i("ERROR JSON DATOS FORM", e.toString());
		}
		return jsonFormularioParaEnviar;
	}
	
	/**
	 * 
	 * @return
	 */
	public int obtenerColor(){
		int mapSize = _mapRespuestas.size();
		
		if(mapSize == 5)
			return 46; //Morado.
		
		else if(mapSize == 1){
			String codigoMarca = "";
			for (Entry<String, Integer> entry : _mapRespuestas.entrySet()) {
	            if (entry.getValue() == 1) {
	            	codigoMarca = entry.getKey();
	            }
			}
			if(codigoMarca.equals("741"))
				return 41; //Verde
			else if(codigoMarca.equals("742"))
				return 42; //Azul
			else if(codigoMarca.equals("743"))
				return 22; //Rojo
			else if(codigoMarca.equals("744"))
				return 43; //Negro
			else
				return 44; //Naranja
		}
		
		else if(mapSize == 2 && _mapRespuestas.get("742") != null && _mapRespuestas.get("743") != null){
				return 47; //Rosado.
		}
		else if(_mapRespuestas.get("741") != null)
			return 45; //Turqueza.
		else
			return 48; // Amarillo
	}
	
	/**
	 * Obtiene el Json Array para las preguntas correspondientes a un negocio.
	 * Agrega un Json para cada opci�n marcada.
	 * @param pIdNegocio
	 * @return
	 */
	public JSONArray jsonDatosPregunta(int pIdNegocio){
		JSONArray arrayPreguntas = new JSONArray();
		Cursor mCursor = _database.rawQuery(_sConsultaPreguntasPorNegocio + pIdNegocio, null);
		if (mCursor.moveToFirst()){
		     do {
		    	 int sCodigoPregunta = mCursor.getInt(0);
		    	 String sCodigosRespuesta = mCursor.getString(1);
				try {
					String[] sRespuestaSplit = sCodigosRespuesta.split("#");
					int nCantidadRespuestas = sRespuestaSplit.length;
					for (int cont = 0; cont < nCantidadRespuestas; cont++) {
						arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaPM(
												sCodigoPregunta, Integer.parseInt(sRespuestaSplit[cont])));
						
						_mapRespuestas.put(sRespuestaSplit[cont], 1);
					}
				}
		    	 catch(Exception e){
		    		 Log.i("ERROR JSON FORMULARIO PRESENCIA MARCA", e.toString());
		    	 }
		     } while(mCursor.moveToNext());
		}
		return arrayPreguntas;
	}
	
	/**
	 * Se cambia el estado de la propiedad "ACTUALIZADO" de la tabla de preguntas, para se�alar cu�les han sido enviadas al SP.
	 */
	public void actualizarEstadoActualizadoPreguntaPM(int pCodigoNegocio, int pEstado){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntasPresenciaMarca.COL_ACTUALIZADO, pEstado);
		String pCondicion = TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.update(TablaPreguntasPresenciaMarca.NOMBRE_TABLA, values, pCondicion, null);
	}
	
	/**
	 * Retorna las opciones marcadas en la pen�ltima pregunta, para habilitar las opciones de la �ltima pregunta.
	 * @return
	 */
	/*
	public int[]listaOpcionesPenultimaPregunta(){
		String sConsulta = _sConsultaPenultimaPregunta;
		int[] lNumeroPreguntas = null;
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		int nContador = 0;
		if (mCursor.moveToFirst()){
			lNumeroPreguntas = new int[mCursor.getCount()];
		     do {
		    	 lNumeroPreguntas[nContador] = mCursor.getInt(0);
		          nContador ++;
		     } while(mCursor.moveToNext());
		}
		return lNumeroPreguntas;
	}
	*/
	
	/**
	 * Borra los datos de la tabla "PreguntasPresenciaMarca".
	 * @param pCodigoUsuario: usuario actual
	 * @param pCodigoPregunta: Pregunta a eliminar. -1 = Todas las preguntas
	 */
	public void borrarPreguntasPresenciaMarca(String pCodigoUsuario, int pCodigoPregunta) 
	{
		String sCondicion = TablaPreguntasPresenciaMarca.COL_CODIGO_USUARIO + " = " + pCodigoUsuario;
		if(pCodigoPregunta != -1)
			sCondicion += " AND " + TablaPreguntasPresenciaMarca.COL_CODIGO_PREGUNTA + " = " + pCodigoPregunta;
		_database.delete(TablaPreguntasPresenciaMarca.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Borra todas las preguntas de un negocio.
	 * @param pCodigoNegocio
	 */
	public void borrarTodasPreguntasPresenciaMarca(int pCodigoNegocio) 
	{
		String sCondicion = TablaPreguntasPresenciaMarca.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.delete(TablaPreguntasPresenciaMarca.NOMBRE_TABLA, sCondicion, null);
	}
}
