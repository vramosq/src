package com.ice.sgpr.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;

/**
 * Fragment que muestra un par de botones para seleccionar el formulario a completar
 * Solo le aparece a los administradores.
 * @author eperaza
 * @since 09.04.15
 */
public class ObservacionesFragment extends SgprFragment{

	private EditText _etObservaciones;
	int _negocioActivoId;
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		View view = inflater.inflate(R.layout.fragment_observaciones, container, false);
		Button btnGuardar = (Button) view.findViewById(R.id.btn_fin_observaciones);
		_etObservaciones = (EditText) view.findViewById(R.id.et_observaciones);
		Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		if(negocioActivo != null){
			_negocioActivoId = negocioActivo.get_nNegocioId();
			TextView txtNombreNegocio = (TextView) view.findViewById(R.id.tv_nombre_negocio_obs);
			txtNombreNegocio.setText(negocioActivo.get_sNombre());
			String observaciones = NegociosImplementor.getInstance().obtenerObservaciones(negocioActivo.get_nNegocioId());
			_etObservaciones.setText(observaciones);
			btnGuardar.setOnClickListener(onGuardar);
		}
		else{
			Toast.makeText(getActivity(), "Debe seleccionar un negocio primero...", Toast.LENGTH_SHORT).show();
			_etObservaciones.setEnabled(false);
			btnGuardar.setEnabled(false);
		}
		String sRolUsuarioLoguedo = UsuariosImplementor.getInstance().obtenerRolUsuarioLogueado();
		
		if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_NORMAL))
			btnGuardar.setEnabled(false);
		return view;
	 }
	
	/**
	 * Listener para el boton que guarda la observacion.
	 */
	OnClickListener onGuardar = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String textoObservaciones = _etObservaciones.getText().toString();
			NegociosImplementor.getInstance().agregarObservaciones(textoObservaciones, _negocioActivoId);
			Toast.makeText(getActivity(), "Observación guardada.", Toast.LENGTH_SHORT).show();
		}
	};
}
