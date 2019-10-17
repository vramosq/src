package com.ice.sgpr.ui.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Utils;
import com.ice.sgpr.implementor.ImagenesImplementor;
import com.ice.sgpr.ui.fragments.ImagenesFragment;
import com.ice.sgpr.ui.utils.ImageChooseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Activity de la seccion de imagenes. Esta seccion esta en un activity aparte
 * porque desde un fragment no puede hacerse uso del onActivityResult. Este
 * activity tiene un fondo transparente para que visualmente se tenga el efecto
 * que desde el fragment se estan mostrando las opciones de tomar/seleccionar
 * foto.
 * 
 * @author eperaza Fecha de creacion: 28/08/2013
 */
public class ImagenesActivity extends Activity {
	private ImageChooseHandler _imageChooseHandler;
	private String _picturePath;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_simple);
		RelativeLayout rlActivity = (RelativeLayout) findViewById(R.id.rl_fragment_simple);
		rlActivity.setBackgroundResource(R.color.transparente);
		_imageChooseHandler = new ImageChooseHandler(this);
		seleccionDeFoto();
	}

	/**
	 * Accion que se dispara al presionar el boton de tomar/seleccionar una
	 * foto.
	 */
	private void seleccionDeFoto() {
		AlertDialog dialog = _imageChooseHandler.createChooseImageDialog();
		dialog.setOnCancelListener(onCancelar);
		dialog.show();
	}

	/**
	 * Respuesta de la accion de tomar/seleccionar foto.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constantes.IMAGEN_DESDE_CAMARA
				&& resultCode == Activity.RESULT_OK) {
			_picturePath = _imageChooseHandler.getPathFromCamera();
			guardarImagen(_picturePath);
		} else if (requestCode == Constantes.IMAGEN_DESDE_ARCHIVO
				&& resultCode == Activity.RESULT_OK) {
			_picturePath = _imageChooseHandler.getPathFromFile(data);
			guardarImagen(_picturePath);
		}
		eliminarActivity();
	}

	/**
	 * Listener del dialogo de opciones en caso de que no se seleccione ninguna
	 * accion.
	 */
	public OnCancelListener onCancelar = new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			eliminarActivity();

		}
	};

	/**
	 * Mata al activity de las imagenes para regresar al activity de los datos
	 * del negocio.
	 */
	public void eliminarActivity() {
		finish();
	}

	/**
	 * Toma la imagen seleccionada, la convirte a byte[] y la guarda en la BD.
	 * 
	 * @param pPathImagen
	 */
	private void guardarImagen(String pPathImagen) {
		// Determina en que posicion viene la imagen:
		ExifInterface ei;
		try {
			ei = new ExifInterface(pPathImagen);

			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int width = Utils.dpToPixels(133);
			int height = Utils.dpToPixels(167);

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_UNDEFINED 
					|| orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				width = Utils.dpToPixels(167);
				height = Utils.dpToPixels(133);
			}

			final BitmapFactory.Options opciones = new BitmapFactory.Options();
			opciones.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pPathImagen, opciones);

			opciones.inSampleSize = calculateInSampleSize(opciones, width, height);
			// Decode bitmap with inSampleSize set
			opciones.inJustDecodeBounds = false;

			Bitmap bmpFoto = BitmapFactory.decodeFile(pPathImagen, opciones);
			if (bmpFoto != null) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					bmpFoto = rotateImage(bmpFoto, 90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					bmpFoto = rotateImage(bmpFoto, 180);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					bmpFoto = rotateImage(bmpFoto, 270);
					break;
				default:
					bmpFoto = rotateImage(bmpFoto, 90);
				}

				// Bitmap rotatedBitmap;
				/*
				 * if(bmpFoto != null){ if (bmpFoto.getWidth() >
				 * bmpFoto.getHeight()) { rotatedBitmap =
				 * getResizedBitmap(bmpFoto, height, width, true); } else{
				 * rotatedBitmap = getResizedBitmap(bmpFoto, width, height,
				 * false); }
				 */
				ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
				bmpFoto.compress(Bitmap.CompressFormat.PNG, 100,
						imageByteStream);
				byte[] byteImagen = imageByteStream.toByteArray();

				ImagenesImplementor.getInstance().insertarImagen(byteImagen);
				ImagenesFragment.setImagenesCargadas(false);
			} else
				Toast.makeText(getApplicationContext(), Constantes.ERROR_FOTO,
						Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), Constantes.ERROR_FOTO,
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private static Bitmap rotateImage(Bitmap img, int degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
				img.getHeight(), matrix, true);
		img.recycle();
		return rotatedImg;
	}
/*
	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight,
			boolean rotate) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		if (rotate)
			matrix.postRotate(90);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		bm.recycle();
		return resizedBitmap;
	}
*/
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
}
