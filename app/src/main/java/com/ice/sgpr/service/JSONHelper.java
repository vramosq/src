package com.ice.sgpr.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Bitacora;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.entidades.Ruta;
import com.ice.sgpr.implementor.NegociosImplementor;

public class JSONHelper {
	
	private static JSONHelper _instance;
	private static String NOMBRE_USUARIO = "Usuario";
	private static String CONTRASENA = "Password";
	private static final String CODIGO = "Codigo";
	private static final String USUARIO = "Usuario";
	private static final String ROL = "Rol";
	private static final String BITACORAS = "Bitacoras";
	private static final String FORMULARIOS = "Formularios";
	private static final String NEGOCIOS = "PtosVenta";
	private static final String SP = "SP";

	//Siglas:
	private static final String CODIGO_SIGLAS = "CD";
	private static final String BASE_DATOS_SIGLAS = "BD";
	private static final String NOMBRE_SIGLAS = "NM";
	private static final String DESCRIPCION_NEGOCIO_SIGLAS = "DS";
	private static final String FRECUENCIA_SIGLAS = "PP";
	private static final String DIRECCION_NEGOCIO_SIGLAS = "DR";
	private static final String TIPO_NEGOCIO_SIGLAS = "TC";
	private static final String PRIORIDAD_NEGOCIO_SIGLAS = "PR";
	private static final String TELEFONO_NEGOCIO_SIGLAS = "TL";
	private static final String TELEFONO_CONTACTO_SIGLAS = "PC";
	private static final String NOMBRE_CONTACTO_NEGOCIO_SIGLAS = "NC";
	private static final String LATITUD_SIGLAS = "LA";
	private static final String LONGITUD_SIGLAS = "LO";
	private static final String ULTIMA_VISITA_NEGOCIO_SIGLAS = "UV";
	private static final String TIPO_COMERCIO_SIGLAS = "TP";
	private static final String FECHA_SIGLAS = "FC";
	private static final String FECHA_INICIO_SIGLAS = "FECI";
	private static final String FECHA_FIN_SIGLAS = "FECF";
	private static final String INDICADOR_SIGLAS = "IND";
	private static final String PUNTO_DE_VENTA_SIGLAS = "PTV";
	private static final String RUTA_SIGLAS = "RTA";
	private static final String CELULAR_CONTACTO_SIGLAS = "CC";
	private static final String VALOR_SIGLAS = "VL";
	private static final String RESPUESTA_SIGLAS = "RS";
	private static final String RESPUESTA_PRINCIPAL_SIGLAS = "RP";
	private static final String PREGUNTA_SIGLAS = "PP";
	private static final String ULTIMA_VISITA_SIGLAS = "UV";
	private static final String RUTA_PENDIENTE = "WW";
	private static final String RESPUESTA_PRINCIPAL_SEC = "RPP";
	private static final String HABILITADO_SIGLAS = "HAB";
	private static final String DISTRITO_SIGLAS = "DT";
	private static final String UID = "UID";
	private static final String ID_TEMPORAL_NEGOCIO_SIGLAS = "CT";
	private static final String CODIGO_COLOR_SIGLAS = "CC";
	private static final String CANTON_SIGLAS = "CN";
	private static final String PROVINCIA_SIGLAS = "PV";
	private static final String OBSERVACIONES_SIGLAS = "OB";
	private static final String FECHA_OBSERVACION_SIGLAS = "FO";
	private static final String USR_ULTIMA_OBSERVACION = "UUO";
	private static final String EMAIL_SIGLAS = "EM";
	private static final String USUARIO_SIGLAS = "US";
	
	//Nombres Objetos:
	private static final String PARAMETROS = "Parametros";
	private static final String PUNTOS_DE_VENTA = "PuntosVenta";
	private static final String PUNTOS_DE_VENTA_VERSIONAMIENTO = "PtosVenta";
	private static final String RUTAS = "Rutas";
	private static final String USUARIOS = "Usuarios";
	private static final String TIPOS_SUP_OPER = "TiposSupOp";
	private static final String SOCIOS = "Socios";
	
	private static final String PREGUNTA_AUDITOR = "quest";
	private static final String ESTADO_AUDITOR_ID = "stateId";
	private static final String ELEMENTO_AUDITOR_ID = "elemId";
	private static final String LABEL_AUDITOR = "label";
	private static final String ESTADOS_AUDITOR = "states";
	private static final String ELEMENTOS_AUDITOR = "elem";
	private static final String OPERADOR_AUDITOR = "oper";
	private static final String RESULTADO_AUDITOR = "result";
	private static final String FECHA_FORMULARIO_AUDITOR = "dateSurvey";
	private static final String CODIGO_NEGOCIO_AUDITOR = "idStorePoint";
	private static final String OWN_USER_BIT = "ownUsrBit";
	private static final String BITACORA_ID_AUDITOR = "idLogSurvey";
	private static final String COLOR_AUDITOR = "color";
	private static final String FECHA_ULT_OBS_AUDITOR = "userLstObs";
	private static final String OBSERVACIONES_AUDITOR = "userObs";
	private static final String USER_OBS_NAME = "userObsName";

	
	private JSONHelper()
	{
		
	}
	
