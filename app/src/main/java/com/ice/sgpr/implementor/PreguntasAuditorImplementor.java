package com.ice.sgpr.implementor;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.ice.sgpr.dataaccess.PreguntasAuditoriaDataAccess;
import com.ice.sgpr.entidades.Pregunta;

/**
 * Implementor para la insercion u consulta de datos que corresponden a
 * las preguntas del auditor.
 * @author eperaza
 * fecha de creacion: 27.03.15.
 */
@SuppressLint("SimpleDateFormat")
public class PreguntasAuditorImplementor{
private PreguntasAuditoriaDataAccess _dataAccess;
private static PreguntasAuditorImplementor _instancia;
	
	private PreguntasAuditorImplementor()
	{
		_dataAccess = new PreguntasAuditoriaDataAccess();
	}
	
	public static PreguntasAuditorImplementor getInstance(){
		if(_instancia == null)
			_instancia = new PreguntasAuditorImplementor();
		return _instancia;
	}
	
	/**
	 * Agrega la informacion de la pregunta contestada a la BD.
	 */
	public void insertarPregunta(Pregunta pPregunta){
		_dataAccess.openForReading();
		int nCodigoUsuario = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		
		_dataAccess.insertarNuevaPregunta(nNegocioId, pPregunta.getCodigoPreguntaAuditor(), pPregunta.getCodigoRespuesta1(), pPregunta.getCodigoRespuesta2(), 
				pPregunta.getCodigoRespuesta3(), nCodigoUsuario);
		_dataAccess.close();
	}
	
	
	/**
	 * Obtiene un array con los numeros de preguntas que han sido contestadas.
	 */
/*	public int[] obtenerNumerosPreguntasContestadas(){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int[] preguntasContestadas = _dataAccess.obtenerNumeroPreguntasContestadas(nNegocioId);
		_dataAccess.close();
		return preguntasContestadas;
	}
*/
	
	/**
	 * Obtiene la cantidad de preguntas contestadas de un operador.
	 */
	public int obtenerCantidadRespuestas(String pCodigoOperador){
		_dataAccess.openForReading();
		if(NegociosImplementor.getInstance().obtenerNegocioActivo() == null)
			return 0;
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int preguntasContestadas = _dataAccess.obtenerCantidadRespuestas(nNegocioId, pCodigoOperador);
		_dataAccess.close();
		return preguntasContestadas;
	}
	
	/**
	 * Obtiene la lista de respuestas marcadas previamente en una pregunta.
	 * @param pCodigoOperador
	 */
	public List<String> obtenerRespuestasElementosGuardados(String pCodigoOperador){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		List<String> sRespuestas = _dataAccess.obtenerRespuestasElementos(nNegocioId, pCodigoOperador);
		_dataAccess.close();
		return sRespuestas;
	}
	
	/**
	 * 
	 * @param pCodigoOperador
	 * @param pCodigoElemento
	 * @return
	 */
	public String[] obtenerRespuestasEstadosGuardados(String pCodigoOperador, String pCodigoElemento){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		String[] sRespuestas = _dataAccess.obtenerRespuestasEstados(nNegocioId, pCodigoOperador, pCodigoElemento);
		_dataAccess.close();
		return sRespuestas;
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
	public String arrayFormulariosVersionamientoAuditor(String pUid, String[] arrayOperadoresAuditor, int pIdNegocio){
		_dataAccess.openForReading();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		JSONObject arrayFormulariosAudit = _dataAccess.obtenerFormulariosAudit(pCodigoUsuario, pUid, arrayOperadoresAuditor, pIdNegocio);
		_dataAccess.close();
		if(arrayFormulariosAudit == null)
			return null;
		return arrayFormulariosAudit.toString();
	}
	
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
	 * Borra todos las preguntas de un usuario.
	 */
	public void borrarPreguntas(){
		_dataAccess.openForWriting();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarPreguntasPorUsuario(pCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Borra los estados de una pregunta.
	 * @param pCodigoPregunta
	 * @param pCodigoRespuesta1
	 * @param pCodigoRespuesta2
	 */
	public void borrarEstados(String pCodigoPregunta, String pCodigoRespuesta1, String pCodigoRespuesta2){
		_dataAccess.openForWriting();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		_dataAccess.borrarPreguntasPorCodigoPreguntaYRespuesta(pCodigoPregunta, pCodigoRespuesta1, pCodigoRespuesta2, 
				Integer.parseInt(pCodigoUsuario), nNegocioId);
		_dataAccess.close();
	}
	
	public void borrarOperadores(){
		_dataAccess.openForWriting();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		_dataAccess.borrarOperadores(pCodigoUsuario, nNegocioId);
		_dataAccess.close();
	}
	
	/**
	 * Obtiene la cantidad de preguntas contestadas.
	 */
	public int obtenerCantidadPreguntasContestadasAuditoria(){
		_dataAccess.openForReading();
		if(NegociosImplementor.getInstance().obtenerNegocioActivo() == null)
			return 0;
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int[] preguntasContestadas = _dataAccess.obtenerNumeroPreguntasContestadasAuditoria(nNegocioId);
		_dataAccess.close();
		if(preguntasContestadas == null)
			return 0;
		return preguntasContestadas.length;
	}
	
	/**
	 * Retorna la cantidad de formularios sin versionar.
	 * @return
	 */
	public int obtenerCantidadFormNoVersionados(){
		_dataAccess.openForReading();
		int cantidadSinVersionar = _dataAccess.obtenerCantidadFormNoVersionados();
		_dataAccess.close();
		return cantidadSinVersionar;
	}
	
	public void borrarPreguntasPorIdNegocio(int idNegocio){

			_dataAccess.openForWriting();
			_dataAccess.borrarPreguntasPorIdNegocio(idNegocio);
			_dataAccess.close();
	}
}