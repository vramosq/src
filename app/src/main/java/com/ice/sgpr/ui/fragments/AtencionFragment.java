package com.ice.sgpr.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
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
import java.util.Locale;

/**
 * Fragment para el tab de atención de terceros.
 */
public class AtencionFragment extends SgprFragment {

    TextView txtDate, txtInitTime, txtFinishTime, tvAtencionInfo;
    Spinner spnSupervisor,spnAssignedTo;
    EditText etObservations;
    CheckBox chkRegisterPen;
    boolean infoCargada; //Indica si la info fue cargada para no modificar la hora fin.

    private long negocioId;

    public AtencionFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            negocioId = getArguments().getLong(SupervisionTercerosActivity.NEGOCIO_ID);
        }
        infoCargada = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atencion, container, false);
        int negocioActivoId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
        Date fechaHora = BitacoraImplementor.getInstance().obtenerFechaNegocio(negocioActivoId);

        txtDate = (TextView) view.findViewById(R.id.txt_atencion_fecha);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText(dateFormat.format(fechaHora));

        txtInitTime = (TextView) view.findViewById(R.id.txt_atencion_hora_ini);
        txtInitTime.setText(fechaHora.getHours() + ":" +fechaHora.getMinutes() + ":" + fechaHora.getSeconds());

        txtFinishTime = (TextView) view.findViewById(R.id.txt_atencion_hora_fin);

        spnSupervisor = (Spinner)view.findViewById(R.id.spn_atencion_supervisa);
        List<Parametro> paramsList = UsersTercerosImplementor.getInstance().obtenerUsuariosTerceros();
        cargarSpinner(paramsList, spnSupervisor, true);

        spnAssignedTo = (Spinner)view.findViewById(R.id.spn_atencion_asignar);
        List<Parametro> paramsList2 = UsersTercerosImplementor.getInstance().obtenerUsuariosTerceros();
        paramsList2.add(0, new Parametro(-1, "- Seleccione -", ""));
        cargarSpinner(paramsList2, spnAssignedTo, false);

        chkRegisterPen = (CheckBox)view.findViewById(R.id.chk_atencion_rp);

        etObservations = (EditText)view.findViewById(R.id.et_atencion_obs);

        Button btnSave = (Button)view.findViewById(R.id.btn_atencion_save);
        btnSave.setOnClickListener(onSave);

        tvAtencionInfo = (TextView) view.findViewById(R.id.atencion_info);

        loadIncidences();
        return view;
    }


    /**
     * Listener del botón de guardar.
     * Obtiene los datos ingresados por el usuario, los guarda en un objeto json y los guarda en
     * la BD del dispositivo.
     */
    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Parametro supervisor = (Parametro) spnSupervisor.getSelectedItem();
            Parametro assignedTo = (Parametro) spnAssignedTo.getSelectedItem();
            Date currentDate = new Date();
            String finishTime;
            if(!infoCargada) {
                finishTime = currentDate.getHours() + ":" + currentDate.getMinutes() + ":" + currentDate.getSeconds();
                txtFinishTime.setText(finishTime);
                infoCargada = true;
            }

            String date = txtDate.getText().toString();
            String initTime = txtInitTime.getText().toString();

            finishTime = txtFinishTime.getText().toString();
            String observaciones = etObservations.getText().toString();

            //Creación del objeto JSON:
            JSONArray arrayOpciones = new JSONArray();
            JSONObject objectOption;
            try {
                //Fecha:
                objectOption = new JSONObject();
                objectOption.put("fe", date);
                arrayOpciones.put(objectOption);

                //hora inicio
                objectOption = new JSONObject();
                objectOption.put("hi", initTime);
                arrayOpciones.put(objectOption);

                //hora fin
                objectOption = new JSONObject();
                objectOption.put("hf", finishTime);
                arrayOpciones.put(objectOption);

                //Supervisa
                objectOption = new JSONObject();
                objectOption.put("sup", supervisor.getCodigo());
                arrayOpciones.put(objectOption);

                //Asignado a
                objectOption = new JSONObject();
                objectOption.put("aa", assignedTo.getCodigo());
                arrayOpciones.put(objectOption);

                //Registrar pendiente
                objectOption = new JSONObject();
                if (chkRegisterPen.isChecked())
                    objectOption.put("rp", "1");
                else
                    objectOption.put("rp", "0");
                arrayOpciones.put(objectOption);

                //Observaciones
                if (!observaciones.equals("")) {
                    objectOption = new JSONObject();
                    objectOption.put("obs", observaciones);
                    arrayOpciones.put(objectOption);
                }

                //Usuario que guarda:
                String[] userInfo = UsuariosImplementor.getInstance().obtenerUsuarioLogueado();
                objectOption = new JSONObject();
                objectOption.put("us", Integer.parseInt(userInfo[0]));
                arrayOpciones.put(objectOption);

                JSONObject jsonAttention = JSONHelper.getInstance().obtenerJsonOpcionesOperadores(arrayOpciones, Constantes.ATENCION);
                int currentUser = Integer.parseInt(UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0]);
                SupervTercerosImplementor.getInstance().insertarNuevaSupervision(negocioId, jsonAttention.toString(), currentUser, Constantes.ATENCION);
                Toast.makeText(getActivity(), Constantes.SAVED_INFO, Toast.LENGTH_SHORT).show();
                tvAtencionInfo.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                Toast.makeText(getActivity(), Constantes.ERROR_JSON_TERCEROS, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void loadIncidences() {
        try {
            String savedJson = SupervTercerosImplementor.getInstance().obtenerJsonInfo(negocioId, Constantes.ATENCION);
            if (savedJson != null) {
                ArrayList<String[]> jsonsList = JSONHelper.getInstance().obtenerInfoSupervTerceros(savedJson, Constantes.ATENCION);
                infoCargada = true;
                tvAtencionInfo.setVisibility(View.VISIBLE);
                //String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                for (String[] option : jsonsList) {
                    if (option[0].equals("fe")) {
                        txtDate.getText();
                    }

                    if (option[0].equals("hi")) {
                        txtInitTime.setText(option[1]);
                    }
                    else if (option[0].equals("hf")) {
                        txtFinishTime.setText(option[1]);
                    }
                    else if (option[0].equals("sup")) {
                        spnSupervisor.setSelection(getSpinnerPosition(option[1], spnSupervisor), false);
                    }
                    else if (option[0].equals("aa")) {
                        spnAssignedTo.setSelection(getSpinnerPosition(option[1], spnAssignedTo), false);
                    }
                    else if (option[0].equals("rp")) {
                        chkRegisterPen.setChecked(option[1].equals("0")? false : true);
                    }
                    else if (option[0].equals("obs")) {
                        etObservations.setText(option[1]);
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
     * @return
     */
    private int getSpinnerPosition(String pOption, Spinner spinner){
        ArrayAdapter<Parametro> adapter = (ArrayAdapter<Parametro>) spinner.getAdapter();
        return adapter.getPosition(new Parametro(Integer.parseInt(pOption), "", ""));
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
                    spnSupervisor.setSelection(spinnerAdapter.getPosition(user));
                    break;
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
