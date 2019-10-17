package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Gps;
import com.ice.sgpr.comun.Utils;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.CantonesImplementor;
import com.ice.sgpr.implementor.DistritosImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.ParametrosImplementor;
import com.ice.sgpr.implementor.PreguntasAuditorImplementor;
import com.ice.sgpr.implementor.PreguntasPresenciaMarcaImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment que muestra la informacion del negocio.
 * @author eperaza
 */
public class InformacionNegocioFragment extends SgprFragment{
	private int _nIdNegocioSeleccionado;
	private int _nIdNegocioAnteriorSeleccionado;
	private EditText _etNombreNegocio, _etDireccionNegocio, _etTelefonoNegocio, _etNombreContacto, _etDescripcionNegocio, 
	_etTelefonoContacto, _etCelularContacto, _etEmailNegocio;
	private TextView _tvUltimaVisita, _tvHabilitado;
	private Negocio _negocioSeleccionado;
	private LocationManager _locationManager;
	private Spinner _spinnerPrioridad, _spinnerTipoNegocio, _spinnerProvincia, _spinnerCanton, _spinnerDistrito;
	public boolean _bPendienteGuardar;
	private String _sLatitud, _sLongitud;
    private Animation animAlpha = null;
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		View view = inflater.inflate(R.layout.fragment_informacion_negocio, container, false);
        animAlpha = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_alpha);
		_negocioSeleccionado = NegociosImplementor.getInstance().obtenerNegocioActivo();
		
		_etNombreNegocio = (EditText)view.findViewById(R.id.et_nombre_negocio);
		_etDireccionNegocio = (EditText)view.findViewById(R.id.et_direccion_negocio);
		_etTelefonoNegocio = (EditText)view.findViewById(R.id.et_telefono_negocio);
		_etDescripcionNegocio = (EditText)view.findViewById(R.id.et_descripcion_negocio);
		_etNombreContacto = (EditText)view.findViewById(R.id.et_nombre_contacto_negocio);
		_etTelefonoContacto = (EditText)view.findViewById(R.id.et_telefono_contacto_negocio);
		_etCelularContacto = (EditText)view.findViewById(R.id.et_celular_contacto_negocio);
		_tvUltimaVisita = (TextView)view.findViewById(R.id.tv_ultima_visita_negocio);
		_spinnerPrioridad = (Spinner)view.findViewById(R.id.spinner_prioridad);
		_spinnerTipoNegocio = (Spinner)view.findViewById(R.id.spinner_tipo_negocio);
		_spinnerProvincia = (Spinner)view.findViewById(R.id.spinner_provincia);
		_spinnerCanton = (Spinner)view.findViewById(R.id.spinner_canton);
		_spinnerDistrito = (Spinner)view.findViewById(R.id.spinner_distrito);
		_tvHabilitado = (TextView)view.findViewById(R.id.tv_habilitado);
		_etEmailNegocio = (EditText)view.findViewById(R.id.et_email_negocio);
		_bPendienteGuardar = false;
		
		TextView btnFinNegocio = (TextView)view.findViewById(R.id.tv_fin_negocio);
		TextView btnGrabarInfo = (TextView)view.findViewById(R.id.tv_grabar);
		TextView btnNuevoNegocio = (TextView)view.findViewById(R.id.tv_nuevo_negocio);
		
		btnFinNegocio.setOnClickListener(onFinNegocio);
		btnGrabarInfo.setOnClickListener(onGrabar);
		btnNuevoNegocio.setOnClickListener(onNuevoNegocio);
		
		if(_negocioSeleccionado != null){
			_nIdNegocioSeleccionado = _negocioSeleccionado.get_nNegocioId();
			setearValoresTexto(_negocioSeleccionado.get_sNombre(), _negocioSeleccionado.get_sDireccion(), _negocioSeleccionado.get_sTelefono(), 
					_negocioSeleccionado.get_sNombreContacto(), _negocioSeleccionado.get_sTelefonoContacto(), _negocioSeleccionado.get_sCelularContacto(),
					_negocioSeleccionado.get_sUltimaVisita(), _negocioSeleccionado.get_sDescripcion(), _negocioSeleccionado.is_bHabilitado(), _negocioSeleccionado.get_sEmail());
			cargarSpinners(false);
		}
		else{
			cargarSpinners(true);
			prepararNuevoNegocio();
		}
		cargarSpinnersProvincias();
		_spinnerProvincia.setOnItemSelectedListener(onProvinciaListener);
		_spinnerCanton.setOnItemSelectedListener(onCantonListener);
		return view;
	 }
	
	/**
	 * Listener del boton para indicar el fin de una ruta. Actualiza el estado de la ruta a "Visitado" en la
	 * tabla de Negocio.
	 */
	public View.OnClickListener onFinNegocio = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            v.startAnimation(animAlpha);
			guardarDatosNegocio();
			if(_negocioSeleccionado == null)
				Toast.makeText(getActivity(), "Debe seleccionar un negocio primero", Toast.LENGTH_SHORT).show();
			else if(_bPendienteGuardar)
				Toast.makeText(getActivity(), "Debe guardar el negocio primero.", Toast.LENGTH_SHORT).show();
			else if(formularioFinalizado()){
				comprobarGPS();
				finalizarNegocio(_sLatitud, _sLongitud);
			}
		}
	};
	
	/**
	 * Listener para el bot�n de grabar los datos del negocio.
	 */
	public View.OnClickListener onGrabar = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            v.startAnimation(animAlpha);
			guardarDatosNegocio();
			if(!_bPendienteGuardar)
				Toast.makeText(getActivity().getApplicationContext(), "Datos Guardados", Toast.LENGTH_SHORT).show();
		}
	};
	
	/**
	 * Finaliza el negocio activo.
	 */
	public void finalizarNegocio(String pLatitud, String pLongitud){
		BitacoraImplementor.getInstance().actualizarBitacora(_nIdNegocioSeleccionado, pLatitud, pLongitud);
		NegociosImplementor.getInstance().actualizarEstadoNegocio(_nIdNegocioSeleccionado, Constantes.ESTADO_NEGOCIO_VISITADO, 
				Constantes.ESTADO_NEGOCIO_ACTIVO, Utils.obtenerFechaActual());
		Toast.makeText(getActivity(), Constantes.AVISO_FIN_PREGUNTAS, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Verifica que la totalidad de preguntas del formulario hayan sido contestadas.
	 * Si el usuario es Normal, s�lo verifica presencia de marca.
	 * Si es Auditor, s�lo verifica Auditor�a.
	 * Si es Administrador, verifica tanto presencia de marca como auditor�a.
	 * @return true si las preguntas fueron contestadas, false en caso contrario.
	 */
	public Boolean formularioFinalizado(){
		/*
		int nPreguntasContestadas = 0;
		String sRolUsuarioLoguedo = UsuariosImplementor.getInstance().obtenerRolUsuarioLogueado();
		
		if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_NORMAL))
			nPreguntasContestadas = PreguntasPresenciaMarcaImplementor.getInstance().obtenerCantidadPreguntasContestadasPM();
		else if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_AUDITOR))
			nPreguntasContestadas = PreguntasAuditorImplementor.getInstance().obtenerCantidadPreguntasContestadasAuditoria();
		else{
			nPreguntasContestadas = PreguntasPresenciaMarcaImplementor.getInstance().obtenerCantidadPreguntasContestadasPM() + 
					PreguntasAuditorImplementor.getInstance().obtenerCantidadPreguntasContestadasAuditoria();
		}
		
		if(nPreguntasContestadas > 0)
			return true;
		else{
			Toast.makeText(getActivity(), Constantes.AVISO_ERROR_PREGUNTAS_PM_PENDIENTES, Toast.LENGTH_SHORT).show();
			return false;
		}*/
		return true;
	}
	
	
	/**
	 * Guarda los datos del negocio.
	 */
	public void guardarDatosNegocio(){
		if(_negocioSeleccionado == null)
			Toast.makeText(getActivity().getApplicationContext(), "Asegúrese de haber seleccionado \"Nuevo\"", Toast.LENGTH_SHORT).show();
		else if(_etNombreNegocio.getText().toString().equals("") || _etDireccionNegocio.getText().toString().equals(""))
			Toast.makeText(getActivity().getApplicationContext(), "Asegúrese de haber colocado el nombre y dirección del negocio", Toast.LENGTH_LONG).show();
		else{
			_negocioSeleccionado.set_nNegocioId(_nIdNegocioSeleccionado);
			_negocioSeleccionado.set_sNombre(_etNombreNegocio.getText().toString());
			_negocioSeleccionado.set_sDireccion(_etDireccionNegocio.getText().toString());
			_negocioSeleccionado.set_sTelefono(_etTelefonoNegocio.getText().toString());
			_negocioSeleccionado.set_sDescripcion(_etDescripcionNegocio.getText().toString());
			Parametro prioridad = (Parametro)_spinnerPrioridad.getSelectedItem();
			Parametro tipoNegocio = (Parametro)_spinnerTipoNegocio.getSelectedItem();
			Parametro distrito = (Parametro)_spinnerDistrito.getSelectedItem();
			Parametro canton = (Parametro)_spinnerCanton.getSelectedItem();
			Parametro provincia = (Parametro)_spinnerProvincia.getSelectedItem();
			_negocioSeleccionado.set_nPrioridad(prioridad.getCodigo());
			_negocioSeleccionado.set_nTipoComercio(tipoNegocio.getCodigo());
			_negocioSeleccionado.set_nCodigoDistrito(distrito.getCodigo());
			_negocioSeleccionado.set_nCodigoCanton(canton.getCodigo());
			_negocioSeleccionado.set_nCodigoProvincia(provincia.getCodigo());
			_negocioSeleccionado.set_sNombreContacto(_etNombreContacto.getText().toString());
			_negocioSeleccionado.set_sTelefonoContacto(_etTelefonoContacto.getText().toString());
			_negocioSeleccionado.set_sCelularContacto(_etCelularContacto.getText().toString());
			_negocioSeleccionado.set_sUltimaVisita(Utils.obtenerFechaActual());
			_negocioSeleccionado.set_nActualizado(1);
			_negocioSeleccionado.set_sEmail(_etEmailNegocio.getText().toString());
			
			NegociosImplementor.getInstance().actualizarNegocios(_negocioSeleccionado);
			
			if(_nIdNegocioAnteriorSeleccionado != 0)
				NegociosImplementor.getInstance().actualizarEstadoNegocio(_nIdNegocioAnteriorSeleccionado, Constantes.ESTADO_NEGOCIO_VISITADO, Constantes.ESTADO_NEGOCIO_INACTIVO, "");
			NegociosImplementor.getInstance().actualizarEstadoNegocio(_nIdNegocioSeleccionado, Constantes.ESTADO_NEGOCIO_ACTUAL, Constantes.ESTADO_NEGOCIO_ACTIVO, "");
			comprobarGPS();
			
			if(_bPendienteGuardar){
				BitacoraImplementor.getInstance().insertarNuevaBitacora(_sLatitud, _sLongitud, true);
				_bPendienteGuardar = false;
			}
		}
	}
	/**
	 * Listener para el nuevo negocio. Verifica que no haya ning�n negocio activo para poder agregar uno nuevo.
	 * Limpia los editText, setea la latitud y longitud actuales y obtiene un Id temporal para guardarlo.
	 */
	public View.OnClickListener onNuevoNegocio = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            v.startAnimation(animAlpha);
			int nCantidadOpcionesSpinner = _spinnerPrioridad.getCount();
			if(nCantidadOpcionesSpinner > 0){
				Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
				if(negocioActivo == null){
					prepararNuevoNegocio();
				}
				else if(negocioActivo.get_nEstado() == Constantes.ESTADO_NEGOCIO_ACTUAL)
					Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_NEGOCIO_SELECCIONADO, Toast.LENGTH_LONG).show();
				else{
					prepararNuevoNegocio();
					Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_INGRESAR_DATOS_NUEVO_NEGOCIO, Toast.LENGTH_LONG).show();
				}
			}
			else
				Toast.makeText(getActivity().getApplicationContext(), Constantes.ADVERTENCIA_DEBE_SINCRONIZAR, Toast.LENGTH_LONG).show();
		}
	};
	
	/**
	 * Se crea un nuevo objeto negocio y se le cargan algunos datos b�sicos.
	 */
	public void prepararNuevoNegocio(){
		setearValoresTexto("", "", "", "", "", "", "", "", true, "");
		_negocioSeleccionado = new Negocio();
		_nIdNegocioAnteriorSeleccionado = _nIdNegocioSeleccionado;
		NegociosImplementor.getInstance().actualizarEstadoNegocio(_nIdNegocioAnteriorSeleccionado, Constantes.ESTADO_NEGOCIO_VISITADO, Constantes.ESTADO_NEGOCIO_INACTIVO, "");
		_nIdNegocioSeleccionado = Utils.obtenerIdGenerico();
		_negocioSeleccionado.set_nRutaId(Constantes.RUTA_ID_NUEVOS_NEGOCIOS);
		_negocioSeleccionado.set_bHabilitado(true);
		//Latitud y longitud
		_locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
		if(respuestaLocalizacion != null){
			_negocioSeleccionado.set_sLatitud(Double.toString(respuestaLocalizacion.getLatitude()));
			_negocioSeleccionado.set_sLongitud(Double.toString(respuestaLocalizacion.getLongitude()));
		}
		else{
			Toast.makeText(getActivity(), "Error obteniendo la ubicación exacta", Toast.LENGTH_LONG).show();
			_negocioSeleccionado.set_sLatitud(Constantes.LATITUD_COSTA_RICA);
			_negocioSeleccionado.set_sLongitud(Constantes.LONGITUD_COSTA_RICA);
		}
		cargarSpinners(true);
		cargarSpinnersProvincias();
		_bPendienteGuardar = true;
	}
	
	/**
	 * Actualiza los campos de texto con la informaci�n que se solicita.
	 * @param pNombreNegocio
	 * @param pDireccion
	 * @param pTelefonoNegocio
	 * @param pNombreContacto
	 * @param pTelefonoContacto
	 * @param pCelularContacto
	 * @param pUltimaVisita
	 * @param pDescripcion
	 */
	public void setearValoresTexto(String pNombreNegocio, String pDireccion, String pTelefonoNegocio, String pNombreContacto, String pTelefonoContacto,
			String pCelularContacto, String pUltimaVisita, String pDescripcion, boolean pHabilitado, String pEmailNegocio){
		_etNombreNegocio.setText(pNombreNegocio);
		_etDireccionNegocio.setText(pDireccion);
		_etTelefonoNegocio.setText(pTelefonoNegocio);
		_etDescripcionNegocio.setText(pDescripcion);
		_etNombreContacto.setText(pNombreContacto);
		_etTelefonoContacto.setText(pTelefonoContacto);
		_etCelularContacto.setText(pCelularContacto);
		_tvUltimaVisita.setText(pUltimaVisita);
		_etEmailNegocio.setText(pEmailNegocio);
		ActualizarEstadoHabilitadoNegocio(pHabilitado);
	}
		
	/**
	 * Actualiza la información del negocio, indicando si se ha habilitado o deshabilitado.
	 * @param pHabilitado
	 */
	public void ActualizarEstadoHabilitadoNegocio(boolean pHabilitado){
		_tvHabilitado.setText(pHabilitado ? "Habilitado" : "Deshabilitado");
		
		if(pHabilitado)
			_tvHabilitado.setTextColor(Color.GREEN);
		else
			_tvHabilitado.setTextColor(Color.RED);
	}
	/**
	 * Carga los datos de los spinners de TipoNegocio y Prioridad. Carga los datos a los spinner mediante un adapter.
	 * Para colocar el spinner en la ubicaci�n guardada del negocio, se recorre la lista de par�metros, con un contador
	 * para ubicar el spinner en el index seleccionado.
	 */
	public void cargarSpinners(Boolean pUbicarIndex){
		List<Parametro> lParametros = ParametrosImplementor.getInstance().obtenerListaParametros(0);
		if(lParametros.size() == 0)
			Toast.makeText(getActivity().getApplicationContext(), Constantes.ADVERTENCIA_DEBE_SINCRONIZAR, Toast.LENGTH_LONG).show();
		else{
			//Tipo Negocio
			cargarSpinner(lParametros, _spinnerTipoNegocio);
			if(!pUbicarIndex)
				ubicarIndexSpinner(lParametros);
			
			//Prioridad
			lParametros = ParametrosImplementor.getInstance().obtenerListaParametros(1);
			cargarSpinner(lParametros, _spinnerPrioridad);
			if(!pUbicarIndex)
				ubicarIndexSpinner(lParametros);
		}
	}
	
	/**
	 * Carga de los spinners de provincias, cantones y distritos.
	 */
	private void cargarSpinnersProvincias(){
		//Provincias:
		String sOpcionesProvincias = getResources().getString(R.string.opciones_provincia);
		List<Parametro> lParametros = obtenerProvincias(sOpcionesProvincias);
		cargarSpinner(lParametros, _spinnerProvincia);
		
		if (_negocioSeleccionado != null) {
			int nCodigoDistrito = _negocioSeleccionado.get_nCodigoDistrito();
			int nCodigoCanton = DistritosImplementor.getInstance().obtenerCodigoCanton(nCodigoDistrito);
			int nCodigoProvincia = CantonesImplementor.getInstance().obtenerCodigoProvincia(nCodigoCanton);

			//ubicarIndexSpinnerProvincias(nCodigoProvincia);
			ubicarIndexSpinnerProvCantDist(nCodigoProvincia, _spinnerProvincia);
		}
		Parametro provincia = (Parametro) _spinnerProvincia.getSelectedItem();
		cargarSpinnerCantones(provincia.getCodigo(), false);
	}
	
	/**
	 * Carga la lista de cantones en el spinner correspondiente, de acuerdo a 
	 * la provincia seleccionada.
	 * @param pCodigoProvincia Codigo de la provincia padre
	 * @param pCambioProvincia En caso de que se haya tocado el spinner para cambiar el cant�n = true
	 */
	private void cargarSpinnerCantones(int pCodigoProvincia, boolean pCambioProvincia){
		List<Parametro> lParametros = CantonesImplementor.getInstance().obtenerListaCantones(pCodigoProvincia);
		cargarSpinner(lParametros, _spinnerCanton);
		
		if (_negocioSeleccionado != null && !pCambioProvincia) {
			int nCodigoDistrito = _negocioSeleccionado.get_nCodigoDistrito();
			int nCodigoCanton = DistritosImplementor.getInstance().obtenerCodigoCanton(nCodigoDistrito);
			//ubicarIndexSpinnerCantones(nCodigoCanton);
			ubicarIndexSpinnerProvCantDist(nCodigoCanton, _spinnerCanton);
		}
		
		Parametro canton = (Parametro)_spinnerCanton.getSelectedItem();
		cargarSpinnerDistritos(canton.getCodigo(), pCambioProvincia);
	}
	
	/**
	 * Carga la lista de distritos en el spinner correspondiente, de acuerdo al 
	 * canton seleccionado.
	 * @param pCodigoCanton Codigo del canton padre
	 * @param pCambioDistrito En caso de que se haya tocado el spinner para cambiar la provincia = true
	 */
	private void cargarSpinnerDistritos(int pCodigoCanton, boolean pCambioDistrito){
		List<Parametro> lParametros = DistritosImplementor.getInstance().obtenerListaDistritos(pCodigoCanton);
		cargarSpinner(lParametros, _spinnerDistrito);
		
		if (_negocioSeleccionado != null && !pCambioDistrito) {
			int nCodigoDistrito = _negocioSeleccionado.get_nCodigoDistrito();
			//ubicarIndexSpinnerDistritos(nCodigoDistrito);
			ubicarIndexSpinnerProvCantDist(nCodigoDistrito, _spinnerDistrito);
		}
	}
	
	/**
	 * Obtiene la lista de provincias con su respectivo c�digo y lo retorna
	 * en una lista de tipo par�metro.
	 * @param pOpcionesProvincias
	 * @return
	 */
	private List<Parametro> obtenerProvincias(String pOpcionesProvincias) {
		String[] sOpcionesSplit = pOpcionesProvincias.split("#");
		int nCantidadOpciones = sOpcionesSplit.length;

		List<Parametro> lParametros = new ArrayList<Parametro>();
		for (int cont = 0; cont < nCantidadOpciones; cont++) {
			if (cont % 2 == 0) {
				String nombre = sOpcionesSplit[cont];
				int codigo = Integer.parseInt(sOpcionesSplit[cont+1]);
				lParametros.add(new Parametro(codigo, nombre, ""));
			}
		}
		return lParametros;
	}
	
	/**
	 * Listener del spinner de provincias
	 */
	private OnItemSelectedListener onProvinciaListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Parametro provincia = (Parametro) parent.getItemAtPosition(pos);
			cargarSpinnerCantones(provincia.getCodigo(), true);
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	/**
	 * Listener del spinner de cantones
	 */
	private OnItemSelectedListener onCantonListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Parametro canton = (Parametro) parent.getItemAtPosition(pos);
			cargarSpinnerDistritos(canton.getCodigo(), true);
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	/**
	 * Arma un spinner, cargando la informaci�n.
	 * @param pParametros
	 * @param pSpinner
	 */
	private void cargarSpinner(List<Parametro> pParametros, Spinner pSpinner){
		ArrayAdapter<Parametro> spinnerAdapter = new ArrayAdapter<Parametro>(getActivity().getApplicationContext(), 
				android.R.layout.simple_spinner_item, pParametros);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pSpinner.setAdapter(spinnerAdapter);
	}
	
	/**
	 * Coloca el index selccionado en el spinner, llevando un contador que se detiene cuando el c�digo de la 
	 * lista de par�metros coincide con el c�digo del tipo de comercio.
	 * @param pParametros
	 */
	public void ubicarIndexSpinner(List<Parametro> pParametros){
		int nIndex = 0;
		for(Parametro param:pParametros){
			if(param.getCodigo() == _negocioSeleccionado.get_nTipoComercio()){
				_spinnerTipoNegocio.setSelection(nIndex);
				break;
			}
			if(param.getCodigo() == _negocioSeleccionado.get_nPrioridad()){
				_spinnerPrioridad.setSelection(nIndex);
				break;
			}
			nIndex ++;
		}
	}
	
	/**
	 * Se ubica el spinner en el index de la provincia, cant�n o distrito seleccionado.
	 */
	@SuppressWarnings("unchecked")
	private void ubicarIndexSpinnerProvCantDist(int pCodigo, Spinner pSpinner){
		ArrayAdapter<Parametro> adapter = (ArrayAdapter<Parametro>) pSpinner.getAdapter();
		int spinnerPosition = adapter.getPosition(new Parametro(pCodigo, "", ""));
		pSpinner.setSelection(spinnerPosition, false);
	}
	
	/**
	 * Comprobaci�n GPS
	 */
	/**
	 * Espera la respuesta sobre la activaci�n del GPS.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Boolean bRespuesta = Gps.getInstance().revisarActivityResult(requestCode, resultCode, data, this.getActivity(), 
				_locationManager, Constantes.ERROR_GPS_NO_ACT_FIN_NEGOCIO);
		if(bRespuesta){
			Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
			if(respuestaLocalizacion != null)
				finalizarNegocio(Double.toString(respuestaLocalizacion.getLatitude()), Double.toString(respuestaLocalizacion.getLongitude()));
		}
	}
	
	/**
	 * Verifica que el GPS est� activado para iniciar el negocio.
	 */
	public void comprobarGPS(){
		_locationManager = Gps.getInstance().revisarGPS(this.getActivity(), _locationManager);
		if(_locationManager != null){
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, Constantes.SET_GPS);
		}
		else{
			_locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
			if(respuestaLocalizacion != null){
				_sLatitud = Double.toString(respuestaLocalizacion.getLatitude());
				_sLongitud = Double.toString(respuestaLocalizacion.getLongitude());
			}
			else{
				_sLatitud = Constantes.LATITUD_COSTA_RICA;
				_sLongitud = Constantes.LONGITUD_COSTA_RICA;
			}
		}
	}
}
