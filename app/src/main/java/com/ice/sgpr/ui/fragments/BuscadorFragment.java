package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Gps;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.error.SgprException;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.service.RestHelper;
import com.ice.sgpr.service.SgprService;
import com.ice.sgpr.ui.adapter.ListaNegociosAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Fragment para el buscador de negocios por nombre.
 * @author eperaza
 * Fecha de creaci�n: 14/08/2013
 */
public class BuscadorFragment extends SgprFragment{
	private static final int CARACTERES_MINIMOS_BUSQUEDA = 3;
	private String _sCriterioBusqueda;
	private int _nIndex;
	ListView _lvBuscadorNegocios;
	private ListaNegociosAdapter _listaNegociosAdapter;
	private Parcelable _posicionScroll; //Ubicaci�n del scroll
	View _footerView;
	private Boolean _bLimpiarLista;
	private ImageView _imgLogoBuscador;
	

	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		View view = inflater.inflate(R.layout.fragment_buscador, container, false);
		EditText etBuscar = (EditText)view.findViewById(R.id.et_buscar_negocios);
		_imgLogoBuscador = (ImageView)view.findViewById(R.id.img_logo_buscador);
		_footerView = ((LayoutInflater) getActivity().getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_lista, null, false);
		_footerView.setVisibility(View.INVISIBLE);
		_lvBuscadorNegocios = (ListView) view.findViewById(R.id.lv_buscador_negocios);
		_lvBuscadorNegocios.setOnScrollListener(onFinScroll);
		_lvBuscadorNegocios.addFooterView(_footerView);
		_lvBuscadorNegocios.setOnItemClickListener(onClickLista);
		ArrayList<Negocio> listaNegocios = new ArrayList<Negocio>();
		_listaNegociosAdapter =  new ListaNegociosAdapter(getActivity(), listaNegocios);
		
		etBuscar.addTextChangedListener(onEscribirTexto);
		
