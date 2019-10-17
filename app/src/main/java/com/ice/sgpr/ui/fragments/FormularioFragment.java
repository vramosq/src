package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Formulario;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.PreguntasImplementor;
import com.ice.sgpr.ui.activities.NegociosActivity;
import com.ice.sgpr.ui.adapter.FormularioAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Fragment para el formulario de la app.
 * Obtiene las preguntas desde el recurso "strings" y arma una lista usada para cargar
 * la lista y, posteriormente, para obtener informacion.
 * @author eperaza
 * Fecha de creacion: 13/08/2013.
 */
public class FormularioFragment extends SgprFragment{
	private ListView _lvFormulario;
	private FormularioAdapter _formularioAdapter;
	public static ArrayList<Formulario> LISTA_PREGUNTAS_FORMULARIO;
	public static int NUMERO_PREGUNTA_SELECCIONADA;
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	 {
		View view = inflater.inflate(R.layout.fragment_formulario, container, false);
		Negocio negocioSeleccionado = NegociosImplementor.getInstance().obtenerNegocioActivo();
		if(negocioSeleccionado != null){
			LISTA_PREGUNTAS_FORMULARIO = new ArrayList<Formulario>();
			_lvFormulario = (ListView) view.findViewById(R.id.lv_formulario);
			_lvFormulario.setClickable(true);
			_lvFormulario.setOnItemClickListener(onClickItem);
			_formularioAdapter = new FormularioAdapter(getActivity(), LISTA_PREGUNTAS_FORMULARIO);
			obtenerPreguntas();
		}
		else
			Toast.makeText(getActivity(), Constantes.AVISO_NEGOCIO_NO_SELECCIONADO, Toast.LENGTH_LONG).show();
		return view;
	 }
	
	/**
	 * Funci�n para obtener la lista de preguntas del formulario desde el servidor.
	 * Recorre la lista de resources de "strings", y obtiene el texto de la pregunta junto a los indicadores
	 * de la misma. A su vez obtiene de la BD la lista de preguntas que han sido contestadas, as� que se marcan con un check.
	 * Muestra las barras de progreso al obtener la lista.
	 */	
	public void obtenerPreguntas(){
		int nNumeroPregunta = 0;
		int[] arrPreguntasContestadas = PreguntasImplementor.getInstance().obtenerNumerosPreguntasContestadas();
		
		String[] sPreguntas = getResources().getStringArray(R.array.array_preguntas);
		String[] sEstados = getResources().getStringArray(R.array.array_estados_preguntas);
		int nCantidadPreguntas = sPreguntas.length;
		
		for(int nContador = 0; nContador < nCantidadPreguntas; nContador++){
			String sTextoPregunta = sPreguntas[nContador];
			String sEstadoPregunta = sEstados[nContador];
			Boolean bMarcada = false;
			if (arrPreguntasContestadas != null && Arrays.binarySearch(arrPreguntasContestadas, nNumeroPregunta) >= 0)
				bMarcada = true;
			Formulario nuevaPregunta = new Formulario(sTextoPregunta, bMarcada, null, nNumeroPregunta, sEstadoPregunta);
			_formularioAdapter.agregarPregunta(nuevaPregunta);
			nNumeroPregunta ++;
		}
		_lvFormulario.setAdapter(_formularioAdapter);
	}
	
	/**
	 * Listener que se llama cuando se toca una fila de la lista.
	 * Como se cambia el fragment dentro del tab es necesario limpiar el stack el Fragment Manager para conseguir que cuando
	 * se pulse el bot�n de atr�s del dispositivo regrese al men� principal y no al fragment anterior.
	 */
	private AdapterView.OnItemClickListener onClickItem = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Formulario formulario = LISTA_PREGUNTAS_FORMULARIO.get(position);
			NUMERO_PREGUNTA_SELECCIONADA = formulario.getNumeroPregunta();
			Boolean bMarcada = formulario.getMarcado();
			
			if(position == 0 || bMarcada){
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
				final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.contenido_principal, new PreguntaFragment(), NegociosActivity.TAG_NEGOCIOS);
				fragmentTransaction.commit();
			}
			else
				Toast.makeText(getActivity().getApplicationContext(), "Esta pregunta no ha sido contestada", Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	/* GET */
	public static int getNumeroPregunta(){
		return NUMERO_PREGUNTA_SELECCIONADA;
	}
	
	public static ArrayList<Formulario> getFormulario(){
		return LISTA_PREGUNTAS_FORMULARIO;
	}
	
	/* SET */
	public static void setNumeroPregunta(int nNumero){
		NUMERO_PREGUNTA_SELECCIONADA = nNumero;		
	}
}
