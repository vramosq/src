package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Pregunta;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.PreguntasAuditorImplementor;
import com.ice.sgpr.ui.adapter.ListaExpandibleAdapter;

/**
 * Fragment que muestra las pregutnas de auditoria.
 * @since 25.03.15
 */
public class PreguntaAuditFragment extends SgprFragment{	
	private ExpandableListView expListView;
	private ListaExpandibleAdapter expListAdapter;
	private List<String[]> listaHeader;
	private HashMap<String, List<String[]>> listaSubMenus;
	private CharSequence[] listaEstados;
	private ArrayList<String> listaCodigosEstados;
    private int cantidadElementos;
	
	/**
	 * Se toman los diferentes estados para la pregunta del archivo "array.xml" y se almacenan en unos arreglos.
	 * El objeto _pregunta se ira actualizando con las respuestas que el usuario ingrese.
	 */
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		View view = inflater.inflate(R.layout.fragment_formulario_auditor, container, false);
		TextView tvNombreNegocio = (TextView)view.findViewById(R.id.tv_nombre_negocio_audit);
		
		Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		if(negocioActivo != null){
			tvNombreNegocio.setText(negocioActivo.get_sNombre());
			expListView = (ExpandableListView) view.findViewById(R.id.elv_formulario_audit);
			loadQuestionOptions();
			expListAdapter = new ListaExpandibleAdapter(getActivity(), listaHeader, listaSubMenus);
			expListView.setAdapter(expListAdapter);
			expListView.setOnGroupClickListener(onMenuTap);
			expListView.setOnChildClickListener(onSubMenuTap);
			remarcarOpcionesGuardadas();
		}
		else{
			tvNombreNegocio.setText(Constantes.AVISO_NEGOCIO_NO_SELECCIONADO);
			TextView tvPregunta = (TextView)view.findViewById(R.id.tv_titulo_form_auditor);
			tvPregunta.setVisibility(View.GONE);
		}
		return view;
	 }
	
	/**
	 * Se carga la infomraci�n de las preguntas
	 */
	private void loadQuestionOptions(){
		listaHeader = new ArrayList<String[]>(); // Lista de headers que se mostrar�n
        listaSubMenus = new HashMap<String, List<String[]>>(); // Contenido (filas) de los header.
        List<String> listaEstadosTemp = new ArrayList<String>();
        listaCodigosEstados = new ArrayList<String>();
        
        
        String[] arrayOperadoresAuditor = getResources().getStringArray(R.array.array_opciones_auditor);
        String[] arrayElementosAuditor = getResources().getStringArray(R.array.array_elemetos_auditor);
        String[] arrayEstadosAuditor = getResources().getStringArray(R.array.array_estados_elementos);
        
        int cantidadOperadores = arrayOperadoresAuditor.length;
        cantidadElementos = arrayElementosAuditor.length;
        int cantidadEstados = arrayEstadosAuditor.length;
    	
        //Se cargan las marcas.
        for(int i = 0; i < cantidadOperadores; i++){
        	List<String[]> subMenus = new ArrayList<String[]>();
        	//Se cargan las opciones de los submenus, para agregarlas a cada marca.
        	//Se omite el agregado de opciones al ultimo menu, que es "ningun operador".
        	//if(i < cantidadOperadores - 1){
        	if(i == 0){ //Se cargan las opciones solo para kolbi
	        	for(int j = 0; j < cantidadElementos; j++){
	        		String[] opcionSplit = arrayElementosAuditor[j].split("#");
	        		String[] opcion = new String[]{opcionSplit[0], opcionSplit[1], "0"};
	        		subMenus.add(opcion);
	        	}
        	}
        	String[] marca = arrayOperadoresAuditor[i].split("#");
        	listaHeader.add(marca);
        	listaSubMenus.put(listaHeader.get(i)[1], subMenus);
        }
        
        //Se cargan los estados
    	for(int j = 0; j < cantidadEstados; j++){
    		String[] estado = arrayEstadosAuditor[j].split("#");
    		listaEstadosTemp.add(estado[0]);
    		listaCodigosEstados.add(estado[1]);
    	}
    	 listaEstados = listaEstadosTemp.toArray(new CharSequence[listaEstadosTemp.size()]);
	}

	
	/**
	 * Accion al tocar uno de los nodos hijos.
	 * Se valida la utima opcion, que no debe mostrar el popUp. En este caso guarda/descarta directamente y sin contenido
	 * en Respuesta 3.
	 */
	private OnChildClickListener onSubMenuTap = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			String[] opcionSeleccionada = (String[]) expListAdapter.getChild(groupPosition, childPosition);
			
			if(childPosition != cantidadElementos -1)
				mostrarDialogo(opcionSeleccionada[0], groupPosition, childPosition, opcionSeleccionada[1]);
			else{
				String[] revisarMarcada = PreguntasAuditorImplementor.getInstance().obtenerRespuestasEstadosGuardados(listaHeader.get(groupPosition)[1], opcionSeleccionada[1]);
				if(revisarMarcada == null){
					onAceptar(null, listaHeader.get(groupPosition)[1], opcionSeleccionada[1]);
					expListAdapter.actualizarChecks(groupPosition, childPosition, "1");
				}
				else{
					onDescartar(listaHeader.get(groupPosition)[1], opcionSeleccionada[1]);
					expListAdapter.actualizarChecks(groupPosition, childPosition, "0");
				}
				expListAdapter.notifyDataSetChanged();
			}
			return false;
		}
	};
	
	/**
	 * Accion al tocar uno de los nodos padres
	 * Verifica el primer item (kolbi) y el ultimo item "Ningun operador" pues requieren
	 * validaciones especiales (debe haberse contestado kolbi antes que ningun otro y si
	 * Ningun operador esta seleccionado no se permite tocar otra opciin.
	 * Retornando "true" se evita la expacion del menu.
	 */
	private OnGroupClickListener onMenuTap = new OnGroupClickListener() {
		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			if(!expListView.isGroupExpanded(listaHeader.size() - 1) || groupPosition == listaHeader.size() - 1){
				if(groupPosition == listaHeader.size() - 1){
					if(expListView.isGroupExpanded(groupPosition)){
						PreguntasAuditorImplementor.getInstance().borrarOperadores();
					}
					else{
						//En caso de que se seleccione "ningun operador":
						PreguntasAuditorImplementor.getInstance().borrarOperadores();
						Pregunta pregunta = new Pregunta();
						//pregunta.setCodigoPreguntaPrincipal(v.getContext().getString(R.string.cod_pregunta_auditor));
						pregunta.setCodigoRespuesta1(listaHeader.get(groupPosition)[1]);
						PreguntasAuditorImplementor.getInstance().insertarPregunta(pregunta);
						expListView.expandGroup(groupPosition, true);
						for(int cont = 0; cont < listaHeader.size() - 1; cont ++)
							if(expListView.isGroupExpanded(cont))
								expListView.collapseGroup(cont);
						reiniciarSubMenus();
						expListAdapter.notifyDataSetChanged();
						return true;
					}
				}
				else{					
					boolean puedeExpandir = false;
					if(groupPosition == 0)
						puedeExpandir = true;
					else{
						int revisarMarcada = PreguntasAuditorImplementor.getInstance().obtenerCantidadRespuestas(listaHeader.get(0)[1]);
						puedeExpandir = (revisarMarcada > 0);
						if(!puedeExpandir){
							 Toast.makeText(v.getContext(), "Debe resolver la encuesta para kölbi primero...", Toast.LENGTH_SHORT).show();
						}
					}
					if(expListView.isGroupExpanded(groupPosition) && puedeExpandir){
						if(groupPosition != 0){
							onDescartar(listaHeader.get(groupPosition)[1], Constantes.RESPUESTA_NO_APLICA_OPERADOR);
						}
						else{
							boolean puedeColapsar = false;
							int revisarMarcada = PreguntasAuditorImplementor.getInstance().obtenerCantidadRespuestas(listaHeader.get(groupPosition)[1]);
							puedeColapsar = (revisarMarcada <= 0);
							if(!puedeColapsar)
								Toast.makeText(v.getContext(), "\"" + listaHeader.get(groupPosition)[0] + "\" tiene opciones marcadas, no se puede cerrar...", Toast.LENGTH_SHORT).show();
							return !puedeColapsar;
						}
					 }
					else if(!expListView.isGroupExpanded(groupPosition)){
						if(groupPosition != 0){
							guardarPregunta(listaHeader.get(groupPosition)[1], Constantes.RESPUESTA_NO_APLICA_OPERADOR, Constantes.ESTADO_NO_APLICA_OPERADOR);
							expListView.expandGroup(groupPosition, true);
							if(expListView.isGroupExpanded(groupPosition))
								expListView.collapseGroup(groupPosition);
						}
					}
					return !puedeExpandir;
				}
			}
			else{
				Toast.makeText(v.getContext(), "\"" + listaHeader.get(listaHeader.size() - 1)[0] + "\" est� marcado, no se puede seleccionar ninguna otra opci�n...", Toast.LENGTH_SHORT).show();
				return true;
			}
			return false;
		}
	};
	
	
	/**
	 * Muestra el dialogo con los estados a marcar.
	 * Al aceptar, se manda a guardar los datos y se marca el check del elemento
	 * Al cancelar borra los datos de la opcion y desmarca el elemento.
	 * @param tituloOpcion
	 */
	private void mostrarDialogo(String tituloOpcion, final int groupPosition, final int childPosition, final String codigoElemento){
		boolean[] estadosMarcados = remarcarEstadosGuardados(groupPosition, codigoElemento);
		final ArrayList<Integer> selectedItemsIndexList = new ArrayList<Integer>();
		
		for(int cont = 0; cont < estadosMarcados.length; cont ++){
			if(estadosMarcados[cont])
				selectedItemsIndexList.add(cont);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final String codigoOperador = listaHeader.get(groupPosition)[1];
		builder.setTitle(tituloOpcion)
		.setMultiChoiceItems(listaEstados, estadosMarcados, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if(isChecked)
					selectedItemsIndexList.add(which);
				else if(selectedItemsIndexList.contains(which))
					selectedItemsIndexList.remove(Integer.valueOf(which));
			}
		})
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int wich) {
				onAceptar(selectedItemsIndexList, codigoOperador, codigoElemento);
				if(selectedItemsIndexList.size() > 0){
					expListAdapter.actualizarChecks(groupPosition, childPosition, "1");
					expListAdapter.notifyDataSetChanged();
				}
			}
		})
		.setNegativeButton("Descartar todo", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onDescartar(codigoOperador, codigoElemento);
				expListAdapter.actualizarChecks(groupPosition, childPosition, "0");
				expListAdapter.notifyDataSetChanged();
			}
		})
		.show();
	}
	
	/**
	 * Evento que se dispara al pulsar "Aceptar" en la lista de 
	 * estados. Guarda la combinaci�n de la pregunta en la base de datos.
	 * @param pArrayOpciones
	 */
	private void onAceptar(ArrayList<Integer> pArrayOpciones, String pCodigoOperador, String pCodigoElemento){
		String opcionesSeleccionadas = "";
		if(pArrayOpciones != null){
			int cantidadOpciones = pArrayOpciones.size();
			if(cantidadOpciones != 0){
				for(int i = 0; i < cantidadOpciones; i++){
					opcionesSeleccionadas = opcionesSeleccionadas + listaCodigosEstados.get(pArrayOpciones.get(i)) + "#";
				}
				opcionesSeleccionadas = opcionesSeleccionadas.substring(0, opcionesSeleccionadas.length()-1);
				
				guardarPregunta(pCodigoOperador, pCodigoElemento, opcionesSeleccionadas);
			}
		}
		else{
			guardarPregunta(pCodigoOperador, pCodigoElemento, opcionesSeleccionadas);
		}
	}
	
	/**
	 * Se manda a guardar la pregunta en la BD del dispositivo.
	 * @param pCodigoOperador
	 * @param pCodigoElemento
	 * @param pOpcionesSeleccionadas
	 */
	private void guardarPregunta(String pCodigoOperador, String pCodigoElemento, String pOpcionesSeleccionadas){
		Pregunta pregunta = new Pregunta();
		pregunta.setCodigoPreguntaAuditor(getString(R.string.cod_pregunta_auditor));
		pregunta.setCodigoRespuesta1(pCodigoOperador);
		pregunta.setCodigoRespuesta2(pCodigoElemento);
		pregunta.setCodigoRespuesta3(pOpcionesSeleccionadas);
		PreguntasAuditorImplementor.getInstance().insertarPregunta(pregunta);
	}
	
	/**
	 * Evento para descartar las opciones marcadas en el popUp.
	 */
	private void onDescartar(String codigoOperador, String codigoElemento){
		PreguntasAuditorImplementor.getInstance().borrarEstados(getString(R.string.cod_pregunta_auditor), codigoOperador, codigoElemento);
	}

	/**
	 * Al entrar a la lista marca las opciones previamente guardadas (si las hay).
	 * La lista de respuestas est� en un string, separadas por el signo numeral (#).
	 * Lleva un contador de posici�n para expandir los men�s que tengan algo.
	 * Tiene una validaci�n especial para la �ltima pregunta, para verificar si esta opci�n fue marcada.
	 */
	public void remarcarOpcionesGuardadas(){
		int position = 0;
		for (String[] marca : listaHeader) {
			if(position != 0){
				int respuesta = PreguntasAuditorImplementor.getInstance().obtenerCantidadRespuestas(marca[1]);
				if(respuesta > 0)
					expListView.expandGroup(position);
			}
			else{
				List<String> sRespuestas = PreguntasAuditorImplementor.getInstance().obtenerRespuestasElementosGuardados(marca[1]);
				if(sRespuestas != null){
					for (String respuesta : sRespuestas) {
						List<String[]> elementos = listaSubMenus.get(marca[1]);
						int cantidadElementos = elementos.size();
						for(int i = 0; i < cantidadElementos; i++){
							if(elementos.get(i)[1].equals(respuesta)){
								listaSubMenus.get(marca[1]).get(i)[2] = "1";
								expListView.expandGroup(position);
								break;
							}
						}				
					}
				}
			}
			position ++;
		}
	}
	
	/**
	 * Revisa las respuestas que han sido marcadas para los estados y los retorna en un
	 * arreglo de booleanos, con el valor "true" en el index que debe aparecer como marcado.
	 * @param pPosicionPadre
	 * @param pCodigoElemento
	 * @return
	 */
	public boolean[] remarcarEstadosGuardados(int pPosicionPadre, String pCodigoElemento){
		boolean[] valoresMarcados = new boolean[listaEstados.length];
		String[] sRespuestas = PreguntasAuditorImplementor.getInstance().obtenerRespuestasEstadosGuardados(listaHeader.get(pPosicionPadre)[1], pCodigoElemento);
		if (sRespuestas != null){
			for(String estadoConsultado : sRespuestas){
				int index = 0;
				for(String estadoLista : listaCodigosEstados){
					if(estadoLista.equals(estadoConsultado)){
						valoresMarcados[index] = true;
						break;
					}
					index ++;
				}
			}
		}
		return valoresMarcados;
	}
	

	/**
	 * Reinicia las marcas de los submen�s, para que no aparezcan chequeados.
	 */
	public void reiniciarSubMenus(){
		for (int cont = 0; cont < listaHeader.size() - 2; cont ++){
			List<String[]> elementos = listaSubMenus.get(listaHeader.get(cont)[1]);
			int cantidadElementos = elementos.size();
			for(int i = 0; i < cantidadElementos; i++){
				listaSubMenus.get(listaHeader.get(cont)[1]).get(i)[2] = "0";
			}
		}
	}
	/**
	 * Muestra un mensaje para el usuario.
	 * @param pMensaje
	 */
	public void mostrarMensaje(String pMensaje){
		Toast.makeText(getActivity(), pMensaje, Toast.LENGTH_SHORT).show();
	}
}
