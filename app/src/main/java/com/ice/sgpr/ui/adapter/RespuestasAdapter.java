package com.ice.sgpr.ui.adapter;

import java.util.ArrayList;

import com.ice.sgpr.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter par ala lista de respuestas. si se trata de una pregunta anidada, cambia el icono de "check" por una
 * flecha hacia adentro, indicando que la pregunta tiene otra secundaria.
 * @author eperaza
 *
 */
public class RespuestasAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<String> opcionesRespuesta;
	protected Boolean bCambiarImgCheck;
	
	public RespuestasAdapter(Activity pActivity, ArrayList<String> pOpcionesRespuesta){
		this.activity = pActivity;
		this.opcionesRespuesta = pOpcionesRespuesta;
	}
	
	@Override
	public int getCount() {
		return opcionesRespuesta.size();
	}

	@Override
	public Object getItem(int position) {
		return opcionesRespuesta.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_list_opcion_respuesta, null);
			if(bCambiarImgCheck){
				ImageView imgCheck = (ImageView) view.findViewById(R.id.img_checkbox);
				imgCheck.setImageResource(R.drawable.ic_anidada);
			}
		}
		
		String sRespuesta = opcionesRespuesta.get(position);
		TextView tvRespuesta = (TextView)view.findViewById(R.id.tv_texto_opcion_respuesta);
		tvRespuesta.setText(sRespuesta);
		return view;
	}
	
	@Override
	public int getViewTypeCount() {
	    return opcionesRespuesta.size();
	}

	@Override
	public int getItemViewType(int position) {
	    return position;
	}
	
	public void agregarOpcionRespuesta(String pOpcion, Boolean pCambiarImagenCheck){
		opcionesRespuesta.add(pOpcion);
		bCambiarImgCheck = pCambiarImagenCheck;
	}
}
