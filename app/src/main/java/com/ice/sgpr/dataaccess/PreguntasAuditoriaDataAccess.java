package com.ice.sgpr.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaNegocios;
import com.ice.sgpr.database.TablaPreguntasAuditor;
import com.ice.sgpr.service.JSONHelper;
/**
 * Acceso a datos de las preguntas de un auditor.
 * @author eperaza
 * Fecha de creacion: 20/08/2013.
 */
public class PreguntasAuditoriaDataAccess extends AbstractDataAccess {
	private String _sConsultaRespuestasElementos = "SELECT " + TablaPreguntasAuditor.COL_RESPUESTA_2 + " FROM " 
			+ TablaPreguntasAuditor.NOMBRE_TABLA + " WHERE " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = N* AND " 
			+ TablaPreguntasAuditor.COL_RESPUESTA_1 + " = 'P*'";
	
	private String _sConsultaRespuestasEstados = "SELECT " + TablaPreguntasAuditor.COL_RESPUESTA_3 + " FROM  " + TablaPreguntasAuditor.NOMBRE_TABLA + " WHERE " 
			+ TablaPreguntasAuditor.COL_NEGOCIO_ID + " = N* AND " + TablaPreguntasAuditor.COL_RESPUESTA_1 + " = 'P*' AND " 
			+ TablaPreguntasAuditor.COL_RESPUESTA_2 + " = 'R*'";
	
	private String _sConsultaCantidadRespuestas = "SELECT COUNT(" + TablaPreguntasAuditor.COL_NEGOCIO_ID + ") FROM " + TablaPreguntasAuditor.NOMBRE_TABLA 
			+ " WHERE " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = N* AND " + TablaPreguntasAuditor.COL_RESPUESTA_1 + " = 'R*'";
	
	private String _sConsultaNegociosConFormularioAudit = "SELECT p."+ TablaPreguntasAuditor.COL_NEGOCIO_ID + ", n." + TablaNegocios.COL_ULTIMA_VISITA
			+  ", n." + TablaNegocios.COL_OBSERVACIONES +  ", n." + TablaNegocios.COL_FECHA_OBSERVACIONES +  ", n." + TablaNegocios.COL_USR_MOD_OBS
			+ " FROM "+ TablaPreguntasAuditor.NOMBRE_TABLA + " p, " + TablaNegocios.NOMBRE_TABLA +" n WHERE p." + TablaPreguntasAuditor.COL_CODIGO_USUARIO 
			+ " = U*" + " AND p." + TablaPreguntasAuditor.COL_ACTUALIZADO + " IS NULL AND n."  + TablaNegocios.COL_ID + " = p." 
			+ TablaPreguntasAuditor.COL_NEGOCIO_ID + " AND p." + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = N* "
			+" GROUP BY p."	+ TablaPreguntasAuditor.COL_NEGOCIO_ID;
	
	private String _sConsultaPreguntasPorNegocio = "SELECT "+ TablaPreguntasAuditor.COL_CODIGO_PREGUNTA + ", "
			+ TablaPreguntasAuditor.COL_RESPUESTA_1 + ", " + TablaPreguntasAuditor.COL_RESPUESTA_2 + ", " + TablaPreguntasAuditor.COL_RESPUESTA_3
			+ " FROM " + TablaPreguntasAuditor.NOMBRE_TABLA + " WHERE " + TablaPreguntasAuditor.COL_NEGOCIO_ID +" = N* ORDER BY " + TablaPreguntasAuditor.COL_RESPUESTA_1;
	
	private String _sConsultaNumerosPregutasContestadas = "SELECT DISTINCT " + TablaPreguntasAuditor.COL_RESPUESTA_1 
			+ " FROM " + TablaPreguntasAuditor.NOMBRE_TABLA 
			+ " WHERE " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = N*";
	
	private String _sConsultaOperadoresMarcados = "SELECT " + TablaPreguntasAuditor.COL_RESPUESTA_1 + " FROM " 
			+ TablaPreguntasAuditor.NOMBRE_TABLA + " WHERE " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = ";
	
