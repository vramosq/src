package com.ice.sgpr.ui.activities;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Gps;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.service.RestHelper;
import com.ice.sgpr.service.SgprService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.AnimationUtils;

/**
 * Activity para el login de la aplicacion
 * @author eperaza
 * fecha de creacion: 26/07/2013
 */
public class LoginActivity extends SgprActionBarActivity{
	private LocationManager _locationManager;
    private Animation animAlpha = null;
	
	EditText txtUsuario, txtPassword;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(Constantes.TITULO_LOGIN);
		txtUsuario = (EditText)findViewById(R.id.txt_usuario);
		txtPassword = (EditText)findViewById(R.id.txt_password);
        TextView btnLogin = (TextView)findViewById(R.id.tvLogin);

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        btnLogin.setOnClickListener(onLoginClick);
		verificarUsuarioLogueado();
	}
	
	/**
	 * Revisa las credenciales del usuario y si son correctas inicia sesi�n.
	 */
	public void loginClickHandler(View pView){		
		String sUsuario = txtUsuario.getText().toString();
		String sPassword = txtPassword.getText().toString();
		
		if(sUsuario.equals("") || sPassword.length() != 4)
			mostrarAviso(Constantes.ERROR_LOGIN_PASS_INVALIDO);
		else if(comprobarGPS()){
			validarUsuario(sUsuario, sPassword);
		}
	}

    private View.OnClickListener onLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.startAnimation(animAlpha);
            String sUsuario = txtUsuario.getText().toString();
            String sPassword = txtPassword.getText().toString();

            if(sUsuario.equals("") || sPassword.equals(""))
                mostrarAviso(Constantes.ERROR_LOGIN_VACIO);
            else if(sUsuario.equals("") || sPassword.length() != 4)
                mostrarAviso(Constantes.ERROR_LOGIN_PASS_INVALIDO);
            else if(comprobarGPS()){
                validarUsuario(sUsuario, sPassword);
            }
        }
    };
	
	/**
	 * Muestra un mensaje al usuario usando toast.
	 * @param pMensaje
	 */
	public void mostrarAviso(String pMensaje){
		Toast.makeText(getApplicationContext(), pMensaje, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Revisa los datos del usuario en la BD del dispositivo.
	 * @param pNombreUsuario
	 * @param pContrasena
	 */
	public void validarUsuario(String pNombreUsuario, String pContrasena){
		int nContrasenaCifrada = Integer.parseInt(cifrarContrasena(pContrasena));
		String nUsuarioId = UsuariosImplementor.getInstance().validarUsuarioLogueado(pNombreUsuario, nContrasenaCifrada);
		if(nUsuarioId != null){
			UsuariosImplementor.getInstance().cambiarDeUsuario(nUsuarioId);
			pasarActivityMenu();
		}
		else
			new AsyncLoginSender().execute(pNombreUsuario, pContrasena);
	}
	
	/**
	 * Hilo para enviar los datos al WS mediante PUT.
	 * @author eperaza
	 *
	 */
	private class AsyncLoginSender extends AsyncTask<String, String, String[]>
	{
		String sNombreUsuario;
		String sContrasenaCifrada;
		@Override
		protected void onPreExecute()
		{
		}
		@Override
		protected String[] doInBackground(String... params) {
			try{
				sNombreUsuario = params[0];
				sContrasenaCifrada = cifrarContrasena(params[1]);
				String requestLogin = JSONHelper.getInstance().obtenerJsonLogin(params[0], sContrasenaCifrada);
				String respuesta = RestHelper.getInstance().PUT(SgprService.getInstance().getLoginUrl(), requestLogin);
				
				return JSONHelper.getInstance().descifrarRespuestaLogin(respuesta);
			}
			catch (Exception e){
				return null;
			}
		}
		@Override
		protected void onPostExecute(String[] pResult)
		{
			if(pResult != null){
				int pCodigoRespuesta = Integer.parseInt(pResult[0]);
				if(pCodigoRespuesta == Constantes.RESPUESTA_USUARIO_VALIDO){
					String sCodigoUsuario = pResult[1];
					String sRolUsuario = pResult[2];
					entrarMenu(sCodigoUsuario, sNombreUsuario, Integer.parseInt(sContrasenaCifrada), sRolUsuario);
				}
				else if(pCodigoRespuesta == Constantes.RESPUESTA_PASS_INVALIDO)
					mostrarAviso(Constantes.ERROR_LOGIN_PASS_INVALIDO);
				else
					mostrarAviso(Constantes.ERROR_LOGIN_USUARIO_INVALIDO);
			}
			else
				mostrarAviso(Constantes.LOGIN_ERROR);
		}
	}
	
	/**
	 * Pasa al menu principal, una vez que se han validado las credenciales. A su vez guarda el usuario
	 * logueado en la BD.
	 * @param pUsuario
	 */
	public void entrarMenu(String pUsuario, String pNombreUsuario, int pContrasena, String pRolUsuario){
		UsuariosImplementor.getInstance().insertarUsuario(pUsuario, pNombreUsuario, pContrasena, pRolUsuario);
		pasarActivityMenu();
	}
	
	public void pasarActivityMenu(){
		startActivity(new Intent(this, MenuPrincipalActivity.class));
		this.finish();
	}
	
	/**
	 * Revisa si existe alguna sesion abierta. Si la hay muestra un aviso.
	 */
	public void verificarUsuarioLogueado(){
		String[] sDatosUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado();
		if(sDatosUsuario != null){
			mostrarDialogo(sDatosUsuario[1]);
		}
	}
	
	/**
	 * Indica al usuario que hay una sesion abierta y pregunta si desea seguir como dicho usuario o si desea cambiar.
	 */
	@SuppressLint("NewApi")
	public void mostrarDialogo(String pNombreUsuario){
		new AlertDialog.Builder(this, R.style.AlertDialog)
	    .setTitle("Hay una sesión abierta. \n ¿Continuar como " + pNombreUsuario + "?")
	    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	pasarActivityMenu();
	        }
	    })
	    .setNegativeButton("Cambiar Usuario", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        }
	    }).show();
	}
	
	/**
	 * Cifra la contraseña ingresada por el usuario.
	 * @param pContrasena: Contraseña ingresada en la pantalla de login.
	 * @return
	 */
	public String cifrarContrasena(String pContrasena){
		int nContrasenaIngresada = Integer.parseInt(pContrasena);
		int nContrasenaCifrada = ((((nContrasenaIngresada * 2) + 2) * 3) - 5);
		String sNuevaContrasena = Integer.toString(nContrasenaCifrada);
		return sNuevaContrasena;
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
}
