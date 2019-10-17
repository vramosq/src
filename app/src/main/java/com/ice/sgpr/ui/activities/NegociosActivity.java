package com.ice.sgpr.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.ImagenesImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.PreguntasAuditorImplementor;
import com.ice.sgpr.implementor.PreguntasPresenciaMarcaImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.ui.fragments.BuscadorFragment;
import com.ice.sgpr.ui.fragments.ImagenesFragment;
import com.ice.sgpr.ui.fragments.InformacionNegocioFragment;
import com.ice.sgpr.ui.fragments.MapaFragment;
import com.ice.sgpr.ui.fragments.ObservacionesFragment;
import com.ice.sgpr.ui.fragments.PreguntaAuditFragment;
import com.ice.sgpr.ui.fragments.PreguntaPresenciaMarcaFragment;
import com.ice.sgpr.ui.fragments.SeleccionarFormularioFragment;
import com.ice.sgpr.ui.fragments.SgprFragment;
import com.ice.sgpr.ui.fragments.SincronizacionFragment;

/**
 * Activity que muestra la los tabs para las funciones necesarias en los negocios
 * @author eperaza
 * Fecha de creación: 09/08/2013.
 */
public class NegociosActivity extends SgprActionBarActivity implements SincronizacionFragment.OnSyncSuccessPass{
	public static final String TAG_NEGOCIOS = "negocios";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_simple);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constantes.TITULO_NEGOCIOS);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar abBarra = getSupportActionBar();
		abBarra.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		String sRolUsuarioLoguedo = UsuariosImplementor.getInstance().obtenerRolUsuarioLogueado();
		/*String sTipoFormulario = Constantes.TAB_PRES_MARCA;
		if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_AUDITOR))
			sTipoFormulario = Constantes.TAB_AUDITORIA;
		else if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_PRINCIPAL))
			sTipoFormulario = Constantes.TAB_FORMULARIO;*/
		String sTipoFormulario = Constantes.TAB_FORMULARIO_INDEF;
		
		ActionBar.Tab tabInfoNegocio = abBarra.newTab();
		tabInfoNegocio.setText(Constantes.TAB_INFORMACION);
		tabInfoNegocio.setTabListener(new NegociosTabsListener());
		
		ActionBar.Tab tabPresMarca = abBarra.newTab();
		tabPresMarca.setText(sTipoFormulario);
		tabPresMarca.setTabListener(new NegociosTabsListener());
		
		ActionBar.Tab tabImagenes = abBarra.newTab();
		tabImagenes.setText(Constantes.TAB_IMAGENES);
		tabImagenes.setTabListener(new NegociosTabsListener());
		
		ActionBar.Tab tabObservaciones = abBarra.newTab();
		tabObservaciones.setText(Constantes.TAB_OBSERVACIONES);
		tabObservaciones.setTabListener(new NegociosTabsListener());
		/*
		ActionBar.Tab tabFormulario = abBarra.newTab();
		tabFormulario.setText(Constantes.TAB_FORMULARIO);
		tabFormulario.setTabListener(new TestListener());
		*/
		ActionBar.Tab tabMapaNegocio = abBarra.newTab();		
		tabMapaNegocio.setText(Constantes.TAB_MAPA);
		tabMapaNegocio.setTabListener(new NegociosTabsListener());
		
		ActionBar.Tab tabBuscador = abBarra.newTab();		
		tabBuscador.setText(Constantes.TAB_BUSCADOR);
		tabBuscador.setTabListener(new NegociosTabsListener());
		
		ActionBar.Tab tabSincronizacion = getSupportActionBar().newTab();
		tabSincronizacion.setText(Constantes.TAB_SINCRONIZACION);
		tabSincronizacion.setTabListener(new NegociosTabsListener());
		
		abBarra.addTab(tabInfoNegocio);
		abBarra.addTab(tabPresMarca);
		abBarra.addTab(tabImagenes);
		//abBarra.addTab(tabFormulario);
		abBarra.addTab(tabObservaciones);
		abBarra.addTab(tabMapaNegocio);
		abBarra.addTab(tabBuscador);
		abBarra.addTab(tabSincronizacion);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
	private class NegociosTabsListener implements ActionBar.TabListener{
		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			if(tab.getPosition() == 0){
				InformacionNegocioFragment frag = new InformacionNegocioFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
			else if(tab.getPosition() == 1){
				String sRolUsuarioLoguedo = UsuariosImplementor.getInstance().obtenerRolUsuarioLogueado();
				SgprFragment frag;
				frag = new SeleccionarFormularioFragment();
				/*if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_NORMAL))
					frag = new PreguntaPresenciaMarcaFragment();
				else if(sRolUsuarioLoguedo.equals(Constantes.ROL_USUARIO_AUDITOR))
					frag = new PreguntaAuditFragment();
				else
					frag = new SeleccionarFormularioFragment();*/
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
			else if(tab.getPosition() == 2){
				ImagenesFragment frag = new ImagenesFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
			/*else if(tab.getPosition() == ?){
				FormularioFragment frag = new FormularioFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}*/
			else if(tab.getPosition() == 3){
				ObservacionesFragment frag = new ObservacionesFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
			
			else if(tab.getPosition() == 4){
				MapaFragment frag = new MapaFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
			else if(tab.getPosition() == 5){
				BuscadorFragment frag = new BuscadorFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
			else{
				SincronizacionFragment frag = new SincronizacionFragment();
				ft.replace(R.id.contenido_principal, frag, TAG_NEGOCIOS);
			}
		}
		
		@Override
		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}
	}
	
	
	/**
	 * Se agrega un men� con las opciones de activar/desactivar negocio y 
	 * descartar negocio.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_negocios, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_activar_negocio:
                actualizarHabilitadoDeshabilitado();
                return true;

            case R.id.menu_descartar_negocio:
                descartarNegocio();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	private void showMessage(String pTexto){
		Toast.makeText(getApplicationContext(), pTexto, Toast.LENGTH_SHORT).show();
	}
    
    /**
     * Se actualiza el estado de un negocio.
     */
    private void actualizarHabilitadoDeshabilitado(){
    	Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
		if(negocioActivo != null){
			int nNuevoEstado = (!negocioActivo.is_bHabilitado())? 1 : 0;
			NegociosImplementor.getInstance().habilitarDesabilitarNegocio(negocioActivo.get_nNegocioId(), nNuevoEstado);
			if(!negocioActivo.is_bHabilitado())
				showMessage("Negocio \"" + negocioActivo.get_sNombre() + "\" Habilitado");
			else
				showMessage("Negocio " + negocioActivo.get_sNombre() + " Deshabilitado");
			
			int currentIndex = getSupportActionBar().getSelectedNavigationIndex();
			if(currentIndex == 0)
				actualizarFragment(true);
			else
				actualizarFragment(false);
		}
    }
    
    /**
     * Se descarta un negocio que ha sido seleccionado o creado por error.
     */
    private void descartarNegocio(){
    	Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
    	if(negocioActivo != null){
    		ImagenesImplementor.getInstance().borrarImagenesNegocio(negocioActivo.get_nNegocioId());
    		PreguntasPresenciaMarcaImplementor.getInstance().borrarTodasPreguntasPresenciaMarca(negocioActivo.get_nNegocioId());
    		BitacoraImplementor.getInstance().borrarBitacorasUsuario(negocioActivo.get_nNegocioId());
    		PreguntasAuditorImplementor.getInstance().borrarPreguntasPorIdNegocio(negocioActivo.get_nNegocioId());
    		NegociosImplementor.getInstance().descartarNegocio(negocioActivo.get_nNegocioId(), negocioActivo.get_nRutaId());
    		showMessage("Negocio descartado.");
    		actualizarFragment(true);
    	}
    	else
    		showMessage("Ya ha contestado una pregunta. No puede descartar este negocio.");
    }
    
    /**
     * Se pasa al primer fragment si se esta ubicado en algun otro y se
     * carga de nuevo.
     * @param changeTabIndex = Indica si es necesario cambiar de tab (solo en caso de descartar un negocio).
     */
	private void actualizarFragment(boolean changeTabIndex){
    	if(changeTabIndex){
    		getSupportActionBar().setSelectedNavigationItem(0);
        	InformacionNegocioFragment frag = new InformacionNegocioFragment();
        	FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.contenido_principal, frag, TAG_NEGOCIOS).commit();
            fm.popBackStackImmediate();
    	}
    }

	@Override
	public void changeToFirstTab() {
		actualizarFragment(true);
	}
}