	private String _sConsultaCantidadFormSinVersionar = "SELECT COUNT(" + TablaPreguntasAuditor.COL_NEGOCIO_ID + ") FROM  " + TablaPreguntasAuditor.NOMBRE_TABLA 
			+ " WHERE " + TablaPreguntasAuditor.COL_ACTUALIZADO + " IS NULL";
	
	public PreguntasAuditoriaDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se inserta la pregunta en la tabla de preguntas
	 * @param pIdNegocio
	 * @param pCodigoPregunta
	 * @param pRespuesta1
	 * @param pRespuesta2
	 * @param pRespuesta3
	 * @param pCodigoUsuario
	 */
	public void insertarNuevaPregunta(int pIdNegocio, String pCodigoPregunta, String pRespuesta1, String pRespuesta2, String pRespuesta3, 
			int pCodigoUsuario){
		
		borrarPreguntasPorCodigoPreguntaYRespuesta(pCodigoPregunta, pRespuesta1, pRespuesta2, pCodigoUsuario, pIdNegocio);
		borrarPreguntasSinElementos(pCodigoPregunta, pCodigoUsuario, pIdNegocio);
		
		ContentValues values = new ContentValues();
		values.put(TablaPreguntasAuditor.COL_NEGOCIO_ID, pIdNegocio);
		values.put(TablaPreguntasAuditor.COL_RESPUESTA_1, pRespuesta1);
		values.put(TablaPreguntasAuditor.COL_RESPUESTA_2, pRespuesta2);
		values.put(TablaPreguntasAuditor.COL_RESPUESTA_3, pRespuesta3);
		values.put(TablaPreguntasAuditor.COL_CODIGO_PREGUNTA, pCodigoPregunta);
		values.put(TablaPreguntasAuditor.COL_CODIGO_USUARIO, pCodigoUsuario);
		
		_database.insert(TablaPreguntasAuditor.NOMBRE_TABLA, null, values);
	}
	
