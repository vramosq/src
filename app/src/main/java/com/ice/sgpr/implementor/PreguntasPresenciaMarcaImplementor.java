package com.ice.sgpr.implementor;

import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.ice.sgpr.dataaccess.PresenciaMarcaDataAccess;
import com.ice.sgpr.entidades.Pregunta;

/**
 * Implementor para la consulta las preguntas de presencia de marca.
 * @author eperaza
 * fecha de creacion: 26/06/2014.
 */
@SuppressLint("SimpleDateFormat")
public class PreguntasPresenciaMarcaImplementor{
private PresenciaMarcaDataAccess _dataAccess;
private static PreguntasPresenciaMarcaImplementor _instancia;
	
	private PreguntasPresenciaMarcaImplementor()
	{
		_dataAccess = new PresenciaMarcaDataAccess();
	}
	
	public static PreguntasPresenciaMarcaImplementor getInstance(){
		if(_instancia == null)
			_instancia = new PreguntasPresenciaMarcaImplementor();
		return _instancia;
	}
	
	/**
	 * Agrega la informacion de la pregunta contestada a la BD.
	 */
	public void insertarActualizarPreguntaPresenciaMarca(Pregunta pPregunta){
		_dataAccess.openForReading();
		int nCodigoUsuario = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		_dataAccess.insertarActualizarNuevaPregunta(nNegocioId, nCodigoUsuario, pPregunta.getNumero(), pPregunta.getCodigoPreguntaPrincipal(), 
				pPregunta.getRespuesta1(), pPregunta.getCodigoRespuesta1());
		_dataAccess.close();
	}
	
	/**
	 * Obtiene un array con los numeros de preguntas de presencia de marca que han sido contestadas.
	 */
	public int[] obtenerNumerosPreguntasContestadas(){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int[] preguntasContestadas = _dataAccess.obtenerNumeroPreguntasContestadasPM(nNegocioId);
		_dataAccess.close();
		return preguntasContestadas;
	}
	
	/**
	 * Borra las preguntas de presencia de marca de un usuario.
	 * @param pCodigoPregunta: Codigo de pregunta a borrar. -1 = Todas
	 */
	public void borrarPreguntasPresenciaMarca(int pCodigoPregunta){
		_dataAccess.openForWriting();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarPreguntasPresenciaMarca(sCodigoUsuario, pCodigoPregunta);
		_dataAccess.close();
	}
	
	/**
	 * Obtiene la lista de respuestas marcadas previamente en una pregunta.
	 * @param pNumeroPregunta
	 */
	public String obtenerRespuestasGuardadas(int pNumeroPregunta,int pNumeroOpcion){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		String sRespuestas = _dataAccess.obtenerRespuestas(nNegocioId, pNumeroPregunta, pNumeroOpcion);
		_dataAccess.close();
		return sRespuestas;
	}
	
	/**
	 * Obtiene la cantidad de preguntas de presencia de marca contestadas.
	 */
	public int obtenerCantidadPreguntasContestadasPM(){
		_dataAccess.openForReading();
		if(NegociosImplementor.getInstance().obtenerNegocioActivo() == null)
			return 0;
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int[] preguntasContestadas = _dataAccess.obtenerNumeroPreguntasContestadasPM(nNegocioId);
		_dataAccess.close();
		if(preguntasContestadas == null)
			return 0;
		return preguntasContestadas.length;
	}
	
	/**
	 * Arreglo con los datos para el versionamiento de Formularios.
	 * @return
	 */
	public String arrayFormulariosPMVersionamiento(String pUid){
		_dataAccess.openForReading();
		String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		JSONObject arrayFormularios = _dataAccess.obtenerFormulariosPM(pCodigoUsuario, pUid);
		_dataAccess.close();
		if(arrayFormularios == null)
			return null;
		return arrayFormularios.toString();
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
	 * Borra todas las preguntas de un negocio
	 * @param pCodigoNegocio
	 */
	public void borrarTodasPreguntasPresenciaMarca(int pCodigoNegocio){
		_dataAccess.openForWriting();
		_dataAccess.borrarTodasPreguntasPresenciaMarca(pCodigoNegocio);
		_dataAccess.close();
	}
}