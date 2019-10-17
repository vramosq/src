package com.ice.sgpr.ui.adapter;

import java.util.ArrayList;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter para las respuestas que cuentan con un campo de texto para el ingreso de datos.
 * tiene dos listas, las de opciones para la respuesta y la de los valores. Cada vez que en la aplicaci�n se 
 * cambian los valores se modifica el adapter para mantener los cambios.
 * @author eperaza
 *
 */
public class RespuestasConTextfieldAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<String> opcionesRespuesta;
	protected ArrayList<Integer> opcionesValores;
	
	public RespuestasConTextfieldAdapter(Activity pActivity, ArrayList<String> pOpcionesRespuesta, ArrayList<Integer> pOpcionesValores){
		this.activity = pActivity;
		this.opcionesRespuesta = pOpcionesRespuesta;
		this.opcionesValores = pOpcionesValores;
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
			view = inflater.inflate(R.layout.item_list_opcion_respuesta_textfield, null);
		}
		String sRespuesta = opcionesRespuesta.get(position);
		String sValor = Integer.toString(opcionesValores.get(position));
		TextView tvRespuesta = (TextView)view.findViewById(R.id.tv_texto_opcion_respuesta_textfield);
		TextView tvValor = (TextView)view.findViewById(R.id.tv_opcion_valor);
		tvRespuesta.setText(sRespuesta);
		if(!sValor.equals("0"))
			tvValor.setText(sValor);
		else if(sValor.equals("0")){
			tvValor.setText(Constantes.TEXTO_NO_RESPONDO);
		}
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
	
	public void agregarOpcionRespuesta(String pOpcion, int pValor){
		opcionesRespuesta.add(pOpcion);
		opcionesValores.add(pValor);
	}
}
