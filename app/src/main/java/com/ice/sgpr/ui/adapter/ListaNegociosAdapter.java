package com.ice.sgpr.ui.adapter;

import java.util.ArrayList;

import com.ice.sgpr.R;
import com.ice.sgpr.entidades.Negocio;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter para la lista de negocios del buscador.
 * @author eperaza
 * Fecha de creaci�n: 30/09/2013.
 */
public class ListaNegociosAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<Negocio> negocioItems;
	int indexSeleccionado = -1;
	
	public ListaNegociosAdapter(Activity pActivity, ArrayList<Negocio> pItems){
		this.activity = pActivity;
	    this.negocioItems = pItems;
	}
	
	@Override
	public int getCount() {
		return negocioItems.size();
	}

	@Override
	public Object getItem(int pPosition) {
		return negocioItems.get(pPosition);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void agregarNegocio(Negocio pNegocio){
		negocioItems.add(pNegocio);
	}
	
	public void limpiarAdapter(){
		negocioItems.clear();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_list_buscador, null);
		}
		
		Negocio item = negocioItems.get(position);
		
		TextView tvNombre = (TextView)view.findViewById(R.id.tv_nombre_negocio_buscador);
		tvNombre.setText(item.get_sNombre());
		
		TextView tvDireccion = (TextView)view.findViewById(R.id.tv_direccion_negocio_buscador);
		tvDireccion.setText(item.get_sDireccion());
		
		TextView tvDescripcion = (TextView)view.findViewById(R.id.tv_descripcion_negocio_buscador);
		tvDescripcion.setText(item.get_sDescripcion());
			
		return view;
	}
}
