package com.ice.sgpr.ui.activities;

import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Gps;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.implementor.ParametrosImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.service.UpdateLocationService;

/**
 * Menu principal de la aplicacion Sgpr.
 * 
 * @author eperaza
 * fecha de creacion: 26/07/2013
 */
public class MenuPrincipalActivity extends SgprActionBarActivity {
	private LocationManager _locationManager;
	private PendingIntent pendingIntent;
    private Animation animAlpha = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
        getSupportActionBar().setTitle(Constantes.TITULO_APP);


        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        //TODO: COMENTAR PARA VERSIÓN ICE:
        //activarSeguimientoGPSVersionICE();
        
        //TODO: DESCOMENTAR PARA VERSIÓN NORMAL
        //Determina si se le ha pedido permiso al usuario para el seguimiento
        //0 = No ha aceptado ni rechazado. 1 = aceptó.
        int aceptaSegimiento = UsuariosImplementor.getInstance().aceptaSeguimiento();
        if(aceptaSegimiento == 0)
        	activarSeguimientoGPSVersionNormal();
        else if(aceptaSegimiento == 1)
        	activarSeguimientoGPSVersionICE();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_activity, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout)
        {
            //Detiene el servicio
            if(isMyServiceRunning(UpdateLocationService.class)){
                Intent i=new Intent(this, UpdateLocationService.class);
                PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pi);
                stopService(i);
            }

            UsuariosImplementor.getInstance().desactivarUsuario();
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
            return true;
        }
        return false;
    }
	
	/**
	 * Evento que se dispara al seleccionar alguno de los elementos del menú principal en el xml.
	 * @param pView
	 */
	 public void clickHandler(View pView){
         pView.startAnimation(animAlpha);
		 if(comprobarGPS()){
			 if (pView.getId() == R.id.tv_puntos_recarga){
				 mostrarNegocios();
			 }
			 else{
				 mostrarRutas();
			 }
		 }
	 }
	 
	 /**
	  * Pasa al activity de Negocios.
	  */
	 public void mostrarNegocios(){
		 List<Parametro> lParametros = ParametrosImplementor.getInstance().obtenerListaParametros(0);
			if(lParametros.size() == 0)
				Toast.makeText(this, Constantes.ADVERTENCIA_DEBE_SINCRONIZAR, Toast.LENGTH_LONG).show();
			else
				startActivity(new Intent(this, NegociosActivity.class));
	 }
	 
	 /**
	  * Pasa al activity de Rutas.
	  */
	 public void mostrarRutas(){
		 startActivity(new Intent(this, RutasActivity.class));
	 }
	 
	/******************
	 * Comprobacion GPS
	 ******************/
	/**
	 * Espera la respuesta sobre la activacion del GPS.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Gps.getInstance().revisarActivityResult(requestCode, resultCode, data, this, 
				_locationManager, Constantes.ADVERTENCIA_ACTIVAR_GPS);
	}
	
	/**
	 * Verifica que el GPS esta activado.
	 */
	public Boolean comprobarGPS(){
		_locationManager = Gps.getInstance().revisarGPS(this, _locationManager);
		if(_locationManager != null){
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, Constantes.SET_GPS);
			return false;
		}
		else
			return true;
	}
	
	/**
	 * Activa el servicio de seguimiento GPS (Versión ICE)
	 */
	private void activarSeguimientoGPSVersionICE(){
		boolean isServiceRunnig = isMyServiceRunning(UpdateLocationService.class);
		if(!isServiceRunnig){
			Intent myIntent = new Intent(MenuPrincipalActivity.this, UpdateLocationService.class);
		   	pendingIntent = PendingIntent.getService(MenuPrincipalActivity.this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 600);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 600000, pendingIntent);
            
            UsuariosImplementor.getInstance().actualizarPermisoSeguimiento(1);
		}
	}
	
	/**
	 * Activa el servicio de seguimiento GPS (Versión normal)
	 */
	private void activarSeguimientoGPSVersionNormal() {
		new AlertDialog.Builder(this, R.style.AlertDialog)
				.setTitle(Constantes.AVISO_ACTIVAR_SEGUIMIENTO)
				.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						activarSeguimientoGPSVersionICE();
						showMessage(Constantes.AVISO_SEGUIMIENTO_ACTIVADO);
						dialog.cancel();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						UsuariosImplementor.getInstance().actualizarPermisoSeguimiento(2);
						dialog.cancel();
					}
				}).show();
	}
	
	/**
	 * Revisa si el servicio para obtener el GPS está activo
	 * @param serviceClass: Clase del servicio.
	 * @return
	 */
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private void showMessage(String mensaje){
		Toast.makeText(MenuPrincipalActivity.this, mensaje, Toast.LENGTH_LONG).show();
	}
}
