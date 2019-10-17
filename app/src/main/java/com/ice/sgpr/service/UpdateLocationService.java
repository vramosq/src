package com.ice.sgpr.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.implementor.SeguimientoImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;

@SuppressLint("SimpleDateFormat")
public class UpdateLocationService extends Service {
	//private static final String TAG = "BOOMBOOMTESTGPS";
	private LocationManager mLocationManager = null;
	private static final int LOCATION_INTERVAL = 600000;
	private static final float LOCATION_DISTANCE = 10f;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
//		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
//				.show();
		initializeLocationManager();
		try {
	        mLocationManager.requestLocationUpdates(
	                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
	                mLocationListeners[1]);
	    } catch (SecurityException ex) {
	    	Toast.makeText(this, "fail to request location update, ignore", Toast.LENGTH_SHORT).show();
	        Log.i(Constantes.TAG_SGPR, "fail to request location update, ignore", ex);
	    } catch (IllegalArgumentException ex) {
	    	Toast.makeText(this, "network provider does not exist, " + ex.getMessage(), Toast.LENGTH_SHORT).show();
	        Log.d(Constantes.TAG_SGPR, "network provider does not exist, " + ex.getMessage());
	    }
	    try {
	        mLocationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
	                mLocationListeners[0]);
	    } catch (SecurityException ex) {
	        Log.i(Constantes.TAG_SGPR, "fail to request location update, ignore", ex);
	    } catch (IllegalArgumentException ex) {
	        Log.d(Constantes.TAG_SGPR, "gps provider does not exist " + ex.getMessage());
	    }
	}

	@Override
	public IBinder onBind(Intent intent) {
//		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG)
//				.show();
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG)
//				.show();
		if (mLocationManager != null) {
	        for (int i = 0; i < mLocationListeners.length; i++) {
	            try {
	                mLocationManager.removeUpdates(mLocationListeners[i]);
	            } catch (Exception ex) {
	                Log.i(Constantes.TAG_SGPR, "fail to remove location listners, ignore", ex);
	            }
	        }
	    }
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
//		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG)
//				.show();
		//String sLatitude = "", sLongitude = "";
		
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		
		if(hour >= 7 && hour <= 19){
			if(mLocationManager != null && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
				Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
				new AsyncLocationSender().execute(location.getLatitude(), location.getLongitude());
	
	//			Toast.makeText(this, "Latitud: " + sLatitude + ", Longitud: " + sLongitude + " Usuario: " + sUsuario[0], Toast.LENGTH_LONG)
	//			.show();
			}
//			else 
//				Toast.makeText(this, "El GPS est� desactivado", Toast.LENGTH_LONG)
//				.show();
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
//		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
//				.show();
		return super.onUnbind(intent);
	}
	
	
	private void initializeLocationManager() {
	    //Log.e(Constantes.TAG_SGPR, "initializeLocationManager");
	    if (mLocationManager == null) {
	        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
	    }
	}
	
	/**
	 * Clase para obtener la localizaci�n
	 */
	private class LocationListener implements android.location.LocationListener{
	    Location mLastLocation;
	    public LocationListener(String provider)
	    {
	        //Log.i(Constantes.TAG_SGPR, "LocationListener " + provider);
	        mLastLocation = new Location(provider);
	    }
	    @Override
	    public void onLocationChanged(Location location)
	    {
	        //Log.i(Constantes.TAG_SGPR, "onLocationChanged: " + location);
	        mLastLocation.set(location);
	    }
	    @Override
	    public void onProviderDisabled(String provider)
	    {
	        //Log.i(Constantes.TAG_SGPR, "onProviderDisabled: " + provider);            
	    }
	    @Override
	    public void onProviderEnabled(String provider)
	    {
	        //Log.i(Constantes.TAG_SGPR, "onProviderEnabled: " + provider);
	    }
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras)
	    {
	        //Log.e(Constantes.TAG_SGPR, "onStatusChanged: " + provider);
	    }
	}
	
	LocationListener[] mLocationListeners = new LocationListener[] {
	        new LocationListener(LocationManager.GPS_PROVIDER),
	        new LocationListener(LocationManager.NETWORK_PROVIDER)
	};
	
	/**
	 * Creaci�n del json de seguimiento
	 * @return
	 * @throws org.json.JSONException
	 */
	public String obtenerJsonSeguimiento(Double pLatitud, Double pLongitud, String date) throws JSONException 
	{
		JSONObject obj = new JSONObject();
		String [] sUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado();
		
		obj.put("US", Long.parseLong(sUsuario[0]));
		obj.put("LA", pLatitud);			
		obj.put("LO", pLongitud);
		obj.put("FE", date);
		
		JSONObject jsonRespuesta = new JSONObject();
		jsonRespuesta.put("SP", SgprService.SP_SEGUIMIENTO);
		jsonRespuesta.put("Info", obj);
		return jsonRespuesta.toString();
	}
	
	/**
	 * Hilo para enviar los datos al WS mediante PUT.
	 * @author eperaza
	 *
	 */
	private class AsyncLocationSender extends AsyncTask<Double, String, String>
	{
		private Double latitude, longitude;
		@Override
		protected void onPreExecute()
		{
		}
		@Override
		protected String doInBackground(Double... params) {
			try{
				latitude = params[0];
				longitude = params[1];
				
				String sUrl = SgprService.getInstance().getSendInfo();
				String sJsonArrayInfo = obtenerJsonSeguimiento(latitude, longitude, dateToString(new Date()));
				String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayInfo);
				
				//return "-1";
				return JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
			}
			catch (Exception e){
				return "";
			}
			
		}
		@Override
		protected void onPostExecute(String pResult)
		{
			if(!pResult.equals("0")){
				String formatedDate = dateToString(new Date());
				SeguimientoImplementor.getInstance().agregarSeguimiento(formatedDate, latitude, longitude);
			}
		}
	}
	
	
	private String dateToString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String formtedDate = "";
		formtedDate = formatter.format(date);
		return formtedDate;
	}
}