package com.ice.sgpr.entidades;

/**
 * Entidad con las propiedades de un negocio.
 * @author eperaza
 * Fecha de creacion: 07/08/2013.
 */
public class Negocio {
	private int _nNegocioId;
	private String _sNombre;
	private String _sDescripcion;
	private String _sDireccion;
	private int _nTipoComercio;
	private int _nPrioridad;
	private String _sTelefono;
	private String _sNombreContacto;
	private String _sTelefonoContacto;
	private String _sUltimaVisita;
	private String _sLatitud;
	private String _sLongitud;
	private int _nRutaId;
	private int _nEstado;//0 = No visitado, 1 = Actualmente visitado, 2 = Visitado
	private String _sPinId;// ID del marcador del mapa.
	private int _nActivo;
	private int _nActualizado; //Indica si el negocio ha sido actualizado para que sea sincronizado. Si el numero es 2, quiere decir que es un negocio nuevo.
	private String _sCelularContacto;
	private boolean _bHabilitado;
	private int _nCodigoDistrito;
	private int _nCodigoCanton;
	private int _nCodigoProvincia;
	
	//Cambio 2015
	private String _sObservaciones;
	private String _sFechaObservaciones;
	private int _nUsrModificaObservacion;
	private String _sEmail;
	
	public Negocio(int pRutaId, int pNegocioId, String pLatitud, String pLongitud, int pEstado, String pNombre, String pDireccion, int pActivo,
			String pDescripcion, int pTipoComercio, int pPrioridad, String pTelefono, String pNombreContacto, String pTelefonoContacto,
			String pUltimaVisita, int pActualizado, String pCelularContacto, boolean pHabilitado, int pCodigoDistrito, int pCodigoCanton,
			int pCodigoProvincia, String pObservaciones, String pFechaObservaciones, int pUsuarioUltimaObservacion, String pEmail){
		this._nRutaId = pRutaId;
		this._nNegocioId = pNegocioId;
		this._sLatitud = pLatitud;
		this._sLongitud = pLongitud;
		this._nEstado = pEstado;
		this._sNombre = pNombre;
		this._sDireccion = pDireccion;
		this._nActivo = pActivo;
		this._sDescripcion = pDescripcion;
		this._nTipoComercio = pTipoComercio;
		this._nPrioridad = pPrioridad;
		this._sTelefono = pTelefono;
		this._sNombreContacto = pNombreContacto;
		this._sTelefonoContacto = pTelefonoContacto;
		this._sUltimaVisita = pUltimaVisita;
		this._nActualizado = pActualizado;
		this._sCelularContacto = pCelularContacto;
		this._bHabilitado = pHabilitado;
		this._nCodigoDistrito = pCodigoDistrito;
		this._nCodigoCanton = pCodigoCanton;
		this._nCodigoProvincia = pCodigoProvincia;
		this._sObservaciones = pObservaciones;
		this._sFechaObservaciones = pFechaObservaciones;
		this._nUsrModificaObservacion = pUsuarioUltimaObservacion;
		this._sEmail = pEmail;
	}
	
	public Negocio(int pNegocioId, String pNombre, String pDireccion, String pDescripcion){
		this._nNegocioId = pNegocioId;
		this._sNombre = pNombre;
		this._sDireccion = pDireccion;
		this._sDescripcion = pDescripcion;
	}
	
	/**
	 * Constructor para mostrar los puntos en el mapa.
	 * @param pRutaId
	 * @param pNegocioId
	 * @param pLatitud
	 * @param pLongitud
	 * @param pEstado
	 * @param pActivo
	 */
	public Negocio(int pRutaId, int pNegocioId, String pLatitud, String pLongitud, int pEstado, int pActivo, String pNombreNegocio) {
		this._nRutaId = pRutaId;
		this._nNegocioId = pNegocioId;
		this._sLatitud = pLatitud;
		this._sLongitud = pLongitud;
		this._nEstado = pEstado;
		this._nActivo = pActivo;
		this._sNombre = pNombreNegocio;
	}

