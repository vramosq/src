package com.ice.sgpr.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.implementor.SupervTercerosImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.ui.activities.SupervisionTercerosActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment de supervisión de distribuidores
 * Efraín Peraza - 18.05.16
 */
public class SupervDistribuidoresFragment extends SgprFragment {
    private long negocioId;

    private OnFragmentInteractionListener mListener;

    //Mapear las variables:
    private Map<String, CheckBox> mapingVariables = new HashMap<>();

    public SupervDistribuidoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pNegocioId Id del negocio.
     * @return A new instance of fragment SupervDistribuidoresFragment.
     */
    public static SupervDistribuidoresFragment newInstance(int pNegocioId) {
        SupervDistribuidoresFragment fragment = new SupervDistribuidoresFragment();
        Bundle args = new Bundle();
        args.putInt(SupervisionTercerosActivity.NEGOCIO_ID, pNegocioId);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superv_distribuidores, container, false);

        //Checkboxes de la opción 1A: Tarjetas
        CheckBox chk_1a_na, chk_1a_k, chk_1a_c, chk_1a_m, chk_1a_t, chk_1a_f;

        //Checkboxes de lb1 opción 1B: Venta Web
        CheckBox chk_1b1_na, chk_1b1_k, chk_1b1_c, chk_1b1_m, chk_1b1_t, chk_1b1_f;

        //Checkboxes de lb2 opción 1B: Venta Móvil
        CheckBox chk_1b2_na, chk_1b2_k, chk_1b2_c, chk_1b2_m, chk_1b2_t, chk_1b2_f;

        //Checkboxes de la opción 1C: Pin electrónico
        CheckBox chk_1c_na, chk_1c_k, chk_1c_c, chk_1c_m, chk_1c_t, chk_1c_f;

        //Checkboxes de opción 2: SIM Prepago
        CheckBox chk_2_na, chk_2_k, chk_2_c, chk_2_m, chk_2_t, chk_2_f;

        //Checkboxes de opción 3: KIT Prepago
        CheckBox chk_3_na, chk_3_k, chk_3_c, chk_3_m, chk_3_t, chk_3_f;

        //Checkboxes de opción 3: Pospago
        CheckBox chk_4_na, chk_4_k, chk_4_c, chk_4_m, chk_4_t, chk_4_f;

        //Checkboxes de opciónes de Interés
        CheckBox chk_1a_in, chk_1b1_in, chk_1b2_in, chk_1c_in, chk_2_in, chk_3_in, chk_4_in;

