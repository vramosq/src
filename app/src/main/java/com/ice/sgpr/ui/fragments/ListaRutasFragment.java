package com.ice.sgpr.ui.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Ruta;
import com.ice.sgpr.implementor.RutaImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.ui.adapter.ListaRutasAdapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment que muestra la lista de rutas, las cuales carga mediante un hilo en la lista.
 * @author eperaza
 *
 */
public class ListaRutasFragment extends SgprFragment{
	private ListView _lvRutas;
	private ListaRutasAdapter _listaRutasAdapter;
	private TextView _tvTitulo;
	private ProgressBar _barraCargando;
	public int _nItemSeleccionado;
    private Animation animAlpha = null;
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	 {
		View view = inflater.inflate(R.layout.fragment_ruteo, container, false);
         animAlpha = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_alpha);
		_tvTitulo = (TextView)view.findViewById(R.id.tv_titulo_rutas_asignadas);
		_lvRutas = (ListView) view.findViewById(R.id.lv_rutas);
		TextView tvIniciarRuta = (TextView) view.findViewById(R.id.tv_iniciar_ruta);
		tvIniciarRuta.setOnClickListener(onClickIniciarRuta);
		TextView tvFinalizarRuta = (TextView) view.findViewById(R.id.tv_finalizar_ruta);
		tvFinalizarRuta.setOnClickListener(onClickFinalizarRuta);
		
		_barraCargando = (ProgressBar)view.findViewById(R.id.progress_bar_rutas);
		_lvRutas.setClickable(true);
		_lvRutas.setOnItemClickListener(onClickLista);
		_lvRutas.setOnScrollListener(onFinScroll);
		
		return view;
	 }
	
	
	/**
	 * Listener que se llama cuando se toca una fila de la lista.
	 * Marca como seleccionado al radioButton del view seleccionado, pero tambi�n
	 * guarda en la variable "indexSeleccionado" el view anterior para desmarcarlo. 
	 */
	private AdapterView.OnItemClickListener onClickLista = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			_nItemSeleccionado = position;
		}
	};
	
	/**
	 * Listener que detecta cuando se llega al final del scroll.
	 * Llama la funci�n que carga m�s noticias desde Parse
	 */
	private AbsListView.OnScrollListener onFinScroll = new AbsListView.OnScrollListener(){

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			//Fin del scroll.
		}
	};
	
	/**
	 * Hilo para obtener la lista de rutas desde el servidor.
	 * Muestra las barras de progreso al obtener la lista.
	 */	
	private class ObtenerListaRutas extends AsyncTask<Void, Integer, Void>{
		@Override
		protected void onPreExecute() {
			_barraCargando.setVisibility(ProgressBar.VISIBLE);
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			List<Ruta> lRutas = RutaImplementor.getInstance().obtenerListaRutas();
			
			//Se verifica que exista al menos un registro
			if (lRutas != null){
				for(Ruta nuevaRuta:lRutas)
					_listaRutasAdapter.agregarPregunta(nuevaRuta);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result){
    		_barraCargando.setVisibility(ProgressBar.GONE);
			_lvRutas.setAdapter(_listaRutasAdapter);
		}
	}
	
	/**
	 * Actualiza el titulo de la lista de rutas, indicando que se debe sincronizar en caso
	 * de que la �ltima fecha de actualizaci�n ya haya pasado.
	 */
	public void actualizarTituloListaRutas(){
		_tvTitulo.setText(R.string.error_tvRutasAsignadas);
		_tvTitulo.setTextColor(getResources().getColor(R.color.kolbi_verde_claro));
	}
	
	/**
	 * Verifica si la fecha actual es menor que la fecha de la �ltima sincronizaci�n
	 * en cuyo caso debe mostrar una advertencia al usuario.
	 */
	@SuppressLint("SimpleDateFormat")
	public void verificarFechaDeSincronizacion(){
		String sFechaUltimaActualizacion = UsuariosImplementor.getInstance().obtenerUltimaSincronizacion();
		if(sFechaUltimaActualizacion != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				Date dateFechaSinc = sdf.parse(sFechaUltimaActualizacion);
			    String sFechaActual  = DateFormat.format("dd-MM-yyyy", new Date().getTime()).toString();
			    Date FechaAct = sdf.parse(sFechaActual);
				
				if(FechaAct.compareTo(dateFechaSinc) > 0){
	        		actualizarTituloListaRutas();
	        	}
			} catch (ParseException e) {
				Log.i("FECHA", "Error convirtiendo la fecha");
			}
		}
		else
			actualizarTituloListaRutas();
	}
	
	/**
	 * Listener del listView que se ejecuta al eleccionar un elemento de la lista y 
	 * se presiona el bot�n "Iniciar Ruta".
	 */
	public View.OnClickListener onClickIniciarRuta = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            v.startAnimation(animAlpha);
			if(_listaRutasAdapter.getCount() > 0){
				Ruta rutaSeleccionada = (Ruta) _listaRutasAdapter.getItem(_nItemSeleccionado);
				RutaImplementor.getInstance().activarDesactivarRuta(rutaSeleccionada.getId(), 1);
				Toast.makeText(getActivity(), Constantes.AVISO_RUTA_INICIADA, Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getActivity(), Constantes.ADVERTENCIA_DEBE_SINCRONIZAR, Toast.LENGTH_SHORT).show();
		}
	};
	
	/**
	 * Listener del listView que se ejecuta al eleccionar un elemento de la lista y 
	 * se presiona el bot�n "Iniciar Ruta".
	 */
	public View.OnClickListener onClickFinalizarRuta = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            v.startAnimation(animAlpha);
			if(_listaRutasAdapter.getCount() > 0){
				Ruta rutaSeleccionada = (Ruta) _listaRutasAdapter.getItem(_nItemSeleccionado);
				RutaImplementor.getInstance().activarDesactivarRuta(rutaSeleccionada.getId(), 0);
				Toast.makeText(getActivity(), Constantes.AVISO_RUTA_FINALIZADA, Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getActivity(), Constantes.ADVERTENCIA_DEBE_SINCRONIZAR, Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	public void onResume(){
		super.onResume();
		verificarFechaDeSincronizacion();
		ArrayList<Ruta> listaRutas = new ArrayList<Ruta>();
		_listaRutasAdapter = new ListaRutasAdapter(getActivity(), listaRutas);			
		new ObtenerListaRutas().execute();
		_nItemSeleccionado = 0;
	}
}
