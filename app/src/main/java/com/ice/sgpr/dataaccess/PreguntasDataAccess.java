package com.ice.sgpr.dataaccess;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaPreguntas;
import com.ice.sgpr.service.JSONHelper;
/**
 * Acceso a datos de las preguntas.
 * @author eperaza
 * Fecha de creacion: 20/08/2013.
 */
public class PreguntasDataAccess extends AbstractDataAccess {
	private String _sConsultaModificarPregunta = "SELECT * FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " + TablaPreguntas.COL_PREGUNTA_NUMERO + " = P* AND "
			+ TablaPreguntas.COL_NEGOCIO_ID + " = N*";
	private String _sConsultaModificarPreguntaAnidada = "SELECT * FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " + TablaPreguntas.COL_PREGUNTA_NUMERO + " = P* AND "
			+ TablaPreguntas.COL_NEGOCIO_ID + " = N* AND " + TablaPreguntas.COL_RESPUESTA_1 + " = R*";
	private String _sConsultaNumerosPregutasContestadas = "SELECT DISTINCT " + TablaPreguntas.COL_PREGUNTA_NUMERO + " FROM " + TablaPreguntas.NOMBRE_TABLA 
			+" WHERE " + TablaPreguntas.COL_NEGOCIO_ID + " = N* ORDER BY PREGUNTA_NUMERO ASC";
	private String _sConsultaRespuestasPrincipal = "SELECT DISTINCT R* FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " + TablaPreguntas.COL_NEGOCIO_ID + " = N* AND " 
			+ TablaPreguntas.COL_PREGUNTA_NUMERO + " = P*";
	private String _sConsultaModificarPreguntaConValor = "SELECT * FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " + TablaPreguntas.COL_PREGUNTA_NUMERO + " = P* AND "
			+ TablaPreguntas.COL_NEGOCIO_ID + " = N* AND " + TablaPreguntas.COL_OPCION_VALOR + " = V*";
	/*private String _sConsultaNegociosConFormulario = "SELECT p."+ TablaPreguntas.COL_NEGOCIO_ID +", n."+ TablaNegocios.COL_ULTIMA_VISITA + " FROM "+ TablaPreguntas.NOMBRE_TABLA 
			+ " p, "+ TablaNegocios.NOMBRE_TABLA +" n WHERE p." + TablaPreguntas.COL_CODIGO_USUARIO  + " = U*" + " AND p." + TablaPreguntas.COL_ACTUALIZADO + " = 0 AND n."
			+ TablaNegocios.COL_ID + " = p." + TablaPreguntas.COL_NEGOCIO_ID  +" GROUP BY p."	+ TablaPreguntas.COL_NEGOCIO_ID + " LIMIT 10";*/
	private String _sConsultaPreguntasPorNegocio = "SELECT "+ TablaPreguntas.COL_VALOR + ", " + TablaPreguntas.COL_CODIGO_RESPUESTA1 + ", "
			+ TablaPreguntas.COL_CODIGO_RESPUESTA2 + ", " + TablaPreguntas.COL_CODIGO_RESPUESTA_VALOR + ", "+ TablaPreguntas.COL_CODIGO_PREGUNTA_DEPENDENCIA 
			+ ", " + TablaPreguntas.COL_CODIGO_PREGUNTA_PRINCIPAL + ", " + TablaPreguntas.COL_CODIGO_PREGUNTA_SECUNDARIA +" FROM "
			+ TablaPreguntas.NOMBRE_TABLA +" WHERE "+ TablaPreguntas.COL_NEGOCIO_ID +" = ";
	private String _sConsultaPenultimaPregunta = "SELECT DISTINCT "+ TablaPreguntas.COL_RESPUESTA_1 +" FROM "+ TablaPreguntas.NOMBRE_TABLA +" WHERE "
			+ TablaPreguntas.COL_CODIGO_PREGUNTA_PRINCIPAL +" = 401 AND "+TablaPreguntas.COL_CODIGO_RESPUESTA2 +" = 809 ORDER BY "+ TablaPreguntas.COL_CODIGO_RESPUESTA1 +" ASC";
	private String _sConsultaNumeroPregunta = "SELECT "+ TablaPreguntas.COL_PREGUNTA_NUMERO + " FROM " + TablaPreguntas.NOMBRE_TABLA + " WHERE " 
			+ TablaPreguntas.COL_CODIGO_PREGUNTA_PRINCIPAL + " = ";

