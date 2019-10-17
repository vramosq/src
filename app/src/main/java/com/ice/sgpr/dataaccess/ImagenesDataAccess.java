package com.ice.sgpr.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaImagenes;
import com.ice.sgpr.entidades.Imagen;
/**
 * Acceso a datos de las preguntas.
 * @author eperaza
 * Fecha de creación: 20/08/2013.
 */
public class ImagenesDataAccess extends AbstractDataAccess {
	private String _consultaObtenerImagenes = "SELECT " + TablaImagenes.COL_IMAGEN + " FROM "+ TablaImagenes.NOMBRE_TABLA +" WHERE NEGOCIO_ID = ";
	private String _consultaCantidadImagenes = "SELECT COUNT() FROM IMAGENES WHERE NEGOCIO_ID = ";
	private String _consultaImagenesVersionamiento = "SELECT " + TablaImagenes.COL_NEGOCIO_ID + ", " + TablaImagenes.COL_IMAGEN + ", " 
			+ TablaImagenes.COL_IMAGEN_ID + " FROM " + TablaImagenes.NOMBRE_TABLA + " WHERE " + TablaImagenes.COL_USUARIO_ID + " = ";
	
	public ImagenesDataAccess()
	{
		_helper = new DatabaseHelper(SgprApplication.getContext());
	}
	
	/**
	 * Inserta la imagen en la BD, con su respectivo negocioID.
	 * @param pIdNegocio
	 * @param pImagen
	 */
	public void insertarNuevaImagen(int pIdNegocio, byte[] pImagen, String pCodigoUsuario){
		ContentValues values = new ContentValues();
		values.put(TablaImagenes.COL_NEGOCIO_ID, pIdNegocio);
		values.put(TablaImagenes.COL_IMAGEN, pImagen);
		values.put(TablaImagenes.COL_ACTUALIZADO, 1);
		values.put(TablaImagenes.COL_USUARIO_ID, pCodigoUsuario);
		
		_database.insert(TablaImagenes.NOMBRE_TABLA, null, values);
	}
	
	/**
	 * Obtiene las im�genes almacenadas en la BD de un determinado negocio.
	 * @param pIdNegocio
	 */
	public List<Bitmap> obtenerImagenes(int pIdNegocio){
		Cursor cursorImagenes = _database.rawQuery(_consultaObtenerImagenes + pIdNegocio, null);
		List<Bitmap>lImagenes = new ArrayList<Bitmap>();
		
		if (cursorImagenes.moveToFirst()){
		     do {
		          byte[] bmpImagen = cursorImagenes.getBlob(0);
		          Bitmap bmp = BitmapFactory.decodeByteArray(bmpImagen, 0, bmpImagen.length);
		          lImagenes.add(bmp);
		     } while(cursorImagenes.moveToNext());
		}
		return lImagenes;
	}
	
	/**
	 * Retorna la cantidad de imagenes de un determinado negocio.
	 * @param pIdNegocio
	 * @return
	 */
	public int obtenerCantidadImagenes(int pIdNegocio){
		Cursor cursorImagenes = _database.rawQuery(_consultaCantidadImagenes + pIdNegocio, null);
		if (cursorImagenes.moveToFirst()){
			return cursorImagenes.getInt(0);
		}
		else return 0;
	}
	
	/**
	 * Obtiene las im�genes almacenadas en la BD de todos los negocios para versionamiento.
	 * @param pIdNegocio
	 */
	public List<Imagen> obtenerImagenesVersionamiento(String pCodigoUsuario){
		Cursor cursorImagenes = _database.rawQuery(_consultaImagenesVersionamiento + pCodigoUsuario, null);
		List<Imagen>lImagenes = new ArrayList<Imagen>();
		
		if (cursorImagenes.moveToFirst()){
		     do {
		          int nCodigoNegocio = cursorImagenes.getInt(0);
		          byte[] bmpImagen = cursorImagenes.getBlob(1);
		          int nImagenId = cursorImagenes.getInt(2);
		          lImagenes.add(new Imagen(nCodigoNegocio, bmpImagen, nImagenId));
		     } while(cursorImagenes.moveToNext());
		}
		return lImagenes;
	}
	
	/**
	 * Actualiza el estado de la imagen a 0, indicando que ya fue versionada.
	 * @param pBitacoraId
	 */
	public void actualizarEstadoImagenVersionamiento(int pImagenId){
		ContentValues values = new ContentValues();
		values.put(TablaImagenes.COL_ACTUALIZADO, 0);
		String sCondicion = TablaImagenes.COL_IMAGEN_ID + " = " + pImagenId;
		_database.update(TablaImagenes.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Actualiza el Id del negocio en la imagen por el nuevo asignado al negocio en el SP.
	 * @param pCodigoAnterior
	 * @param pCodigoNuevo
	 */
	public void actualizarIdImagenVersionamiento(int pCodigoAnterior, int pCodigoNuevo){
		ContentValues values = new ContentValues();
		values.put(TablaImagenes.COL_NEGOCIO_ID, pCodigoNuevo);
		String sCondicion = TablaImagenes.COL_NEGOCIO_ID + " = " + pCodigoAnterior;
		_database.update(TablaImagenes.NOMBRE_TABLA, values, sCondicion, null);
	}
	
	/**
	 * Borra las im�genes de un determinado usuario.
	 * @param pCodigoUsuario
	 */
	public void borrarImagenesUsuario(String pCodigoUsuario){
		String sCondicion = TablaImagenes.COL_USUARIO_ID + " = " + pCodigoUsuario;
		_database.delete(TablaImagenes.NOMBRE_TABLA, sCondicion, null);
	}
	
	/**
	 * Borra las im�genes correspondientes a un negocio descartado.
	 * @param pCodigoNegocio
	 */
	public void borrarImagenesNegocio(int pCodigoNegocio){
		String sCondicion = TablaImagenes.COL_NEGOCIO_ID + " = " + pCodigoNegocio;
		_database.delete(TablaImagenes.NOMBRE_TABLA, sCondicion, null);
	}
}
