package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Formulario;
import com.ice.sgpr.entidades.Pregunta;
import com.ice.sgpr.implementor.PreguntasImplementor;
import com.ice.sgpr.ui.activities.NegociosActivity;
import com.ice.sgpr.ui.adapter.RespuestasAdapter;
import com.ice.sgpr.ui.adapter.RespuestasConTextfieldAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment que muestra la pregunta seleccionada, junto con sus opciones.
 * Obtiene el numero de pregunta seleccionado desde el fragment del formulario, con este numero
 * obtiene la informacion de la pregunta seleccionada, desde la lista que se cargA tambien en el fragment del formulario.
 * Carga los codigos de las preguntas, ya sea que estA en la posiciOn principal o secundaria (anidada).
 * Las para mostrar las opciones se obtiene la lista de "estados", que se encarga de identificar que tipo de preguntas
 * y opciones contiene la pregunta.
 * Simbologia de los estados -> u:Pregunta unica. a:Tiene una pregunta anidada. s:Pregunta de seleccion unica. m:Pregunta de seleccion multiple.
 * Espacios separados por comas -> Primer espacio: Tipo de pregunta. Segundo espacio: Cantidad de opciones de la pregunta principal. 
 * Tercer espacio: Cantidad de opciones de la pregunta anidada.
 * @author eperaza
 * Fecha de creacion: 16/08/2013.
 */
public class PreguntaFragment extends SgprFragment{
	private ListView _lvOpcionesPregunta;
	private String[] _sEstadosSplit;
	private int _nNumeroPregunta;
	private Pregunta _pregunta;
	private TextView _tvPregunta, _tvBotonVolver;
	private Boolean _bPreguntaAnidada; //false si se cargan las opciones de la pregunta principal, true si son las de la pregunta secundaria.
	private int _nPosicionPreguntaPrincipal; //Posicion en el listview de la pregunta seleccionada.
	private String _sTextoOpcionSeleccionada;
	private ArrayList<Integer> _listaOpcionesValores;
	private List<String> _listaCodigosRespuesta;
	private ArrayList<Boolean> _listaDeshabilitadas; //Lista con las opciones que deben deshabilitarse en las preguntas que depende de otras
	
	/**
	 * Se toman los diferentes estados para la pregunta del archivo "array.xml" y se almacenan en unos arreglos.
	 * El objeto _pregunta se ira actualizando con las respuestas que el usuario ingrese.
	 */
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		View view = inflater.inflate(R.layout.fragment_pregunta, container, false);
		_pregunta = new Pregunta();
		_bPreguntaAnidada = false;
		_nNumeroPregunta = FormularioFragment.getNumeroPregunta();
		_pregunta.setNumero(_nNumeroPregunta);
		_listaDeshabilitadas = new ArrayList<Boolean>();
		
		String[] arrayCodigosPreguntaPrincipal = getResources().getStringArray(R.array.array_codigos_pregunta_principal);
		String[] arrayCodigosPreguntaSecundaria = getResources().getStringArray(R.array.array_codigos_pregunta_secundaria);
		String[] arrayCodigosPreguntaDependencia = getResources().getStringArray(R.array.array_codigos_pregunta_dependencia);
		
		_pregunta.setCodigoPreguntaPrincipal(Integer.parseInt(arrayCodigosPreguntaPrincipal[_nNumeroPregunta]));
		_pregunta.setCodigoPreguntaDependencia(Integer.parseInt(arrayCodigosPreguntaDependencia[_nNumeroPregunta]));
		if(_pregunta.getCodigoPreguntaPrincipal() == 390){
			for(int i = 0; i < 6; i ++)
				deshabilitarOpciones(i);
		}
		String sCodigoPreguntaSecundaria = arrayCodigosPreguntaSecundaria[_nNumeroPregunta];
		if(!sCodigoPreguntaSecundaria.equals(""))
			_pregunta.setCodigoPreguntaSecundaria(Integer.parseInt(sCodigoPreguntaSecundaria));
		