	public PreguntasDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Se inserta la pregunta terminada en la tabla de preguntas.
	 * Si la pregunta ya fue contestada la actualiza. Se verifica si es una respuesta anidada o si es una pregunta
	 * con valor ingresado, lo cual variar�a la consulta para obtener las respuestas ya guardadas.
	 * @param pIdNegocio
	 * @param pNumeroPregunta
	 * @param pRespuesta1
	 * @param pRespuesta2
	 * @param pPreguntaMarcada
	 */
	public void insertarNuevaPregunta(int pIdNegocio, int pNumeroPregunta, String pRespuesta1, String pRespuesta2, String pValor, 
			int pOpcionValor, Boolean pAnidada, Boolean pAnidadaConValor, int pCodigoPreguntaPrincipal, int pCodigoPreguntaSecundaria,
			int pCodigoPreguntaDependencia, String pCodigoRespuesta1, String pCodigoRespuesta2, String pCodigoRespuestaValor, int pCodigoUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntas.COL_NEGOCIO_ID, pIdNegocio);
		values.put(TablaPreguntas.COL_RESPUESTA_1, pRespuesta1);
		values.put(TablaPreguntas.COL_RESPUESTA_2, pRespuesta2);
		values.put(TablaPreguntas.COL_CODIGO_PREGUNTA_PRINCIPAL, pCodigoPreguntaPrincipal);
		values.put(TablaPreguntas.COL_CODIGO_PREGUNTA_SECUNDARIA, pCodigoPreguntaSecundaria);
		values.put(TablaPreguntas.COL_CODIGO_PREGUNTA_DEPENDENCIA, pCodigoPreguntaDependencia);
		values.put(TablaPreguntas.COL_CODIGO_RESPUESTA1, pCodigoRespuesta1);
		values.put(TablaPreguntas.COL_CODIGO_RESPUESTA2, pCodigoRespuesta2);
		values.put(TablaPreguntas.COL_CODIGO_RESPUESTA_VALOR, pCodigoRespuestaValor);
		values.put(TablaPreguntas.COL_CODIGO_USUARIO, pCodigoUsuario);
		values.put(TablaPreguntas.COL_ACTUALIZADO, 0);
		
		String sConsulta;
		String sCondicion;
		if(pValor != null && !(pValor.equals(""))){
			values.put(TablaPreguntas.COL_VALOR, pValor);
			values.put(TablaPreguntas.COL_OPCION_VALOR, pOpcionValor);
			
			sConsulta = _sConsultaModificarPreguntaConValor.replace("N*", Integer.toString(pIdNegocio));
			sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
			sConsulta = sConsulta.replace("V*", Integer.toString(pOpcionValor));
			
			if(pAnidadaConValor)
				sConsulta += " AND " + TablaPreguntas.COL_RESPUESTA_1 + " = " + pRespuesta1;
		}
		else if(!pAnidada){
			sConsulta = _sConsultaModificarPregunta.replace("N*", Integer.toString(pIdNegocio));
			sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
		}
		else{
			sConsulta = _sConsultaModificarPreguntaAnidada.replace("N*", Integer.toString(pIdNegocio));
			sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
			sConsulta = sConsulta.replace("R*", pRespuesta1);
		}
		if(pAnidadaConValor)
			values.put(TablaPreguntas.COL_ANIDADA_CON_VALOR, 1);
		else
			values.put(TablaPreguntas.COL_ANIDADA_CON_VALOR, 0);
		