	public static JSONHelper getInstance()
	{
		if (_instance == null)
			_instance = new JSONHelper();
		return _instance;
	}

	/**
	 * Genera un Json para las credenciales de login del usuario.
	 * @param pUsuario
	 * @param pContrasena
	 * @return JSON para el login.
	 * @throws org.json.JSONException
	 */
	public String obtenerJsonLogin(String pUsuario, String pContrasena) throws JSONException 
	{
		JSONObject obj = new JSONObject();
		obj.put(NOMBRE_USUARIO, pUsuario);
		obj.put(CONTRASENA, pContrasena);
		return obj.toString();
	}
	
	/**
	 * Genera un JSON para la bitacora que sera enviada para versionamiento.
	 * @return JSON para la bitacora.
	 * @throws org.json.JSONException
	 */
	public String obtenerJsonBitacoraVersionamiento(List<Bitacora> pListaBitacoras, String pUid) throws JSONException 
	{
		JSONArray bitacorasArray = new JSONArray();
		
		for(Bitacora bitacoraActual:pListaBitacoras){
			JSONObject obj = new JSONObject();
			obj.put(LATITUD_SIGLAS, Double.parseDouble(bitacoraActual.getLatitud()));
			obj.put(LONGITUD_SIGLAS, Double.parseDouble(bitacoraActual.getLongitud()));			
			obj.put(FECHA_INICIO_SIGLAS, bitacoraActual.getFechaInicio());
			obj.put(FECHA_FIN_SIGLAS, bitacoraActual.getFechaFin());
			obj.put(INDICADOR_SIGLAS, bitacoraActual.getActualizada());
			obj.put(PUNTO_DE_VENTA_SIGLAS, bitacoraActual.getNegocioId());
			obj.put(RUTA_SIGLAS, bitacoraActual.getRutaId());
			obj.put(UID, pUid);
			bitacorasArray.put(obj);
		}
		JSONObject jsonRespuesta = new JSONObject();
		jsonRespuesta.put(SP, SgprService.SP_BITACORA_VERSIONAMIENTO);
		jsonRespuesta.put(BITACORAS, bitacorasArray.toString());
		return jsonRespuesta.toString();
	}
	
	/**
	 * Obtiene el Json para la lista de negocios.
	 * @param pListaNegocios
	 * @param pNuevoNegocio: True si se desea el json para los negocios nuevos, false para los negocios de la ruta.
	 * @return
	 * @throws org.json.JSONException
	 */
	public String obtenerJsonNegocioVersionamiento(List<Negocio> pListaNegocios, Boolean pNuevoNegocio, String uid) throws JSONException 
	{
		JSONArray negociosArray = new JSONArray();
		
		for(Negocio negocioActual:pListaNegocios){
			JSONObject obj = new JSONObject();
			obj.put(TELEFONO_CONTACTO_SIGLAS, negocioActual.get_sTelefonoContacto());
			obj.put(ULTIMA_VISITA_NEGOCIO_SIGLAS, negocioActual.get_sUltimaVisita());
			obj.put(TIPO_NEGOCIO_SIGLAS, negocioActual.get_nTipoComercio());
			obj.put(NOMBRE_CONTACTO_NEGOCIO_SIGLAS, negocioActual.get_sNombreContacto());
			obj.put(CODIGO_SIGLAS, negocioActual.get_nNegocioId());
			obj.put(NOMBRE_SIGLAS, negocioActual.get_sNombre());
			obj.put(DIRECCION_NEGOCIO_SIGLAS, negocioActual.get_sDireccion());
			obj.put(DESCRIPCION_NEGOCIO_SIGLAS, negocioActual.get_sDescripcion());
			obj.put(PRIORIDAD_NEGOCIO_SIGLAS, negocioActual.get_nPrioridad());
			obj.put(CELULAR_CONTACTO_SIGLAS, negocioActual.get_sCelularContacto());
			obj.put(LATITUD_SIGLAS, Double.parseDouble(negocioActual.get_sLatitud()));
			obj.put(TELEFONO_NEGOCIO_SIGLAS, negocioActual.get_sTelefono());
			obj.put(LONGITUD_SIGLAS, Double.parseDouble(negocioActual.get_sLongitud()));
			obj.put(HABILITADO_SIGLAS, ((negocioActual.is_bHabilitado())? 1 : 0));
			obj.put(DISTRITO_SIGLAS, negocioActual.get_nCodigoDistrito());
			obj.put(CANTON_SIGLAS, negocioActual.get_nCodigoCanton());
			obj.put(PROVINCIA_SIGLAS, negocioActual.get_nCodigoProvincia());
			obj.put(UID, uid);
			obj.put(ID_TEMPORAL_NEGOCIO_SIGLAS, negocioActual.get_nNegocioId());
			obj.put(EMAIL_SIGLAS, negocioActual.get_sEmail());
			//Observaciones
			obj.put(OBSERVACIONES_SIGLAS, negocioActual.get_sObservaciones());
			obj.put(FECHA_OBSERVACION_SIGLAS, negocioActual.get_sFechaObservaciones());
			if(negocioActual.get_nUsrModificaObservacion() == -1 || negocioActual.get_nUsrModificaObservacion() == 0)
				obj.put(USR_ULTIMA_OBSERVACION, null);
			else
				obj.put(USR_ULTIMA_OBSERVACION, negocioActual.get_nUsrModificaObservacion());
			
			negociosArray.put(obj);
		}
		JSONObject jsonRespuesta = new JSONObject();
		if(!pNuevoNegocio)
			jsonRespuesta.put(SP, SgprService.SP_NEGOCIO_VERSIONAMIENTO);
		else
			jsonRespuesta.put(SP, SgprService.SP_NUEVO_NEGOCIO_VERSIONAMIENTO);
		jsonRespuesta.put(NEGOCIOS, negociosArray.toString());
		return jsonRespuesta.toString();
	}
		
