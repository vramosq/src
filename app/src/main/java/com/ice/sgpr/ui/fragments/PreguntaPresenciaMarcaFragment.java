package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Formulario;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Pregunta;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.PreguntasPresenciaMarcaImplementor;
import com.ice.sgpr.ui.activities.NegociosActivity;
import com.ice.sgpr.ui.adapter.RespuestasAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Efra�n Peraza
 * Fecha de creaci�n: 25/06/2014.
 */
public class PreguntaPresenciaMarcaFragment extends SgprFragment{
	private ListView _lvOpcionesPregunta;
	private int _nNumeroPregunta;
	private Pregunta _pregunta;
	private TextView _tvPregunta, _tvBotonSiguiente, _tvBotonAnterior;
	private int _nPosRespuestaSeleccionada; //Posici�n en el listview de la pregunta seleccionada.
	private List<String> _listaCodigosRespuesta;
	
	/**
	 * Se toman las preguntas y respuestas del archivo "array.xml" y se almacenan en unos arreglos.
	 * El objeto _pregunta se ir� actualizando con las respuestas que el usuario ingrese.
	 */
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		View view = inflater.inflate(R.layout.fragment_presencia_marca, container, false);
		_tvPregunta = (TextView)view.findViewById(R.id.tv_pregunta_pm);
		_tvBotonSiguiente = (TextView)view.findViewById(R.id.tv_boton_siguiente_pm);
		_tvBotonAnterior = (TextView)view.findViewById(R.id.tv_boton_anterior_pm);
		TextView tvNombreNegocio = (TextView)view.findViewById(R.id.tv_nombre_negocio);
		
		Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		if (negocioActivo != null) {
			tvNombreNegocio.setText(negocioActivo.get_sNombre());
			_tvBotonSiguiente.setOnClickListener(onPreguntaSiguiente);
			_tvBotonAnterior.setOnClickListener(onPreguntaAnterior);

			_lvOpcionesPregunta = (ListView) view
					.findViewById(R.id.lv_opciones_pm);
			_lvOpcionesPregunta.setClickable(true);
			_lvOpcionesPregunta.setOnItemClickListener(onClickRespuesta);

			prepararPregunta();
			_nPosRespuestaSeleccionada = -1;
			_nNumeroPregunta = 0;
		}
		else{
			tvNombreNegocio.setText(Constantes.AVISO_NEGOCIO_NO_SELECCIONADO);
			_tvBotonAnterior.setVisibility(View.GONE);
			_tvBotonSiguiente.setVisibility(View.GONE);
		}
		return view;
	 }
	
	/**
	 * Carga las propiedades de una pregunta. Se crea una pregunta nueva.
	 */
	private void prepararPregunta(){
		_pregunta = new Pregunta();
		_pregunta.setNumero(_nNumeroPregunta);	
		String[] arrayCodigosPreguntaPM = getResources().getStringArray(R.array.array_codigos_preg_presencia_marca);
		_pregunta.setCodigoPreguntaPrincipal(Integer.parseInt(arrayCodigosPreguntaPM[_nNumeroPregunta]));
		
		Formulario formularioSeleccionado = obtenerFormularioPresenciaMarca(_nNumeroPregunta);
		_tvPregunta.setText(formularioSeleccionado.getPreguntaPrincipal());
		obtenerOpcionesRespuestaPM();
		remarcarOpcionesGuardadas();
		comprobarPrimerYUltimaPregunta();
	}
	
	/**
	 * Funci�n para obtener la lista de preguntas del formulario de presencia de marca.
	 * Recorre la lista de resources de "strings", y obtiene el texto de cada pregunta. 
	 * A su vez obtiene de la BD la lista de preguntas que han sido contestadas, as� que se marcan con un check.
	 * Muestra las barras de progreso al obtener la lista.
	 */	
	public Formulario obtenerFormularioPresenciaMarca(int pNumeroPregunta){
		int nNumeroPregunta = 0;
		int[] arrPreguntasContestadas = PreguntasPresenciaMarcaImplementor.getInstance().obtenerNumerosPreguntasContestadas();
		
		String[] sPreguntas = getResources().getStringArray(R.array.array_preguntas_presencia_marca);
		String sEstado = getResources().getString(R.string.tipo_pregunta_unica);
		
		String sTextoPregunta = sPreguntas[pNumeroPregunta];
		Boolean bMarcada = false;
		if (arrPreguntasContestadas != null && Arrays.binarySearch(arrPreguntasContestadas, nNumeroPregunta) >= 0)
			bMarcada = true;
		return new Formulario(sTextoPregunta, bMarcada, null, nNumeroPregunta, sEstado);		
	}
	
	/**
	 * Listener que se activa al seleccionar un elemento de la lista.
	 * Se toman las respuestas seleccionadas y se guardan instant�neamente en la BD.
	 */
	private AdapterView.OnItemClickListener onClickRespuesta = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Obtiene la lista de elementos seleccionados
			if (_nPosRespuestaSeleccionada == -1)
				_nPosRespuestaSeleccionada = position;
			obtenerListaRespuestasSeleccionadas(position);
			guardarPregunta();
		}
	};
	
	/**
	 * Obtiene las posibles respuestas para la pregunta, carg�ndolas en un adapter
	 */
	public void obtenerOpcionesRespuestaPM(){
		ArrayList<String>opcionesRespuesta = new ArrayList<String>();
		RespuestasAdapter respuestaAdapter = new RespuestasAdapter(getActivity(), opcionesRespuesta);
		
		String sOpcionesRespuestas = getResources().getString(R.string.respuestas_presencia_marca);
		_listaCodigosRespuesta = new ArrayList<String>();
		_lvOpcionesPregunta.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				
		remarcarOpciones(sOpcionesRespuestas, respuestaAdapter);
	}
	
	/**
	 * Coloca las respuestas previamente marcadas y guardados en la BD en su respectivo index en la lista.
	 * @param sOpcionesString
	 * @param respuestaAdapter
	 */
	public void remarcarOpciones(String sOpcionesString, RespuestasAdapter respuestaAdapter){
		String[] sOpcionesSplit = sOpcionesString.split("#");
		int nCantidadOpciones = sOpcionesSplit.length;

		for(int cont = 0; cont < nCantidadOpciones; cont ++){
			if(respuestaAdapter != null){
				if(cont % 2 == 0){
					respuestaAdapter.agregarOpcionRespuesta(sOpcionesSplit[cont], false);
				}
				else
					_listaCodigosRespuesta.add(sOpcionesSplit[cont]);
			}
		}

		_lvOpcionesPregunta.setAdapter(respuestaAdapter);
	}
	
	/**
	 * Existen varias preguntas que son excepciones (son anidadas en la l�gica, pero no en la programaci�n).
	 * Si la pregunta tiene un c�digo dentro de los rangos que se verifican, se obtiene la lista de opciones habilitadas
	 * de la pregunta de la cual depende y se actualiza un arreglo de boolean, que indica qu� opciones est�n disponibles y
	 * cuales no. Luego, cuando el usuario intenta seleccionar las opciones, de la pregunta anidada, se verifica el arreglo,
	 * para averifuar si la opci�n en el index seleccionado estp� habilitada o no.
	 *//*
	public void deshabilitarOpciones(int pNumeroOpcion){
		int[] listaOpcionesMarcadas = null;
		if((_pregunta.getCodigoPreguntaPrincipal() >= 385 && _pregunta.getCodigoPreguntaPrincipal() <= 388)
				|| (_pregunta.getCodigoPreguntaPrincipal() == 391)
				|| (_pregunta.getCodigoPreguntaPrincipal() == 390 && !_bPreguntaAnidada)
				|| (_pregunta.getCodigoPreguntaPrincipal() >= 394 && _pregunta.getCodigoPreguntaPrincipal() <= 395)
				|| (_pregunta.getCodigoPreguntaPrincipal() >= 397 && _pregunta.getCodigoPreguntaPrincipal() <= 398)
				|| (_pregunta.getCodigoPreguntaPrincipal() == 400)){
			listaOpcionesMarcadas = PreguntasImplementor.getInstance().listaOpcionesPorPregunta(_pregunta.getCodigoPreguntaDependencia());
		}
		else if(_pregunta.getCodigoPreguntaPrincipal() == 402)
			listaOpcionesMarcadas = PreguntasImplementor.getInstance().listaOpcionesPenultimaPregunta();
		else
			return;
		
		if(listaOpcionesMarcadas == null){
			_listaDeshabilitadas.add(true);
			return;
		}
		if(Arrays.binarySearch(listaOpcionesMarcadas, pNumeroOpcion) < 0)
			_listaDeshabilitadas.add(true);
		else
			_listaDeshabilitadas.add(false);
		
	}
	
	*//**
	 * Bot�n de "Salvar".
	 * Guarda el estado de la pregunta (opci�n marcada).
	 *//*
	public View.OnClickListener onSalvar = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Boolean bPreguntaGuardada = guardarPregunta();
			if(bPreguntaGuardada)
				mostrarMensaje(Constantes.AVISO_FORMULARIO_GUARDADO);
		}
	};
	
	*//**
	 * Bot�n de "Siguiente".
	 * Pasa a la siguiente pregunta, "reseteando" el objeto pregunta actual y cargando otro.
	 */
	public View.OnClickListener onPreguntaSiguiente = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_nNumeroPregunta ++;
			if(_nNumeroPregunta < Constantes.CANTIDAD_DE_PREGUNTAS)
				prepararPregunta();
		}
	};
	
	/**
	 * Bot�n de "Anterior".
	 * Pasa a la pregunta anterior.
	 */
	public View.OnClickListener onPreguntaAnterior = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_nNumeroPregunta --;
			if(_nNumeroPregunta >= 0)
				prepararPregunta();
		}
	};
	
	/**
	 * Bot�n de "Finalizar" (Siguiente).
	 * Retorna a la lsita de preguntas principal.
	 *//*
	public View.OnClickListener onUltimaPregunta = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Boolean bPreguntaGuardada = guardarPregunta();
			if(bPreguntaGuardada){
				mostrarMensaje(Constantes.AVISO_FORMULARIO_FINALIZADO);
				reemplazarFragment(true);
			}
		}
	};
	
	*//**
	 * Bot�n de "Volver a pregunta principal".
	 * Retorna a la pregunta principal desde una pregunta anidada. Carga de nuevo el fragment con la misma pregunta.
	 *//*
	public View.OnClickListener onVolverPreguntaPrincipal = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			cambiarPregunta(_nNumeroPregunta, true);
		}
	};
	
	/**
	 * Reemplaza el fragment por otro con otra pregunta.
	 * Como se cambia el fragment dentro del tab es necesario limpiar el stack el Fragment Manager para conseguir que cuando
	 * se pulse el bot�n de atr�s del dispositivo regrese al men� principal y no al fragment anterior.
	 */
	public void reemplazarFragment(Boolean pUltimaPregunta){
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStack();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(!pUltimaPregunta)
			fragmentTransaction.replace(R.id.contenido_principal, new PreguntaPresenciaMarcaFragment(), NegociosActivity.TAG_NEGOCIOS);

		fragmentTransaction.commit();
	}
	
	/**
	 * Obtiene la lista de opciones marcadas en el listview.
	 */
	public void obtenerListaRespuestasSeleccionadas(int pOpcion){
		SparseBooleanArray checked = _lvOpcionesPregunta.getCheckedItemPositions();
		
		int cantOpcionesMarcadas = checked.size();
		
		String sItemsSeleccionadosMultiple = "";
		String sCodigosSeleccionadosMultiple = "";
        for (int i = 0; i < cantOpcionesMarcadas; i++) {
            int nPosicionSeleccionada = checked.keyAt(i);
            if (checked.valueAt(i)){
            	if(!sItemsSeleccionadosMultiple.equals("")){
            		sItemsSeleccionadosMultiple += "#";
            		sCodigosSeleccionadosMultiple += "#";
            	}
                sItemsSeleccionadosMultiple += nPosicionSeleccionada;
                sCodigosSeleccionadosMultiple += _listaCodigosRespuesta.get(nPosicionSeleccionada);
            }
		}
		_pregunta.setRespuesta1(sItemsSeleccionadosMultiple);
		_pregunta.setCodigoRespuesta1(sCodigosSeleccionadosMultiple);
	}
	
	/**
	 * Cambia la pregunta y la lista de opciones de la interfaz por las opciones de la 
	 * pregunta anidada.
	 *//*
	public void pasarAPreguntaAnidada(int pOpcionMarcada){
		_pregunta.setPosicion(pOpcionMarcada);
		_pregunta.setRespuesta1(Integer.toString(pOpcionMarcada));
		_pregunta.setCodigoRespuesta1(_listaCodigosRespuesta.get(pOpcionMarcada));
		//Se obtiene la pregunta anidada.
		String[] sArrayPreguntasAnidadas = getResources().getStringArray(R.array.array_preguntas_anidadas);
		String sPreguntaAnidada = sArrayPreguntasAnidadas[_pregunta.getNumero()];
		_tvPregunta.setText(sPreguntaAnidada + "\n - " + _sTextoOpcionSeleccionada);
		_bPreguntaAnidada = true;
		_tvBotonVolver.setVisibility(View.VISIBLE);
		_tvBotonVolver.setOnClickListener(onVolverPreguntaPrincipal);
		_listaDeshabilitadas = new ArrayList<Boolean>();
		obtenerOpcionesRespuesta();
		remarcarOpcionesGuardadas();
	}
	
	*//**
	 * Al entrar a la lista marca las opciones previamente guardadas (si las hay).
	 * La lista de respuestas est� en un string, separadas por el signo numeral (#).
	 */
	public void remarcarOpcionesGuardadas(){
		String sRespuestas = PreguntasPresenciaMarcaImplementor.getInstance().obtenerRespuestasGuardadas(_nNumeroPregunta, _nPosRespuestaSeleccionada);
		if(sRespuestas != null && !sRespuestas.equals("")){
			_pregunta.setPreguntaContestada(true);
			String[] sRespuestasSplit = sRespuestas.split("#");
			for(String sRespuestaActual : sRespuestasSplit){
				_lvOpcionesPregunta.setItemChecked(Integer.parseInt(sRespuestaActual), true);
			}
		}
	}
	
	/**
	 * Guarda la informaci�n de la pregunta en la base de datos del tel�fono.
	 * @return true si la pregunta se pudo guardar, false si no se ha seleccionado una opci�n.
	 */
	public void guardarPregunta(){
		if(_pregunta.getRespuesta1() != null && !_pregunta.getRespuesta1().equals("")){
			PreguntasPresenciaMarcaImplementor.getInstance().insertarActualizarPreguntaPresenciaMarca(_pregunta);
		}
		else
			PreguntasPresenciaMarcaImplementor.getInstance().borrarPreguntasPresenciaMarca(_pregunta.getCodigoPreguntaPrincipal());
	}
	
	/**
	 * Si es la primer pregunta se oculta se oculta el bot�n de anterior, si es la �ltima se oculta el bot�n de siguiente.
	 */
	public void comprobarPrimerYUltimaPregunta(){
		_tvBotonAnterior.setVisibility(View.VISIBLE);
		_tvBotonSiguiente.setVisibility(View.VISIBLE);
		if(_nNumeroPregunta == 0)
			_tvBotonAnterior.setVisibility(View.GONE);
		else if(_nNumeroPregunta == Constantes.CANTIDAD_DE_PREGUNTAS_PM - 1){
			_tvBotonSiguiente.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Dialogo para ingresar valores monetarios a la lista.
	 * @param pIndexLista: Index seleccionado en la lista.
	 * @param pIndexVisible: Index seleccionado, pero dentro del �rea visible del listview
	 *//*
	@SuppressLint("NewApi")
	public void mostrarDialogo(final int pIndexLista, final int pIndexVisible){
		final View viewIngresarValor = View.inflate(getActivity(), R.layout.item_ingreso_valores, null);
		new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
	    .setView(viewIngresarValor)
	    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
				EditText etValor = (EditText)viewIngresarValor.findViewById(R.id.et_ingreso_valor);
				CheckBox chkNoRespondo = (CheckBox)viewIngresarValor.findViewById(R.id.chk_no_respondo);
				if(!chkNoRespondo.isChecked()){
					String sValorIngresado = etValor.getText().toString();
					int nValorIngresado = 0;
					if(!sValorIngresado.equals(""))
						nValorIngresado = Integer.parseInt(sValorIngresado);
					if(nValorIngresado <= 0){
						mostrarMensaje(Constantes.ERROR_VALOR_INVALIDO);
					}
					else
						_listaOpcionesValores.set(pIndexLista, nValorIngresado);
				}
				else
					_listaOpcionesValores.set(pIndexLista, Constantes.ESTADO_PREGUNTA_SIN_VALOR_INGRESADO);
				((BaseAdapter) _lvOpcionesPregunta.getAdapter()).notifyDataSetChanged(); //Actualiza el adapter.
	        }
	    })
	    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        }
	    }).show();
	}
	
	*//**
	 * Muestra un mensaje para el usuario.
	 * @param pMensaje
	 *//*
	public void mostrarMensaje(String pMensaje){
		Toast.makeText(getActivity(), pMensaje, Toast.LENGTH_SHORT).show();
	}*/
}
