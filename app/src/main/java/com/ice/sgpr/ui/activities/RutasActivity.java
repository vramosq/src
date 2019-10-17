package com.ice.sgpr.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.ui.utils.SgprTabListener;
import com.ice.sgpr.ui.fragments.InformacionRutaFragment;
import com.ice.sgpr.ui.fragments.ListaRutasFragment;
import com.ice.sgpr.ui.fragments.MapaFragment;
import com.ice.sgpr.ui.fragments.SincronizacionFragment;

/**
 * Activity de rutas, donde se muestran los tabs (y fragments) de las rutas, mapa e informaci�n.
 * @author eperaza
 * Fecha de creaci�n: 26/07/2013.
 */
public class RutasActivity extends SgprActionBarActivity implements SincronizacionFragment.OnSyncSuccessPass{
	public static final String TAG_RUTAS = "rutas";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(null);
		setContentView(R.layout.activity_fragment_simple);
		setProgressBarIndeterminateVisibility(true);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constantes.TITULO_RUTAS);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab tabRuteo = getSupportActionBar().newTab();
		tabRuteo.setText(Constantes.TAB_RUTEO);
		tabRuteo.setTabListener(new SgprTabListener<ListaRutasFragment>(this, TAG_RUTAS, ListaRutasFragment.class));
		getSupportActionBar().addTab(tabRuteo);
		
		ActionBar.Tab tabMapa = getSupportActionBar().newTab();
		tabMapa.setText(Constantes.TAB_MAPA);
		tabMapa.setTabListener(new SgprTabListener<MapaFragment>(this, TAG_RUTAS, MapaFragment.class));
		getSupportActionBar().addTab(tabMapa);
		
		ActionBar.Tab tabInfo = getSupportActionBar().newTab();
		tabInfo.setText(Constantes.TAB_INFORMACION);
		tabInfo.setTabListener(new SgprTabListener<InformacionRutaFragment>(this, TAG_RUTAS, InformacionRutaFragment.class));
		getSupportActionBar().addTab(tabInfo);
		
		ActionBar.Tab tabSincronizacion = getSupportActionBar().newTab();
		tabSincronizacion.setText(Constantes.TAB_SINCRONIZACION);
		tabSincronizacion.setTabListener(new SgprTabListener<SincronizacionFragment>(this, TAG_RUTAS, SincronizacionFragment.class));
		getSupportActionBar().addTab(tabSincronizacion);
	}

	@Override
	public void changeToFirstTab() {
		// TODO Auto-generated method stub
		
	}
}