	/**
	 * Decodifica el JSON de respuesta del servidor.
	 * @param pResponse
	 * @return
	 * @throws org.json.JSONException
	 */
	public String[] descifrarRespuestaLogin(String pResponse) throws JSONException {
		JSONObject obj = new JSONObject(pResponse);
		String[] sRespuesta = new String []{obj.getString(CODIGO), obj.getString(USUARIO), obj.getString(ROL)};
		return sRespuesta;
	}
	
	public String descifrarRespuestaVersionamiento(String pResponse) throws JSONException {
		JSONObject obj = new JSONObject(pResponse);
		return obj.getString(CODIGO);
	}
	
	/**
	 * Obtiene una lista de negocios nuevos con el Id temporal de la aplicaci�n y el ID asignado en el SP.
	 * @return
	 * @throws org.json.JSONException
	 */
	public List<String[]> descifrarRespuestaVersionamientoNuevoNegocio(String pJSONString) throws JSONException {
		List<String[]> listaDatosNuevoNegocio = new ArrayList<String[]>();
		JSONObject nuevosNegociosObject = new JSONObject(pJSONString);
		JSONArray nuevosNegociosArray = nuevosNegociosObject.getJSONArray(PUNTOS_DE_VENTA_VERSIONAMIENTO);
		int nCantidadNegocios = nuevosNegociosArray.length();
		JSONObject obj;
		
		for (int i = 0; i < nCantidadNegocios; i++)
		{
			obj = nuevosNegociosArray.getJSONObject(i);
			try
			{
				String[] sDatosNegocio = {obj.getString(CODIGO_SIGLAS), obj.getString(BASE_DATOS_SIGLAS)};
				listaDatosNuevoNegocio.add(sDatosNegocio);
			}
			catch(Exception ex)
			{
				Log.i(Constantes.LOG_TAG, "Error new commerce: " + ex.getMessage());
			}
		}		
		return listaDatosNuevoNegocio;
	}
	
	/**
	 * Obtiene las rutas desde un WS.
	 * @param pJSONString
	 * @return
	 * @throws org.json.JSONException
	 */
	public List<Ruta> obtenerRutasDesdeJson(String pJSONString) throws JSONException {
		List<Ruta> lRutas = new ArrayList<Ruta>();
		JSONObject rutasObject = new JSONObject(pJSONString);
		JSONArray rutasArray = rutasObject.getJSONArray(RUTAS);
		int nCantidadRutas = rutasArray.length();
		JSONObject obj;
		
		for (int i = 0; i < nCantidadRutas; i++)
		{
			obj = rutasArray.getJSONObject(i);
			try
			{
				String sUltimaVisita = obj.getString(ULTIMA_VISITA_SIGLAS);
				if(sUltimaVisita.equals(""))
					sUltimaVisita = "";
				lRutas.add(
						new Ruta(
								obj.getInt(CODIGO_SIGLAS),
								obj.getString(NOMBRE_SIGLAS),
								obj.getString(DESCRIPCION_NEGOCIO_SIGLAS),
								obj.getString(FRECUENCIA_SIGLAS),
								sUltimaVisita,
								Integer.parseInt(obj.getString(RUTA_PENDIENTE)),
								obj.getString(LATITUD_SIGLAS),
								obj.getString(LONGITUD_SIGLAS),
								0
								));
			}
			catch(Exception ex)
			{
				Log.i(Constantes.LOG_TAG, "Error georeference: " + ex.getMessage());
			}
		}
		return lRutas;
	}
	