	/**
	 * Se cambia el estado de la propiedad "ACTUALIZADO" de la tabla de preguntas, para se�alar cu�les han sido enviadas al SP.
	 */
	public void actualizarEstadoActualizadoPregunta(int pCodigoNegocio, int pEstado){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntasAuditor.COL_ACTUALIZADO, pEstado);
		String pCondicion = TablaPreguntasAuditor.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.update(TablaPreguntasAuditor.NOMBRE_TABLA, values, pCondicion, null);
	}
	
	
	/**
	 * Borra los datos de la tabla "PreguntasAuditor".
	 */
	public void borrarPreguntasPorUsuario(String pCodigoUsuario) 
	{
		String sCondicion = TablaPreguntasAuditor.COL_CODIGO_USUARIO + " = " + pCodigoUsuario;
		_database.delete(TablaPreguntasAuditor.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Borra las respuestas de acuerdo al n�mero de pregunta.
	 * @param pCodigoPregunta
	 */
	public void borrarPreguntasPorCodigoPreguntaYRespuesta(String pCodigoPregunta, String pCodigoRespuesta1, String pCodigoRespuesta2, 
			int pCodigoUsuario, int pCodigoNegocio) 
	{
		String sCondicion = TablaPreguntasAuditor.COL_CODIGO_PREGUNTA + " = '" + pCodigoPregunta + "'"
				+ " AND " + TablaPreguntasAuditor.COL_RESPUESTA_1 + " = '" + pCodigoRespuesta1 + "'"
				+ " AND " + TablaPreguntasAuditor.COL_RESPUESTA_2 + " = '" + pCodigoRespuesta2 + "'"
				+ " AND " + TablaPreguntasAuditor.COL_CODIGO_USUARIO + " = " + pCodigoUsuario
				+ " AND " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.delete(TablaPreguntasAuditor.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Elimina los registros que no tengan respuesta 2 y 3. Esto es para las opciones guardadas 
	 * como "Ning�n operador"
	 * @param pCodigoPregunta
	 * @param pCodigoUsuario
	 * @param pCodigoNegocio
	 */
	public void borrarPreguntasSinElementos(String pCodigoPregunta, int pCodigoUsuario, int pCodigoNegocio) 
	{
		String sCondicion = TablaPreguntasAuditor.COL_CODIGO_PREGUNTA + " = '" + pCodigoPregunta + "'"
				+ " AND " + TablaPreguntasAuditor.COL_RESPUESTA_2 + " IS NULL "
				+ " AND " + TablaPreguntasAuditor.COL_RESPUESTA_3 + " IS NULL "
				+ " AND " + TablaPreguntasAuditor.COL_CODIGO_USUARIO + " = " + pCodigoUsuario
				+ " AND " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.delete(TablaPreguntasAuditor.NOMBRE_TABLA, sCondicion, null);
	}
	
	
	/**
	 * Borra los operadores guardados. Esto para que s�lo quede la opci�n "Ning�n operador".
	 * @param pCodigoUsuario
	 * @param pCodigoNegocio
	 */
	public void borrarOperadores(String pCodigoUsuario, int pCodigoNegocio)
	{
		String sCondicion = TablaPreguntasAuditor.COL_CODIGO_USUARIO + " = " + pCodigoUsuario
				+ " AND " + TablaPreguntasAuditor.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.delete(TablaPreguntasAuditor.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Actualiza el Id del negocio en la pregunta por el nuevo que fue asignado en el SP.
	 * @param pCodigoAnterior
	 * @param pCodigoNuevo
	 */
	public void actualizarIdPreguntaVersionamiento(int pCodigoAnterior, int pCodigoNuevo){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntasAuditor.COL_NEGOCIO_ID, pCodigoNuevo);
		String sCondicion = TablaPreguntasAuditor.COL_NEGOCIO_ID + " = " + pCodigoAnterior;
		_database.update(TablaPreguntasAuditor.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Retorna la lista de respuestas guardadas en la base de datos del dispositivo.
	 * Las respuestas corresponden a los elementos (sub men�s).
	 * @param pIdNegocio
	 * @param codigoOperador
	 * @return
	 */
	public List<String> obtenerRespuestasElementos(int pIdNegocio, String codigoOperador){
		String sConsulta = _sConsultaRespuestasElementos.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("P*", codigoOperador);
		List<String> sRespuestas = new ArrayList<String>();
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if (mCursor.moveToFirst()){
			do {
				sRespuestas.add(mCursor.getString(0));
		     } while(mCursor.moveToNext());
			
			return sRespuestas;
		}
		else
			return null;
	}
	
	/**
	 * Retorna la lista de preguntas 
	 * @param pIdNegocio
	 * @param codigoOperador
	 * @param codigoElemento
	 * @return
	 */
	public String[] obtenerRespuestasEstados(int pIdNegocio, String codigoOperador, String codigoElemento){
		String sConsulta = _sConsultaRespuestasEstados.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("P*", codigoOperador);
		sConsulta = sConsulta.replace("R*", codigoElemento);
		
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if (mCursor.moveToFirst()){
			String[] estadosMarcados = mCursor.getString(0).split("#");
			return estadosMarcados;
		}
		else
			return null;
	}
	
	/**
	 * Obtiene la cantidad de respuestas que tiene un operador.
	 */
	public int obtenerCantidadRespuestas(int pIdNegocio, String codigoOperador){
		String sConsulta = _sConsultaCantidadRespuestas.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("R*", codigoOperador);
		
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if (mCursor.moveToFirst()){
			int nCantidadRespuestas = mCursor.getInt(0);
			return nCantidadRespuestas;
		}
		else
			return 0;
	}
	
	/**
	 * Obtiene la lista de negocios que tienen formulario y los envia a otro metodo para crear un Json con el contenido
	 * de las preguntas, cuando lo retorna, crea el "Json Array Padre" con la demas informacion (Nombre del SP, Fecha de modificacion e
	 * Id del negocio).
	 * Recibe un arreglo con los operadores y su respectivo codigo.
	 */
	public JSONObject obtenerFormulariosAudit(String pCodigoUsuario, String pUid, String[] arrayOperadoresAuditor, int pIdNegocio){
		JSONObject jsonDatosFormulario = new JSONObject();
		String sConsulta = _sConsultaNegociosConFormularioAudit.replace("U*", pCodigoUsuario);
		sConsulta = sConsulta.replace("N*", Integer.toString(pIdNegocio));
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		
		if (mCursor.moveToFirst()){
		     do {
		    	 try {
		    		 String codigoColor = getColorCode(arrayOperadoresAuditor, mCursor.getInt(0));
		    		 JSONArray arrayPreguntas = jsonDatosPreguntaAuditor(mCursor.getInt(0));
		    		 jsonDatosFormulario = JSONHelper.getInstance().obtenerJsonInformacionFormularioAuditoria(mCursor.getInt(0), mCursor.getString(1),
		    				 arrayPreguntas, pUid, codigoColor, mCursor.getString(2), mCursor.getString(3), mCursor.getInt(4), pCodigoUsuario);
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
			jsonFormularioParaEnviar = JSONHelper.getInstance().obtenerJsonFormularioAuditorParaEnviar(jsonDatosFormulario);
		}
		catch (JSONException e) {
			Log.i("ERROR JSON DATOS FORM", e.toString());
		}
		return jsonFormularioParaEnviar;
	}
	
	/**
	 * Obtiene el Json Array para las preguntas correspondientes a un negocio.
	 * Agrega un Json para cada opci�n marcada.
	 * @param pIdNegocio
	 * @return
	 */
	public JSONArray jsonDatosPreguntaAuditor(int pIdNegocio){
		JSONArray arrayElementos = new JSONArray();
		JSONArray arrayEstados = new JSONArray();
   	 	String[] arrayEstadosAuditor = Constantes.ARREGLO_PREGUNTAS_AUDITORIA;
   	 	String sConsulta = _sConsultaPreguntasPorNegocio.replace("N*", Integer.toString(pIdNegocio));
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		JSONObject jsonEstado = new JSONObject();
		String operadorActual = "";
		if (mCursor.moveToFirst()){
		     do {
		    	 //int sCodigoPregunta = mCursor.getInt(0);
		    	 String respuesta1 = mCursor.getString(1);
		    	 if(operadorActual.equals(""))
		    		 operadorActual = respuesta1;
		    	 String sRespuesta2 = mCursor.getString(2);
		    	 String sRespuesta3 = mCursor.getString(3);
		    	 JSONArray arrayPreguntas = new JSONArray();
				try {
					/** Se crea el JSON de estados **/
					if(sRespuesta3 != null && !sRespuesta3.equals("")){
			    		 String[] sRespuesta3Split = sRespuesta3.split("#");
			    		 int nCantidadRespuestas3 = sRespuesta3Split.length;
			    		 for(int cont = 0; cont < nCantidadRespuestas3; cont ++){
			    			 arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaAuditor(sRespuesta3Split[cont], arrayEstadosAuditor[2]));
			    		 }
			    	 }
//					else if(sRespuesta2 != null && !sRespuesta2.equals("")){
//						arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaAuditor(sRespuesta1, sRespuesta2, "", arrayEstadosAuditor[2]));
//					}
//					else
//						arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaAuditor(sRespuesta1, "", "", arrayEstadosAuditor[2]));
					/** Se agrega el JSON de elementos **/
					jsonEstado = new JSONObject();
					if(sRespuesta2 != null && !sRespuesta2.equals(""))
						jsonEstado = JSONHelper.getInstance().obtenerJsonNegocioEstadoAuditor(sRespuesta2, arrayEstadosAuditor[1], arrayPreguntas);
					
					/** Se agrega el JSON de operdores **/
					if(operadorActual.equals(respuesta1)){
						if(jsonEstado.length() > 0)
							arrayEstados.put(jsonEstado);
					}
					else{
						arrayElementos.put(JSONHelper.getInstance().obtenerJsonNegocioElementosAuditor(operadorActual, arrayEstadosAuditor[0], arrayEstados));
						arrayEstados = new JSONArray();
						if(jsonEstado.length() > 0)
							arrayEstados.put(jsonEstado);
						operadorActual = respuesta1;
					}
				}
		    	 catch(Exception e){
		    		 Log.i("ERR JSON FORM PRES MARC", e.toString());
		    	 }
		     } while(mCursor.moveToNext());
		     try {
			    //arrayEstados.put(jsonEstado);
				arrayElementos.put(JSONHelper.getInstance().obtenerJsonNegocioElementosAuditor(operadorActual, arrayEstadosAuditor[0], arrayEstados));
			} catch (JSONException e) {
				Log.i("ERROR JSON FORM PM", e.toString());
			}
		}
		return arrayElementos;
	}
	
	/**
	 * Obtiene una lista con los numeros de preguntas que han sido contestadas (se encuentran en la BD).
	 */
	public int[] obtenerNumeroPreguntasContestadasAuditoria(int pIdNegocio){
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
	 * Consulta la lista de operadores que han sido tomados en cuenta para
	 * un negocio.
	 * @param arrayOperadoresAuditor
	 * @param pIdNegocio
	 * @return
	 */
	private String getColorCode(String[] arrayOperadoresAuditor, int pIdNegocio) {
		List<String> sRespuestas = new ArrayList<String>();
		Cursor mCursor = _database.rawQuery(_sConsultaOperadoresMarcados + pIdNegocio, null);
		if (mCursor.moveToFirst()){
			do {
				if(!sRespuestas.contains(mCursor.getString(0)))
					sRespuestas.add(mCursor.getString(0));
		     } while(mCursor.moveToNext());
		}
		
		if(sRespuestas.size() == 1) {
			if(sRespuestas.contains(arrayOperadoresAuditor[0].split("#")[1])) {
				return Constantes.COLOR_KOLBI;
			}
			else if(sRespuestas.contains(arrayOperadoresAuditor[1].split("#")[1])){
				return Constantes.COLOR_CLARO_MOVISTAR;
			}
			else if(sRespuestas.contains(arrayOperadoresAuditor[2].split("#")[1])){
				return Constantes.COLOR_CLARO;
			}
			else if(sRespuestas.contains(arrayOperadoresAuditor[3].split("#")[1])){
				return Constantes.COLOR_TUYOMOVIL;
			}
			else if (sRespuestas.contains(arrayOperadoresAuditor[4].split("#")[1])){
				return Constantes.COLOR_FULLMOVIL;
			}
			else{
				return Constantes.COLOR_NINGUNO;
			}
		}
		else if(sRespuestas.size() == 5){
			return Constantes.COLOR_TODOS;
		}
		else{
			if(sRespuestas.size() == 2 && sRespuestas.contains(arrayOperadoresAuditor[2].split("#")[1])
					&& sRespuestas.contains(arrayOperadoresAuditor[1].split("#")[1])) {
				return Constantes.COLOR_CLARO_MOVISTAR;
			}
			else if(!sRespuestas.contains(arrayOperadoresAuditor[0].split("#")[1])){
				return Constantes.COLOR_NO_KOLBI;
			}
			else if(sRespuestas.contains(arrayOperadoresAuditor[0].split("#")[1])){
				return Constantes.COLOR_KOLBI_OTRO;
			}
		}
		return "";
	}
	
	/**
	 * Obtiene la cantidad de formularios que no han sido versionadas.
	 */
	public int obtenerCantidadFormNoVersionados(){
		Cursor mCursor = _database.rawQuery(_sConsultaCantidadFormSinVersionar, null);
		int nNumeroPreguntas = 0;
		if (mCursor.moveToFirst()){
			nNumeroPreguntas = mCursor.getInt(0);
		}
		return nNumeroPreguntas;
	}
	
	/**
	 * Borra las preguntas por Id de negocio (al descartar)
	 * @param pIdNegocio
	 */
	public void borrarPreguntasPorIdNegocio(int pIdNegocio){
		String sCondicion = TablaPreguntasAuditor.COL_NEGOCIO_ID + " = " + pIdNegocio;
		_database.delete(TablaPreguntasAuditor.NOMBRE_TABLA, sCondicion, null);
	}
}
