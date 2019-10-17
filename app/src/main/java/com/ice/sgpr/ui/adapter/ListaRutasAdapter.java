package com.ice.sgpr.ui.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Ruta;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter para la lista de rutas.
 * @author eperaza
 * Fecha de creaci�n: 31/07/2013.
 */
public class ListaRutasAdapter extends BaseAdapter implements Serializable{
	private static final long serialVersionUID = 7090002108537308006L;
	protected Activity activity;
	protected ArrayList<Ruta> rutaItems;
	int indexSeleccionado = -1;
	
	public ListaRutasAdapter(Activity pActivity, ArrayList<Ruta> pItems){
		this.activity = pActivity;
	    this.rutaItems = pItems;
	}
	
	@Override
	public int getCount() {
		return rutaItems.size();
	}

	@Override
	public Object getItem(int pPosition) {
		return rutaItems.get(pPosition);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void agregarPregunta(Ruta pRuta){
		rutaItems.add(pRuta);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_lista_ruta, null);
		}
		
		Ruta item = rutaItems.get(position);
		ImageView imgPendiente = (ImageView)view.findViewById(R.id.img_alerta_ruta_pendiente);
		if(item.getPendiente() != 1)
			imgPendiente.setVisibility(View.INVISIBLE);
		
		TextView tvTituloRuta = (TextView)view.findViewById(R.id.tv_titulo_ruta);
		tvTituloRuta.setText(item.getNombre());
		
		TextView tvUltimaVisita = (TextView)view.findViewById(R.id.tv_ultima_visita_fecha);
		tvUltimaVisita.setText(item.getFecha().equals("0") ? Constantes.VACIO : item.getFecha());
		
		TextView tvFrecuencia = (TextView)view.findViewById(R.id.tv_frecuencia_visitas);
		tvFrecuencia.setText(item.getFrecuencia());
		
		return view;
	}
}