	/**
	 * Obtiene los negocios desde un WS. Loas guarda en la BD del tel�fono.
	 * @param pJSONString
	 * @return
	 * @throws org.json.JSONException
	 */
	public Boolean obtenerNegociosDesdeJson(String pJSONString, int pRutaId) throws JSONException {
		JSONObject negociosObject = new JSONObject(pJSONString);
		JSONArray negociosArray = negociosObject.getJSONArray(PUNTOS_DE_VENTA);
		int nCantidadNegocios = negociosArray.length();
		JSONObject obj;
		if(nCantidadNegocios > 0){
			for (int i = 0; i < nCantidadNegocios; i++)
			{
				obj = negociosArray.getJSONObject(i);
				Negocio negocio = new Negocio(
						pRutaId, 
						obj.getInt(CODIGO_SIGLAS), 
						obj.getString(LATITUD_SIGLAS), 
						obj.getString(LONGITUD_SIGLAS), 
						Constantes.ESTADO_NEGOCIO_SIN_VISITAR, 
						obj.getString(NOMBRE_SIGLAS), 
						obj.getString(DIRECCION_NEGOCIO_SIGLAS), 
						Constantes.ESTADO_NEGOCIO_INACTIVO, 
						obj.getString(DESCRIPCION_NEGOCIO_SIGLAS), 
						obj.getInt(TIPO_NEGOCIO_SIGLAS), 
						obj.getInt(PRIORIDAD_NEGOCIO_SIGLAS), 
						obj.getString(TELEFONO_NEGOCIO_SIGLAS), 
						obj.getString(NOMBRE_CONTACTO_NEGOCIO_SIGLAS), 
						obj.getString(TELEFONO_CONTACTO_SIGLAS), 
						obj.getString(ULTIMA_VISITA_NEGOCIO_SIGLAS),
						0,
						obj.getString(CELULAR_CONTACTO_SIGLAS),
						((obj.getInt(HABILITADO_SIGLAS) == 1)? true : false),
						obj.getInt(DISTRITO_SIGLAS),
						obj.getInt(CANTON_SIGLAS),
						obj.getInt(PROVINCIA_SIGLAS),
						obj.getString(OBSERVACIONES_SIGLAS), 
						obj.getString(FECHA_OBSERVACION_SIGLAS),
						obj.getInt(USR_ULTIMA_OBSERVACION), 
						obj.getString(EMAIL_SIGLAS)
						);
				
				NegociosImplementor.getInstance().actualizarNegocios(negocio);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Retorna una lista de parametros obtenidos del WS.
	 * @param pJSONString
	 * @return
	 * @throws org.json.JSONException
	 */
	public List<Parametro> obtenerParametrosDesdeJson(String pJSONString) throws JSONException {
		List<Parametro> lParametros = new ArrayList<Parametro>();
		JSONObject parametrosObject = new JSONObject(pJSONString);
		JSONArray parametrosArray = parametrosObject.getJSONArray(PARAMETROS);
		int nCantidadParametros = parametrosArray.length();
		JSONObject obj;
		
		for (int i = 0; i < nCantidadParametros; i++)
		{
			obj = parametrosArray.getJSONObject(i);
			try
			{
				lParametros.add(
						new Parametro(
								obj.getInt(CODIGO_SIGLAS),
								obj.getString(NOMBRE_SIGLAS),
								obj.getString(TIPO_COMERCIO_SIGLAS)
								));
			}
			catch(Exception ex)
			{
				Log.i(Constantes.LOG_TAG, "Error: " + ex.getMessage());
			}
		}
		return lParametros;
	}
	
	/**
	 * Obtiene los negocios cercanos a un punto en el mapa.
	 * @param pJSONString
	 * @return
	 * @throws org.json.JSONException
	 */
	public List<Negocio> obtenerNegociosCercanosDesdeJson(String pJSONString) throws JSONException {
		List<Negocio> lNegocios = new ArrayList<Negocio>();
		JSONObject negociosObject = new JSONObject(pJSONString);
		JSONArray negociosArray = negociosObject.getJSONArray(PUNTOS_DE_VENTA);
		int nCantidadNegocios = negociosArray.length();
		JSONObject obj;
		if(nCantidadNegocios > 0){
			for (int i = 0; i < nCantidadNegocios; i++)
			{
				obj = negociosArray.getJSONObject(i);
				Negocio negocio = new Negocio(-1, obj.getInt(CODIGO_SIGLAS), obj.getString(LATITUD_SIGLAS), obj.getString(LONGITUD_SIGLAS), -1, obj.getString(NOMBRE_SIGLAS), "", -1, 
						"", -1, -1, "", "", "", "", -1, "", true, -1, -1, -1, "", "", -1, "");
				
				lNegocios.add(negocio);
			}
		}
		return lNegocios;
	}
	
	/**
	 * Obtiene los negocios producto de una busqueda.
	 * @param pJSONString
	 * @return
	 * @throws org.json.JSONException
	 */
	public List<Negocio> obtenerNegociosBusquedaDesdeJson(String pJSONString) throws JSONException {
		List<Negocio> lNegocios = new ArrayList<Negocio>();
		JSONObject negociosObject = new JSONObject(pJSONString);
		JSONArray negociosArray = negociosObject.getJSONArray(PUNTOS_DE_VENTA);
		int nCantidadNegocios = negociosArray.length();
		JSONObject obj;
		if(nCantidadNegocios > 0){
			for (int i = 0; i < nCantidadNegocios; i++)
			{
				obj = negociosArray.getJSONObject(i);
//				Negocio negocio = new Negocio(-1, obj.getInt(CODIGO_SIGLAS), "", "", -1, obj.getString(NOMBRE_SIGLAS), obj.getString(DIRECCION_NEGOCIO_SIGLAS), -1, 
//						obj.getString(DESCRIPCION_NEGOCIO_SIGLAS), -1, -1, "", "", "", "", -1, "", false, -1, -1, -1);
				Negocio negocio = new Negocio(obj.getInt(CODIGO_SIGLAS), obj.getString(NOMBRE_SIGLAS), obj.getString(DIRECCION_NEGOCIO_SIGLAS), 
						obj.getString(DESCRIPCION_NEGOCIO_SIGLAS));
				
				lNegocios.add(negocio);
			}
		}
		return lNegocios;
	}
	
	/**
	 * Obtiene los negocios desde un WS. Loas guarda en la BD del telefono.
	 * @param pJSONString
	 * @return
	 * @throws org.json.JSONException
	 */
	public void insertarNuevoNegocioBuscado(String pJSONString) throws JSONException {
		JSONObject negocioObject = new JSONObject(pJSONString);
		Negocio negocio = new Negocio(
				-1, 
				negocioObject.getInt(CODIGO_SIGLAS), 
				negocioObject.getString(LATITUD_SIGLAS), 
				negocioObject.getString(LONGITUD_SIGLAS), 
				Constantes.ESTADO_NEGOCIO_SIN_VISITAR, 
				negocioObject.getString(NOMBRE_SIGLAS), 
				negocioObject.getString(DIRECCION_NEGOCIO_SIGLAS), 
				Constantes.ESTADO_NEGOCIO_INACTIVO, 
				negocioObject.getString(DESCRIPCION_NEGOCIO_SIGLAS), 
				negocioObject.getInt(TIPO_NEGOCIO_SIGLAS), 
				negocioObject.getInt(PRIORIDAD_NEGOCIO_SIGLAS), 
				negocioObject.getString(TELEFONO_NEGOCIO_SIGLAS), 
				negocioObject.getString(NOMBRE_CONTACTO_NEGOCIO_SIGLAS), 
				negocioObject.getString(TELEFONO_CONTACTO_SIGLAS), 
				negocioObject.getString(ULTIMA_VISITA_NEGOCIO_SIGLAS),
				0,
				negocioObject.getString(CELULAR_CONTACTO_SIGLAS),
				((negocioObject.getInt(HABILITADO_SIGLAS) == 1)? true : false),
				negocioObject.getInt(DISTRITO_SIGLAS),
				negocioObject.getInt(CANTON_SIGLAS),
				negocioObject.getInt(PROVINCIA_SIGLAS),
				negocioObject.getString(OBSERVACIONES_SIGLAS), 
				negocioObject.getString(FECHA_OBSERVACION_SIGLAS),
				negocioObject.getInt(USR_ULTIMA_OBSERVACION),
				negocioObject.getString(EMAIL_SIGLAS)
				);
				
				NegociosImplementor.getInstance().actualizarNegocios(negocio);
	}
	
	/**
	 * Json para una pregunta del formulario.
	 * @param pValor
	 * @param pRespuesta
	 * @param pRespuestaPrincipal
	 * @param pPregunta
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonNegocioPreguntaIndividual(String pValor, int pRespuesta, int pRespuestaPrincipal, int pPregunta, 
			int pRespuestaPrincipalSec) throws JSONException 
	{
			JSONObject obj = new JSONObject();
			obj.put(PREGUNTA_SIGLAS, pPregunta);
			obj.put(RESPUESTA_PRINCIPAL_SIGLAS, pRespuestaPrincipal);
			obj.put(RESPUESTA_SIGLAS, pRespuesta);
			obj.put(VALOR_SIGLAS, pValor);
			obj.put(RESPUESTA_PRINCIPAL_SEC, pRespuestaPrincipalSec);
			return obj;
	}
	
	/**
	 * Json para la consulta de negocios cercanos a un punto.
	 */
	public JSONObject obtenerJsonNegociosCercanos(Double pLatitud, Double pLongitud) throws JSONException 
	{
			JSONObject obj = new JSONObject();
			obj.put(LATITUD_SIGLAS, pLatitud);
			obj.put(LONGITUD_SIGLAS, pLongitud);
			return obj;
	}
	
	/**
	 * Json para armar el array del formulario que sera enviado al SP
	 * @param pCodigoNegocio
	 * @param pFecha
	 * @param pArrayPreguntas
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonInformacionFormularioPM(int pCodigoNegocio, String pFecha, JSONArray pArrayPreguntas, String pUid, int pColor, String pCodigoUsuario) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(RESPUESTA_PRINCIPAL_SIGLAS, pArrayPreguntas);
		obj.put(FECHA_SIGLAS, pFecha);
		obj.put(CODIGO_SIGLAS, pCodigoNegocio);
		obj.put(UID, pUid);
		obj.put(CODIGO_COLOR_SIGLAS, pColor);
		obj.put(USUARIO_SIGLAS, Integer.parseInt(pCodigoUsuario));
		return obj;
	}
	
	/**
	 * Retorna el Json que sera enviado al SP de Formularios.
	 * @param pArrayFormularios
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonFormularioParaEnviar(JSONArray pArrayFormularios) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(SP, SgprService.SP_FORMULARIO_VERSIONAMIENTO);
		obj.put(FORMULARIOS, pArrayFormularios);
		return obj;
	}
	
	/**
	 * Json para armar el array del formulario que sera enviado al SP
	 * @param pFecha
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonInformacionSeguimiento(String pUid, String pFecha, String pLatitud, String pLongitud) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(UID, Integer.parseInt(pUid));
		obj.put(FECHA_SIGLAS, pFecha);
		obj.put(LATITUD_SIGLAS,  Double.parseDouble(pLatitud));
		obj.put(LONGITUD_SIGLAS,  Double.parseDouble(pLongitud));
		return obj;
	}
	
	/**
	 * Crea el JSON para las preguntas (con su respuesta) para los formularios de presencia de marca.
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonNegocioPreguntaPM(int pCodigoPregunta, int pCodigoRespuesta) throws JSONException 
	{
			JSONObject obj = new JSONObject();
			obj.put(PREGUNTA_SIGLAS, pCodigoPregunta);
			obj.put(RESPUESTA_SIGLAS, pCodigoRespuesta);
			return obj;
	}
	
	public JSONObject obtenerJsonFormularioPMParaEnviar(JSONArray pArrayFormularios) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(SP, SgprService.SP_FORMULARIO_VERSIONAMIENTO);
		obj.put(FORMULARIOS, pArrayFormularios);
		return obj;
	}
	
	/**
	 * Arma el formulario de auditoria para enviar.
	 * @param pJsonFormularios
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonFormularioAuditorParaEnviar(JSONObject pJsonFormularios) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(SP, SgprService.SP_FORMULARIO_AUDIT_VERSIONAMIENTO);
		obj.put(FORMULARIOS, pJsonFormularios);
		return obj;
	}
	
	/**
	 * Genera el JSON de los estados del formulario de un auditor.
	 * @param pRespuesta3: Estados
	 * @param pPregunta
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonNegocioPreguntaAuditor(String pRespuesta3, String pPregunta) throws JSONException 
	{
			JSONObject obj = new JSONObject();
			obj.put(ESTADO_AUDITOR_ID, pRespuesta3);
			obj.put(LABEL_AUDITOR, "");
			obj.put(PREGUNTA_AUDITOR, pPregunta);
			return obj;
	}
	
	/**
	 * Genera el JSON de los elementos de un auditor.
	 * @param pRespuesta2: Elementos
	 * @param pPregunta
	 * @param arrayEstados
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonNegocioEstadoAuditor(String pRespuesta2, String pPregunta, JSONArray arrayEstados) throws JSONException 
	{
			JSONObject obj = new JSONObject();
			obj.put(ELEMENTO_AUDITOR_ID, pRespuesta2);
			obj.put(PREGUNTA_AUDITOR, pPregunta);
			obj.put(LABEL_AUDITOR, "");
			obj.put(ESTADOS_AUDITOR, arrayEstados);
			return obj;
	}
	
	/**
	 * Genera el JSON de los operadores de un auditor.
	 * @param pRespuesta1: Operador
	 * @param pPregunta
	 * @param arrayElementos
	 * @return
	 * @throws org.json.JSONException
	 */
	public JSONObject obtenerJsonNegocioElementosAuditor(String pRespuesta1, String pPregunta, JSONArray arrayElementos) throws JSONException 
	{
			JSONObject obj = new JSONObject();
			obj.put(OPERADOR_AUDITOR, pRespuesta1);
			obj.put(PREGUNTA_AUDITOR, pPregunta);
			obj.put(LABEL_AUDITOR, "");
			obj.put(ELEMENTOS_AUDITOR, arrayElementos);
			return obj;
	}
	
	/**
	 * Arma el JSON para la informacion del negocio.
	 * Altera la fecha del formulario, agregandole 1 minuto, para evitar conflictos en la base de datos
	 * cuando se ingresa junto al de presencia de marca.
	 * @param pCodigoNegocio
	 * @param pFecha
	 * @param pArrayOperadores
	 * @param pUid
	 * @param pColor
	 * @param pObservaciones
	 * @param sFechaObservaciones
	 * @param pUsrUltimaObservacion
	 * @return
	 * @throws org.json.JSONException
	 */
	@SuppressLint("SimpleDateFormat")
	public JSONObject obtenerJsonInformacionFormularioAuditoria(int pCodigoNegocio, String pFecha, JSONArray pArrayOperadores, String pUid, String pColor, 
			String pObservaciones, String sFechaObservaciones, int pUsrUltimaObservacion, String ownerUser) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(RESULTADO_AUDITOR, pArrayOperadores);
		obj.put(OWN_USER_BIT, Integer.parseInt(ownerUser));
		obj.put(BITACORA_ID_AUDITOR, "");
		obj.put(FECHA_ULT_OBS_AUDITOR, sFechaObservaciones);
		obj.put(FECHA_FORMULARIO_AUDITOR, pFecha);
		obj.put(OBSERVACIONES_AUDITOR, pObservaciones);
		if(pUsrUltimaObservacion != -1 && pUsrUltimaObservacion != 0)
			obj.put(USER_OBS_NAME, pUsrUltimaObservacion);
		else
			obj.put(USER_OBS_NAME, "");
		obj.put(CODIGO_NEGOCIO_AUDITOR, pCodigoNegocio);
		obj.put(UID, pUid);
		obj.put(COLOR_AUDITOR, pColor);
		return obj;
	}
	
	public JSONObject obtenerJsonSeguimientosParaEnviar(JSONArray pArraySeguimientos) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(SP, SgprService.SP_SEGUIMIENTOS_GUARDADOS);
		obj.put("Info", pArraySeguimientos);
		return obj;
	}

	/**
	 * Se crea el json a guardar en la BD y para enviar mediante el versionado
     * @param parentObjetName Siglas del formulario padre.
	 */
	public JSONObject obtenerJsonOpcionesOperadores(JSONArray arrayOpciones, String parentObjetName) throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put(parentObjetName, arrayOpciones);
		return obj;
	}