		Gps.getInstance().internetCheck(getActivity().getApplicationContext());
		return view;
	 }
	
	/**
	 * Listener del buscador. Verifica que se hayan escrito al menos 3 letras para comenzar a buscar 
	 * coincidencias.
	 */
	public TextWatcher onEscribirTexto = new TextWatcher() {
		
		@Override
		public void afterTextChanged(Editable pTexto) {
			if (pTexto.length() >= CARACTERES_MINIMOS_BUSQUEDA)
			{
				_nIndex = 1;
				_bLimpiarLista = true;
				_sCriterioBusqueda = pTexto.toString();
				_posicionScroll = null;
				_imgLogoBuscador.setVisibility(View.INVISIBLE);
				new asyncTaskBuscarNegocios().execute();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};
	
	/**
	 * Listener que se llama cuando se toca una fila de la lista.
	 */
	private AdapterView.OnItemClickListener onClickLista = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Negocio negocioSeleccionado = (Negocio)_listaNegociosAdapter.getItem(position);
			mostrarDialogo(negocioSeleccionado.get_sNombre(), negocioSeleccionado.get_nNegocioId());
		}
	};
	
	/**
	 * Hilo para la b�squeda de negocios.
	 * @author eperaza
	 *
	 */
	private class asyncTaskBuscarNegocios extends AsyncTask<Object, Object, Object>
	{
    	private List<Negocio> _resultados;

		@Override
		protected Object doInBackground(Object... pParams) 
		{
			try
			{
				_resultados = NegociosImplementor.getInstance().buscarNegocios(_sCriterioBusqueda, _nIndex);
				if(_resultados.size() == 0)
					_nIndex = -1;
			}
			catch(SgprException ex)
			{
				_resultados = null;
				Log.i("ERROR BUSCANDO NEGOCIOS", ex.toString());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object pResult) 
		{
			if (_resultados != null)
				mostrarNegocios(_resultados);
		}
	}
	
	/**
	 * Carga la lista de negocios en el listview.
	 * @param pListaNegocios
	 */
	public void mostrarNegocios(List<Negocio> pListaNegocios)
    {
		if (_bLimpiarLista)
    	{
    		_lvBuscadorNegocios.setVisibility(ListView.VISIBLE);
        	_listaNegociosAdapter.limpiarAdapter();
    	}
		for(Negocio negocio:pListaNegocios){
			_listaNegociosAdapter.agregarNegocio(negocio);
		}
    	_footerView.setVisibility(View.INVISIBLE);
    	_lvBuscadorNegocios.setAdapter(_listaNegociosAdapter);
    	if(_posicionScroll != null)
    		_lvBuscadorNegocios.onRestoreInstanceState(_posicionScroll);
    }
	
	/**
	 * Listener para el final del scroll.
	 */
	private AbsListView.OnScrollListener onFinScroll = new AbsListView.OnScrollListener(){

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
		}
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			 final int lastItem = firstVisibleItem + visibleItemCount;
	            if(_listaNegociosAdapter != null &&_listaNegociosAdapter.getCount() > 0 && lastItem == totalItemCount && _nIndex != -1){
	            	_nIndex ++;
	            	_bLimpiarLista = false;
	            	_footerView.setVisibility(View.VISIBLE);
	            	_posicionScroll = _lvBuscadorNegocios.onSaveInstanceState();
	            	new asyncTaskBuscarNegocios().execute();
	            }
		}
	};
	
	/**
	 * Di�logo de confirmaci�n al seleccionar un negocio.
	 */
	@SuppressLint("NewApi")
	public void mostrarDialogo(String pNombreNegocio, final int nIdNegocio){
		new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
	    .setTitle("¿Cargar los datos del negocio \"" + pNombreNegocio + "\"?")
	    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	hideKeyboard();
	        	cargarDatosNegocioSeleccionado(nIdNegocio);
	        }
	    })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        }
	    }).show();
	}
	
	/**
	 * Se inicia el negocio, primero revisa que el negocio actual esta finalizado para comenzar con el nuevo.
	 * Si el negocio no existe, se inserta el nuevo negocio.
	 * @param nIdNegocioSeleccionado
	 */
	public void cargarDatosNegocioSeleccionado(int nIdNegocioSeleccionado){
		Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		if(negocioActivo == null)
			new AsyncbuscarYGuardarNuevoNegocio().execute(Integer.toString(nIdNegocioSeleccionado), "-1");
		else if(negocioActivo.get_nEstado() != Constantes.ESTADO_NEGOCIO_ACTUAL){
			if(!NegociosImplementor.getInstance().existeNegocio(nIdNegocioSeleccionado))
				new AsyncbuscarYGuardarNuevoNegocio().execute(Integer.toString(nIdNegocioSeleccionado), Integer.toString(negocioActivo.get_nNegocioId()));
			else
				activarNegocio(nIdNegocioSeleccionado, negocioActivo.get_nNegocioId());
		}
		else
			Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_NEGOCIO_SELECCIONADO, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Cambia el estado del negocio actual a inactivo y coloca el estado actual al nuevo negocio seleccionado. 
	 * @param pNegocioSeleccionadoId
	 */
	public void activarNegocio(int pNegocioSeleccionadoId, int pNegocioAnteriorId){
		if(pNegocioAnteriorId != -1)
			NegociosImplementor.getInstance().actualizarEstadoNegocio(pNegocioAnteriorId, Constantes.ESTADO_NEGOCIO_VISITADO, Constantes.ESTADO_NEGOCIO_INACTIVO, "");
		NegociosImplementor.getInstance().actualizarEstadoNegocio(pNegocioSeleccionadoId, Constantes.ESTADO_NEGOCIO_ACTUAL, Constantes.ESTADO_NEGOCIO_ACTIVO, "");
		BitacoraImplementor.getInstance().insertarNuevaBitacora("", "", true);
		Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_NEGOCIO_CARGADO, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Hilo que obtiene el negocio buscado y lo guarda en la BD.
	 * @author eperaza
	 */
	private class AsyncbuscarYGuardarNuevoNegocio extends AsyncTask<String, String, Boolean>
	{
		String nIdNegocioSeleccionado;
		String nIdNegocioAnterior;
		@Override
		protected void onPreExecute()
		{
		}
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				nIdNegocioSeleccionado = params[0];
				nIdNegocioAnterior = params[1];
				String sRespuestaBusqueda = RestHelper.getInstance().GET(SgprService.getInstance().getNegocioBuscadoUrl(params[0]), true);
				if(sRespuestaBusqueda != null && !sRespuestaBusqueda.equals(""))
					JSONHelper.getInstance().insertarNuevoNegocioBuscado(sRespuestaBusqueda);
				else{
					return false;
				}
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("ERROR BUSCANDO NEGOCIO", e.toString());
			}
			return false;
		}
		
		protected void onPostExecute(Boolean pResult)
		{
			if(pResult)
				activarNegocio(Integer.parseInt(nIdNegocioSeleccionado), Integer.parseInt(nIdNegocioAnterior));
			else
				Toast.makeText(getActivity().getApplicationContext(), "Ha ocurrido un error al cargar el negocio.", Toast.LENGTH_LONG).show();
		}
	}
	
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = getActivity().getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
}