        //1A tarjetas
        chk_1a_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_na);
        chk_1a_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_kolbi);
        chk_1a_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_claro);
        chk_1a_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_movistar);
        chk_1a_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_tuyo);
        chk_1a_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_full);

        chk_1a_na.setOnCheckedChangeListener(onNASelected);
        chk_1a_k.setOnCheckedChangeListener(onNASelected);
        chk_1a_c.setOnCheckedChangeListener(onNASelected);
        chk_1a_m.setOnCheckedChangeListener(onNASelected);
        chk_1a_t.setOnCheckedChangeListener(onNASelected);
        chk_1a_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_1a_na", chk_1a_na);
        mapingVariables.put("chk_1a_k", chk_1a_k);
        mapingVariables.put("chk_1a_c", chk_1a_c);
        mapingVariables.put("chk_1a_m", chk_1a_m);
        mapingVariables.put("chk_1a_t", chk_1a_t);
        mapingVariables.put("chk_1a_f", chk_1a_f);


        //1B1 Venta Web
        chk_1b1_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_na);
        chk_1b1_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_kolbi);
        chk_1b1_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_claro);
        chk_1b1_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_movistar);
        chk_1b1_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_tuyo);
        chk_1b1_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_full);

        chk_1b1_na.setOnCheckedChangeListener(onNASelected);
        chk_1b1_k.setOnCheckedChangeListener(onNASelected);
        chk_1b1_c.setOnCheckedChangeListener(onNASelected);
        chk_1b1_m.setOnCheckedChangeListener(onNASelected);
        chk_1b1_t.setOnCheckedChangeListener(onNASelected);
        chk_1b1_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_1b1_na", chk_1b1_na);
        mapingVariables.put("chk_1b1_k", chk_1b1_k);
        mapingVariables.put("chk_1b1_c", chk_1b1_c);
        mapingVariables.put("chk_1b1_m", chk_1b1_m);
        mapingVariables.put("chk_1b1_t", chk_1b1_t);
        mapingVariables.put("chk_1b1_f", chk_1b1_f);

        //1B2 Venta Móvil
        chk_1b2_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_na);
        chk_1b2_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_kolbi);
        chk_1b2_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_claro);
        chk_1b2_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_movistar);
        chk_1b2_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_tuyo);
        chk_1b2_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_full);

        chk_1b2_na.setOnCheckedChangeListener(onNASelected);
        chk_1b2_k.setOnCheckedChangeListener(onNASelected);
        chk_1b2_c.setOnCheckedChangeListener(onNASelected);
        chk_1b2_m.setOnCheckedChangeListener(onNASelected);
        chk_1b2_t.setOnCheckedChangeListener(onNASelected);
        chk_1b2_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_1b2_na", chk_1b2_na);
        mapingVariables.put("chk_1b2_k", chk_1b2_k);
        mapingVariables.put("chk_1b2_c", chk_1b2_c);
        mapingVariables.put("chk_1b2_m", chk_1b2_m);
        mapingVariables.put("chk_1b2_t", chk_1b2_t);
        mapingVariables.put("chk_1b2_f", chk_1b2_f);

        //1C Pin electrónico
        chk_1c_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_na);
        chk_1c_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_kolbi);
        chk_1c_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_claro);
        chk_1c_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_movistar);
        chk_1c_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_tuyo);
        chk_1c_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_full);

        chk_1c_na.setOnCheckedChangeListener(onNASelected);
        chk_1c_k.setOnCheckedChangeListener(onNASelected);
        chk_1c_c.setOnCheckedChangeListener(onNASelected);
        chk_1c_m.setOnCheckedChangeListener(onNASelected);
        chk_1c_t.setOnCheckedChangeListener(onNASelected);
        chk_1c_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_1c_na", chk_1c_na);
        mapingVariables.put("chk_1c_k", chk_1c_k);
        mapingVariables.put("chk_1c_c", chk_1c_c);
        mapingVariables.put("chk_1c_m", chk_1c_m);
        mapingVariables.put("chk_1c_t", chk_1c_t);
        mapingVariables.put("chk_1c_f", chk_1c_f);

        //2. SIM Prepago
        chk_2_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_na);
        chk_2_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_kolbi);
        chk_2_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_claro);
        chk_2_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_movistar);
        chk_2_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_tuyo);
        chk_2_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_full);

        chk_2_na.setOnCheckedChangeListener(onNASelected);
        chk_2_k.setOnCheckedChangeListener(onNASelected);
        chk_2_c.setOnCheckedChangeListener(onNASelected);
        chk_2_m.setOnCheckedChangeListener(onNASelected);
        chk_2_t.setOnCheckedChangeListener(onNASelected);
        chk_2_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_2_na", chk_2_na);
        mapingVariables.put("chk_2_k", chk_2_k);
        mapingVariables.put("chk_2_c", chk_2_c);
        mapingVariables.put("chk_2_m", chk_2_m);
        mapingVariables.put("chk_2_t", chk_2_t);
        mapingVariables.put("chk_2_f", chk_2_f);

        // 3 KIT Prepago
        chk_3_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_na);
        chk_3_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_kolbi);
        chk_3_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_claro);
        chk_3_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_movistar);
        chk_3_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_tuyo);
        chk_3_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_full);

        chk_3_na.setOnCheckedChangeListener(onNASelected);
        chk_3_k.setOnCheckedChangeListener(onNASelected);
        chk_3_c.setOnCheckedChangeListener(onNASelected);
        chk_3_m.setOnCheckedChangeListener(onNASelected);
        chk_3_t.setOnCheckedChangeListener(onNASelected);
        chk_3_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_3_na", chk_3_na);
        mapingVariables.put("chk_3_k", chk_3_k);
        mapingVariables.put("chk_3_c", chk_3_c);
        mapingVariables.put("chk_3_m", chk_3_m);
        mapingVariables.put("chk_3_t", chk_3_t);
        mapingVariables.put("chk_3_f", chk_3_f);

        // 4 Pospago
        chk_4_na = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_na);
        chk_4_k = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_kolbi);
        chk_4_c = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_claro);
        chk_4_m = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_movistar);
        chk_4_t = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_tuyo);
        chk_4_f = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_full);

        chk_4_na.setOnCheckedChangeListener(onNASelected);
        chk_4_k.setOnCheckedChangeListener(onNASelected);
        chk_4_c.setOnCheckedChangeListener(onNASelected);
        chk_4_m.setOnCheckedChangeListener(onNASelected);
        chk_4_t.setOnCheckedChangeListener(onNASelected);
        chk_4_f.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_4_na", chk_4_na);
        mapingVariables.put("chk_4_k", chk_4_k);
        mapingVariables.put("chk_4_c", chk_4_c);
        mapingVariables.put("chk_4_m", chk_4_m);
        mapingVariables.put("chk_4_t", chk_4_t);
        mapingVariables.put("chk_4_f", chk_4_f);

        //Intereses
        chk_1a_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_1a_in);
        chk_1b1_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b1_in);
        chk_1b2_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_1b2_in);
        chk_1c_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_1c_in);
        chk_2_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_2_in);
        chk_3_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_3_in);
        chk_4_in = (CheckBox) view.findViewById(R.id.chk_sup_terc_4_in);

        chk_1a_in.setOnCheckedChangeListener(onNASelected);
        chk_1b1_in.setOnCheckedChangeListener(onNASelected);
        chk_1b2_in.setOnCheckedChangeListener(onNASelected);
        chk_1c_in.setOnCheckedChangeListener(onNASelected);
        chk_2_in.setOnCheckedChangeListener(onNASelected);
        chk_3_in.setOnCheckedChangeListener(onNASelected);
        chk_4_in.setOnCheckedChangeListener(onNASelected);

        //Se agregan las variables al mapeo:
        mapingVariables.put("chk_1a_in", chk_1a_in);
        mapingVariables.put("chk_1b1_in", chk_1b1_in);
        mapingVariables.put("chk_1b2_in", chk_1b2_in);
        mapingVariables.put("chk_1c_in", chk_1c_in);
        mapingVariables.put("chk_2_in", chk_2_in);
        mapingVariables.put("chk_3_in", chk_3_in);
        mapingVariables.put("chk_4_in", chk_4_in);

        //Boton de guardado
        Button btnSave = (Button) view.findViewById(R.id.btn_frag_sup_terc_sup_dist_save);
        btnSave.setOnClickListener(onSave);

        loadPreviousSelections();
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    /**
     * Listener de los checkboxes de la opción "N/A" (para todas las opciones)
     * Desmarca cada una de las opciones de su fila.
     *
     * @param view
     */
    private CompoundButton.OnCheckedChangeListener onNASelected = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButtonView, boolean isChecked) {
            int viewId = compoundButtonView.getId();
            if (isChecked) {
                switch (viewId) {
                    case R.id.chk_sup_terc_1a_na:
                        mapingVariables.get("chk_1a_k").setChecked(false);
                        mapingVariables.get("chk_1a_c").setChecked(false);
                        mapingVariables.get("chk_1a_m").setChecked(false);
                        mapingVariables.get("chk_1a_t").setChecked(false);
                        mapingVariables.get("chk_1a_f").setChecked(false);
                        return;
                    case R.id.chk_sup_terc_1b1_na:
                        mapingVariables.get("chk_1b1_k").setChecked(false);
                        mapingVariables.get("chk_1b1_c").setChecked(false);
                        mapingVariables.get("chk_1b1_m").setChecked(false);
                        mapingVariables.get("chk_1b1_t").setChecked(false);
                        mapingVariables.get("chk_1b1_f").setChecked(false);
                        return;
                    case R.id.chk_sup_terc_1b2_na:
                        mapingVariables.get("chk_1b2_k").setChecked(false);
                        mapingVariables.get("chk_1b2_c").setChecked(false);
                        mapingVariables.get("chk_1b2_m").setChecked(false);
                        mapingVariables.get("chk_1b2_t").setChecked(false);
                        mapingVariables.get("chk_1b2_f").setChecked(false);
                        return;
                    case R.id.chk_sup_terc_2_na:
                        mapingVariables.get("chk_2_k").setChecked(false);
                        mapingVariables.get("chk_2_c").setChecked(false);
                        mapingVariables.get("chk_2_m").setChecked(false);
                        mapingVariables.get("chk_2_t").setChecked(false);
                        mapingVariables.get("chk_2_f").setChecked(false);
                        return;
                    case R.id.chk_sup_terc_1c_na:
                        mapingVariables.get("chk_1c_k").setChecked(false);
                        mapingVariables.get("chk_1c_c").setChecked(false);
                        mapingVariables.get("chk_1c_m").setChecked(false);
                        mapingVariables.get("chk_1c_t").setChecked(false);
                        mapingVariables.get("chk_1c_f").setChecked(false);
                        return;
                    case R.id.chk_sup_terc_3_na:
                        mapingVariables.get("chk_3_k").setChecked(false);
                        mapingVariables.get("chk_3_c").setChecked(false);
                        mapingVariables.get("chk_3_m").setChecked(false);
                        mapingVariables.get("chk_3_t").setChecked(false);
                        mapingVariables.get("chk_3_f").setChecked(false);
                        return;
                    case R.id.chk_sup_terc_4_na:
                        mapingVariables.get("chk_4_k").setChecked(false);
                        mapingVariables.get("chk_4_c").setChecked(false);
                        mapingVariables.get("chk_4_m").setChecked(false);
                        mapingVariables.get("chk_4_t").setChecked(false);
                        mapingVariables.get("chk_4_f").setChecked(false);
                        return;
                }

                /**
                 * Se obtiene el nombre del view. Todos los id's contienen el nombre de la opción:
                 * para 1A Tarjetas: Opción kolbi es chk_sup_terc_1a_kolbi, Claro es chk_sup_terc_1a_claro
                 * Para 1B1 Vta. Web: kolbi es chk_sup_terc_1b1_kolbi, Claro es chk_sup_terc_1b1_claro
                 * Y así sucesivamente.
                 * Entonces se verifica que el nombre del view contenga el substring de la opción para
                 * saber cual opción de N/A se debe colocar en falso.
                 */
                String viewName = getResources().getResourceEntryName(viewId);
                if (!viewName.contains("in")) { //Excluye a las opciones de intereses
                    if (viewName.contains("1a"))
                        mapingVariables.get("chk_1a_na").setChecked(false);
                    else if (viewName.contains("1b1"))
                        mapingVariables.get("chk_1b1_na").setChecked(false);
                    else if (viewName.contains("1b2"))
                        mapingVariables.get("chk_1b2_na").setChecked(false);
                    else if (viewName.contains("1c"))
                        mapingVariables.get("chk_1c_na").setChecked(false);
                    else if (viewName.contains("_2"))
                        mapingVariables.get("chk_2_na").setChecked(false);
                    else if (viewName.contains("_3"))
                        mapingVariables.get("chk_3_na").setChecked(false);
                    else if (viewName.contains("_4"))
                        mapingVariables.get("chk_4_na").setChecked(false);
                }
            }
        }
    };

    /**
     * Listener para el botón de guardar
     */
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String variableOption = "";
            String variableOperator = "";
            String currentOption = "";

            //Creación del objeto JSON:
            JSONArray arrayOpciones = new JSONArray();
            JSONObject objectOption;

            for (int opcionCont = 0; opcionCont < 7; opcionCont++) {
                variableOption = Constantes.CHK_SUP_DIST_FORMAT;
                JSONArray arrayOperators = new JSONArray();
                objectOption = new JSONObject();

                switch (opcionCont) {
                    case 0:
                        currentOption = "1a";
                        variableOption = variableOption.replace("*OPC", "1a");
                        break;
                    case 1:
                        currentOption = "1b1";
                        variableOption = variableOption.replace("*OPC", "1b1");
                        break;
                    case 2:
                        currentOption = "1b2";
                        variableOption = variableOption.replace("*OPC", "1b2");
                        break;
                    case 3:
                        currentOption = "1c";
                        variableOption = variableOption.replace("*OPC", "1c");
                        break;
                    case 4:
                        currentOption = "2";
                        variableOption = variableOption.replace("*OPC", "2");
                        break;
                    case 5:
                        currentOption = "3";
                        variableOption = variableOption.replace("*OPC", "3");
                        break;
                    case 6:
                        currentOption = "4";
                        variableOption = variableOption.replace("*OPC", "4");
                        break;
                }
                for (int operadorCont = 0; operadorCont < 7; operadorCont++) {
                    variableOperator = variableOption;
                    try {
                        switch (operadorCont) {
                            case (0): //N/A
                                variableOperator = variableOperator.replace("*OPER", Constantes.NA_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked()) {
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.NA_OPTION));
                                    continue;
                                }
                                break;
                            case (1): // kolbi
                                variableOperator = variableOperator.replace("*OPER", Constantes.KOLBI_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked())
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.KOLBI_OPTION));
                                break;
                            case (2): // Claro
                                variableOperator = variableOperator.replace("*OPER", Constantes.CLARO_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked())
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.CLARO_OPTION));
                                break;
                            case (3): //Movistar
                                variableOperator = variableOperator.replace("*OPER", Constantes.MOVISTAR_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked())
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.MOVISTAR_OPTION));
                                break;
                            case (4): // Tuyo
                                variableOperator = variableOperator.replace("*OPER", Constantes.TUYO_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked())
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.TUYO_OPTION));
                                break;
                            case (5): // Full
                                variableOperator = variableOperator.replace("*OPER", Constantes.FULL_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked())
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.FULL_OPTION));
                                break;
                            case (6): // Interés
                                variableOperator = variableOperator.replace("*OPER", Constantes.INTERES_OPTION);
                                if (mapingVariables.get(variableOperator).isChecked())
                                    arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.INTERES_OPTION));
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
                    }
                }
                try {
                    objectOption.put(currentOption, arrayOperators);
                    arrayOpciones.put(objectOption);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
                }
            }
            try {
                JSONObject jsonSupMarc = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, Constantes.SUPERV_DIST);
                int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
                SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonSupMarc.toString(), currentUser, Constantes.SUPERV_DIST);
                Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * Función para cargar la información previamente cargada para un determinado negocio.
     */
    private void loadPreviousSelections() {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, Constantes.SUPERV_DIST);
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, Constantes.SUPERV_DIST);

                for (String[] option : jsonsList) {
                    String strVariableNameTemp = Constantes.CHK_SUP_DIST_FORMAT.replace("*OPC", option[0]);

                    String[] operators = option[1].split(";");
                    for (String operatorInitial : operators) {
                        String opction = JSONHelper.getInstance().descifrarOpcion(operatorInitial);
                        String strVariableWithOperator = strVariableNameTemp.replace("*OPER", opction);
                        mapingVariables.get(strVariableWithOperator).setChecked(true);
                    }
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }
}