	/**
	 * Método que obtiene la información a partir del json de productos disponibles.
	 * Recorre el json onbteniendo las opciones y luego recorre el array hijo para obtener los operadores,
     * que se concatenan. Sólo se agregan a la lista aquellas opciones que tienen operadores.
     * {"1b1","c;m;in"}
	 * @Type: Tipo de información a cargar (SD: Superv. distribuidores, PM: Presencia Marca
	 */
	public ArrayList<String[]> obtenerInfoSupervTerceros(String pJSONString, String pType) throws JSONException {
        JSONObject obj = new JSONObject(pJSONString);
        JSONArray selectionsJsonList = obj.getJSONArray(pType);
        ArrayList<String[]>selectionsList = new ArrayList<>();

        int itemsCount = selectionsJsonList.length();
        JSONObject tempObj;
        if(itemsCount > 0){
            for (int i = 0; i < itemsCount; i++)
            {
                tempObj = selectionsJsonList.getJSONObject(i);
                Iterator<String> keys= tempObj.keys();
                while (keys.hasNext())
                {
                    String keyValue = keys.next();
                    String operatorsConcat = "";

                    if(!pType.equals(Constantes.PRES_MARCA) && !pType.equals(Constantes.SUPERV_DIST)) //Los de publicidad, promocional, pop y observaciones no tienen un array, sólo un valor
                            operatorsConcat = tempObj.getString(keyValue);
                    else { //Presencia de marca y publicidad tienen un array como valor
                        //Array hijos:
                        JSONArray arrayOperators = tempObj.getJSONArray(keyValue);
                        int cantOptions = arrayOperators.length();
                        for (int x = 0; x < cantOptions; x++) {
                            operatorsConcat += arrayOperators.getString(x) + ";";
                        }
                    }

                    if (!operatorsConcat.equals("")) {
                        if(pType.equals(Constantes.PRES_MARCA) && pType.equals(Constantes.SUPERV_DIST)) //Si no es publicidad, promocional, pop ni observaciones, se quita el último caracter, que podría ser un ";"
                            operatorsConcat = operatorsConcat.substring(0, operatorsConcat.length() - 1);
                        String[] objectForList = {keyValue, operatorsConcat};
                        selectionsList.add(objectForList);
                    }
                }
            }
        }
        return selectionsList;
	}