		_nPosicionPreguntaPrincipal = -1;
		Formulario formularioSeleccionado = FormularioFragment.getFormulario().get(_nNumeroPregunta);
		TextView tvBotonSalvar = (TextView)view.findViewById(R.id.tv_boton_salvar);
		TextView tvBotonSiguiente = (TextView)view.findViewById(R.id.tv_boton_siguiente);
		TextView tvBotonAnterior = (TextView)view.findViewById(R.id.tv_boton_anterior);
		_tvBotonVolver = (TextView)view.findViewById(R.id.tv_boton_volver);
		_tvPregunta = (TextView)view.findViewById(R.id.tv_pregunta_principal);
		String sEstadosPregunta = formularioSeleccionado.getEstados();
		
		_sEstadosSplit = sEstadosPregunta.split(",");
		_lvOpcionesPregunta = (ListView)view.findViewById(R.id.lv_opciones_pregunta);
		_lvOpcionesPregunta.setClickable(true);
		_lvOpcionesPregunta.setOnItemClickListener(onClickRespuesta);
		obtenerOpcionesRespuesta();
		remarcarOpcionesGuardadas();
		
		_tvPregunta.setText(formularioSeleccionado.getPreguntaPrincipal());
		tvBotonSalvar.setOnClickListener(onSalvar);
		tvBotonSiguiente.setOnClickListener(onPreguntaSiguiente);
		tvBotonAnterior.setOnClickListener(onPreguntaAnterior);

