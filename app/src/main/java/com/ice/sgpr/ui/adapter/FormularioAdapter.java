package com.ice.sgpr.ui.adapter;

import java.util.ArrayList;

import com.ice.sgpr.R;
import com.ice.sgpr.entidades.Formulario;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FormularioAdapter extends BaseAdapter{

	protected Activity activity;
	protected ArrayList<Formulario> formularioItems;
	
	public FormularioAdapter(Activity pActivity, ArrayList<Formulario> pItems){
		this.activity = pActivity;
		this.formularioItems = pItems;
	}
	
	@Override
	public int getCount() {
		return formularioItems.size();
	}

	@Override
	public Object getItem(int pPosition) {
		return formularioItems.get(pPosition);
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
			view = inflater.inflate(R.layout.item_list_formulario, null);
		}
		
		Formulario item = formularioItems.get(position);
		
		TextView tvPregunta = (TextView)view.findViewById(R.id.tv_titulo_pregunta_formulario);
		ImageView imgIndicador = (ImageView)view.findViewById(R.id.img_indicador_item_formulario);
		String sPreviaTexto = item.getPreguntaPrincipal();
		if (sPreviaTexto.length() > 30)
			sPreviaTexto = sPreviaTexto.substring(0, 30) + "...";
		tvPregunta.setText(sPreviaTexto);
		if(item.getMarcado())
			imgIndicador.setImageResource(R.drawable.ic_check);
		else
			imgIndicador.setImageResource(R.drawable.ic_alerta);
		
		return view;
	}

	public void agregarPregunta(Formulario pNuevaPregunta) {
		formularioItems.add(pNuevaPregunta);
	}
}