		Cursor mcursor = _database.rawQuery(sConsulta, null);
		if(mcursor.moveToFirst()){
			int nPreguntaId = mcursor.getInt(0);
			if(!pAnidada)
				sCondicion = TablaPreguntas.COL_PREGUNTA_ID + " = " + nPreguntaId;
			else
				sCondicion = TablaPreguntas.COL_PREGUNTA_ID + " = " + nPreguntaId + " AND " + TablaPreguntas.COL_RESPUESTA_1 + " = " + pRespuesta1;
			_database.update(TablaPreguntas.NOMBRE_TABLA, values, sCondicion, null);
		}
		else{
			values.put(TablaPreguntas.COL_PREGUNTA_NUMERO, pNumeroPregunta);
			_database.insert(TablaPreguntas.NOMBRE_TABLA, null, values);
		}
	}
	
	/**
	 * Obtiene una lista con los n�meros de preguntas que han sido contestadas (se encuentran en la BD).
	 * @param pIdNegocio.
	 */
	public int[] obtenerNumeroPreguntasContestadas(int pIdNegocio){
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
	 * @param pAnidada: "true" si las respuestas consultadas son para la pregunta anidada.
	 * @param pNumeroOpcion: N�mero de la posici�n seleccionada en el listview de la pregunta principal.
	 */
	public String obtenerRespuestas(int pIdNegocio, int pNumeroPregunta, Boolean pAnidada, int pNumeroOpcion){
		String sConsulta = _sConsultaRespuestasPrincipal.replace("N*", Integer.toString(pIdNegocio));
		sConsulta = sConsulta.replace("P*", Integer.toString(pNumeroPregunta));
		if(!pAnidada)
			sConsulta = sConsulta.replace("R*", TablaPreguntas.COL_RESPUESTA_1);
		else{
			sConsulta = sConsulta.replace("R*", TablaPreguntas.COL_RESPUESTA_2);
			sConsulta += " AND " + TablaPreguntas.COL_RESPUESTA_1 + " = " + pNumeroOpcion;
		}
		
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
	public int obtenerNumeroPregunta(int nCodigoPregunta){
		Cursor mCursor = _database.rawQuery(_sConsultaNumeroPregunta + nCodigoPregunta, null);
		int nNumeroPregunta = 0;
		if (mCursor.moveToFirst())
			nNumeroPregunta = mCursor.getInt(0);
		return nNumeroPregunta;
	}
	
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
	
	/**
	 * Actualiza el Id del negocio en la pregunta por el nuevo que fue asignado en el SP.
	 * @param pCodigoAnterior
	 * @param pCodigoNuevo
	 */
	public void actualizarIdPreguntaVersionamiento(int pCodigoAnterior, int pCodigoNuevo){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntas.COL_NEGOCIO_ID, pCodigoNuevo);
		String sCondicion = TablaPreguntas.COL_NEGOCIO_ID + " = " + pCodigoAnterior;
		_database.update(TablaPreguntas.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * VERSIONAMIENTO PREGUNTAS
	 */	
	/**
	 * Obtiene la lista de negocios que tienen formulario y los env�a a otro m�todo para crear un Json con el contenido
	 * de las preguntas, cuando lo retorna, crea el "Json Array Padre" con la dem�s informaci�n (Nombre del SP, Fecha de modificaci�n e 
	 * Id del negocio).
	 */
	/*public JSONObject obtenerFormularios(String pCodigoUsuario, String pUid){
		JSONArray arrayFormulario = new JSONArray();
		String sConsulta = _sConsultaNegociosConFormulario.replace("U*", pCodigoUsuario);
		Cursor mCursor = _database.rawQuery(sConsulta, null);
		if (mCursor.moveToFirst()){
		     do {
		    	 try {
		    		 JSONArray arrayPreguntas = jsonDatosPregunta(mCursor.getInt(0));
		    		 JSONObject jsonDatosFormulario = JSONHelper.getInstance().obtenerJsonInformacionFormulario(mCursor.getInt(0), mCursor.getString(1), arrayPreguntas, pUid);
		    		 arrayFormulario.put(jsonDatosFormulario);
		    		 actualizarEstadoActualizadoPregunta(mCursor.getInt(0), 1);
				}
		    	 catch (JSONException e) {
					Log.i("ERROR JSON DATOS FORMULARIO", e.toString());
		    	}
		     } while(mCursor.moveToNext());
		}
		else 
			return null;
		JSONObject jsonFormularioParaEnviar = null;
		try {
			jsonFormularioParaEnviar = JSONHelper.getInstance().obtenerJsonFormularioParaEnviar(arrayFormulario);
		}
		catch (JSONException e) {
			Log.i("ERROR JSON DATOS FORMULARIO", e.toString());
		}
		return jsonFormularioParaEnviar;
	}*/
	
	/**
	 * Obtiene el Json Array para las preguntas correspondientes a un negocio.
	 * Agrega un Json para cada opci�n marcada.
	 * Hay 3 procesos diferentes seg�n el tipo de pregunta:
	 * - Preguntas con un valor ingresado: En caso de que la respuesta contenga un valor diferente de cero, se inserta el
	 * valor, el c�digo de la respuesta que contiene dicho valor, la pregunta de la que depende (si la hay), el c�digo de pregunta
	 * que se est� respondiendo y, dependiendo del tipo de pregunta, el c�digo de la respuesta 1. Si es una pregunta anidada, se inserta
	 * simplemente el c�digo de la respuesta principal, si no est� anidada (dentro de la programaci�n, no de la l�gica) se coloca el c�digo
	 * de la respuesta que contiene el valor. Se activa la bandera "bValorActivado" que indica que la pregunta ya se insert� como una que
	 * contiene valor, para evitar que se vuelva a insertar como pregunta sencilla m�s adelante.
	 * - Preguntas con c�digo de Respuesta 1: Son los casos de selecci�n simple o m�ltiple, anidada o no. Simplemente se insertan en el JSON
	 * si contienen alg�n c�digo en la columna de respuesta 1. Se inserta un cero en el c�digo de pregunta de dependencia y en Respuesta1, 
	 * puesto que es una pregunta principal en s� misma
	 * - Preguntas con c�digo de respuesta 2: Casos de preguntas anidadas, se insertan en el JSON si el espacio de CodigoRespuesta2 contiene 
	 * valores. Se inserta casi igual que las preguntas ocn Respuesta1, s�lo que el c�digo se cambia por el de Respuesta2, el espacio de Respuesta1
	 * lo ocupa el c�digo de la respuesta principal y en pregunta dependencia el c�digo de la pregunta principal.
	 * @param pIdNegocio
	 * @return
	 */
	public JSONArray jsonDatosPregunta(int pIdNegocio){
		JSONArray arrayPreguntas = new JSONArray();
		Cursor mCursor = _database.rawQuery(_sConsultaPreguntasPorNegocio + pIdNegocio, null);
		if (mCursor.moveToFirst()){
		     do {
		    	 String sValor = mCursor.getString(0);
		    	 String sCodigoRespuesta1 = mCursor.getString(1);
		    	 String sCodigoRespuesta2 = mCursor.getString(2);
		    	 String sCodigoRespuestaValor = mCursor.getString(3);
		    	 String sCodigoPreguntaDependencia = mCursor.getString(4);
		    	 String sCodigoPreguntaPrincipal = mCursor.getString(5);
		    	 String sCodigoPreguntaSecundaria = mCursor.getString(6);
		    	 Boolean bValorActivado = false;
		    	 
		    	 try{
		    		 //Preguntas con valor
			    	 if(sValor != null && !sValor.equals("0")){
			    		 if(sCodigoRespuesta1 == null){
			    			 if(sCodigoRespuestaValor != null && !sCodigoRespuestaValor.equals("0"))
			    				 sCodigoRespuesta1 = sCodigoRespuestaValor;
			    			 else
			    				 sCodigoRespuesta1 = "0";
			    		 }
			    		 arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaIndividual(sValor, Integer.parseInt(sCodigoRespuestaValor), 
			    				 Integer.parseInt(sCodigoPreguntaDependencia), Integer.parseInt(sCodigoPreguntaPrincipal),Integer.parseInt(sCodigoRespuesta1)));
			    		 bValorActivado = true;
			    	 }
			    	 else
			    		 sValor = "0";
			    	 if(sCodigoRespuesta1 != null && !sCodigoPreguntaPrincipal.equals("0") && !bValorActivado && sCodigoRespuestaValor == null
			    			 && (sCodigoRespuesta2 == null || !sCodigoPreguntaSecundaria.equals("0"))){
			    		 String[] sRespuesta1Split = sCodigoRespuesta1.split("#");
			    		 int nCantidadRespuestas1 = sRespuesta1Split.length;
			    		 for(int cont = 0; cont < nCantidadRespuestas1; cont ++){
			    			 arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaIndividual(sValor, Integer.parseInt(sRespuesta1Split[cont]), 
			    					 0, Integer.parseInt(sCodigoPreguntaPrincipal),0));
			    		 }
			    	 }
			    	//Preguntas Anidadas
			    	 if(sCodigoRespuesta2 != null && !sCodigoPreguntaSecundaria.equals("0") && !bValorActivado){
			    		 String[] sRespuesta2Split = sCodigoRespuesta2.split("#");
			    		 int nCantidadRespuestas2 = sRespuesta2Split.length;
			    		 for(int cont = 0; cont < nCantidadRespuestas2; cont ++){
			    			 arrayPreguntas.put(JSONHelper.getInstance().obtenerJsonNegocioPreguntaIndividual(sValor, Integer.parseInt(sRespuesta2Split[cont]), 
			    					 Integer.parseInt(sCodigoPreguntaDependencia), Integer.parseInt(sCodigoPreguntaSecundaria), Integer.parseInt(sCodigoRespuesta1)));
			    		 }
			    	 }    	 
		    	 }
		    	 catch(Exception e){
		    		 Log.i("ERROR JSON FORMULARIO", e.toString());
		    	 }
		     } while(mCursor.moveToNext());
		}
		return arrayPreguntas;
	}
	
	/**
	 * Se cambia el estado de la propiedad "ACTUALIZADO" de la tabla de preguntas, para se�alar cu�les han sido enviadas al SP.
	 */
	public void actualizarEstadoActualizadoPregunta(int pCodigoNegocio, int pEstado){
		ContentValues values = new ContentValues();
		values.put(TablaPreguntas.COL_ACTUALIZADO, pEstado);
		String pCondicion = TablaPreguntas.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.update(TablaPreguntas.NOMBRE_TABLA, values, pCondicion, null);
	}
	
	/**
	 * Retorna las opciones marcadas en la pen�ltima pregunta, para habilitar las opciones de la �ltima pregunta.
	 * @return
	 */
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
	
	/**
	 * Borra los datos de la tabla "Preguntas".
	 */
	public void borrarPreguntas(String pCodigoUsuario) 
	{
		String sCondicion = TablaPreguntas.COL_CODIGO_USUARIO + " = " + pCodigoUsuario;
		_database.delete(TablaPreguntas.NOMBRE_TABLA, sCondicion, null);
	}
}