		if(!comprobarSegundaPregunta())
			comprobarPrimerYUltimaPregunta(tvBotonAnterior, tvBotonSiguiente);
		return view;
	 }
	
	/**
	 * Listener que se llama cuando se toca una fila de la lista.
	 * La lista tiene doble funcion (seleccion unica y multiple) asi que se verifica el tipo de pregunta que
	 * se esta contestando.
	 * Tambien se toma en cuenta si la pregunta acutal esta anidada dentro de otra, en cuyo caso, aparte de mantener los
	 * datos del objeto pregunta, le añade las respuestas en el campo CodigoRespuesta2.
	 * Verifica de que tipo de pregunta se trata.
	 * Consulta el arreglo "_listaDeshabilitadas", que contiene true o false en el index seleccionado, indicando si 
	 * la opcion puede marcarse o no. Este arreglo tiene contenido solo en ciertas opciones, de preguntas que deben ser
	 * anidadas.
	 */
	private AdapterView.OnItemClickListener onClickRespuesta = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(_sEstadosSplit[Constantes.ESTADO_PREGUNTA_CON_VALOR].equals("t")){
				int nPrimerIndexVisible = _lvOpcionesPregunta.getFirstVisiblePosition();
				int nIndexVisibleSeleccionado = position - nPrimerIndexVisible;
				if(_listaDeshabilitadas.size() == 0 || !_listaDeshabilitadas.get(position))
					mostrarDialogo(position, nIndexVisibleSeleccionado);
			}
			else{
				int nEstadoSeleccionado = Constantes.ESTADO_PREGUNTA_PRINCIPAL;
				//Obtiene la respuesta seleccionada
				if(_sEstadosSplit[nEstadoSeleccionado].equals("s")){
					if(!_bPreguntaAnidada){
						_pregunta.setRespuesta1(Integer.toString(position));
						_pregunta.setCodigoRespuesta1(_listaCodigosRespuesta.get(position));
					}
					else{
						_pregunta.setRespuesta2(Integer.toString(position));
						_pregunta.setCodigoRespuesta2(_listaCodigosRespuesta.get(position));
					}
				}
				//Obtiene la lista de elementos seleccionados
				else if(_sEstadosSplit[nEstadoSeleccionado].equals("m") && (_listaDeshabilitadas.size() == 0 || !_listaDeshabilitadas.get(position))){
					if(_nPosicionPreguntaPrincipal == -1)
						_nPosicionPreguntaPrincipal = position;
					obtenerListaRespuestasSeleccionadas(position);
				}
				else if(_pregunta.getCodigoPreguntaPrincipal() == 390)
					return;
				if(!_bPreguntaAnidada){
					TextView tvOpcionMarcada = (TextView)view.findViewById(R.id.tv_texto_opcion_respuesta);
					_sTextoOpcionSeleccionada = tvOpcionMarcada.getText().toString();
					if(_sEstadosSplit[Constantes.ESTADO_TIPO_PREGUNTA].equals("a")){
						pasarAPreguntaAnidada(position);
					}
				}
				else{
					if(_sEstadosSplit[Constantes.ESTADO_PREGUNTA_ANIDADA].equals("t")){
						int nPrimerIndexVisible = _lvOpcionesPregunta.getFirstVisiblePosition();
						int nIndexVisibleSeleccionado = position - nPrimerIndexVisible;
						mostrarDialogo(position, nIndexVisibleSeleccionado);
					}
					nEstadoSeleccionado = Constantes.ESTADO_PREGUNTA_ANIDADA;
				}
			}
		}
	};
	
	/**
	 * Obtiene las posibles respuestas para la pregunta.
	 * Carga las opciones de la pregunta principal o secundaria.
	 * Cambia el adapter, dependiendo si se trata de una pregunta normal u otra que tenga un textfield.
	 * Agrega los valores de los campos de texto en caso de tener.
	 */
	public void obtenerOpcionesRespuesta(){
		String sOpcionesString;
		ArrayList<String>opcionesRespuesta = new ArrayList<String>();
		RespuestasAdapter respuestaAdapter = null;
		RespuestasConTextfieldAdapter respuestaConTextfieldAdapter = null;
		ArrayList<int[]> arrayOpcionesValores = new ArrayList<int[]>();
		String[] sRespuestas = getResources().getStringArray(R.array.array_respuestas);
		String[] sRespuestasAnidadas = getResources().getStringArray(R.array.array_respuestas_anidadas);
		_listaCodigosRespuesta = new ArrayList<String>();
		
		if(!_sEstadosSplit[Constantes.ESTADO_PREGUNTA_CON_VALOR].equals("t")){
			respuestaAdapter = new RespuestasAdapter(getActivity(), opcionesRespuesta);
		}
		else{
			_listaOpcionesValores = new ArrayList<Integer>();
			respuestaConTextfieldAdapter = new RespuestasConTextfieldAdapter(getActivity(),opcionesRespuesta, _listaOpcionesValores);
			arrayOpcionesValores = PreguntasImplementor.getInstance().obtenerRespuestasGuardadasConValor(_nNumeroPregunta, false);
		}
		int nEstadoSeleccionado = Constantes.ESTADO_PREGUNTA_PRINCIPAL;
		if(!_bPreguntaAnidada){
			sOpcionesString = sRespuestas[_nNumeroPregunta];
		}
		else{
			//Obtiene las opciones de respuesta anidadad de la pregunta.
			if(_sEstadosSplit[Constantes.ESTADO_PREGUNTA_ANIDADA].equals("t")){
				_listaOpcionesValores = new ArrayList<Integer>();
				respuestaConTextfieldAdapter = new RespuestasConTextfieldAdapter(getActivity(),opcionesRespuesta, _listaOpcionesValores);
				arrayOpcionesValores = PreguntasImplementor.getInstance().obtenerRespuestasGuardadasConValor(_nNumeroPregunta, true, 
						_nPosicionPreguntaPrincipal);
				respuestaAdapter = null;
			}
			sOpcionesString = sRespuestasAnidadas[_nNumeroPregunta];
			nEstadoSeleccionado = Constantes.ESTADO_PREGUNTA_ANIDADA;
		}
		//Diferencia entre selecci�n �nica y m�ltiple
		if(_sEstadosSplit[nEstadoSeleccionado].equals("s")){
			_lvOpcionesPregunta.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		else if(_sEstadosSplit[nEstadoSeleccionado].equals("m")){
			_lvOpcionesPregunta.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		remarcarOpciones(sOpcionesString, respuestaAdapter, arrayOpcionesValores, respuestaConTextfieldAdapter);
	}
	
	/**
	 * Coloca los valores guardados en la BD en su respectivo index en la lista.
	 * Si se trata de una pregunta anidada cambia la imagen del checkbox por un simbolo de "adelante", indicando que hay una segunda pregunta.
	 * @param sOpcionesString
	 * @param respuestaAdapter
	 * @param arrayOpcionesValores
	 * @param respuestaConTextfieldAdapter
	 */
	public void remarcarOpciones(String sOpcionesString, RespuestasAdapter respuestaAdapter, ArrayList<int[]> arrayOpcionesValores,
			RespuestasConTextfieldAdapter respuestaConTextfieldAdapter){
		String[] sOpcionesSplit = sOpcionesString.split("#");
		int nCantidadOpciones = sOpcionesSplit.length;

		//Opciones con ingreso de valores:
		int contadorRemarcarOpciones = 0;
		int contadorOpciones = 0;
		for(int cont = 0; cont < nCantidadOpciones; cont ++){
			if(respuestaAdapter != null){
				if(cont % 2 == 0){
					Boolean bCambiarImgCheckbox = false;
					if(!_bPreguntaAnidada && _sEstadosSplit[Constantes.ESTADO_TIPO_PREGUNTA].equals("a"))
						bCambiarImgCheckbox = true;
					respuestaAdapter.agregarOpcionRespuesta(sOpcionesSplit[cont], bCambiarImgCheckbox);
				}
				else
					_listaCodigosRespuesta.add(sOpcionesSplit[cont]);
			}
			else{
				int nCantidadValores = 0;
				if(arrayOpcionesValores != null)
					nCantidadValores = arrayOpcionesValores.size();
				int sValorFila = 0;
				if(cont % 2 == 0){
					for(int contValores = 0; contValores < nCantidadValores; contValores ++){
						if(arrayOpcionesValores.get(contValores)[1] == contadorRemarcarOpciones){
							sValorFila = arrayOpcionesValores.get(contValores)[0];
							contadorRemarcarOpciones++;
							break;
						}
					}
					deshabilitarOpciones(contadorOpciones);
					respuestaConTextfieldAdapter.agregarOpcionRespuesta(sOpcionesSplit[cont], sValorFila);
					 contadorOpciones ++;
				}
				else
					_listaCodigosRespuesta.add(sOpcionesSplit[cont]);
			}
		}
		if(respuestaAdapter != null)
			_lvOpcionesPregunta.setAdapter(respuestaAdapter);
		else{
			_lvOpcionesPregunta.setAdapter(respuestaConTextfieldAdapter);
		}
	}
	/**
	 * Existen varias preguntas que son excepciones (son anidadas en la logica, pero no en la programacion).
	 * Si la pregunta tiene un codigo dentro de los rangos que se verifican, se obtiene la lista de opciones habilitadas
	 * de la pregunta de la cual depende y se actualiza un arreglo de boolean, que indica que opciones estan disponibles y
	 * cuales no. Luego, cuando el usuario intenta seleccionar las opciones, de la pregunta anidada, se verifica el arreglo,
	 * para averifuar si la opcion en el index seleccionado esta habilitada o no.
	 */
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
	
	/**
	 * Boton de "Salvar".
	 * Guarda el estado de la pregunta (opcion marcada).
	 */
	public View.OnClickListener onSalvar = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Boolean bPreguntaGuardada = guardarPregunta();
			if(bPreguntaGuardada)
				mostrarMensaje(Constantes.AVISO_FORMULARIO_GUARDADO);
		}
	};
	
	/**
	 * Boton de "Siguiente".
	 * Pasa a la siguiente pregunta.
	 */
	public View.OnClickListener onPreguntaSiguiente = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int nSiguientePregunta = _nNumeroPregunta + 1;
			if(nSiguientePregunta < Constantes.CANTIDAD_DE_PREGUNTAS)
				cambiarPregunta(nSiguientePregunta, false);
			else
				guardarPregunta();
		}
	};
	
	/**
	 * Boton de "Anterior".
	 * Pasa a la pregunta anterior.
	 */
	public View.OnClickListener onPreguntaAnterior = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int nPreguntaAnterior = _nNumeroPregunta - 1;
			if(nPreguntaAnterior >= 0)
				cambiarPregunta(nPreguntaAnterior, false);
		}
	};
	
	/**
	 * Boton de "Finalizar" (Siguiente).
	 * Retorna a la lsita de preguntas principal.
	 */
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
	
	/**
	 * Boton de "Volver a pregunta principal".
	 * Retorna a la pregunta principal desde una pregunta anidada. Carga de nuevo el fragment con la misma pregunta.
	 */
	public View.OnClickListener onVolverPreguntaPrincipal = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			cambiarPregunta(_nNumeroPregunta, true);
		}
	};
	
	/**
	 * Cambia el fragment con los datos de la pregunta siguiente o anterior.
	 * @param nPregunta: numero de pregunta a pasar.
	 */
	public void cambiarPregunta(int nPregunta, Boolean pExcepcionAnidada){
		Boolean bPreguntaGuardada = guardarPregunta();
		if(bPreguntaGuardada || pExcepcionAnidada){
			FormularioFragment.setNumeroPregunta(nPregunta);
			reemplazarFragment(false);
		}
	}
	
	/**
	 * Reemplaza el fragment por otro con otra pregunta.
	 * Como se cambia el fragment dentro del tab es necesario limpiar el stack el Fragment Manager para conseguir que cuando
	 * se pulse el boton de atras del dispositivo regrese al menu principal y no al fragment anterior.
	 */
	public void reemplazarFragment(Boolean pUltimaPregunta){
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStack();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(!pUltimaPregunta)
			fragmentTransaction.replace(R.id.contenido_principal, new PreguntaFragment(), NegociosActivity.TAG_NEGOCIOS);
		else
			fragmentTransaction.replace(R.id.contenido_principal, new FormularioFragment(), NegociosActivity.TAG_NEGOCIOS);
		fragmentTransaction.commit();
	}
	
	/**
	 * Obtiene la lista de opciones marcadas en el listview.
	 */
	public void obtenerListaRespuestasSeleccionadas(int pOpcion){
		SparseBooleanArray checked = _lvOpcionesPregunta.getCheckedItemPositions();
		
		String sItemsSeleccionadosMultiple = "";
		String sCodigosSeleccionadosMultiple = "";
        for (int i = 0; i < checked.size(); i++) {
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
        if(!_bPreguntaAnidada){
        	_pregunta.setRespuesta1(sItemsSeleccionadosMultiple);
        	_pregunta.setCodigoRespuesta1(sCodigosSeleccionadosMultiple);
        }
        else{
        	_pregunta.setRespuesta2(sItemsSeleccionadosMultiple);
        	_pregunta.setCodigoRespuesta2(sCodigosSeleccionadosMultiple);
        }
	}
	
	/**
	 * Cambia la pregunta y la lista de opciones de la interfaz por las opciones de la 
	 * pregunta anidada.
	 */
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
	
	/**
	 * Al entrar a la lista marca las opciones previamente guardadas (si las hay).
	 * La lista de respuestas esta en un string, separadas por el signo numeral (#).
	 * En el caso de que la pregunta tenga campos de texto para ingresar valores, se recibe una lista con arrays
	 * que contiene la opcion y el valor respectivo.
	 */
	public void remarcarOpcionesGuardadas(){
		String sRespuestas = PreguntasImplementor.getInstance().obtenerRespuestasGuardadas(_nNumeroPregunta, _bPreguntaAnidada, _nPosicionPreguntaPrincipal);
		if(sRespuestas != null && !sRespuestas.equals("")){
			_pregunta.setPreguntaContestada(true);
			String[] sRespuestasSplit = sRespuestas.split("#");
			for(String sRespuestaActual : sRespuestasSplit){
				_lvOpcionesPregunta.setItemChecked(Integer.parseInt(sRespuestaActual), true);
			}
		}
	}
	
	/**
	 * Guarda la informacion de la pregunta en la base de datos del telefono.
	 * @return true si la pregunta se pudo guardar, false si no se ha seleccionado una opcion.
	 */
	public Boolean guardarPregunta(){
		if(_sEstadosSplit[Constantes.ESTADO_PREGUNTA_CON_VALOR].equals("t")){
			PreguntasImplementor.getInstance().insertarPreguntaConValor(_listaOpcionesValores, _nNumeroPregunta, false, -1, 
					_pregunta.getCodigoPreguntaPrincipal(), _pregunta.getCodigoPreguntaSecundaria(), _pregunta.getCodigoPreguntaDependencia(),
					_pregunta.getCodigoRespuesta1(), _pregunta.getCodigoRespuesta2(), _pregunta.getCodigoRespuestaValor(), _listaCodigosRespuesta);
			return true;
		}
		else if(_bPreguntaAnidada && _sEstadosSplit[Constantes.ESTADO_PREGUNTA_ANIDADA].equals("t")){
			PreguntasImplementor.getInstance().insertarPregunta(_pregunta, true, true);
			PreguntasImplementor.getInstance().insertarPreguntaConValor(_listaOpcionesValores, _nNumeroPregunta, true, _nPosicionPreguntaPrincipal,
					_pregunta.getCodigoPreguntaPrincipal(), _pregunta.getCodigoPreguntaSecundaria(), _pregunta.getCodigoPreguntaDependencia(), 
					_pregunta.getCodigoRespuesta1(), _pregunta.getCodigoRespuesta2(), _pregunta.getCodigoRespuestaValor(), _listaCodigosRespuesta);
			return true;
		}
		else if(!_bPreguntaAnidada && _sEstadosSplit[Constantes.ESTADO_TIPO_PREGUNTA].equals("u") && (_pregunta.getRespuesta1() != null)){
			PreguntasImplementor.getInstance().insertarPregunta(_pregunta, false, false);
			return true;
		}
		else if(_bPreguntaAnidada && _sEstadosSplit[Constantes.ESTADO_PREGUNTA_ANIDADA].equals("s") && (_pregunta.getRespuesta2() != null)){
			PreguntasImplementor.getInstance().insertarPregunta(_pregunta, true, false);
			return true;
		}
		else if(_bPreguntaAnidada && _sEstadosSplit[Constantes.ESTADO_TIPO_PREGUNTA].equals("a") && (_pregunta.getRespuesta2() != null)){
			PreguntasImplementor.getInstance().insertarPregunta(_pregunta, true, false);
			return true;
		}
		else if(_pregunta.getPreguntaContestada() != null)
			return true;
		else{
			mostrarMensaje(Constantes.AVISO_ERROR_GUARDANDO_PREGUNTA);
			return false;
		}
	}
	
	/**
	 * Verifica si la pregunta es la segunda del formulario, para mostrar la opcion de finalizarlo en ese momento.
	 * @return
	 */
	private boolean comprobarSegundaPregunta(){
		if(_nNumeroPregunta != 1)
			return false;
		else{
			_tvBotonVolver.setVisibility(View.VISIBLE);
			_tvBotonVolver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fin_formulario, 0, 0, 0);
			_tvBotonVolver.setText(Constantes.FIN_FORMULARIO_SEGUNDA_PREG);
			_tvBotonVolver.setOnClickListener(onUltimaPregunta);
			return true;
		}
	}
	/**
	 * Si es la primer pregunta se oculta se oculta el boton de anterior, si es la ultima se actualiza el boton de siguiente.
	 * @param pAnterior
	 * @param pSiguiente
	 */
	public void comprobarPrimerYUltimaPregunta(TextView pAnterior, TextView pSiguiente){
		if(_nNumeroPregunta == 0)
			pAnterior.setVisibility(View.GONE);
		else if(_nNumeroPregunta == Constantes.CANTIDAD_DE_PREGUNTAS - 1){
			pSiguiente.setText(Constantes.FIN_DE_PREGUNTAS_FORMULARIO);
			//pSiguiente.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_fin_formulario, 0);
			pSiguiente.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.ic_fin_formulario), null);
			pSiguiente.setOnClickListener(onUltimaPregunta);
		}
	}
	
	/**
	 * Dialogo para ingresar valores monetarios a la lista.
	 * @param pIndexLista: Index seleccionado en la lista.
	 * @param pIndexVisible: Index seleccionado, pero dentro del area visible del listview
	 */
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
	
	/**
	 * Muestra un mensaje para el usuario.
	 * @param pMensaje
	 */
	public void mostrarMensaje(String pMensaje){
		Toast.makeText(getActivity(), pMensaje, Toast.LENGTH_SHORT).show();
	}
}
