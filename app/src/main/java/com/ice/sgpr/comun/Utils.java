package com.ice.sgpr.comun;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ice.sgpr.SgprApplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

@SuppressLint("SimpleDateFormat")
public class Utils {
	public static int dpToPixels(int pDp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pDp, SgprApplication.getContext().getResources().getDisplayMetrics());
	}
	
	/**
	 * Creates a drawable from an url
	 * @param url ImageUrl
	 * @param src_name Name of the drawable
	 * @return
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	public static Bitmap createBitmapFromUrl(String url, String src_name) throws java.net.MalformedURLException, java.io.IOException 
    {	
		URLConnection con =  new URL(url).openConnection();
		con.setConnectTimeout(10000);
		InputStream is = con.getInputStream();
		return BitmapFactory.decodeStream(is);
    }
	
	/**
	 * Se obtiene la hora y fecha del sistema.
	 * @return Hora y fecha actual.
	 */
	public static String obtenerFechaActual(){
		Date dFechaInicio = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String sFecha = df.format(dFechaInicio);
		return sFecha;
	}
	
	/**
	 * Transforma una fecha a un formato de día-mes-año
	 * @param pFecha
	 * @return
	 */
	public static String convertirFecha(String pFecha){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String sFecha = df.format(pFecha);
		return sFecha;
	}
	
	/**
	 * Se obtiene la fecha actual y se convierte en un numero que funcione como
	 * ID temporal para un nuevo negocio.
	 * @return
	 */
	public static int obtenerIdGenerico(){
		Date dFechaInicio = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
		String sFecha = df.format(dFechaInicio);
		return Integer.parseInt(sFecha);
	}
}
