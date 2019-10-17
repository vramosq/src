package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Bitacora;
import com.ice.sgpr.entidades.Ruta;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.RutaImplementor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Fragment que muestra la informacion de la ruta, junto con la lista de bitacoras registradas.
 * @author eperaza
 */
public class InformacionRutaFragment extends SgprFragment{
	private ListView lvBitacora;
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {		
		View view = inflater.inflate(R.layout.fragment_informacion_ruta, container, false);
		Ruta rutaSeleccionada = RutaImplementor.getInstance().obtenerInformacionRuta();
		if(rutaSeleccionada != null){
			TextView tvNombreRuta = (TextView)view.findViewById(R.id.tv_nombre_ruta);
			TextView tvdescripcionRuta = (TextView)view.findViewById(R.id.tv_descripcion_ruta);
			lvBitacora = (ListView)view.findViewById(R.id.lv_bitacora);
			tvNombreRuta.setText(rutaSeleccionada.getNombre());
			tvdescripcionRuta.setText(rutaSeleccionada.getDescripcion());
			new ObtenerListaBitacora().execute();
		}
		else
			Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_SELECCIONAR_RUTA, Toast.LENGTH_SHORT).show();
		return view;
	 }
	
	/**
	 * Hilo para obtener la lista de Bitacoras almacenadas en SQLite.
	 */
	private class ObtenerListaBitacora extends AsyncTask<Void, Integer, Void>{
		ArrayList<String> listaInfoBitacora = new ArrayList<String>();
		ArrayAdapter<String> adapter;
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			List<Bitacora> lBitacora = BitacoraImplementor.getInstance().obtenerBitacora();
			if (lBitacora != null){
				for(Bitacora nuevaBitacora:lBitacora){
					if(nuevaBitacora.getFechaFin() != null)
						listaInfoBitacora.add(nuevaBitacora.getFechaFin() + "\n" + nuevaBitacora.getNombreNegocio() + " - " + Constantes.AVISO_NEGOCIO_FINALIZADO);
					listaInfoBitacora.add(nuevaBitacora.getFechaInicio() + "\n" + nuevaBitacora.getNombreNegocio() + " - " + Constantes.AVISO_NEGOCIO_INICIADO);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result){
			adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_list_texto_simple, listaInfoBitacora);
			lvBitacora.setAdapter(adapter);
		}
	}
}
