package com.ice.sgpr.ui.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ice.sgpr.R;

/**
 * Adapter para la lista de presencia de marca de un auditor
 * @author eperaza
 * @since 27.03.15
 */
public class ListaExpandibleAdapter extends BaseExpandableListAdapter{

	private Activity context;
	private Map<String, List<String[]>> opcionesSubMenu;//Arreglo: Codigo opcion [0]. Nombre opcion [1]. Check opcion [2]
	private List<String[]> marcas;
	

	public ListaExpandibleAdapter(Activity context, List<String[]> marcas,
			Map<String, List<String[]>> options) {
		this.context = context;
		this.opcionesSubMenu = options;
		this.marcas = marcas;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return opcionesSubMenu.get(marcas.get(groupPosition)[1]).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	/**
	 * Acciones en los sub menus
	 */
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String[] opcion = (String[]) getChild(groupPosition, childPosition);
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_child_list_form_audit, null);
		}
		
		TextView textoOpcion = (TextView) convertView.findViewById(R.id.tv_child_opcion_name);
		textoOpcion.setText(opcion[0]);
		
		ImageView imgCheck = (ImageView) convertView.findViewById(R.id.img_check_child_audit);
		if(opcion[2].equals("1"))
			imgCheck.setImageResource(R.drawable.ic_check_niv2);
		else
			imgCheck.setImageResource(R.drawable.ic_uncheck);
		
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return opcionesSubMenu.get(marcas.get(groupPosition)[1]).size();
	}

	public Object getGroup(int groupPosition) {
		return marcas.get(groupPosition);
	}

	public int getGroupCount() {
		return marcas.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Acciones del los menus padres
	 * Revisa si se ha guardado alguna respuesta para la opcion de kolbi (posicion 0).
	 * Si no hay opciones para kolbi, no puede expandir las demas.
	 * Tambien revisa si el menu se puede colapsar, esto es si no tiene submenus marcados.
	 */
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, final ViewGroup parent) {
		String[] nombreMarca = (String[]) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.item_group_list_form_audit, null);
		}
		TextView textoMarca = (TextView) convertView.findViewById(R.id.tv_titulo_opcion);
		textoMarca.setTypeface(null, Typeface.BOLD);
		textoMarca.setText(nombreMarca[0]);
		
		//Imagen del "check"
		final ImageView imgCheck = (ImageView) convertView.findViewById(R.id.img_check_option_audit);
		if(isExpanded)
			imgCheck.setImageResource(R.drawable.ic_check);
		else
    	   imgCheck.setImageResource(R.drawable.ic_uncheck);
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	/**
	 * Actualiza la marca del check en la lista de opciones. Le coloca un "1" undicando que est� marcada
	 * @param groupPosition
	 * @param childPosition
	 */
	public void actualizarChecks(int groupPosition, int childPosition, String value){
		opcionesSubMenu.get(marcas.get(groupPosition)[1]).get(childPosition)[2] = value;
	}
}