	/********* TERCEROS ********/
	/**
	 * Json de terceos, arma el json con el objeto a enviar y el SP correspondiente
	 * @param pArrayTerceros
	 * @param pTipo
	 * @return
	 * @throws JSONException
	 */
	public JSONObject obtenerJsonTercerosParaEnviar(JSONArray pArrayTerceros, String pTipo) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(SP, pTipo);
		obj.put("Info", pArrayTerceros);
		return obj;
	}

	/**
	 * Obtiene el objeto json de la opción seleccionada para guardar en los formularios de
	 * teceros
	 * @param pOperador
	 * @return
	 * @throws JSONException
	 */
	public JSONObject obtenerJsonOperador(String pOperador) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(Constantes.OPTION, pOperador);
		return obj;
	}

	/**
	 * Opción que retorna la opción seleccionada en los formularios de terceros
	 * @param pOption
	 * @return
	 * @throws JSONException
	 */
	public String descifrarOpcion(String pOption) throws JSONException {
		JSONObject obj = new JSONObject(pOption);
		return obj.getString(Constantes.OPTION);
	}

	/**
	 * Recibe la lista de usuarios y la pasa de json a una lista
	 * @param pJSONString
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String[]> obtenerUsuariosDesdeJson(String pJSONString) throws JSONException {
		ArrayList<String[]> lUsuarios = new ArrayList<>();
		JSONObject parametrosObject = new JSONObject(pJSONString);
		JSONArray parametrosArray = parametrosObject.getJSONArray(USUARIOS);
		int nCantidadParametros = parametrosArray.length();
		JSONObject obj;
		for (int i = 0; i < nCantidadParametros; i++)
		{
			obj = parametrosArray.getJSONObject(i);
			try
			{
				lUsuarios.add(
						new String[]{obj.getString(CODIGO_SIGLAS),
								obj.getString(NOMBRE_SIGLAS)});
			}
			catch(Exception ex)
			{
				Log.i(Constantes.LOG_TAG, "Error: " + ex.getMessage());
			}
		}
		return lUsuarios;
	}

	/**
	 * Recibe la lista de tipos de supervisión y la pasa de json a una lista
	 * @param pJSONString
	 * @param pTipo Tipo de datos a guardar: "T_INCI" para tipos de incidencias, "SOC" para socios
	 * @return
	 * @throws JSONException
	 */
	public List<Parametro> obtenerTiposSupervisionDesdeJson(String pJSONString, String pTipo) throws JSONException {
		List<Parametro> lUsuarios = new ArrayList<>();
		JSONObject parametrosObject = new JSONObject(pJSONString);
		JSONArray parametrosArray = new JSONArray();
		if(pTipo.equals("SOC"))
			parametrosArray = parametrosObject.getJSONArray(SOCIOS);
		else
			parametrosArray = parametrosObject.getJSONArray(TIPOS_SUP_OPER);

		int nCantidadParametros = parametrosArray.length();
		JSONObject obj;
		for (int i = 0; i < nCantidadParametros; i++)
		{
			obj = parametrosArray.getJSONObject(i);
			try
			{
				lUsuarios.add(new Parametro(obj.getInt(CODIGO_SIGLAS), obj.getString(NOMBRE_SIGLAS), pTipo));
			}
			catch(Exception ex)
			{
				Log.i(Constantes.LOG_TAG, "Error: " + ex.getMessage());
			}
		}
		return lUsuarios;
	}
}