package com.ice.sgpr.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.implementor.ParametrosImplementor;
import com.ice.sgpr.implementor.SupervTercerosImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.ui.activities.SupervisionTercerosActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Efrain Peraza 11.07.16
 * Fragment para asignar socio al punto de venta
 */
public class SocioPDVFragment extends SgprFragment {

    private long negocioId;
    Spinner spnSocio, spnFreqVisita, spnEntregaProducto, spnInstalaPop;
    EditText etObservaciones;
    //Mapear las variables:
    private Map<String, CheckBox> mapingVariables = new HashMap<>();

    public SocioPDVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            negocioId = getArguments().getLong(SupervisionTercerosActivity.NEGOCIO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_socio_pdv, container, false);
        String[] arrayFreqVisita = getResources().getStringArray(R.array.array_frec_visita_pdv);
        String[] arrayEntregaProducto = getResources().getStringArray(R.array.array_entrega_producto);
        String[] arrayInstalaPop = new String[]{"Sí", "No"};

        spnSocio = (Spinner) view.findViewById(R.id.spn_spdv_so);
        spnFreqVisita = (Spinner) view.findViewById(R.id.spn_spdv_fv);
        spnEntregaProducto = (Spinner) view.findViewById(R.id.spn_spdv_ep);
        spnInstalaPop = (Spinner) view.findViewById(R.id.spn_spdv_ip);

        setAdapters(spnFreqVisita, arrayFreqVisita);
        setAdapters(spnEntregaProducto, arrayEntregaProducto);
        setAdapters(spnInstalaPop, arrayInstalaPop);
        etObservaciones = (EditText) view.findViewById(R.id.et_spdv_obs);

        List<Parametro> sociosList = ParametrosImplementor.getInstance().obtenerListaParametros(4);
        cargarSpinner(sociosList, spnSocio);

        Button btnSave = (Button) view.findViewById(R.id.btn_frag_socio_save);
        btnSave.setOnClickListener(onSave);

        //Se agregan los checkbox al mapeo de variables
        CheckBox chk_spdv_1a = (CheckBox) view.findViewById(R.id.chk_spdv_1a);
        CheckBox chk_spdv_1b1 = (CheckBox) view.findViewById(R.id.chk_spdv_1b1);
        CheckBox chk_spdv_1b2 = (CheckBox) view.findViewById(R.id.chk_spdv_1b2);
        CheckBox chk_spdv_1c = (CheckBox) view.findViewById(R.id.chk_spdv_1c);
        CheckBox chk_spdv_2 = (CheckBox) view.findViewById(R.id.chk_spdv_2);
        CheckBox chk_spdv_3 = (CheckBox) view.findViewById(R.id.chk_spdv_3);
        CheckBox chk_spdv_4 = (CheckBox) view.findViewById(R.id.chk_spdv_4);

        mapingVariables.put("chk_spdv_1a", chk_spdv_1a);
        mapingVariables.put("chk_spdv_1b1", chk_spdv_1b1);
        mapingVariables.put("chk_spdv_1b2", chk_spdv_1b2);
        mapingVariables.put("chk_spdv_1c", chk_spdv_1c);
        mapingVariables.put("chk_spdv_2", chk_spdv_2);
        mapingVariables.put("chk_spdv_3", chk_spdv_3);
        mapingVariables.put("chk_spdv_4", chk_spdv_4);

        loadIncidences();

