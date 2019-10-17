package com.ice.sgpr.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

public class EncuestaPDVFragment extends SgprFragment {

    private Spinner spnSocio;
    private Map<String, Object> mapingItems = new HashMap<>();
    private long negocioId;

    public EncuestaPDVFragment() {
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
        View view = inflater.inflate(R.layout.fragment_encuesta_pdv, container, false);

        spnSocio = (Spinner) view.findViewById(R.id.spn_enc_pdv_op);

        //Pregunta 1
        CheckBox chk_enc_pdv_1_1 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_1_1);
        CheckBox chk_enc_pdv_1_2 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_1_2);
        CheckBox chk_enc_pdv_1_3 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_1_3);
        CheckBox chk_enc_pdv_1_4 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_1_4);
        CheckBox chk_enc_pdv_1_5 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_1_5);

        EditText et_enc_pdv_1_1 = (EditText) view.findViewById(R.id.et_enc_pdv_1_1);
        EditText et_enc_pdv_1_2 = (EditText) view.findViewById(R.id.et_enc_pdv_1_2);
        EditText et_enc_pdv_1_3 = (EditText) view.findViewById(R.id.et_enc_pdv_1_3);
        EditText et_enc_pdv_1_4 = (EditText) view.findViewById(R.id.et_enc_pdv_1_4);
        EditText et_enc_pdv_1_5 = (EditText) view.findViewById(R.id.et_enc_pdv_1_5);

        //Pregunta 2
        RadioButton rb_enc_pdv_2_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_2_1);
        RadioButton rb_enc_pdv_2_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_2_2);
        RadioButton rb_enc_pdv_2_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_2_3);

        //Pregunta 3
        RadioButton rb_enc_pdv_3_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_3_1);
        RadioButton rb_enc_pdv_3_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_3_2);
        RadioButton rb_enc_pdv_3_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_3_3);

        //Pregunta 4
        RadioButton rb_enc_pdv_4_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_4_1);
        RadioButton rb_enc_pdv_4_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_4_2);
        RadioButton rb_enc_pdv_4_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_4_3);
        RadioButton rb_enc_pdv_4_4 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_4_4);
        RadioButton rb_enc_pdv_4_5 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_4_5);
        RadioButton rb_enc_pdv_4_6 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_4_6);

        //Pregunta 5
        RadioButton rb_enc_pdv_5_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_5_1);
        RadioButton rb_enc_pdv_5_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_5_2);

        //Pregunta 6
        CheckBox chk_enc_pdv_6_1 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_6_1);
        CheckBox chk_enc_pdv_6_2 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_6_2);
        CheckBox chk_enc_pdv_6_3 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_6_3);
        CheckBox chk_enc_pdv_6_4 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_6_4);
        CheckBox chk_enc_pdv_6_5 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_6_5);

        EditText et_enc_pdv_6_1 = (EditText) view.findViewById(R.id.et_enc_pdv_6_1);
        EditText et_enc_pdv_6_2 = (EditText) view.findViewById(R.id.et_enc_pdv_6_2);
        EditText et_enc_pdv_6_3 = (EditText) view.findViewById(R.id.et_enc_pdv_6_3);
        EditText et_enc_pdv_6_4 = (EditText) view.findViewById(R.id.et_enc_pdv_6_4);
        EditText et_enc_pdv_6_5 = (EditText) view.findViewById(R.id.et_enc_pdv_6_5);

        //Pregunta 7
        RadioButton rb_enc_pdv_7_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_7_1);
        RadioButton rb_enc_pdv_7_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_7_2);
        CheckBox chk_enc_pdv_7_3 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_7_3);

        //Pregunta 8
        RadioButton rb_enc_pdv_8_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_8_1);
        RadioButton rb_enc_pdv_8_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_8_2);
        EditText et_enc_pdv_8_3 = (EditText) view.findViewById(R.id.et_enc_pdv_8_3);

        //Pregunta 9
        RadioButton rb_enc_pdv_9_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_9_1);
        RadioButton rb_enc_pdv_9_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_9_2);
        RadioButton rb_enc_pdv_9_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_9_3);

        //Pregunta 10
        RadioButton rb_enc_pdv_10_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_10_1);
        RadioButton rb_enc_pdv_10_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_10_2);
        RadioButton rb_enc_pdv_10_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_10_3);

        //Pregunta 11
        RadioButton rb_enc_pdv_11_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_11_1);
        RadioButton rb_enc_pdv_11_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_11_2);
        EditText et_enc_pdv_11_3 = (EditText) view.findViewById(R.id.et_enc_pdv_11_3);

        //Pregunta 12
        CheckBox chk_enc_pdv_12_1 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_12_1);
        CheckBox chk_enc_pdv_12_2 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_12_2);
        CheckBox chk_enc_pdv_12_3 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_12_3);
        CheckBox chk_enc_pdv_12_4 = (CheckBox) view.findViewById(R.id.chk_enc_pdv_12_4);
        EditText et_enc_pdv_12_5 = (EditText) view.findViewById(R.id.et_enc_pdv_12_5);

        //Pregunta 13
        RadioButton rb_enc_pdv_13_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_13_1);
        RadioButton rb_enc_pdv_13_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_13_2);
        RadioButton rb_enc_pdv_13_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_13_3);
        EditText et_enc_pdv_13_4 = (EditText) view.findViewById(R.id.et_enc_pdv_13_4);

        //Pregunta 14
        RadioButton rb_enc_pdv_14_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_14_1);
        RadioButton rb_enc_pdv_14_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_14_2);
        RadioButton rb_enc_pdv_14_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_14_3);
        RadioButton rb_enc_pdv_14_4 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_14_4);

        //Pregunta 15
        RadioButton rb_enc_pdv_15_1 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_15_1);
        RadioButton rb_enc_pdv_15_2 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_15_2);
        RadioButton rb_enc_pdv_15_3 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_15_3);
        RadioButton rb_enc_pdv_15_4 = (RadioButton) view.findViewById(R.id.rb_enc_pdv_15_4);

        //Pregunta 16
        EditText et_enc_pdv_16_1 = (EditText) view.findViewById(R.id.et_enc_pdv_16_1);

        mapingItems.put("chk_enc_pdv_1_1", chk_enc_pdv_1_1);
        mapingItems.put("chk_enc_pdv_1_2", chk_enc_pdv_1_2);
        mapingItems.put("chk_enc_pdv_1_3", chk_enc_pdv_1_3);
        mapingItems.put("chk_enc_pdv_1_4", chk_enc_pdv_1_4);
        mapingItems.put("chk_enc_pdv_1_5", chk_enc_pdv_1_5);

        mapingItems.put("et_enc_pdv_1_1", et_enc_pdv_1_1);
        mapingItems.put("et_enc_pdv_1_2", et_enc_pdv_1_2);
        mapingItems.put("et_enc_pdv_1_3", et_enc_pdv_1_3);
        mapingItems.put("et_enc_pdv_1_4", et_enc_pdv_1_4);
        mapingItems.put("et_enc_pdv_1_5", et_enc_pdv_1_5);

        mapingItems.put("rb_enc_pdv_2_1", rb_enc_pdv_2_1);
        mapingItems.put("rb_enc_pdv_2_2", rb_enc_pdv_2_2);
        mapingItems.put("rb_enc_pdv_2_3", rb_enc_pdv_2_3);

        mapingItems.put("rb_enc_pdv_3_1", rb_enc_pdv_3_1);
        mapingItems.put("rb_enc_pdv_3_2", rb_enc_pdv_3_2);
        mapingItems.put("rb_enc_pdv_3_3", rb_enc_pdv_3_3);

        mapingItems.put("rb_enc_pdv_4_1", rb_enc_pdv_4_1);
        mapingItems.put("rb_enc_pdv_4_2", rb_enc_pdv_4_2);
        mapingItems.put("rb_enc_pdv_4_3", rb_enc_pdv_4_3);
        mapingItems.put("rb_enc_pdv_4_4", rb_enc_pdv_4_4);
        mapingItems.put("rb_enc_pdv_4_5", rb_enc_pdv_4_5);
        mapingItems.put("rb_enc_pdv_4_6", rb_enc_pdv_4_6);

        mapingItems.put("rb_enc_pdv_5_1", rb_enc_pdv_5_1);
        mapingItems.put("rb_enc_pdv_5_2", rb_enc_pdv_5_2);

        mapingItems.put("chk_enc_pdv_6_1", chk_enc_pdv_6_1);
        mapingItems.put("chk_enc_pdv_6_2", chk_enc_pdv_6_2);
        mapingItems.put("chk_enc_pdv_6_3", chk_enc_pdv_6_3);
        mapingItems.put("chk_enc_pdv_6_4", chk_enc_pdv_6_4);
        mapingItems.put("chk_enc_pdv_6_5", chk_enc_pdv_6_5);

        mapingItems.put("et_enc_pdv_6_1", et_enc_pdv_6_1);
        mapingItems.put("et_enc_pdv_6_2", et_enc_pdv_6_2);
        mapingItems.put("et_enc_pdv_6_3", et_enc_pdv_6_3);
        mapingItems.put("et_enc_pdv_6_4", et_enc_pdv_6_4);
        mapingItems.put("et_enc_pdv_6_5", et_enc_pdv_6_5);

        mapingItems.put("rb_enc_pdv_7_1", rb_enc_pdv_7_1);
        mapingItems.put("rb_enc_pdv_7_2", rb_enc_pdv_7_2);
        mapingItems.put("chk_enc_pdv_7_3", chk_enc_pdv_7_3);

        mapingItems.put("rb_enc_pdv_8_1", rb_enc_pdv_8_1);
        mapingItems.put("rb_enc_pdv_8_2", rb_enc_pdv_8_2);
        mapingItems.put("et_enc_pdv_8_3", et_enc_pdv_8_3);

        mapingItems.put("rb_enc_pdv_9_1", rb_enc_pdv_9_1);
        mapingItems.put("rb_enc_pdv_9_2", rb_enc_pdv_9_2);
        mapingItems.put("rb_enc_pdv_9_3", rb_enc_pdv_9_3);

        mapingItems.put("rb_enc_pdv_10_1", rb_enc_pdv_10_1);
        mapingItems.put("rb_enc_pdv_10_2", rb_enc_pdv_10_2);
        mapingItems.put("rb_enc_pdv_10_3", rb_enc_pdv_10_3);

        mapingItems.put("rb_enc_pdv_11_1", rb_enc_pdv_11_1);
        mapingItems.put("rb_enc_pdv_11_2", rb_enc_pdv_11_2);
        mapingItems.put("et_enc_pdv_11_3", et_enc_pdv_11_3);

        mapingItems.put("chk_enc_pdv_12_1", chk_enc_pdv_12_1);
        mapingItems.put("chk_enc_pdv_12_2", chk_enc_pdv_12_2);
        mapingItems.put("chk_enc_pdv_12_3", chk_enc_pdv_12_3);
        mapingItems.put("chk_enc_pdv_12_4", chk_enc_pdv_12_4);
        mapingItems.put("et_enc_pdv_12_5", et_enc_pdv_12_5);

        mapingItems.put("rb_enc_pdv_13_1", rb_enc_pdv_13_1);
        mapingItems.put("rb_enc_pdv_13_2", rb_enc_pdv_13_2);
        mapingItems.put("rb_enc_pdv_13_3", rb_enc_pdv_13_3);
        mapingItems.put("et_enc_pdv_13_4", et_enc_pdv_13_4);

        mapingItems.put("rb_enc_pdv_14_1", rb_enc_pdv_14_1);
        mapingItems.put("rb_enc_pdv_14_2", rb_enc_pdv_14_2);
        mapingItems.put("rb_enc_pdv_14_3", rb_enc_pdv_14_3);
        mapingItems.put("rb_enc_pdv_14_4", rb_enc_pdv_14_4);

        mapingItems.put("rb_enc_pdv_15_1", rb_enc_pdv_15_1);
        mapingItems.put("rb_enc_pdv_15_2", rb_enc_pdv_15_2);
        mapingItems.put("rb_enc_pdv_15_3", rb_enc_pdv_15_3);
        mapingItems.put("rb_enc_pdv_15_4", rb_enc_pdv_15_4);

        mapingItems.put("et_enc_pdv_16_1", et_enc_pdv_16_1);


        List<Parametro> sociosList = ParametrosImplementor.getInstance().obtenerListaParametros(4);
        cargarSpinner(sociosList, spnSocio);

        Button btnSave = (Button) view.findViewById(R.id.btn_encuesta_pdv_save);
        btnSave.setOnClickListener(onSave);

        loadEncuestas();
        return view;
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
     * Acción de guardar la info de la encuesta PDV.
     */
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Parametro socio = (Parametro) spnSocio.getSelectedItem();

            //Creación del objeto JSON:
            JSONArray arrayOpciones = new JSONArray();
            JSONObject objectOption = new JSONObject();

            String variableOption = "";

            try {
                objectOption.put("so", Integer.toString(socio.getCodigo()));
                arrayOpciones.put(objectOption);
                String currentOption = "1";
                EditText genericEditText = null;

                //Foreach para cada elemento del mapping
                for (Map.Entry<String, Object> entry : mapingItems.entrySet()) {
                    String key = entry.getKey();
                    Object item = entry.getValue();
                    objectOption = new JSONObject();
                    /*Todos los elementos tienen la misma estructura en el nombre:
                    *Tipo*_enc_pdv_*NumeroOpcion*_*numeroSubOpcion*
                    * Así que los datos que necesitamos quedarían:
                    * [0] = Tipo
                    * [3] = Número de opción
                    * [4] = Numero de subOpción.
                     */
                    String[] splitKey = key.split("_");
                    //Si el tipo es checkbox
                    if (splitKey[0].equals("chk")) {
                        if (((CheckBox) item).isChecked())
                            objectOption.put("chk_" + splitKey[3] + "_" + splitKey[4], "1");
                        else if(splitKey[3].equals("7"))
                            objectOption.put("chk_" + splitKey[3] + "_" + splitKey[4], "0");
                    }
                    //Si el tipo es Edittext
                    else if (splitKey[0].equals("et")) {
                        genericEditText = (EditText) item;
                        if (!genericEditText.getText().toString().equals(""))
                            objectOption.put("et_" + splitKey[3] + "_" + splitKey[4], genericEditText.getText().toString());
                    }
                    //Si el tipo es radioButton
                    else if (splitKey[0].equals("rb")) {
                        if (((RadioButton) item).isChecked()) {
                            int value = Integer.parseInt(splitKey[4]);
                            objectOption.put("rb_" + splitKey[3], Integer.toString(value));
                        }
                    }
                    if (objectOption.length() != 0)
                        arrayOpciones.put(objectOption);
                }

                //inserta la info
                JSONObject jsonSocioPDV = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, Constantes.ENCUESTA_PDV);
                int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
                SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonSocioPDV.toString(), currentUser, Constantes.ENCUESTA_PDV);
                Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void loadEncuestas() {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, Constantes.ENCUESTA_PDV);
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, Constantes.ENCUESTA_PDV);

                String strOption = "";
                String[] optionSplit = new String[]{};

                for (String[] option : jsonsList) {
                    if (option[0].equals("so")) {
                        spnSocio.setSelection(getSpinnerPosition(option[1], spnSocio), false);
                    }
                    else if(option[0].equals("obs")){

                    }
                    else{
                        optionSplit = option[0].split("_");
                        if(!optionSplit[0].equals("rb"))
                            strOption = optionSplit[0] + "_enc_pdv_" + optionSplit[1] + "_" + optionSplit[2];

                        if(optionSplit[0].equals("chk") && option[1].equals("1"))
                            ((CheckBox)mapingItems.get(strOption)).setChecked(true);

                        else if(optionSplit[0].equals("et"))
                            ((EditText)mapingItems.get(strOption)).setText(option[1]);

                        else if(optionSplit[0].equals("rb")) {
                            int value = Integer.parseInt(option[1]);
                            strOption = optionSplit[0] + "_enc_pdv_" + optionSplit[1] + "_" + (value);
                            ((RadioButton) mapingItems.get(strOption)).setChecked(true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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