package com.ice.sgpr.service;

public class SgprService {
	
	private static SgprService _instancia;
	//private static final String BASE_URL = "http://appcenter.grupoice.com/WSSGPR/services/";
	//private static final String BASE_URL = "http://cer.infocom.ice/WSSGPR/services/";
	private static final String BASE_URL = "http://192.168.0.204:9081/WSSGPR/services/";
	
	
	private static final String LOGIN = "ValidateUser/SP1/";
	private static final String RUTAS = "getInfo/SP2/";
	private static final String NEGOCIOS = "getInfo/SP3/";
	private static final String PARAMETROS = "getInfo/SP4/";
	private static final String BITACORA_NEGOCIO_VERSIONAMIENTO = "EnviarInfo/SP1";
	private static final String IMAGENES_VERSIONAMIENTO = "GuardarImg/";
	public static final String SP_BITACORA_VERSIONAMIENTO = "SP5";
	public static final String SP_NEGOCIO_VERSIONAMIENTO = "SP6";
	public static final String SP_IMAGEN_VERSIONAMIENTO = "SP7";
	public static final String SP_NUEVO_NEGOCIO_VERSIONAMIENTO = "SP8";
	private static final String INFO_NEGOCIO = "getInfo/SP9/";
	private static final String BUSQUEDA_NEGOCIO = "getInfo/SP10/";
	public static final String SP_FORMULARIO_VERSIONAMIENTO = "SP13";
	public static final String SP_FORMULARIO_PM_VERSIONAMIENTO = "SP14";
	public static final String SP_FORMULARIO_AUDIT_VERSIONAMIENTO = "SP15";
	private static final String FORMULARIO_VERSIONAMIENTO = "EnviarInfo/SP13";
	private static final String BUSQUEDA_NEGOCIOS_CERCANOS = "getPtos/SP1";
	public static final String FORMULARIO_VERSIONAMIENTO_AUDITOR = "EnviarInfo/SP15";
	public  static final String SP_SEGUIMIENTO = "SP16";
	public  static final String SP_SEGUIMIENTOS_GUARDADOS = "SP17";
	public static final String ENVIAR_INFO = "EnviarInfo/SP16";
	public static final String ENVIAR_INFO_SEGUIMIENTOS = "EnviarInfo/SP17";

	//Terceros
	public  static final String ENVIAR_INFO_SUPERV_DIST = "EnviarInfo/SP18";
	public  static final String SP_SUPERV_DIST = "SP18";
	public  static final String SP_PRES_MARCA = "SP19";
	public  static final String SP_PUBLICIDAD = "SP20";
	public  static final String SP_PROMOCIONAL = "SP21";
	public  static final String SP_POP_ESPECIAL = "SP22";
	public  static final String SP_OBSERVACIONES = "SP23";
	public  static final String SP_INCIDENCIAS = "SP24";
	public  static final String SP_ATENCIONES = "SP25";
	public  static final String SP_SOCIO_PDV = "SP29";
	public  static final String SP_ENCUESTA_PDV = "SP30";

	private static final String USUARIOS = "getInfo/SP26/";
	private static final String TIPOS_SUPERV = "getInfo/SP27/";
	private static final String SOCIOS = "getInfo/SP28/";
	
	private SgprService(){
		
	}
	
	public static SgprService getInstance(){
		if (_instancia == null)
			_instancia = new SgprService();
		return _instancia;
	}	
	public String getLoginUrl(){
		return BASE_URL + LOGIN;
	}	
	public String getRutasUrl(String pUsuario){
		return BASE_URL + RUTAS + pUsuario;
	}	
	public String getNegociosUrl(int pIdRuta, int pPagina){
		return BASE_URL + NEGOCIOS + pIdRuta + "/" + pPagina;
	}	
	public String getParametrosUrl(){
		return BASE_URL + PARAMETROS;
	}	
	public String getBitacoraNegocioVersionamientoUrl(){
		return BASE_URL + BITACORA_NEGOCIO_VERSIONAMIENTO;
	}	
	public String getImagenesVersionamientoUrl(int pCodigoNegocio){
		return BASE_URL + IMAGENES_VERSIONAMIENTO + pCodigoNegocio;
	}	
	public String getNegocioBuscadoUrl(String pNegocioId){
		return BASE_URL + INFO_NEGOCIO + pNegocioId;
	}
	public String getBusquedaNegocioUrl(String pParametro, int pIndex){
		return BASE_URL + BUSQUEDA_NEGOCIO + pParametro + "/" + pIndex;
	}
	public String getFormularioUrl(){
        return BASE_URL + FORMULARIO_VERSIONAMIENTO;
    }

	public String getNegociosCercanosUrl(){
		return BASE_URL + BUSQUEDA_NEGOCIOS_CERCANOS;
	}
	public String getSendInfo(){
		return BASE_URL + ENVIAR_INFO;
	}
	public String getSendInfoSavedTracking(){
		return BASE_URL + ENVIAR_INFO_SEGUIMIENTOS;
	}

    /** Terceros **/
    public String getSupervDistUrl(){
        return BASE_URL + ENVIAR_INFO_SUPERV_DIST;
    }
	public String getUsuariosUrl(){
		return BASE_URL + USUARIOS;
	}

	public String getTiposSupervisionUrl(){
		return BASE_URL + TIPOS_SUPERV;
	}
	public String getSociosUrl(){
		return BASE_URL + SOCIOS;
	}
}
