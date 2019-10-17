package com.ice.sgpr.ui.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Parametro;
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
 * A simple {@link Fragment} subclass.
 */
public class SupervMarcaFragment extends SgprFragment {
    private long negocioId;
    private Map<String, CheckBox> mapingPreseciaMarca;
    private Map<String, EditText> mapingPubProPop; // Map para los edittext de publicidad, promocional y POP especial

    //Checkbox genérico para pubicidad y promcional. Es el check de la pregunta
    private CheckBox chkGenericPublicidadPromocional;

    //RadioButtons genéricos para publicdad y promocional. Son los RB's del motivo
    private RadioButton rbGenericPublicidadProm1, rbGenericPublicidadProm2, rbGenericPublicidadProm3;

    //DropDown para Pop Especial
    private Spinner spnPopInsall;

    //Lista de estados para pop Especial
    //ArrayList<String[]> statusList;
    ArrayList<Parametro> statusList;

    LinearLayout llOptions;

    private String formType;

    public SupervMarcaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            negocioId = getArguments().getLong(SupervisionTercerosActivity.NEGOCIO_ID);
        }
    }

    /**
     * Fragment para el formulario de supervición de marca.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Botones
        View view = inflater.inflate(R.layout.fragment_superv_marca, container, false);
        Button btnPresMarca = (Button) view.findViewById(R.id.btn_sup_marc_presencia);
        Button btnPublicidad = (Button) view.findViewById(R.id.btn_sup_marc_publicidad);
        Button btnPromocional = (Button) view.findViewById(R.id.btn_sup_marc_promocional);
        Button btnPopEspecial = (Button) view.findViewById(R.id.btn_sup_marc_pop);
        Button btnObservaciones = (Button) view.findViewById(R.id.btn_sup_marc_observaciones);

        btnPresMarca.setOnClickListener(onOptionPressed);
        btnPublicidad.setOnClickListener(onOptionPressed);
        btnPromocional.setOnClickListener(onOptionPressed);
        btnPopEspecial.setOnClickListener(onOptionPressed);
        btnObservaciones.setOnClickListener(onOptionPressed);

        return view;
    }


    /**
     * Acción al presionar alguno de los botones para motrar el formulario correspondiente
     */
    private View.OnClickListener onOptionPressed = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_sup_marc_presencia:
                    showDialog(0);
                    break;
                case R.id.btn_sup_marc_publicidad:
                    showDialog(1);
                    break;
                case R.id.btn_sup_marc_promocional:
                    showDialog(2);
                    break;
                case R.id.btn_sup_marc_pop:
                    showDialog(3);
                    break;
                case R.id.btn_sup_marc_observaciones:
                    showDialog(4);
                    break;
            }
        }
    };

    private void showDialog(final int type) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        switch (type) {
            case 0:
                dialog.setContentView(R.layout.dialog_presencia_marca);
                dialog.setTitle(Constantes.TITLE_PRES_MARCA);
                mapChecksPresMarca(dialog);
                loadPresenciaMarca();
                break;
            case 1:
                dialog.setContentView(R.layout.dialog_publicidad);
                dialog.setTitle(Constantes.TITLE_PUBLICIDAD);
                mapTextsPublicidad(dialog);
                formType = Constantes.PUBLICIDAD;
                loadPubPromPopObs(Constantes.PUBLICIDAD);
                break;
            case 2:
                dialog.setContentView(R.layout.dialog_promocional);
                dialog.setTitle(Constantes.TITLE_PROMOCIONAL);
                mapTextsPromocional(dialog);
                formType = Constantes.PROMOCIONAL;
                loadPubPromPopObs(Constantes.PROMOCIONAL);
                break;
            case 3:
                dialog.setContentView(R.layout.dialog_pop_especial);
                dialog.setTitle(Constantes.TITLE_POP_ESPECIAL);
                mapTextsPop(dialog);
                formType = Constantes.POP_ESPECIAL;
                loadPubPromPopObs(Constantes.POP_ESPECIAL);
                break;
            case 4:
                dialog.setContentView(R.layout.dialog_observaciones);
                dialog.setTitle(Constantes.TITLE_OBSERVACIONES);
                mapTextObservaciones(dialog);
                break;
        }


        //fullscreen
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_dialog_pres_marca_cancelar);
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.btn_dialog_pres_marca_aceptar);

        // Click cancel to dismiss android custom dialog box
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Your android custom dialog ok action
        // Action for custom dialog ok button click
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 0:
                        savePresenciaMarca();
                        break;
                    case 1:
                        savePubPromPopObs(Constantes.PUBLICIDAD);
                        break;
                    case 2:
                        savePubPromPopObs(Constantes.PROMOCIONAL);
                        break;
                    case 3:
                        savePubPromPopObs(Constantes.POP_ESPECIAL);
                        break;
                    case 4:
                        savePubPromPopObs(Constantes.OBSERVACIONES);
                        break;
                }

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        if (type == 4)
            loadPubPromPopObs(Constantes.OBSERVACIONES);
    }

    /********************************************************************
     *********************** PRESENCIA DE MARCA *************************
     ********************************************************************/

    /**
     * Se mapean todos los checks de presencia de marca y se insertan en un hashmap.
     *
     * @param view
     */
    private void mapChecksPresMarca(Dialog view) {

        mapingPreseciaMarca = new HashMap<String, CheckBox>();
        //Hablador externo
        CheckBox chk_pres_marc_he_na, chk_pres_marc_he_k, chk_pres_marc_he_c, chk_pres_marc_he_m, chk_pres_marc_he_t, chk_pres_marc_he_f;
        //Hablador interno
        CheckBox chk_pres_marc_hi_na, chk_pres_marc_hi_k, chk_pres_marc_hi_c, chk_pres_marc_hi_m, chk_pres_marc_hi_t, chk_pres_marc_hi_f;
        //Colgantes
        CheckBox chk_pres_marc_co_na, chk_pres_marc_co_k, chk_pres_marc_co_c, chk_pres_marc_co_m, chk_pres_marc_co_t, chk_pres_marc_co_f;
        //Gráfica puerta
        CheckBox chk_pres_marc_gp_na, chk_pres_marc_gp_k, chk_pres_marc_gp_c, chk_pres_marc_gp_m, chk_pres_marc_gp_t, chk_pres_marc_gp_f;
        //Gráfica mediana
        CheckBox chk_pres_marc_gm_na, chk_pres_marc_gm_k, chk_pres_marc_gm_c, chk_pres_marc_gm_m, chk_pres_marc_gm_t, chk_pres_marc_gm_f;
        //Gráfica grande
        CheckBox chk_pres_marc_gg_na, chk_pres_marc_gg_k, chk_pres_marc_gg_c, chk_pres_marc_gg_m, chk_pres_marc_gg_t, chk_pres_marc_gg_f;
        //Afiche info recarga
        CheckBox chk_pres_marc_air_na, chk_pres_marc_air_k, chk_pres_marc_air_c, chk_pres_marc_air_m, chk_pres_marc_air_t, chk_pres_marc_air_f;
        //Afiche info sim
        CheckBox chk_pres_marc_ais_na, chk_pres_marc_ais_k, chk_pres_marc_ais_c, chk_pres_marc_ais_m, chk_pres_marc_ais_t, chk_pres_marc_ais_f;
        //En urnas
        CheckBox chk_pres_marc_eu_na, chk_pres_marc_eu_k, chk_pres_marc_eu_c, chk_pres_marc_eu_m, chk_pres_marc_eu_t, chk_pres_marc_eu_f;
        //Pizarra
        CheckBox chk_pres_marc_pi_na, chk_pres_marc_pi_k, chk_pres_marc_pi_c, chk_pres_marc_pi_m, chk_pres_marc_pi_t, chk_pres_marc_pi_f;
        //Pedestal
        CheckBox chk_pres_marc_pe_na, chk_pres_marc_pe_k, chk_pres_marc_pe_c, chk_pres_marc_pe_m, chk_pres_marc_pe_t, chk_pres_marc_pe_f;
        //Rótulo termoformado
        CheckBox chk_pres_marc_rt_na, chk_pres_marc_rt_k, chk_pres_marc_rt_c, chk_pres_marc_rt_m, chk_pres_marc_rt_t, chk_pres_marc_rt_f;
        //Floor stick
        CheckBox chk_pres_marc_fs_na, chk_pres_marc_fs_k, chk_pres_marc_fs_c, chk_pres_marc_fs_m, chk_pres_marc_fs_t, chk_pres_marc_fs_f;
        //Lona
        CheckBox chk_pres_marc_lo_na, chk_pres_marc_lo_k, chk_pres_marc_lo_c, chk_pres_marc_lo_m, chk_pres_marc_lo_t, chk_pres_marc_lo_f;
        //Rótulo
        CheckBox chk_pres_marc_ro_na, chk_pres_marc_ro_k, chk_pres_marc_ro_c, chk_pres_marc_ro_m, chk_pres_marc_ro_t, chk_pres_marc_ro_f;
        //Microperforado
        CheckBox chk_pres_marc_mi_na, chk_pres_marc_mi_k, chk_pres_marc_mi_c, chk_pres_marc_mi_m, chk_pres_marc_mi_t, chk_pres_marc_mi_f;


        chk_pres_marc_he_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_he_na);
        chk_pres_marc_he_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_he_k);
        chk_pres_marc_he_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_he_c);
        chk_pres_marc_he_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_he_m);
        chk_pres_marc_he_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_he_t);
        chk_pres_marc_he_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_he_f);

        chk_pres_marc_he_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_he_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_he_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_he_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_he_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_he_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_he_na", chk_pres_marc_he_na);
        mapingPreseciaMarca.put("chk_pres_marc_he_k", chk_pres_marc_he_k);
        mapingPreseciaMarca.put("chk_pres_marc_he_c", chk_pres_marc_he_c);
        mapingPreseciaMarca.put("chk_pres_marc_he_m", chk_pres_marc_he_m);
        mapingPreseciaMarca.put("chk_pres_marc_he_t", chk_pres_marc_he_t);
        mapingPreseciaMarca.put("chk_pres_marc_he_f", chk_pres_marc_he_f);


        chk_pres_marc_hi_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_hi_na);
        chk_pres_marc_hi_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_hi_k);
        chk_pres_marc_hi_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_hi_c);
        chk_pres_marc_hi_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_hi_m);
        chk_pres_marc_hi_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_hi_t);
        chk_pres_marc_hi_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_hi_f);

        chk_pres_marc_hi_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_hi_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_hi_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_hi_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_hi_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_hi_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_hi_na", chk_pres_marc_hi_na);
        mapingPreseciaMarca.put("chk_pres_marc_hi_k", chk_pres_marc_hi_k);
        mapingPreseciaMarca.put("chk_pres_marc_hi_c", chk_pres_marc_hi_c);
        mapingPreseciaMarca.put("chk_pres_marc_hi_m", chk_pres_marc_hi_m);
        mapingPreseciaMarca.put("chk_pres_marc_hi_t", chk_pres_marc_hi_t);
        mapingPreseciaMarca.put("chk_pres_marc_hi_f", chk_pres_marc_hi_f);


        chk_pres_marc_co_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_co_na);
        chk_pres_marc_co_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_co_k);
        chk_pres_marc_co_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_co_c);
        chk_pres_marc_co_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_co_m);
        chk_pres_marc_co_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_co_t);
        chk_pres_marc_co_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_co_f);

        chk_pres_marc_co_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_co_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_co_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_co_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_co_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_co_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_co_na", chk_pres_marc_co_na);
        mapingPreseciaMarca.put("chk_pres_marc_co_k", chk_pres_marc_co_k);
        mapingPreseciaMarca.put("chk_pres_marc_co_c", chk_pres_marc_co_c);
        mapingPreseciaMarca.put("chk_pres_marc_co_m", chk_pres_marc_co_m);
        mapingPreseciaMarca.put("chk_pres_marc_co_t", chk_pres_marc_co_t);
        mapingPreseciaMarca.put("chk_pres_marc_co_f", chk_pres_marc_co_f);


        chk_pres_marc_gp_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_gp_na);
        chk_pres_marc_gp_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_gp_k);
        chk_pres_marc_gp_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_gp_c);
        chk_pres_marc_gp_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_gp_m);
        chk_pres_marc_gp_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_gp_t);
        chk_pres_marc_gp_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_gp_f);

        chk_pres_marc_gp_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gp_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gp_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gp_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gp_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gp_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_gp_na", chk_pres_marc_gp_na);
        mapingPreseciaMarca.put("chk_pres_marc_gp_k", chk_pres_marc_gp_k);
        mapingPreseciaMarca.put("chk_pres_marc_gp_c", chk_pres_marc_gp_c);
        mapingPreseciaMarca.put("chk_pres_marc_gp_m", chk_pres_marc_gp_m);
        mapingPreseciaMarca.put("chk_pres_marc_gp_t", chk_pres_marc_gp_t);
        mapingPreseciaMarca.put("chk_pres_marc_gp_f", chk_pres_marc_gp_f);


        chk_pres_marc_gm_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_gm_na);
        chk_pres_marc_gm_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_gm_k);
        chk_pres_marc_gm_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_gm_c);
        chk_pres_marc_gm_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_gm_m);
        chk_pres_marc_gm_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_gm_t);
        chk_pres_marc_gm_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_gm_f);

        chk_pres_marc_gm_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gm_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gm_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gm_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gm_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gm_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_gm_na", chk_pres_marc_gm_na);
        mapingPreseciaMarca.put("chk_pres_marc_gm_k", chk_pres_marc_gm_k);
        mapingPreseciaMarca.put("chk_pres_marc_gm_c", chk_pres_marc_gm_c);
        mapingPreseciaMarca.put("chk_pres_marc_gm_m", chk_pres_marc_gm_m);
        mapingPreseciaMarca.put("chk_pres_marc_gm_t", chk_pres_marc_gm_t);
        mapingPreseciaMarca.put("chk_pres_marc_gm_f", chk_pres_marc_gm_f);


        chk_pres_marc_gg_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_gg_na);
        chk_pres_marc_gg_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_gg_k);
        chk_pres_marc_gg_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_gg_c);
        chk_pres_marc_gg_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_gg_m);
        chk_pres_marc_gg_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_gg_t);
        chk_pres_marc_gg_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_gg_f);

        chk_pres_marc_gg_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gg_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gg_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gg_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gg_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_gg_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_gg_na", chk_pres_marc_gg_na);
        mapingPreseciaMarca.put("chk_pres_marc_gg_k", chk_pres_marc_gg_k);
        mapingPreseciaMarca.put("chk_pres_marc_gg_c", chk_pres_marc_gg_c);
        mapingPreseciaMarca.put("chk_pres_marc_gg_m", chk_pres_marc_gg_m);
        mapingPreseciaMarca.put("chk_pres_marc_gg_t", chk_pres_marc_gg_t);
        mapingPreseciaMarca.put("chk_pres_marc_gg_f", chk_pres_marc_gg_f);


        chk_pres_marc_air_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_air_na);
        chk_pres_marc_air_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_air_k);
        chk_pres_marc_air_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_air_c);
        chk_pres_marc_air_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_air_m);
        chk_pres_marc_air_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_air_t);
        chk_pres_marc_air_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_air_f);

        chk_pres_marc_air_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_air_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_air_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_air_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_air_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_air_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_air_na", chk_pres_marc_air_na);
        mapingPreseciaMarca.put("chk_pres_marc_air_k", chk_pres_marc_air_k);
        mapingPreseciaMarca.put("chk_pres_marc_air_c", chk_pres_marc_air_c);
        mapingPreseciaMarca.put("chk_pres_marc_air_m", chk_pres_marc_air_m);
        mapingPreseciaMarca.put("chk_pres_marc_air_t", chk_pres_marc_air_t);
        mapingPreseciaMarca.put("chk_pres_marc_air_f", chk_pres_marc_air_f);


        chk_pres_marc_ais_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_ais_na);
        chk_pres_marc_ais_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_ais_k);
        chk_pres_marc_ais_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_ais_c);
        chk_pres_marc_ais_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_ais_m);
        chk_pres_marc_ais_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_ais_t);
        chk_pres_marc_ais_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_ais_f);

        chk_pres_marc_ais_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ais_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ais_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ais_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ais_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ais_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_ais_na", chk_pres_marc_ais_na);
        mapingPreseciaMarca.put("chk_pres_marc_ais_k", chk_pres_marc_ais_k);
        mapingPreseciaMarca.put("chk_pres_marc_ais_c", chk_pres_marc_ais_c);
        mapingPreseciaMarca.put("chk_pres_marc_ais_m", chk_pres_marc_ais_m);
        mapingPreseciaMarca.put("chk_pres_marc_ais_t", chk_pres_marc_ais_t);
        mapingPreseciaMarca.put("chk_pres_marc_ais_f", chk_pres_marc_ais_f);


        chk_pres_marc_eu_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_eu_na);
        chk_pres_marc_eu_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_eu_k);
        chk_pres_marc_eu_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_eu_c);
        chk_pres_marc_eu_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_eu_m);
        chk_pres_marc_eu_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_eu_t);
        chk_pres_marc_eu_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_eu_f);

        chk_pres_marc_eu_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_eu_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_eu_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_eu_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_eu_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_eu_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_eu_na", chk_pres_marc_eu_na);
        mapingPreseciaMarca.put("chk_pres_marc_eu_k", chk_pres_marc_eu_k);
        mapingPreseciaMarca.put("chk_pres_marc_eu_c", chk_pres_marc_eu_c);
        mapingPreseciaMarca.put("chk_pres_marc_eu_m", chk_pres_marc_eu_m);
        mapingPreseciaMarca.put("chk_pres_marc_eu_t", chk_pres_marc_eu_t);
        mapingPreseciaMarca.put("chk_pres_marc_eu_f", chk_pres_marc_eu_f);


        chk_pres_marc_pi_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_pi_na);
        chk_pres_marc_pi_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_pi_k);
        chk_pres_marc_pi_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_pi_c);
        chk_pres_marc_pi_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_pi_m);
        chk_pres_marc_pi_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_pi_t);
        chk_pres_marc_pi_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_pi_f);

        chk_pres_marc_pi_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pi_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pi_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pi_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pi_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pi_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_pi_na", chk_pres_marc_pi_na);
        mapingPreseciaMarca.put("chk_pres_marc_pi_k", chk_pres_marc_pi_k);
        mapingPreseciaMarca.put("chk_pres_marc_pi_c", chk_pres_marc_pi_c);
        mapingPreseciaMarca.put("chk_pres_marc_pi_m", chk_pres_marc_pi_m);
        mapingPreseciaMarca.put("chk_pres_marc_pi_t", chk_pres_marc_pi_t);
        mapingPreseciaMarca.put("chk_pres_marc_pi_f", chk_pres_marc_pi_f);


        chk_pres_marc_pe_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_pe_na);
        chk_pres_marc_pe_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_pe_k);
        chk_pres_marc_pe_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_pe_c);
        chk_pres_marc_pe_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_pe_m);
        chk_pres_marc_pe_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_pe_t);
        chk_pres_marc_pe_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_pe_f);

        chk_pres_marc_pe_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pe_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pe_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pe_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pe_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_pe_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_pe_na", chk_pres_marc_pe_na);
        mapingPreseciaMarca.put("chk_pres_marc_pe_k", chk_pres_marc_pe_k);
        mapingPreseciaMarca.put("chk_pres_marc_pe_c", chk_pres_marc_pe_c);
        mapingPreseciaMarca.put("chk_pres_marc_pe_m", chk_pres_marc_pe_m);
        mapingPreseciaMarca.put("chk_pres_marc_pe_t", chk_pres_marc_pe_t);
        mapingPreseciaMarca.put("chk_pres_marc_pe_f", chk_pres_marc_pe_f);


        chk_pres_marc_rt_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_rt_na);
        chk_pres_marc_rt_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_rt_k);
        chk_pres_marc_rt_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_rt_c);
        chk_pres_marc_rt_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_rt_m);
        chk_pres_marc_rt_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_rt_t);
        chk_pres_marc_rt_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_rt_f);

        chk_pres_marc_rt_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_rt_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_rt_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_rt_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_rt_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_rt_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_rt_na", chk_pres_marc_rt_na);
        mapingPreseciaMarca.put("chk_pres_marc_rt_k", chk_pres_marc_rt_k);
        mapingPreseciaMarca.put("chk_pres_marc_rt_c", chk_pres_marc_rt_c);
        mapingPreseciaMarca.put("chk_pres_marc_rt_m", chk_pres_marc_rt_m);
        mapingPreseciaMarca.put("chk_pres_marc_rt_t", chk_pres_marc_rt_t);
        mapingPreseciaMarca.put("chk_pres_marc_rt_f", chk_pres_marc_rt_f);


        chk_pres_marc_fs_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_fs_na);
        chk_pres_marc_fs_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_fs_k);
        chk_pres_marc_fs_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_fs_c);
        chk_pres_marc_fs_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_fs_m);
        chk_pres_marc_fs_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_fs_t);
        chk_pres_marc_fs_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_fs_f);

        chk_pres_marc_fs_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_fs_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_fs_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_fs_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_fs_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_fs_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_fs_na", chk_pres_marc_fs_na);
        mapingPreseciaMarca.put("chk_pres_marc_fs_k", chk_pres_marc_fs_k);
        mapingPreseciaMarca.put("chk_pres_marc_fs_c", chk_pres_marc_fs_c);
        mapingPreseciaMarca.put("chk_pres_marc_fs_m", chk_pres_marc_fs_m);
        mapingPreseciaMarca.put("chk_pres_marc_fs_t", chk_pres_marc_fs_t);
        mapingPreseciaMarca.put("chk_pres_marc_fs_f", chk_pres_marc_fs_f);


        chk_pres_marc_lo_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_lo_na);
        chk_pres_marc_lo_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_lo_k);
        chk_pres_marc_lo_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_lo_c);
        chk_pres_marc_lo_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_lo_m);
        chk_pres_marc_lo_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_lo_t);
        chk_pres_marc_lo_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_lo_f);

        chk_pres_marc_lo_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_lo_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_lo_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_lo_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_lo_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_lo_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_lo_na", chk_pres_marc_lo_na);
        mapingPreseciaMarca.put("chk_pres_marc_lo_k", chk_pres_marc_lo_k);
        mapingPreseciaMarca.put("chk_pres_marc_lo_c", chk_pres_marc_lo_c);
        mapingPreseciaMarca.put("chk_pres_marc_lo_m", chk_pres_marc_lo_m);
        mapingPreseciaMarca.put("chk_pres_marc_lo_t", chk_pres_marc_lo_t);
        mapingPreseciaMarca.put("chk_pres_marc_lo_f", chk_pres_marc_lo_f);


        chk_pres_marc_ro_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_ro_na);
        chk_pres_marc_ro_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_ro_k);
        chk_pres_marc_ro_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_ro_c);
        chk_pres_marc_ro_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_ro_m);
        chk_pres_marc_ro_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_ro_t);
        chk_pres_marc_ro_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_ro_f);

        chk_pres_marc_ro_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ro_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ro_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ro_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ro_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_ro_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_ro_na", chk_pres_marc_ro_na);
        mapingPreseciaMarca.put("chk_pres_marc_ro_k", chk_pres_marc_ro_k);
        mapingPreseciaMarca.put("chk_pres_marc_ro_c", chk_pres_marc_ro_c);
        mapingPreseciaMarca.put("chk_pres_marc_ro_m", chk_pres_marc_ro_m);
        mapingPreseciaMarca.put("chk_pres_marc_ro_t", chk_pres_marc_ro_t);
        mapingPreseciaMarca.put("chk_pres_marc_ro_f", chk_pres_marc_ro_f);


        chk_pres_marc_mi_na = (CheckBox) view.findViewById(R.id.chk_pres_marc_mi_na);
        chk_pres_marc_mi_k = (CheckBox) view.findViewById(R.id.chk_pres_marc_mi_k);
        chk_pres_marc_mi_c = (CheckBox) view.findViewById(R.id.chk_pres_marc_mi_c);
        chk_pres_marc_mi_m = (CheckBox) view.findViewById(R.id.chk_pres_marc_mi_m);
        chk_pres_marc_mi_t = (CheckBox) view.findViewById(R.id.chk_pres_marc_mi_t);
        chk_pres_marc_mi_f = (CheckBox) view.findViewById(R.id.chk_pres_marc_mi_f);

        chk_pres_marc_mi_na.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_mi_k.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_mi_c.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_mi_m.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_mi_t.setOnCheckedChangeListener(onNASelected);
        chk_pres_marc_mi_f.setOnCheckedChangeListener(onNASelected);

        mapingPreseciaMarca.put("chk_pres_marc_mi_na", chk_pres_marc_mi_na);
        mapingPreseciaMarca.put("chk_pres_marc_mi_k", chk_pres_marc_mi_k);
        mapingPreseciaMarca.put("chk_pres_marc_mi_c", chk_pres_marc_mi_c);
        mapingPreseciaMarca.put("chk_pres_marc_mi_m", chk_pres_marc_mi_m);
        mapingPreseciaMarca.put("chk_pres_marc_mi_t", chk_pres_marc_mi_t);
        mapingPreseciaMarca.put("chk_pres_marc_mi_f", chk_pres_marc_mi_f);
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
                    case R.id.chk_pres_marc_he_na:
                        mapingPreseciaMarca.get("chk_pres_marc_he_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_he_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_he_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_he_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_he_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_hi_na:
                        mapingPreseciaMarca.get("chk_pres_marc_hi_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_hi_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_hi_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_hi_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_hi_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_co_na:
                        mapingPreseciaMarca.get("chk_pres_marc_co_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_co_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_co_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_co_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_co_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_gp_na:
                        mapingPreseciaMarca.get("chk_pres_marc_gp_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gp_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gp_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gp_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gp_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_gm_na:
                        mapingPreseciaMarca.get("chk_pres_marc_gm_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gm_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gm_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gm_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gm_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_gg_na:
                        mapingPreseciaMarca.get("chk_pres_marc_gg_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gg_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gg_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gg_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_gg_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_air_na:
                        mapingPreseciaMarca.get("chk_pres_marc_air_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_air_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_air_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_air_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_air_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_ais_na:
                        mapingPreseciaMarca.get("chk_pres_marc_ais_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ais_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ais_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ais_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ais_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_eu_na:
                        mapingPreseciaMarca.get("chk_pres_marc_eu_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_eu_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_eu_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_eu_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_eu_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_pi_na:
                        mapingPreseciaMarca.get("chk_pres_marc_pi_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pi_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pi_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pi_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pi_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_pe_na:
                        mapingPreseciaMarca.get("chk_pres_marc_pe_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pe_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pe_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pe_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_pe_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_rt_na:
                        mapingPreseciaMarca.get("chk_pres_marc_rt_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_rt_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_rt_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_rt_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_rt_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_fs_na:
                        mapingPreseciaMarca.get("chk_pres_marc_fs_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_fs_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_fs_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_fs_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_fs_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_lo_na:
                        mapingPreseciaMarca.get("chk_pres_marc_lo_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_lo_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_lo_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_lo_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_lo_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_ro_na:
                        mapingPreseciaMarca.get("chk_pres_marc_ro_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ro_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ro_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ro_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_ro_f").setChecked(false);
                        return;
                    case R.id.chk_pres_marc_mi_na:
                        mapingPreseciaMarca.get("chk_pres_marc_mi_k").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_mi_c").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_mi_m").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_mi_t").setChecked(false);
                        mapingPreseciaMarca.get("chk_pres_marc_mi_f").setChecked(false);
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
                if (viewName.contains("he"))
                    mapingPreseciaMarca.get("chk_pres_marc_he_na").setChecked(false);
                else if (viewName.contains("hi"))
                    mapingPreseciaMarca.get("chk_pres_marc_hi_na").setChecked(false);
                else if (viewName.contains("co"))
                    mapingPreseciaMarca.get("chk_pres_marc_co_na").setChecked(false);
                else if (viewName.contains("gp"))
                    mapingPreseciaMarca.get("chk_pres_marc_gp_na").setChecked(false);
                else if (viewName.contains("gm"))
                    mapingPreseciaMarca.get("chk_pres_marc_gm_na").setChecked(false);
                else if (viewName.contains("gg"))
                    mapingPreseciaMarca.get("chk_pres_marc_gg_na").setChecked(false);
                else if (viewName.contains("air"))
                    mapingPreseciaMarca.get("chk_pres_marc_air_na").setChecked(false);
                else if (viewName.contains("ais"))
                    mapingPreseciaMarca.get("chk_pres_marc_ais_na").setChecked(false);
                else if (viewName.contains("eu"))
                    mapingPreseciaMarca.get("chk_pres_marc_eu_na").setChecked(false);
                else if (viewName.contains("pi"))
                    mapingPreseciaMarca.get("chk_pres_marc_pi_na").setChecked(false);
                else if (viewName.contains("pe"))
                    mapingPreseciaMarca.get("chk_pres_marc_pe_na").setChecked(false);
                else if (viewName.contains("rt"))
                    mapingPreseciaMarca.get("chk_pres_marc_rt_na").setChecked(false);
                else if (viewName.contains("fs"))
                    mapingPreseciaMarca.get("chk_pres_marc_fs_na").setChecked(false);
                else if (viewName.contains("lo"))
                    mapingPreseciaMarca.get("chk_pres_marc_lo_na").setChecked(false);
                else if (viewName.contains("ro"))
                    mapingPreseciaMarca.get("chk_pres_marc_ro_na").setChecked(false);
                else if (viewName.contains("mi"))
                    mapingPreseciaMarca.get("chk_pres_marc_mi_na").setChecked(false);
            }
        }
    };

    /**
     * Guarda la lista de opciones marcadas para la presencia de marca.
     * Recorre opción por opcion y dentro de cada una recorre operador por operador.
     * Se tiene una plantilla para los nombres de los check: chk_pres_marc_*OPC_*OPER
     * En cada iteración se cambia "*OPC" por la opción y "*OPER" por el operador.
     * Cuando se arma el nombre, se busca en el map de checks. Si el check está marcado se agrega
     * a un jsonarray cada operador, este array se pone como valor del objeto json de su
     * respectiva opción.
     */
    private void savePresenciaMarca() {
        String variableOption = "";
        String variableOperator = "";
        String currentOption = "";

        //Creación del objeto JSON:
        JSONArray arrayOpciones = new JSONArray();
        JSONObject objectOption;

        for (int opcionCont = 0; opcionCont < 16; opcionCont++) {
            variableOption = Constantes.CHK_PRES_MARCA_FORMAT;
            JSONArray arrayOperators = new JSONArray();
            objectOption = new JSONObject();

            switch (opcionCont) {
                case 0:
                    currentOption = "he";
                    variableOption = variableOption.replace("*OPC", "he");
                    break;
                case 1:
                    currentOption = "hi";
                    variableOption = variableOption.replace("*OPC", "hi");
                    break;
                case 2:
                    currentOption = "co";
                    variableOption = variableOption.replace("*OPC", "co");
                    break;
                case 3:
                    currentOption = "gp";
                    variableOption = variableOption.replace("*OPC", "gp");
                    break;
                case 4:
                    currentOption = "gm";
                    variableOption = variableOption.replace("*OPC", "gm");
                    break;
                case 5:
                    currentOption = "gg";
                    variableOption = variableOption.replace("*OPC", "gg");
                    break;
                case 6:
                    currentOption = "air";
                    variableOption = variableOption.replace("*OPC", "air");
                    break;
                case 7:
                    currentOption = "ais";
                    variableOption = variableOption.replace("*OPC", "ais");
                    break;
                case 8:
                    currentOption = "eu";
                    variableOption = variableOption.replace("*OPC", "eu");
                    break;
                case 9:
                    currentOption = "pi";
                    variableOption = variableOption.replace("*OPC", "pi");
                    break;
                case 10:
                    currentOption = "pe";
                    variableOption = variableOption.replace("*OPC", "pe");
                    break;
                case 11:
                    currentOption = "rt";
                    variableOption = variableOption.replace("*OPC", "rt");
                    break;
                case 12:
                    currentOption = "fs";
                    variableOption = variableOption.replace("*OPC", "fs");
                    break;
                case 13:
                    currentOption = "lo";
                    variableOption = variableOption.replace("*OPC", "lo");
                    break;
                case 14:
                    currentOption = "ro";
                    variableOption = variableOption.replace("*OPC", "ro");
                    break;
                case 15:
                    currentOption = "mi";
                    variableOption = variableOption.replace("*OPC", "mi");
                    break;
            }
            for (int operadorCont = 0; operadorCont < 6; operadorCont++) {
                variableOperator = variableOption;
                try {
                    switch (operadorCont) {
                        case (0): //N/A
                            variableOperator = variableOperator.replace("*OPER", Constantes.NA_OPTION);
                            if (mapingPreseciaMarca.get(variableOperator).isChecked()) {
                                arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.NA_OPTION));
                                continue;
                            }
                            break;
                        case (1): // kolbi
                            variableOperator = variableOperator.replace("*OPER", Constantes.KOLBI_OPTION);
                            if (mapingPreseciaMarca.get(variableOperator).isChecked())
                                arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.KOLBI_OPTION));
                            break;
                        case (2): // Claro
                            variableOperator = variableOperator.replace("*OPER", Constantes.CLARO_OPTION);
                            if (mapingPreseciaMarca.get(variableOperator).isChecked())
                                arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.CLARO_OPTION));
                            break;
                        case (3): //Movistar
                            variableOperator = variableOperator.replace("*OPER", Constantes.MOVISTAR_OPTION);
                            if (mapingPreseciaMarca.get(variableOperator).isChecked())
                                arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.MOVISTAR_OPTION));
                            break;
                        case (4): // Tuyo
                            variableOperator = variableOperator.replace("*OPER", Constantes.TUYO_OPTION);
                            if (mapingPreseciaMarca.get(variableOperator).isChecked())
                                arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.TUYO_OPTION));
                            break;
                        case (5): // Full
                            variableOperator = variableOperator.replace("*OPER", Constantes.FULL_OPTION);
                            if (mapingPreseciaMarca.get(variableOperator).isChecked())
                                arrayOperators.put(JSONHelper.getInstance().obtenerJsonOperador(Constantes.FULL_OPTION));
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
            JSONObject jsonPresMarca = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, Constantes.PRES_MARCA);
            int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
            SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonPresMarca.toString(), currentUser, Constantes.PRES_MARCA);
            Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Función para cargar las opciones de presencia de marca previamente cargada para un determinado negocio.
     */
    private void loadPresenciaMarca() {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, Constantes.PRES_MARCA);
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, Constantes.PRES_MARCA);

                for (String[] option : jsonsList) {
                    String strVariableNameTemp = Constantes.CHK_PRES_MARCA_FORMAT.replace("*OPC", option[0]);

                    String[] operators = option[1].split(";");
                    for (String operatorInitial : operators) {
                        String opcion = JSONHelper.getInstance().descifrarOpcion(operatorInitial);
                        String strVariableWithOperator = strVariableNameTemp.replace("*OPER", opcion);
                        mapingPreseciaMarca.get(strVariableWithOperator).setChecked(true);
                    }
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }

    /************************************************************************************/

    /********************************************************************
     *************************** PUBLICIDAD *****************************
     ********************************************************************/
    /**
     * Se mapean todos los checks de presencia de marca y se insertan en un hashmap.
     *
     * @param view
     */
    private void mapTextsPublicidad(Dialog view) {

        mapingPubProPop = new HashMap<String, EditText>();

        EditText et_publicidad_he, et_publicidad_hi, et_publicidad_co, et_publicidad_gp, et_publicidad_gm,
                et_publicidad_gg, et_publicidad_la, et_publicidad_pu, et_publicidad_pa, et_publicidad_tr, et_publicidad_ca,
                et_publicidad_ap, et_publicidad_vo, et_publicidad_pb, et_publicidad_bp, et_publicidad_pt;

        chkGenericPublicidadPromocional = (CheckBox) view.findViewById(R.id.chk_publicidad_pop);
        rbGenericPublicidadProm1 = (RadioButton) view.findViewById(R.id.rb_publicidad_np);
        rbGenericPublicidadProm2 = (RadioButton) view.findViewById(R.id.rb_publicidad_cp);
        rbGenericPublicidadProm3 = (RadioButton) view.findViewById(R.id.rb_publicidad_nap);

        et_publicidad_he = (EditText) view.findViewById(R.id.et_publicidad_he);
        et_publicidad_hi = (EditText) view.findViewById(R.id.et_publicidad_hi);
        et_publicidad_co = (EditText) view.findViewById(R.id.et_publicidad_co);
        et_publicidad_gp = (EditText) view.findViewById(R.id.et_publicidad_gp);
        et_publicidad_gm = (EditText) view.findViewById(R.id.et_publicidad_gm);
        et_publicidad_gg = (EditText) view.findViewById(R.id.et_publicidad_gg);
        et_publicidad_la = (EditText) view.findViewById(R.id.et_publicidad_la);
        et_publicidad_pu = (EditText) view.findViewById(R.id.et_publicidad_pu);
        et_publicidad_pa = (EditText) view.findViewById(R.id.et_publicidad_pa);
        et_publicidad_tr = (EditText) view.findViewById(R.id.et_publicidad_tr);
        et_publicidad_ca = (EditText) view.findViewById(R.id.et_publicidad_ca);
        et_publicidad_ap = (EditText) view.findViewById(R.id.et_publicidad_ap);
        et_publicidad_vo = (EditText) view.findViewById(R.id.et_publicidad_vo);
        et_publicidad_pb = (EditText) view.findViewById(R.id.et_publicidad_pb);
        et_publicidad_bp = (EditText) view.findViewById(R.id.et_publicidad_bp);
        et_publicidad_pt = (EditText) view.findViewById(R.id.et_publicidad_pt);

        mapingPubProPop.put("et_publicidad_he", et_publicidad_he);
        mapingPubProPop.put("et_publicidad_hi", et_publicidad_hi);
        mapingPubProPop.put("et_publicidad_co", et_publicidad_co);
        mapingPubProPop.put("et_publicidad_gp", et_publicidad_gp);
        mapingPubProPop.put("et_publicidad_gm", et_publicidad_gm);
        mapingPubProPop.put("et_publicidad_gg", et_publicidad_gg);
        mapingPubProPop.put("et_publicidad_la", et_publicidad_la);
        mapingPubProPop.put("et_publicidad_pu", et_publicidad_pu);
        mapingPubProPop.put("et_publicidad_pa", et_publicidad_pa);
        mapingPubProPop.put("et_publicidad_tr", et_publicidad_tr);
        mapingPubProPop.put("et_publicidad_ca", et_publicidad_ca);
        mapingPubProPop.put("et_publicidad_ap", et_publicidad_ap);
        mapingPubProPop.put("et_publicidad_vo", et_publicidad_vo);
        mapingPubProPop.put("et_publicidad_pb", et_publicidad_pb);
        mapingPubProPop.put("et_publicidad_bp", et_publicidad_bp);
        mapingPubProPop.put("et_publicidad_pt", et_publicidad_pt);

        llOptions = (LinearLayout)view.findViewById(R.id.ll_options);
        chkGenericPublicidadPromocional.setOnCheckedChangeListener(onCheckClick);
    }

    /**
     * Función para cargar las opciones de publicidad, promoción o pop Especial previamente cargada para un determinado negocio.
     *
     * @param type tipo de información a cargar (Publicidad, Promoción, Pop Especial u observaciones)
     */
    private void loadPubPromPopObs(String type) {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, type);
            boolean isOptionSelected = false; //Indica si hay alguna opción seleccionada en los paneles
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, type);

                for (String[] option : jsonsList) {
                    if (option[0].equals("pop") || option[0].equals("mat")) //Check de instalación de material pop (Publicidad) o promocional
                        chkGenericPublicidadPromocional.setChecked(true);

                    else if (option[0].equals("mimp")) { //radioButton de motivo de instalación de material pop o promocional
                        if (option[1].equals("1"))
                            rbGenericPublicidadProm1.setChecked(true);
                        else if (option[1].equals("2"))
                            rbGenericPublicidadProm2.setChecked(true);
                        else
                            rbGenericPublicidadProm3.setChecked(true);
                        isOptionSelected = true;
                    } else if (option[0].equals("stat")) { // Opción del spinner de popEspecial
                        ArrayAdapter<Parametro> adapter = (ArrayAdapter<Parametro>) spnPopInsall.getAdapter();
                        int spinnerPosition = adapter.getPosition(new Parametro(Integer.parseInt(option[1]), "", ""));
                        spnPopInsall.setSelection(spinnerPosition, false);
                    } else {
                        String strVariableNameTemp = Constantes.ET_PUBLICIDAD_FORMAT;
                        if (type.equals(Constantes.PROMOCIONAL))
                            strVariableNameTemp = Constantes.ET_PROMOCIONAL_FORMAT;
                        else if (type.equals(Constantes.POP_ESPECIAL))
                            strVariableNameTemp = Constantes.ET_POP_FORMAT;
                        else if (type.equals(Constantes.OBSERVACIONES))
                            strVariableNameTemp = "et_observ_";
                        mapingPubProPop.get(strVariableNameTemp + option[0]).setText(option[1]);
                        isOptionSelected = true;
                    }
                }
            }
            if (!type.equals(Constantes.OBSERVACIONES))
                enableDialogOptions(isOptionSelected, type);
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Guarda el objeto Json con la publicidad, promoción, pop especial u observación
     * Se guardan primero el check y el radioButton seleccionado (si están marcados) ya que estos no cumplen
     * con un nombre estándar.
     * Para los edittext se creó un nombre genérico: et_publicidad_xxxx. El nombre se completa
     * en un ciclo que asigna las siglas que completan el nombre de cada edittext.
     * Cuando el nombre está "armado", se busca en el arreglo de edittext y se toma el valor
     * (Si lo tiene) y se guarda en un jsonArray el objeto Json.
     */
    private void savePubPromPopObs(String type) {

        //Creación del objeto JSON:
        JSONArray arrayOpciones = new JSONArray();
        JSONObject objectOption = new JSONObject();

        try {
            //Guarda el valor del check de material pop (si fue checado)
            if (!type.equals(Constantes.OBSERVACIONES) && chkGenericPublicidadPromocional.isChecked()) {
                String tag = "pop";
                if (type.equals(Constantes.PROMOCIONAL) || type.equals(Constantes.POP_ESPECIAL))
                    tag = "mat";
                objectOption.put(tag, "1");
                arrayOpciones.put(objectOption);
            }
            //Guarda el valor del radio button marcado
            if (!type.equals(Constantes.POP_ESPECIAL) && !type.equals(Constantes.OBSERVACIONES)) {
                objectOption = new JSONObject();
                if (rbGenericPublicidadProm1.isChecked()) {
                    objectOption.put("mimp", "1");
                    arrayOpciones.put(objectOption);
                } else if (rbGenericPublicidadProm2.isChecked()) {
                    objectOption.put("mimp", "2");
                    arrayOpciones.put(objectOption);
                } else if (rbGenericPublicidadProm3.isChecked()) {
                    objectOption.put("mimp", "3");
                    arrayOpciones.put(objectOption);
                }
            }
            //Se guarda la selección del spinner
            if (type.equals(Constantes.POP_ESPECIAL)) {
                Parametro selectedStatus = (Parametro) spnPopInsall.getSelectedItem();
                objectOption = new JSONObject();
                objectOption.put("stat", Integer.toString(selectedStatus.getCodigo()));
                arrayOpciones.put(objectOption);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (type.equals(Constantes.PUBLICIDAD))
            addPublicidadOptionsJson(arrayOpciones);
        else if (type.equals(Constantes.PROMOCIONAL))
            addPromocionalOptionsJson(arrayOpciones);
        else if (type.equals(Constantes.POP_ESPECIAL))
            addPopOptionsJson(arrayOpciones);
        else
            addObservacionesOptionsJson(arrayOpciones);

        try {
            JSONObject jsonPresMarca = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, type);
            int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
            SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonPresMarca.toString(), currentUser, type);
            Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Función auxiliar que agrega los valores ingresados en los edittext de la publicidad.
     *
     * @param pArrayOpciones
     */
    private void addPublicidadOptionsJson(JSONArray pArrayOpciones) {
        String variableOption = "";
        String currentOption = "";
        JSONObject objectOption;
        for (int opcionCont = 0; opcionCont < 16; opcionCont++) {
            objectOption = new JSONObject();
            switch (opcionCont) {
                case 0:
                    currentOption = "he";
                    break;
                case 1:
                    currentOption = "hi";
                    break;
                case 2:
                    currentOption = "co";
                    break;
                case 3:
                    currentOption = "gp";
                    break;
                case 4:
                    currentOption = "gm";
                    break;
                case 5:
                    currentOption = "gg";
                    break;
                case 6:
                    currentOption = "la";
                    break;
                case 7:
                    currentOption = "pu";
                    break;
                case 8:
                    currentOption = "pa";
                    break;
                case 9:
                    currentOption = "tr";
                    break;
                case 10:
                    currentOption = "ca";
                    break;
                case 11:
                    currentOption = "ap";
                    break;
                case 12:
                    currentOption = "vo";
                    break;
                case 13:
                    currentOption = "pb";
                    break;
                case 14:
                    currentOption = "bp";
                    break;
                case 15:
                    currentOption = "pt";
                    break;
            }
            try {
                variableOption = mapingPubProPop.get(Constantes.ET_PUBLICIDAD_FORMAT + currentOption).getText().toString();
                if (!variableOption.equals("")) {
                    objectOption.put(currentOption, variableOption);
                    pArrayOpciones.put(objectOption);
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*****************************************************************************/

    /*****************************************************************************/
    /***************************** PROMOCIONAL ***********************************/
    /*****************************************************************************/

    private void mapTextsPromocional(Dialog view) {

        mapingPubProPop = new HashMap<String, EditText>();

        EditText et_prom_laz, et_prom_lap, et_prom_cua, et_prom_lk, et_prom_reg,
                et_prom_sal, et_prom_ant, et_prom_car, et_prom_vis, et_prom_bae,
                et_prom_cv, et_prom_bot, et_prom_cap, et_prom_som, et_prom_vt,
                et_prom_gor, et_prom_cam, et_prom_cha, et_prom_lla, et_prom_bol,
                et_prom_usb, et_prom_carg, et_prom_em, et_prom_et, et_prom_cara,
                et_prom_bf, et_prom_camp;

        chkGenericPublicidadPromocional = (CheckBox) view.findViewById(R.id.chk_prom_mat);
        rbGenericPublicidadProm1 = (RadioButton) view.findViewById(R.id.rb_prom_rc);
        rbGenericPublicidadProm2 = (RadioButton) view.findViewById(R.id.rb_prom_rpdv);
        rbGenericPublicidadProm3 = (RadioButton) view.findViewById(R.id.rb_prom_ev);

        et_prom_laz = (EditText) view.findViewById(R.id.et_prom_laz);
        et_prom_lap = (EditText) view.findViewById(R.id.et_prom_lap);
        et_prom_cua = (EditText) view.findViewById(R.id.et_prom_cua);
        et_prom_lk = (EditText) view.findViewById(R.id.et_prom_lk);
        et_prom_reg = (EditText) view.findViewById(R.id.et_prom_reg);
        et_prom_sal = (EditText) view.findViewById(R.id.et_prom_sal);
        et_prom_ant = (EditText) view.findViewById(R.id.et_prom_ant);
        et_prom_car = (EditText) view.findViewById(R.id.et_prom_car);
        et_prom_vis = (EditText) view.findViewById(R.id.et_prom_vis);
        et_prom_bae = (EditText) view.findViewById(R.id.et_prom_bae);
        et_prom_cv = (EditText) view.findViewById(R.id.et_prom_cv);
        et_prom_bot = (EditText) view.findViewById(R.id.et_prom_bot);
        et_prom_cap = (EditText) view.findViewById(R.id.et_prom_cap);
        et_prom_som = (EditText) view.findViewById(R.id.et_prom_som);
        et_prom_vt = (EditText) view.findViewById(R.id.et_prom_vt);
        et_prom_gor = (EditText) view.findViewById(R.id.et_prom_gor);
        et_prom_cam = (EditText) view.findViewById(R.id.et_prom_cam);
        et_prom_cha = (EditText) view.findViewById(R.id.et_prom_cha);
        et_prom_lla = (EditText) view.findViewById(R.id.et_prom_lla);
        et_prom_bol = (EditText) view.findViewById(R.id.et_prom_bol);
        et_prom_usb = (EditText) view.findViewById(R.id.et_prom_usb);
        et_prom_carg = (EditText) view.findViewById(R.id.et_prom_carg);
        et_prom_em = (EditText) view.findViewById(R.id.et_prom_em);
        et_prom_et = (EditText) view.findViewById(R.id.et_prom_et);
        et_prom_cara = (EditText) view.findViewById(R.id.et_prom_cara);
        et_prom_bf = (EditText) view.findViewById(R.id.et_prom_bf);
        et_prom_camp = (EditText) view.findViewById(R.id.et_prom_camp);

        mapingPubProPop.put("et_prom_laz", et_prom_laz);
        mapingPubProPop.put("et_prom_lap", et_prom_lap);
        mapingPubProPop.put("et_prom_cua", et_prom_cua);
        mapingPubProPop.put("et_prom_lk", et_prom_lk);
        mapingPubProPop.put("et_prom_reg", et_prom_reg);
        mapingPubProPop.put("et_prom_sal", et_prom_sal);
        mapingPubProPop.put("et_prom_ant", et_prom_ant);
        mapingPubProPop.put("et_prom_car", et_prom_car);
        mapingPubProPop.put("et_prom_vis", et_prom_vis);
        mapingPubProPop.put("et_prom_bae", et_prom_bae);
        mapingPubProPop.put("et_prom_cv", et_prom_cv);
        mapingPubProPop.put("et_prom_bot", et_prom_bot);
        mapingPubProPop.put("et_prom_cap", et_prom_cap);
        mapingPubProPop.put("et_prom_som", et_prom_som);
        mapingPubProPop.put("et_prom_vt", et_prom_vt);
        mapingPubProPop.put("et_prom_gor", et_prom_gor);
        mapingPubProPop.put("et_prom_cam", et_prom_cam);
        mapingPubProPop.put("et_prom_cha", et_prom_cha);
        mapingPubProPop.put("et_prom_lla", et_prom_lla);
        mapingPubProPop.put("et_prom_bol", et_prom_bol);
        mapingPubProPop.put("et_prom_usb", et_prom_usb);
        mapingPubProPop.put("et_prom_carg", et_prom_carg);
        mapingPubProPop.put("et_prom_em", et_prom_em);
        mapingPubProPop.put("et_prom_et", et_prom_et);
        mapingPubProPop.put("et_prom_cara", et_prom_cara);
        mapingPubProPop.put("et_prom_bf", et_prom_bf);
        mapingPubProPop.put("et_prom_camp", et_prom_camp);

        llOptions = (LinearLayout)view.findViewById(R.id.ll_options);
        chkGenericPublicidadPromocional.setOnCheckedChangeListener(onCheckClick);
    }

    /**
     * Función auxiliar que agrega los valores ingresados en los edittext de promocional.
     *
     * @param pArrayOpciones
     */
    private void addPromocionalOptionsJson(JSONArray pArrayOpciones) {
        String variableOption = "";
        String currentOption = "";
        JSONObject objectOption;
        for (int opcionCont = 0; opcionCont < 27; opcionCont++) {
            objectOption = new JSONObject();
            switch (opcionCont) {
                case 0:
                    currentOption = "laz";
                    break;
                case 1:
                    currentOption = "lap";
                    break;
                case 2:
                    currentOption = "cua";
                    break;
                case 3:
                    currentOption = "lk";
                    break;
                case 4:
                    currentOption = "reg";
                    break;
                case 5:
                    currentOption = "sal";
                    break;
                case 6:
                    currentOption = "ant";
                    break;
                case 7:
                    currentOption = "car";
                    break;
                case 8:
                    currentOption = "vis";
                    break;
                case 9:
                    currentOption = "bae";
                    break;
                case 10:
                    currentOption = "cv";
                    break;
                case 11:
                    currentOption = "bot";
                    break;
                case 12:
                    currentOption = "cap";
                    break;
                case 13:
                    currentOption = "som";
                    break;
                case 14:
                    currentOption = "vt";
                    break;
                case 15:
                    currentOption = "gor";
                    break;
                case 16:
                    currentOption = "cam";
                    break;
                case 17:
                    currentOption = "cha";
                    break;
                case 18:
                    currentOption = "lla";
                    break;
                case 19:
                    currentOption = "bol";
                    break;
                case 20:
                    currentOption = "usb";
                    break;
                case 21:
                    currentOption = "carg";
                    break;
                case 22:
                    currentOption = "em";
                    break;
                case 23:
                    currentOption = "et";
                    break;
                case 24:
                    currentOption = "cara";
                    break;
                case 25:
                    currentOption = "bf";
                    break;
                case 26:
                    currentOption = "camp";
                    break;
            }
            try {
                variableOption = mapingPubProPop.get(Constantes.ET_PROMOCIONAL_FORMAT + currentOption).getText().toString();
                if (!variableOption.equals("")) {
                    objectOption.put(currentOption, variableOption);
                    pArrayOpciones.put(objectOption);
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*******************************************************************/

    /*************************** Pop Especial **************************/
    /*******************************************************************/
    private void mapTextsPop(Dialog view) {

        mapingPubProPop = new HashMap<String, EditText>();

        EditText et_pop_especial_rt, et_pop_especial_lm, et_pop_especial_mi;

        chkGenericPublicidadPromocional = (CheckBox) view.findViewById(R.id.chk_pop_especial_mat);

        //Cargar el spinner
        spnPopInsall = (Spinner) view.findViewById(R.id.spn_pop_esp_st);

        statusList = new ArrayList<>();
        statusList.add(new Parametro(1, "Pendiente", ""));
        statusList.add(new Parametro(2, "Atendido", ""));
        statusList.add(new Parametro(3, "Rechazado", ""));

        ArrayAdapter<Parametro> spinnerAdapter = new ArrayAdapter<Parametro>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, statusList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPopInsall.setAdapter(spinnerAdapter);
        spnPopInsall.setOnItemSelectedListener(onItemSelected);

        et_pop_especial_rt = (EditText) view.findViewById(R.id.et_pop_especial_rt);
        et_pop_especial_lm = (EditText) view.findViewById(R.id.et_pop_especial_lm);
        et_pop_especial_mi = (EditText) view.findViewById(R.id.et_pop_especial_mi);

        mapingPubProPop.put("et_pop_especial_rt", et_pop_especial_rt);
        mapingPubProPop.put("et_pop_especial_lm", et_pop_especial_lm);
        mapingPubProPop.put("et_pop_especial_mi", et_pop_especial_mi);

        llOptions = (LinearLayout)view.findViewById(R.id.ll_options);
        chkGenericPublicidadPromocional.setOnCheckedChangeListener(onCheckClick);
    }

    /**
     * Cambia el color del spinner de pop especial a negro
     */
    private AdapterView.OnItemSelectedListener onItemSelected = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        }
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * Función auxiliar que agrega los valores ingresados en los edittext de Pop Especial.
     *
     * @param pArrayOpciones
     */
    private void addPopOptionsJson(JSONArray pArrayOpciones) {
        String variableOption = "";
        String currentOption = "";
        JSONObject objectOption;
        for (int opcionCont = 0; opcionCont < 3; opcionCont++) {
            objectOption = new JSONObject();
            switch (opcionCont) {
                case 0:
                    currentOption = "rt";
                    break;
                case 1:
                    currentOption = "lm";
                    break;
                case 2:
                    currentOption = "mi";
                    break;
            }
            try {
                variableOption = mapingPubProPop.get(Constantes.ET_POP_FORMAT + currentOption).getText().toString();
                if (!variableOption.equals("")) {
                    objectOption.put(currentOption, variableOption);
                    pArrayOpciones.put(objectOption);
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*************************************************************/

    /********************* Observaciones *************************/
    /*************************************************************/
    private void mapTextObservaciones(Dialog view) {
        mapingPubProPop = new HashMap<String, EditText>();
        EditText et_observ_obs = (EditText) view.findViewById(R.id.et_observ_obs);
        mapingPubProPop.put("et_observ_obs", et_observ_obs);
    }

    /**
     * Función auxiliar que agrega los valores ingresados en el edittext de observaciones.
     *
     * @param pArrayOpciones
     */
    private void addObservacionesOptionsJson(JSONArray pArrayOpciones) {
        String variableOption = "";
        JSONObject objectOption = new JSONObject();
        try {
            variableOption = mapingPubProPop.get("et_observ_obs").getText().toString();
            if (!variableOption.equals("")) {
                objectOption.put("obs", variableOption);
                pArrayOpciones.put(objectOption);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra/oculta los paneles que dependen de un check
     * @param isEnabled
     * @param type
     */
    private void enableDialogOptions(boolean isEnabled, String type){
        if(isEnabled){
            llOptions.setVisibility(View.VISIBLE);
        }
        else {
            llOptions.setVisibility(View.GONE);
            SupervTercerosImplementor.getInstance().borrarSuperviciones(negocioId, type);
            //Se limpian todos los campos
            /*for(Map.Entry<String, EditText> entry : mapingPubProPop.entrySet()) {
                EditText value = entry.getValue();
                value.setText("");
            }*/
        }
    }

    /**
     * Acción del check de material pop / promocional
     */
    private CompoundButton.OnCheckedChangeListener onCheckClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableDialogOptions(b, formType);
        }
    };
}