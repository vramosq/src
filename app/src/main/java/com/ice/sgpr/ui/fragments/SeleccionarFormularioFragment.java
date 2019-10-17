package com.ice.sgpr.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.ui.activities.NegociosActivity;
import com.ice.sgpr.ui.activities.SupervisionTercerosActivity;

/**
 * Fragment que muestra un par de botones para seleccionar el formulario a completar
 * Solo le aparece a los administradores.
 * @author eperaza
 * @since 01.04.15
 */
public class SeleccionarFormularioFragment extends SgprFragment{

	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
         View view = inflater.inflate(R.layout.fragment_seleccionar_formulario, container, false);
         //Button btnPresMarca = (Button) view.findViewById(R.id.btn_pres_marca);
         //Button btnAuditoria = (Button) view.findViewById(R.id.btn_auditoria);
         Button btnTerceros = (Button) view.findViewById(R.id.btn_terceros);
		
         //btnPresMarca.setOnClickListener(onPressMarcaTap);
         //btnAuditoria.setOnClickListener(onAuditoriaTap);
         btnTerceros.setOnClickListener(onTercerosTap);

         return view;
	 }
	
	/**
	 * Listener para el boton que cambia al fragment del formulario de presencia de marca.
	 */
	OnClickListener onPressMarcaTap = new OnClickListener() {
		@Override
		public void onClick(View v) {
			cambioFragment(true);
		}
	};
	
	/**
	 * Listener para el boton que cambia al fragment del formulario de auditoria.
	 */
	OnClickListener onAuditoriaTap = new OnClickListener() {
		@Override
		public void onClick(View v) {
			cambioFragment(false);
		}
	};

    /**
     * Listener para el botón que pasa al formulario de terceros.
     */
    OnClickListener onTercerosTap = new OnClickListener() {
        @Override
        public void onClick(View v) {
			Negocio negocio = NegociosImplementor.getInstance().obtenerNegocioActivo();
			if(negocio != null){
            	Intent intent = new Intent(getActivity(), SupervisionTercerosActivity.class);
            	startActivity(intent);
			}
			else
				Toast.makeText(getActivity(), "Debe existir un negocio activo", Toast.LENGTH_SHORT).show();
        }
    };
	
	/**
	 * Accion del cambio de fragment.
	 * Como se cambia el fragment dentro del tab es necesario limpiar el stack el Fragment Manager para conseguir que cuando
	 * se pulse el boton de atras del dispositivo regrese al menu principal y no al fragment anterior.
	 * @param isPresMarca true = Debe cambiar al form. de pres. de marca, false al de auditoria.
	 */
	private void cambioFragment(boolean isPresMarca){
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStack();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(isPresMarca)
			fragmentTransaction.replace(R.id.contenido_principal, new PreguntaPresenciaMarcaFragment(), NegociosActivity.TAG_NEGOCIOS);
		else
			fragmentTransaction.replace(R.id.contenido_principal, new PreguntaAuditFragment(), NegociosActivity.TAG_NEGOCIOS);
		fragmentTransaction.commit();
	}
}
