package com.ice.sgpr.ui.fragments;

import java.util.List;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.implementor.ImagenesImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.ui.activities.ImagenesActivity;
import com.ice.sgpr.ui.adapter.GaleriaImagenesAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * Fragment que muestra las im�genes del negocio.
 * Tambi�n tiene un bot�n para agregar im�genes nuevas.
 * @author eperaza
 * Fecha de creaci�n: 28/08/2013
 */
@SuppressWarnings("deprecation")
public class ImagenesFragment extends SgprFragment{
	GaleriaImagenesAdapter _adapter;
	private View _view;
	private int _selectedIndex = -1;
	private ProgressBar _progressBar;
	private Bitmap[] _imagenes;
	private static Boolean _bImagenesCargadas;
    private Animation animAlpha = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	_view = inflater.inflate(R.layout.fragment_imagenes, container, false);
        animAlpha = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_alpha);
        _progressBar = (ProgressBar) _view.findViewById(R.id.progressBar);
    	TextView tvAgregarFoto = (TextView) _view.findViewById(R.id.tv_agregar_foto);
    	tvAgregarFoto.setOnClickListener(onAgregarFoto);
    	_bImagenesCargadas = false;
		return _view;
    }
    
	public void cargarImagenes(){
    	int cantidadImagenes = ImagenesImplementor.getInstance().obtenerCantidadImagenes();
		_imagenes = new Bitmap[cantidadImagenes];
    	_adapter = new GaleriaImagenesAdapter(getActivity(), _imagenes);
    	Gallery gallery = (Gallery) _view.findViewById(R.id.galeria);
		gallery.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();
		
    	if (_imagenes != null && _imagenes.length > 0) {
			ImageView detail = (ImageView) _view.findViewById(R.id.img_detalle);
			detail.setImageBitmap(_imagenes[0]);

			if (_imagenes.length == 1) {
				_view.findViewById(R.id.galeria).setVisibility(View.GONE);
			}
		}

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				_selectedIndex = position;
				ImageView detail = (ImageView) _view.findViewById(R.id.img_detalle);
				detail.setImageBitmap(_adapter.getItem(position));
			}
		});
		
		new BackgroundImagesLoader().execute();

		if (_selectedIndex != -1) {
			gallery.setSelection(_selectedIndex);
			ImageView detail = (ImageView) _view.findViewById(R.id.img_detalle);
			detail.setImageBitmap(_adapter.getItem(_selectedIndex));
		}
    }
    
    /**
	 * Listener del bot�n para agregar una foto.
	 * Llama al activity que carga la opci�n de tomar una foto nueva o seleccionar una previamente almacenada.
	 * Verfica que no existan m�s de 3 im�genes (m�ximo).
	 */
	public OnClickListener onAgregarFoto = new OnClickListener() {
		@Override
		public void onClick(View v) {
            v.startAnimation(animAlpha);
			if(NegociosImplementor.getInstance().obtenerNegocioActivo() != null){
				int nCantidadImagenes = ImagenesImplementor.getInstance().obtenerCantidadImagenes();
				if(nCantidadImagenes < 3){
					Intent intent = new Intent(getActivity(), ImagenesActivity.class);
					startActivityForResult(intent, 999);
				}
				else
					Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_MAXIMO_IMAGENES, Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private class BackgroundImagesLoader extends AsyncTask<Void, Integer, Void>
	{
    	@Override
        protected void onPreExecute() 
		{
    		_progressBar.setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			List<Bitmap> listaImagenes = ImagenesImplementor.getInstance().obtenerListaImagenes();
			
			if (listaImagenes.size() > 0)
			{
				int nTotalImagenes = listaImagenes.size();
				for (int i = 0; i < nTotalImagenes; i++)
				{
					try
					{
						Log.i("SGPR", "Imagen " + i);
						_imagenes[i] = listaImagenes.get(i);
					}
					catch(Exception ex)
					{
					}
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) 
		{
			_adapter.notifyDataSetChanged();
			if (_imagenes.length > 0) {
				ImageView detail = (ImageView) _view.findViewById(R.id.img_detalle);
				detail.setImageBitmap(_imagenes[0]);
				if (_imagenes.length == 1)
					_view.findViewById(R.id.galeria).setVisibility(View.GONE);
				else
					_view.findViewById(R.id.galeria).setVisibility(View.VISIBLE);
				_bImagenesCargadas = true;
			}
			_progressBar.setVisibility(ProgressBar.GONE);
		}
	}
	
	/**
	 * Actualiza las imagenes guardadas cuando el tab de imagenes regresa a tener focus. Exepto cuando no se hayan
	 * agregado im�genes nuevas.
	 */
    @Override
    public void onResume() 
    {
        super.onResume();
        if(!_bImagenesCargadas && NegociosImplementor.getInstance().obtenerNegocioActivo() != null)
        	cargarImagenes();
        else
        	Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_NEGOCIO_NO_SELECCIONADO, Toast.LENGTH_LONG).show();
    }
    
    /**
     * Cuando se retorna del activity que a�ade una imagen se deben volver a cargarlas.
     * @param pValor
     */
    public static void setImagenesCargadas(Boolean pValor){
    	_bImagenesCargadas = pValor;
    }
}
