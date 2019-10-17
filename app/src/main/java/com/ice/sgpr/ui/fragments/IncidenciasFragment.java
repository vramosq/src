package com.ice.sgpr.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.implementor.ParametrosImplementor;
import com.ice.sgpr.implementor.SupervTercerosImplementor;
import com.ice.sgpr.implementor.UsersTercerosImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.ui.activities.SupervisionTercerosActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase para las incidencias
 */
public class IncidenciasFragment extends SgprFragment {

    Spinner spnOperator, spnType, spnRegisteredBy, spnAssignedTo;
    TextView etIncidence, etUpgrade;
    private long negocioId;
    String[] arrayOperadores;

    public IncidenciasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incidencias, container, false);

        spnOperator = (Spinner) view.findViewById(R.id.spn_incidencias_op);
        List<Parametro> paramsList = ParametrosImplementor.getInstance().obtenerListaParametros(2);

        arrayOperadores = getResources().getStringArray(R.array.array_opciones_terceros);
        int cantidadOperadores = arrayOperadores.length;
        for(int j = 0; j < cantidadOperadores; j++){
            String[] opcionSplit = arrayOperadores[j].split("#");
            Parametro opcion = new Parametro(0,opcionSplit[0], opcionSplit[1]);
            paramsList.add(opcion);
        }

        cargarSpinner(paramsList, spnOperator, false);

        spnType = (Spinner) view.findViewById(R.id.spn_incidencias_tip);
        paramsList = ParametrosImplementor.getInstance().obtenerListaParametros(3);
        cargarSpinner(paramsList, spnType, false);

        spnRegisteredBy = (Spinner) view.findViewById(R.id.spn_incidencias_rp);
        paramsList = UsersTercerosImplementor.getInstance().obtenerUsuariosTerceros();
        //paramsList.add(0, new Parametro(-1, "- Seleccione -", ""));
        cargarSpinner(paramsList, spnRegisteredBy, true);

        spnAssignedTo = (Spinner) view.findViewById(R.id.spn_incidencias_aa);
        cargarSpinner(paramsList, spnAssignedTo, false);

        etIncidence = (TextView) view.findViewById(R.id.et_incidencias_in);
        etUpgrade = (TextView) view.findViewById(R.id.et_incidencias_me);

        Button btnSave = (Button) view.findViewById(R.id.btn_incidencias_save);
        btnSave.setOnClickListener(onSave);
        loadIncidences();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            negocioId = getArguments().getLong(SupervisionTercerosActivity.NEGOCIO_ID);
        }
    }

    /**
     * Arma un spinner, cargando la informacion.
     */
    private void cargarSpinner(List<Parametro> pParametros, Spinner pSpinner, boolean isSetUser) {
        ArrayAdapter<Parametro> spinnerAdapter = new ArrayAdapter<Parametro>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, pParametros);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pSpinner.setAdapter(spinnerAdapter);

        if(isSetUser){
            int userId = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
            for(Parametro user : pParametros){
                if(user.getCodigo() == userId) {
                    spnRegisteredBy.setSelection(spinnerAdapter.getPosition(user));
                    break;
                }
            }
        }
    }

    /**
     * Listener del botón de guardar
     * Crea el objeto json para  incidencias y lo manda a guardar en la BD
     */
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Parametro operator = (Parametro) spnOperator.getSelectedItem();
            Parametro kind = (Parametro) spnType.getSelectedItem();
            Parametro registeredBy = (Parametro) spnRegisteredBy.getSelectedItem();
            Parametro assignedTo = (Parametro) spnAssignedTo.getSelectedItem();

            String incidence = etIncidence.getText().toString();
            String upgrade = etUpgrade.getText().toString();

            //Creación del objeto JSON:
            JSONArray arrayOpciones = new JSONArray();
            JSONObject objectOption;
            try {
                //Operador:
                objectOption = new JSONObject();
                objectOption.put("op", operator.getCategoria());
                arrayOpciones.put(objectOption);

                //Tipo
                objectOption = new JSONObject();
                objectOption.put("tip", kind.getCodigo());
                arrayOpciones.put(objectOption);

                //Registrado por
                objectOption = new JSONObject();
                objectOption.put("rp", registeredBy.getCodigo());
                arrayOpciones.put(objectOption);

                //Asignado a
                objectOption = new JSONObject();
                objectOption.put("aa", assignedTo.getCodigo());
                arrayOpciones.put(objectOption);

                //Incidencia
                //if (!incidence.equals("")) {
                    objectOption = new JSONObject();
                    objectOption.put("in", incidence);
                    arrayOpciones.put(objectOption);
                //}

                //Mejora
                //if (!upgrade.equals("")) {
                    objectOption = new JSONObject();
                    objectOption.put("me", upgrade);
                    arrayOpciones.put(objectOption);
                //}

                //Fecha
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                objectOption = new JSONObject();
                objectOption.put("fe", df.format(new Date()));
                arrayOpciones.put(objectOption);

                JSONObject jsonIncidences = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, Constantes.INCIDENCIAS);
                int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
                SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonIncidences.toString(), currentUser, Constantes.INCIDENCIAS);
                Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Se carga la información de las incidencias con los datos almacenados en el json.
     */
    private void loadIncidences() {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, Constantes.INCIDENCIAS);
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, Constantes.INCIDENCIAS);

                for (String[] option : jsonsList) {
                    if (option[0].equals("op")) {
                        spnOperator.setSelection(getSpinnerPosition(option[1], spnOperator, true), false);
                    }
                    else if (option[0].equals("tip")) {
                        spnType.setSelection(getSpinnerPosition(option[1], spnType, false), false);
                    }
                    else if (option[0].equals("rp")) {
                        spnRegisteredBy.setSelection(getSpinnerPosition(option[1], spnRegisteredBy, false), false);
                    }
                    else if (option[0].equals("aa")) {
                        spnAssignedTo.setSelection(getSpinnerPosition(option[1], spnAssignedTo, false), false);
                    }
                    else if (option[0].equals("in")) {
                        etIncidence.setText(option[1]);
                    }
                    else if (option[0].equals("me")) {
                        etUpgrade.setText(option[1]);
                    }
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Retorna el index seleccionado del spinner
     * @param pOption
     * @param isOperator: Indica si es el spinner de operadores
     * @return
     */
    private int getSpinnerPosition(String pOption, Spinner spinner, boolean isOperator){
        ArrayAdapter<Parametro> adapter = (ArrayAdapter<Parametro>) spinner.getAdapter();

        if(isOperator){
            int cantidadOperadores = arrayOperadores.length;
            for(int j = 0; j < cantidadOperadores; j++){
                String[] opcionSplit = arrayOperadores[j].split("#");
                if(opcionSplit[1].equals(pOption))
                    return j;
            }
        }
        return adapter.getPosition(new Parametro(Integer.parseInt(pOption), "", ""));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
