package com.ice.sgpr.implementor;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import com.ice.sgpr.dataaccess.PreguntasDataAccess;
import com.ice.sgpr.entidades.Pregunta;

/**
 * Implementor para la consulta de datos relacionados con Negocios.
 * @author eperaza
 * fecha de creacion: 07/08/2013.
 */
@SuppressLint("SimpleDateFormat")
public class PreguntasImplementor{
private PreguntasDataAccess _dataAccess;
private static PreguntasImplementor _instancia;
	
	private PreguntasImplementor()
	{
		_dataAccess = new PreguntasDataAccess();
	}
	
	public static PreguntasImplementor getInstance(){
		if(_instancia == null)
			_instancia = new PreguntasImplementor();
		return _instancia;
	}
	
	/**
	 * Agrega la informacion de la pregunta contestada a la BD.
	 */
	public void insertarPregunta(Pregunta pPregunta, Boolean pAnidada, Boolean pAnidadaConValor){
		_dataAccess.openForReading();
		if(pPregunta.getRespuesta1() == null)
			pPregunta.setRespuesta1("");
		if(pPregunta.getRespuesta2() == null)
			pPregunta.setRespuesta2("");
		if(pAnidadaConValor){
			pPregunta.setCodigoPreguntaSecundaria(0);
		}
		int nCodigoUsuario = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		_dataAccess.insertarNuevaPregunta(nNegocioId, pPregunta.getNumero(), pPregunta.getRespuesta1(), pPregunta.getRespuesta2(), 
				pPregunta.getValor(), pPregunta.getPosicion(), pAnidada, false, pPregunta.getCodigoPreguntaPrincipal(), 
				pPregunta.getCodigoPreguntaSecundaria(), pPregunta.getCodigoPreguntaDependencia(), pPregunta.getCodigoRespuesta1(), 
				pPregunta.getCodigoRespuesta2(), pPregunta.getCodigoRespuestaValor(), nCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Agrega la informacion de la pregunta contestada a la BD.
	 * @param pListaValores: Lista de los valores ingresados por el usuario.
	 * @param pNumeroPregunta: Numero de la pregunta contestada.
	 * @param pAnidada: True si la pregunta es anidada.
	 * @param pPosicionRespuesta: Posicion en el listview.
	 */
	public void insertarPreguntaConValor(ArrayList<Integer> pListaValores, int pNumeroPregunta, Boolean pAnidada, int pPosicionRespuesta,
			int pCodigoPreguntaPrincipal, int pCodigoPreguntaSecundaria, int pCodigoPreguntaDependencia, String pCodigoRespuesta1,
			String pCodigoRespuesta2, String pCodigoRespuestaValor, List<String> pCodigosValores){
		_dataAccess.openForReading();
		int nCodigoUsuario = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
		int nContador = 0;
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		for(int nValor:pListaValores){
			String sPosicionRespuesta = "";
			if(pPosicionRespuesta != -1)
				sPosicionRespuesta = Integer.toString(pPosicionRespuesta);

			if(pCodigosValores.get(nContador) != null)
				pCodigoRespuesta2 = null;
			
			_dataAccess.insertarNuevaPregunta(nNegocioId, pNumeroPregunta, sPosicionRespuesta, "", Integer.toString(nValor), nContador, false, 
					pAnidada, pCodigoPreguntaPrincipal, pCodigoPreguntaSecundaria, pCodigoPreguntaDependencia, pCodigoRespuesta1, 
					pCodigoRespuesta2, pCodigosValores.get(nContador), nCodigoUsuario);
			nContador ++;
		}
		_dataAccess.close();
	}
	
	/**
	 * Obtiene un array con los numeros de preguntas que han sido contestadas.
	 */
	public int[] obtenerNumerosPreguntasContestadas(){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int[] preguntasContestadas = _dataAccess.obtenerNumeroPreguntasContestadas(nNegocioId);
		_dataAccess.close();
		return preguntasContestadas;
	}
	
	/**
	 * Obtiene la cantidad de preguntas contestadas.
	 */
	public int obtenerCantidadPreguntasContestadas(){
		_dataAccess.openForReading();
		if(NegociosImplementor.getInstance().obtenerNegocioActivo() == null)
			return 0;
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int[] preguntasContestadas = _dataAccess.obtenerNumeroPreguntasContestadas(nNegocioId);
		_dataAccess.close();
		if(preguntasContestadas == null)
			return 0;
		return preguntasContestadas.length;
	}
	
	/**
	 * Obtiene la lista de respuestas marcadas previamente en una pregunta.
	 * @param pNumeroPregunta
	 * @param pAnidada: "true" si las respuestas consultadas son para la pregunta anidada  
	 */
	public String obtenerRespuestasGuardadas(int pNumeroPregunta, Boolean pAnidada, int pNumeroOpcion){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		String sRespuestas = _dataAccess.obtenerRespuestas(nNegocioId, pNumeroPregunta, pAnidada, pNumeroOpcion);
		_dataAccess.close();
		return sRespuestas;
	}
	
	/**
	 * Obtiene las respuestas que tienen un valor numerico.
	 * @param pNumeroPregunta
	 * @param pAnidada: Indica si la pregunta esta anidada.
	 * @return lista de opciones con el valor y la posicion de la respuesta en la lista.
	 */
	public ArrayList<int[]> obtenerRespuestasGuardadasConValor(int pNumeroPregunta, Boolean pAnidada){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		ArrayList<int[]> arrayRespuestas = _dataAccess.obtenerRespuestasConValor(nNegocioId, pNumeroPregunta, pAnidada, "");
		_dataAccess.close();
		return arrayRespuestas;
	}
	
	public ArrayList<int[]> obtenerRespuestasGuardadasConValor(int pNumeroPregunta, Boolean pAnidada, int pOpcionAnidada){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		ArrayList<int[]> arrayRespuestas = _dataAccess.obtenerRespuestasConValor(nNegocioId, pNumeroPregunta, pAnidada, Integer.toString(pOpcionAnidada));
		_dataAccess.close();
		return arrayRespuestas;
	}
	
	/**
	 * Actualiza el ID del negocio nuevo en el formulario.
	 * @param pCodigos
	 */
	public void actualizarIdNuevoNegocio(String[] pCodigos){
		_dataAccess.openForReading();
		_dataAccess.actualizarIdPreguntaVersionamiento(Integer.parseInt(pCodigos[0]), Integer.parseInt(pCodigos[1]));
		_dataAccess.close();
	}
	
	/**
	 * Arreglo con los datos para el versionamiento de Formularios.
	 * @return
	 */
	/*public String arrayFormulariosVersionamiento(String pUid){
		_dataAccess.openForReading();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		JSONObject arrayFormularios = _dataAccess.obtenerFormularios(pCodigoUsuario, pUid);
		_dataAccess.close();
		if(arrayFormularios == null)
			return null;
		return arrayFormularios.toString();
	}*/
	
	/**
	 * Actualiza el estado "Activo" de las preguntas de un negocio para indicar si han sido versionadas.
	 * @param pEstado
	 */
	public void actualizarEstadoActivoPregunta(int pCodigoNegocio, int pEstado){
		_dataAccess.openForWriting();
		_dataAccess.actualizarEstadoActualizadoPregunta(pCodigoNegocio, pEstado);
		_dataAccess.close();
	}
	
	/**
	 * Retorna la lista de opciones marcadas en la penultima pregunta.
	 * @return
	 */
	public int[]listaOpcionesPenultimaPregunta(){
		_dataAccess.openForWriting();
		int[] listaOpciones = _dataAccess.listaOpcionesPenultimaPregunta();
		_dataAccess.close();
		return listaOpciones;
	}
	
	public int[]listaOpcionesPorPregunta(int pCodigoPreguntaDependencia){
		_dataAccess.openForWriting();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int nNumeroPregunta = _dataAccess.obtenerNumeroPregunta(pCodigoPreguntaDependencia);
		String sRespuestas = _dataAccess.obtenerRespuestas(nNegocioId, nNumeroPregunta, false, -1);
		String[] sRespuestasSplit = sRespuestas.split("#");
		int[] listaOpciones = new int[sRespuestasSplit.length];
		int cont = 0;
		for(String sRespuestaActual : sRespuestasSplit){
			listaOpciones[cont] = Integer.parseInt(sRespuestaActual);
			cont ++;
		}
		_dataAccess.close();
		return listaOpciones;
	}
	
	/**
	 * Borra todos las preguntas de un usuario.
	 */
	public void borrarPreguntas(){
		_dataAccess.openForWriting();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarPreguntas(pCodigoUsuario);
		_dataAccess.close();
	}
}