        return view;
    }


    private void setAdapters(Spinner spinner, String[] array) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, array);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * Arma un spinner, cargando la informacion.
     */
    private void cargarSpinner(List<Parametro> pParametros, Spinner pSpinner) {
        ArrayAdapter<Parametro> spinnerAdapter = new ArrayAdapter<Parametro>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, pParametros);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pSpinner.setAdapter(spinnerAdapter);
    }

    /**
     * Listener del botón de guardar.
     */
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Parametro socio = (Parametro) spnSocio.getSelectedItem();
            int frecVisita = spnFreqVisita.getSelectedItemPosition();
            int entProducto = spnEntregaProducto.getSelectedItemPosition();
            int instalaPop = spnInstalaPop.getSelectedItemPosition();
            String observations = etObservaciones.getText().toString();

            String variableOption = "";
            String currentOption = "";

            //Creación del objeto JSON:
            JSONArray arrayOpciones = new JSONArray();
            JSONObject objectOption = new JSONObject();

            try {
                objectOption.put("so", Integer.toString(socio.getCodigo()));
                arrayOpciones.put(objectOption);

                for (int opcionCont = 0; opcionCont < 7; opcionCont++) {
                    variableOption = Constantes.SPN_SOCIO_PDV_FORMAT;
                    switch (opcionCont) {
                        case 0:
                            currentOption = "1a";
                            variableOption += "1a";
                            break;
                        case 1:
                            currentOption = "1b1";
                            variableOption += "1b1";
                            break;
                        case 2:
                            currentOption = "1b2";
                            variableOption += "1b2";
                            break;
                        case 3:
                            currentOption = "1c";
                            variableOption += "1c";
                            break;
                        case 4:
                            currentOption = "2";
                            variableOption += "2";
                            break;
                        case 5:
                            currentOption = "3";
                            variableOption += "3";
                            break;
                        case 6:
                            currentOption = "4";
                            variableOption += "4";
                            break;
                    }
                    if (mapingVariables.get(variableOption).isChecked()) {
                        objectOption = new JSONObject();
                        objectOption.put(currentOption, "1");
                        arrayOpciones.put(objectOption);
                    }
                }

                arrayOpciones.put(addJsonOption("fv", frecVisita));
                arrayOpciones.put(addJsonOption("ep", entProducto));
                arrayOpciones.put(addJsonOption("ip", instalaPop));

                if(!observations.equals("")) {
                    objectOption = new JSONObject();
                    objectOption.put("obs", observations);
                    arrayOpciones.put(objectOption);
                }

                //inserta la info
                JSONObject jsonSocioPDV = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, Constantes.SOCIO_PDV);
                int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
                SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonSocioPDV.toString(), currentUser, Constantes.SOCIO_PDV);
                Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Se carga la información de las incidencias con los datos almacenados en el json.
     */
    private void loadIncidences() {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, Constantes.SOCIO_PDV);
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, Constantes.SOCIO_PDV);

                for (String[] option : jsonsList) {
                    if (option[0].equals("so")) {
                        spnSocio.setSelection(getSpinnerPosition(option[1], spnSocio), false);
                    }
                    else if (option[0].equals("1a")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "1a").setChecked(true);
                    }
                    else if (option[0].equals("1b1")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "1b1").setChecked(true);
                    }
                    else if (option[0].equals("1b2")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "1b2").setChecked(true);
                    }
                    else if (option[0].equals("1c")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "1c").setChecked(true);
                    }
                    else if (option[0].equals("2")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "2").setChecked(true);
                    }
                    else if (option[0].equals("3")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "3").setChecked(true);
                    }
                    else if (option[0].equals("4")) {
                        mapingVariables.get(Constantes.SPN_SOCIO_PDV_FORMAT + "4").setChecked(true);
                    }
                    else if (option[0].equals("fv")) {
                        spnFreqVisita.setSelection(Integer.parseInt(option[1])-1);
                    }
                    else if (option[0].equals("ep")) {
                        spnEntregaProducto.setSelection(Integer.parseInt(option[1])-1);
                    }
                    else if (option[0].equals("ip")) {
                        spnInstalaPop.setSelection(Integer.parseInt(option[1])-1);
                    }
                    else if (option[0].equals("obs")) {
                        etObservaciones.setText(option[1]);
                    }
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }

    //Arma el json de las opciones de los checks
    private JSONObject addJsonOption(String name, int value){
        JSONObject objectOption = new JSONObject();
        try {
            objectOption.put(name, Integer.toString(value + 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objectOption;
    }

    /**
     * Retorna el index seleccionado del spinner
     * @param pOption
     * @return
     */
    private int getSpinnerPosition(String pOption, Spinner spinner){
        ArrayAdapter<Parametro> adapter = (ArrayAdapter<Parametro>) spinner.getAdapter();
        return adapter.getPosition(new Parametro(Integer.parseInt(pOption), "", ""));
    }
}