	public Negocio() {
	}
	public int get_nNegocioId() {
		return _nNegocioId;
	}
	public void set_nNegocioId(int _nNegocioId) {
		this._nNegocioId = _nNegocioId;
	}
	public String get_sNombre() {
		return _sNombre;
	}
	public void set_sNombre(String _sNombre) {
		this._sNombre = _sNombre;
	}
	public String get_sDescripcion() {
		return _sDescripcion;
	}
	public void set_sDescripcion(String _sDescripcion) {
		this._sDescripcion = _sDescripcion;
	}
	public String get_sDireccion() {
		return _sDireccion;
	}
	public void set_sDireccion(String _sDireccion) {
		this._sDireccion = _sDireccion;
	}
	public int get_nTipoComercio() {
		return _nTipoComercio;
	}
	public void set_nTipoComercio(int _nTipoComercio) {
		this._nTipoComercio = _nTipoComercio;
	}
	public int get_nPrioridad() {
		return _nPrioridad;
	}
	public void set_nPrioridad(int _nPrioridad) {
		this._nPrioridad = _nPrioridad;
	}
	public String get_sTelefono() {
		return _sTelefono;
	}
	public void set_sTelefono(String _sTelefono) {
		this._sTelefono = _sTelefono;
	}
	public String get_sNombreContacto() {
		return _sNombreContacto;
	}
	public void set_sNombreContacto(String _sNombreContacto) {
		this._sNombreContacto = _sNombreContacto;
	}
	public String get_sTelefonoContacto() {
		return _sTelefonoContacto;
	}
	public void set_sTelefonoContacto(String _sTelefonoContacto) {
		this._sTelefonoContacto = _sTelefonoContacto;
	}
	public String get_sUltimaVisita() {
		return _sUltimaVisita;
	}
	public void set_sUltimaVisita(String _sUltimaVisita) {
		this._sUltimaVisita = _sUltimaVisita;
	}
	public String get_sLatitud() {
		return _sLatitud;
	}
	public void set_sLatitud(String _sLatitud) {
		this._sLatitud = _sLatitud;
	}
	public String get_sLongitud() {
		return _sLongitud;
	}
	public void set_sLongitud(String _sLongitud) {
		this._sLongitud = _sLongitud;
	}
	public int get_nRutaId() {
		return _nRutaId;
	}
	public void set_nRutaId(int _nRutaId) {
		this._nRutaId = _nRutaId;
	}
	public int get_nEstado() {
		return _nEstado;
	}
	public void set_nEstado(int _nEstado) {
		this._nEstado = _nEstado;
	}
	public String get_sPinId() {
		return _sPinId;
	}
	public void set_sPinId(String _sPinId) {
		this._sPinId = _sPinId;
	}
	public int get_nActivo() {
		return _nActivo;
	}
	public void set_nActivo(int _nActivo) {
		this._nActivo = _nActivo;
	}
	public int get_nActualizado() {
		return _nActualizado;
	}
	public void set_nActualizado(int _nActualizado) {
		this._nActualizado = _nActualizado;
	}
	public String get_sCelularContacto() {
		return _sCelularContacto;
	}
	public void set_sCelularContacto(String _sCelularContacto) {
		this._sCelularContacto = _sCelularContacto;
	}
	public boolean is_bHabilitado() {
		return _bHabilitado;
	}
	public void set_bHabilitado(boolean _bHabilitado) {
		this._bHabilitado = _bHabilitado;
	}
	public int get_nCodigoDistrito() {
		return _nCodigoDistrito;
	}
	public void set_nCodigoDistrito(int _nCodigoDistrito) {
		this._nCodigoDistrito = _nCodigoDistrito;
	}
	public int get_nCodigoCanton() {
		return _nCodigoCanton;
	}
	public void set_nCodigoCanton(int _nCodigoCanton) {
		this._nCodigoCanton = _nCodigoCanton;
	}
	public int get_nCodigoProvincia() {
		return _nCodigoProvincia;
	}
	public void set_nCodigoProvincia(int _nCodigoProvincia) {
		this._nCodigoProvincia = _nCodigoProvincia;
	}
	public String get_sObservaciones() {
		return _sObservaciones;
	}
	public void set_sObservaciones(String _sObservaciones) {
		this._sObservaciones = _sObservaciones;
	}
	public String get_sFechaObservaciones() {
		return _sFechaObservaciones;
	}
	public void set_sFechaObservaciones(String _sFechaObservaciones) {
		this._sFechaObservaciones = _sFechaObservaciones;
	}
	public int get_nUsrModificaObservacion() {
		return _nUsrModificaObservacion;
	}
	public void set_nUsrModificaObservacion(int _nUsrModificaObservacion) {
		this._nUsrModificaObservacion = _nUsrModificaObservacion;
	}
	public String get_sEmail() {
		return _sEmail;
	}
	public void set_sEmail(String _sEmail) {
		this._sEmail = _sEmail;
	}
}