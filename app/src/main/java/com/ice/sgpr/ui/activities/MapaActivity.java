package com.ice.sgpr.ui.activities;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Gps;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Ruta;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.PaginacionImplementor;
import com.ice.sgpr.implementor.RutaImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.service.RestHelper;
import com.ice.sgpr.service.SgprService;
import com.ice.sgpr.ui.fragments.MapaFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * Activity que muestra el mapa con los puntos marcados en el mapa.
 * @author eperaza
 * Fecha de creación 01/08/2013
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapaActivity extends Activity{
	private List<Negocio> _lNegocios;
	private LatLng _puntoMapa;
	private GoogleMap _mapa;
	private Boolean _bCentrarMapa; // Indica si se debe centra el mapa (s�lo la primera vez que se muestra el mapa)
	private LocationManager _locationManager;
	private String _sLatitud;
	private String _sLongitud;
	Button _btnCargarMasNegocios;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_mapa);
    	_bCentrarMapa = true;
	    _mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
	    _btnCargarMasNegocios = (Button)findViewById(R.id.btn_mapa);
	    Bundle extras = getIntent().getExtras();
	    _puntoMapa = new LatLng(Double.parseDouble(Constantes.LATITUD_COSTA_RICA), Double.parseDouble(Constantes.LONGITUD_COSTA_RICA));
		/*_mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(_puntoMapa,
				Constantes.ZOOM_DEFAULT_MAPA));*/
		moveCamera();

	    if(RutaImplementor.getInstance().obtenerIdRutaSeleccionada() != 0){
		    if(extras.getString(MapaFragment.TIPO_MAPA).equals(RutasActivity.TAG_RUTAS))
		    	activarMapaRutas();
		    else
		    	activarMapaNegocio();
	    }
	    else
	    	activarMapaNegocio();
	}
	
	private void moveCamera(){
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(_puntoMapa).tilt(60).zoom(Constantes.ZOOM_DEFAULT_MAPA).bearing(0).build();
		_mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	/***************************************************************
	 * RUTAS
	 * ************************************************************/
	/**
	 * Mapa para la vista de rutas.
	 * Obtiene la lista de negocios, los cuales se agregan como marcadores (pines) al mapa. Por defecto, a cada marcador
	 * se le asigna un Id autom�tico, as� que este id se agrega al objeto "Negocio" para indicar a cual corresponde cada 
	 * marcador.
	 */
	public void activarMapaRutas(){
		_lNegocios = NegociosImplementor.getInstance().obtenerListaNegocios();
	    if(_lNegocios.size() > 0){
		    _btnCargarMasNegocios.setText(Constantes.TEXTO_BOTON_MAPA_CARGAR_NEG);
			if(_lNegocios.size() > 0){
			    //Se obtienen los puntos, se agregan al mapa con un marcador y se agregan para centrar el mapa.
			    for(Negocio negocio:_lNegocios){
			    	int pin = R.drawable.ic_pin_rojo;
			    	if(negocio.get_nEstado() == Constantes.ESTADO_NEGOCIO_SIGUIENTE || negocio.get_nEstado() == Constantes.ESTADO_NEGOCIO_ACTUAL)
			    		pin = R.drawable.ic_pin_naranja;
			    	else if(negocio.get_nEstado() == Constantes.ESTADO_NEGOCIO_VISITADO)
			    		pin = R.drawable.ic_pin_verde;
			    	agregarPuntoEnMapa(negocio, pin);
			    }
			    _btnCargarMasNegocios.setOnClickListener(onClickMasNegocios);
	    		
			    if(_bCentrarMapa){
				    Ruta rutaSeleccionada = RutaImplementor.getInstance().obtenerInformacionRuta();
				    _puntoMapa = new LatLng(Double.parseDouble(rutaSeleccionada.getLatitud()), Double.parseDouble(rutaSeleccionada.getLongitud()));
				    _mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(_puntoMapa, Constantes.ZOOM_INICIAL_MAPA));
				    //_mapa.animateCamera(CameraUpdateFactory.zoomTo(Constantes.ZOOM_FINAL_MAPA), 1000, null);
				    moveCamera();
					_bCentrarMapa = false;
			    }
		    }
		    else{
		    	_mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Constantes.LATITUD_COSTA_RICA), 
		    			Double.parseDouble(Constantes.LONGITUD_COSTA_RICA)),Constantes.ZOOM_INICIAL_MAPA));
		    }
		    _mapa.setOnInfoWindowClickListener(onPinNegocioClickListener);
	    }
	    else{ //if(Gps.getInstance().internetCheck(getApplicationContext()))
	    	PaginacionImplementor.getInstance().actualizarPagina(0);
	    	actualizarPaginacion(false);
	    }
	}
	
	/**
	 * Consulta la p�gina actual, si a�n no hay p�gina guardada para la ruta (-1) inserta un nuevo campo en la tabla de
	 * paginaci�n, comenzando por la p�gina 0, sino, simplemente se actualiza la p�gina en la BD.
	 */
	public void actualizarPaginacion(Boolean pSiguientePagina){
		int nPaginaActual = PaginacionImplementor.getInstance().obtenerPaginaActual();
		if(nPaginaActual == 0){
			nPaginaActual ++;
			new AsyncNegociosSender().execute(Integer.toString(nPaginaActual));
		}
		else if(pSiguientePagina){
			nPaginaActual ++;
			new AsyncNegociosSender().execute(Integer.toString(nPaginaActual));
		}
		//PaginacionImplementor.getInstance().actualizarPagina(nPaginaActual);
	}
	
	/**
	 * Listener del marcador del punto se�alado en el mapa.
	 * Guarda el marcador seleccionado en una variable global y comprueba el GPS.
	 */
	public OnInfoWindowClickListener onPinNegocioClickListener = new OnInfoWindowClickListener() {
		@Override
		public void onInfoWindowClick(Marker marker) {
			comprobarGPS();
			iniciarNegocio(marker);
		}
    };
    
    /**
     * Listener del bot�n "M�s Negocios" que solicita la siguente p�gina al WS.
     */
    OnClickListener onClickMasNegocios = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(Gps.getInstance().internetCheck(getApplicationContext()))
				actualizarPaginacion(true);
		}
	};
	
	/**
	 * Hilo que obtiene los negocios desde el WS.
	 * @author eperaza
	 */
	private class AsyncNegociosSender extends AsyncTask<String, String, Integer>
	{
		String sRequestNegocios;
		int nIdRuta = RutaImplementor.getInstance().obtenerInformacionRuta().getId();
		ProgressDialog pDialog = new ProgressDialog(MapaActivity.this);
		Intent intent;
		@Override
		protected void onPreExecute()
		{
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Cargando negocios...");
			pDialog.show();
			intent = new Intent(MapaActivity.this, ActivityVacio.class);
			startActivity(intent);
		}
		@Override
		protected Integer doInBackground(String... params) {
			try {
				sRequestNegocios = RestHelper.getInstance().GET(SgprService.getInstance().getNegociosUrl(nIdRuta, Integer.parseInt(params[0])), true);
				if(JSONHelper.getInstance().obtenerNegociosDesdeJson(sRequestNegocios, nIdRuta)){
					PaginacionImplementor.getInstance().actualizarPagina(Integer.parseInt(params[0]));
					return 1;
				}
				else 
					return 0;
				
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
				return -1;
			}
		}
		
		protected void onPostExecute(Integer pResult)
		{
			ActivityVacio.getInstance().finalizarActivity();
			pDialog.dismiss();
			if(pResult == 1){
				activarMapaRutas();
			}
			else if(pResult == 0)
				mostarMensaje(Constantes.ERROR_NO_MAS_NEGOCIOS);
			else
				mostarMensaje(Constantes.ERROR_LOGIN_CONEXION);
		}
	}
	
	/**
	 * Da por iniciado un negocio, actualiza el estado del negocio seleccionado, cambia el pin e inserta 
	 * un nuevo registro en la bit�cora.
	 */
	public void iniciarNegocio(Marker pin){
		String sMarcadorId = pin.getId();
		int nNegocioSeleccionadoId = -1;
		Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		int nNegocioActivo = -1;
		if(negocioActivo != null)
			nNegocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		if(negocioActivo != null && negocioActivo.get_nEstado() == Constantes.ESTADO_NEGOCIO_ACTUAL)
			Toast.makeText(getApplicationContext(), Constantes.AVISO_NEGOCIO_SELECCIONADO, Toast.LENGTH_LONG).show();
		else{
			for(Negocio negocio:_lNegocios){
				if(sMarcadorId.equals(negocio.get_sPinId())){
					nNegocioSeleccionadoId = negocio.get_nNegocioId();
					negocio.set_nEstado(Constantes.ESTADO_NEGOCIO_ACTUAL);
				}
			}
			if(nNegocioSeleccionadoId != -1){
				BitmapDescriptor marcador = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_naranja);
				pin.setIcon(marcador);
				if(nNegocioActivo != -1)
					NegociosImplementor.getInstance().actualizarEstadoNegocio(nNegocioActivo, Constantes.ESTADO_NEGOCIO_VISITADO, Constantes.ESTADO_NEGOCIO_INACTIVO, "");
				NegociosImplementor.getInstance().actualizarEstadoNegocio(nNegocioSeleccionadoId, Constantes.ESTADO_NEGOCIO_ACTUAL, Constantes.ESTADO_NEGOCIO_ACTIVO, "");
				comprobarGPS();
				BitacoraImplementor.getInstance().insertarNuevaBitacora(_sLatitud, _sLongitud, false);
				Toast.makeText(getApplicationContext(), Constantes.AVISO_NEGOCIO_CARGADO, Toast.LENGTH_LONG).show();
			}
		}
	}
    
	/***************************************************************
	 * NEGOCIOS
	 * ************************************************************/    
    /**
     * Muestra la ubicaci�n de un negocio mediante un pin. Se puede abrir otro servidor de 
     * mapas para encontrar una ruta hasta ese punto al tocar dicho pin.
     */
    public void activarMapaNegocio(){
    	Negocio negocioSeleccionado = NegociosImplementor.getInstance().obtenerNegocioActivo();
	    BitmapDescriptor marcador = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_verde);
    	if(negocioSeleccionado != null){
    		_puntoMapa = new LatLng(Double.parseDouble(negocioSeleccionado.get_sLatitud()), Double.parseDouble(negocioSeleccionado.get_sLongitud()));
    		Toast.makeText(getApplicationContext(), "Mostrando ubicación de: " + negocioSeleccionado.get_sNombre(), Toast.LENGTH_SHORT).show();
    	}
    	else{
    		comprobarGPS();
    		_puntoMapa = new LatLng(Double.parseDouble(_sLatitud), Double.parseDouble(_sLongitud));
    		Toast.makeText(getApplicationContext(), "Mostrando ubicación actual", Toast.LENGTH_SHORT).show();
    	}
    	
    	_mapa.addMarker(new MarkerOptions().position(_puntoMapa).title(Constantes.TITULO_MARCADOR_NEGOCIO).icon(marcador));
		_btnCargarMasNegocios.setText(Constantes.TEXTO_BOTON_MAPA_BUSCAR_NEG);
		_btnCargarMasNegocios.setOnClickListener(onClickBuscarNegocios);
    	
    	_mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(_puntoMapa, Constantes.ZOOM_INICIAL_MAPA));
    	//_mapa.animateCamera(CameraUpdateFactory.zoomTo(Constantes.ZOOM_FINAL_MAPA), 1000, null);
    	moveCamera();
    	_mapa.setOnInfoWindowClickListener(onInfoNegocioClickListener);
    }    
    
    /**
	 * Listener del marcador del punto se�alado en el mapa. Abre la opci�n de abrir la ubicaci�n con
	 * otro servidor de mapas.
	 */
	public OnInfoWindowClickListener onInfoNegocioClickListener = new OnInfoWindowClickListener() {
		String sGeoUriFormat = "geo: %s,%s";
		@Override
		public void onInfoWindowClick(Marker marker) {
			String uri = String.format(sGeoUriFormat, _puntoMapa.latitude, _puntoMapa.longitude);
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		}
    };
    
    /**
     * Listener del bot�n "Buscar Negocios" que muestra los negocios cercanos a 5 km a la redonda.
     * Crea un c�rculo semitransparente para representar el rango.
     */
    OnClickListener onClickBuscarNegocios = new OnClickListener() {
		@Override
		public void onClick(View v) {
			_mapa.addCircle(new CircleOptions()
	        .center(_puntoMapa)
	        .radius(4000)
	        .strokeColor(Color.TRANSPARENT)
	        .strokeWidth(1)
	        .fillColor(Constantes.COLOR_SEMITRANSPARENTE_MAPA));
			
			new AsyncNegociosCercanosSender().execute();
		}
	};
	
	/**
	 * Hilo que obtiene los negocios cercanos a un punto desde el WS.
	 * @author eperaza
	 */
	private class AsyncNegociosCercanosSender extends AsyncTask<String, String, Boolean>
	{
		ProgressDialog pgDialog;
		
		@Override
		protected void onPreExecute()
		{
			pgDialog = new ProgressDialog(MapaActivity.this);
			pgDialog.setTitle("Por favor, espere");
			pgDialog.setMessage("Cargando negocios...");
			pgDialog.setCancelable(false);
			pgDialog.show();
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String jsonPuntoMapa;
			try {
				jsonPuntoMapa = JSONHelper.getInstance().obtenerJsonNegociosCercanos(_puntoMapa.latitude, _puntoMapa.longitude).toString();
				String sRespuesta = RestHelper.getInstance().PUT(SgprService.getInstance().getNegociosCercanosUrl(), jsonPuntoMapa);
				_lNegocios =  JSONHelper.getInstance().obtenerNegociosCercanosDesdeJson(sRespuesta);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		protected void onPostExecute(Boolean pResult)
		{
			if(_lNegocios != null && _lNegocios.size() > 0){
			    //Se obtienen los puntos, se agregan al mapa con un marcador y se agregan para centrar el mapa.
			    for(Negocio negocio:_lNegocios){
			    	int pin = R.drawable.ic_pin_rojo;
			    	agregarPuntoEnMapa(negocio, pin);
			    }
			    _mapa.setOnInfoWindowClickListener(onPinNegocioBusquedaClickListener);
		    }
			pgDialog.dismiss();
		}
	}
	
	/**
	 * Listener del marcador del punto se�alado en el mapa.
	 * Guarda el marcador seleccionado en una variable global y comprueba el GPS.
	 */
	public OnInfoWindowClickListener onPinNegocioBusquedaClickListener = new OnInfoWindowClickListener() {
		@Override
		public void onInfoWindowClick(Marker marker) {
			iniciarNegocioCercano(marker);
		}
    };
    
    /**
	 * Da por iniciado un negocio, actualiza el estado del negocio seleccionado, cambia el pin e inserta 
	 * un nuevo registro en la bit�cora.
	 */
	public void iniciarNegocioCercano(Marker pin){
		String sMarcadorId = pin.getId();
		int nNegocioSeleccionadoId = -1;
		Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		int nNegocioActivo = -1;
		if(negocioActivo != null)
			nNegocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		if(negocioActivo != null && negocioActivo.get_nEstado() == Constantes.ESTADO_NEGOCIO_ACTUAL)
			Toast.makeText(getApplicationContext(), Constantes.AVISO_NEGOCIO_SELECCIONADO, Toast.LENGTH_LONG).show();
		else{
			for(Negocio negocio:_lNegocios){
				if(sMarcadorId.equals(negocio.get_sPinId())){
					nNegocioSeleccionadoId = negocio.get_nNegocioId();
					negocio.set_nEstado(Constantes.ESTADO_NEGOCIO_ACTUAL);
				}
			}
			if(nNegocioSeleccionadoId != -1){
				BitmapDescriptor marcador = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_naranja);
				pin.setIcon(marcador);
				new AsyncbuscarYGuardarNuevoNegocio().execute(Integer.toString(nNegocioSeleccionadoId), Integer.toString(nNegocioActivo));
			}
		}
		
	}
	
	/**
	 * Hilo que obtiene el negocio buscado y lo guarda en la BD.
	 * @author eperaza
	 */
	private class AsyncbuscarYGuardarNuevoNegocio extends AsyncTask<String, String, Boolean>
	{
		String nIdNegocioSeleccionado;
		String nIdNegocioAnterior;
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				nIdNegocioSeleccionado = params[0];
				nIdNegocioAnterior = params[1];
				String sRespuestaBusqueda = RestHelper.getInstance().GET(SgprService.getInstance().getNegocioBuscadoUrl(params[0]), true);
				JSONHelper.getInstance().insertarNuevoNegocioBuscado(sRespuestaBusqueda);
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("ERROR BUSCANDO NEGOCIO", e.toString());
			}
			return false;
		}
		
		protected void onPostExecute(Boolean pResult)
		{
			if(pResult)
				activarNegocio(Integer.parseInt(nIdNegocioSeleccionado), Integer.parseInt(nIdNegocioAnterior));
			else
				Toast.makeText(getApplicationContext(), "A ocrrido un error al cargar el negocio.", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Cambia el estado del negocio actual a inactivo y coloca el estado actual al nuevo negocio seleccionado. 
	 * @param pNegocioSeleccionadoId
	 */
	public void activarNegocio(int pNegocioSeleccionadoId, int pNegocioAnteriorId){
		if(pNegocioAnteriorId != -1)
			NegociosImplementor.getInstance().actualizarEstadoNegocio(pNegocioAnteriorId, Constantes.ESTADO_NEGOCIO_VISITADO, Constantes.ESTADO_NEGOCIO_INACTIVO, "");
		NegociosImplementor.getInstance().actualizarEstadoNegocio(pNegocioSeleccionadoId, Constantes.ESTADO_NEGOCIO_ACTUAL, Constantes.ESTADO_NEGOCIO_ACTIVO, "");
		comprobarGPS();
		BitacoraImplementor.getInstance().insertarNuevaBitacora(_sLatitud, _sLongitud, true);
		Toast.makeText(getApplicationContext(), Constantes.AVISO_NEGOCIO_CARGADO, Toast.LENGTH_LONG).show();
	}
	
	/***************************************************************
	 * AMBOS
	 * ************************************************************/
	/**
	 * Agrega un nuevo pin en el mapa.
	 * @param pNegocio
	 * @param pPin
	 */
	public void agregarPuntoEnMapa(Negocio pNegocio, int pPin){
		LatLng puntoNegocio = new LatLng(Double.parseDouble(pNegocio.get_sLatitud()), Double.parseDouble(pNegocio.get_sLongitud()));
    	BitmapDescriptor pin = BitmapDescriptorFactory.fromResource(pPin);
	    Marker marcador = _mapa.addMarker(new MarkerOptions().position(puntoNegocio).title(pNegocio.get_sNombre()).icon(pin));
	    pNegocio.set_sPinId(marcador.getId());
	}
	
	/**
	 * Muestra un toast con el mensaje que se desea publicar.
	 * @param pMensaje
	 */
	public void mostarMensaje(String pMensaje){
		Toast.makeText(getApplicationContext(), pMensaje, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Verifica que el GPS est� activado y actualiza la latitud y la longitud..
	 */
	public void comprobarGPS(){
		_locationManager = Gps.getInstance().revisarGPS(this, _locationManager);
		if(_locationManager != null){
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, Constantes.SET_GPS);
		}
		else{
			_locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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