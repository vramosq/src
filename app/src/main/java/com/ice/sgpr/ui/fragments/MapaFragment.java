package com.ice.sgpr.ui.fragments;

import com.ice.sgpr.ui.activities.MapaActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Fragment que muestra el activity del mapa.
 * @author eperaza
 * Fecha de creacion 01/08/2013.
 */
@SuppressLint("ValidFragment")
@SuppressWarnings("deprecation")
public class MapaFragment extends SgprFragment{

	private static final String KEY_STATE_BUNDLE = "localActivityManagerState";
	private LocalActivityManager _localActivityManager;
	//private int _nIdRutaSeleccionada;
	public static String TIPO_MAPA;
    protected Activity activity;
	
	@Override
	public void onAttach(Activity pActivity) {
		super.onAttach(pActivity);
	}

    @SuppressLint("ValidFragment")
    public MapaFragment(Activity pActivity) {
        this.activity = pActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		Bundle state = null;
        if (savedInstanceState != null)
        {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }
        _localActivityManager = new LocalActivityManager(getActivity(), true);
        _localActivityManager.dispatchCreate(state);
    }
	
	
	/**
     * El mapa debe mostrarse en un activity, asi que dento de este fragment se crea uno y ahi se llama
     * al fragment del mapa. Se obtiene el TAG para identificar si debe mostrarse la lista de negocios en
     * una ruta (en caso de seleccionar el mapa de Rutas) o si se debe mostrar un unico negocio (en caso de
     * elegir la informacion de un negocio.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {    	
    	String sTabTag = this.getTag();
		_localActivityManager = new LocalActivityManager(getActivity(), true);
        _localActivityManager.dispatchCreate(savedInstanceState);
        
    	Intent intent = new Intent(getActivity(), MapaActivity.class);
        intent.putExtra(TIPO_MAPA, sTabTag);
    	
        Window w = _localActivityManager.startActivity("activity", intent);
        View currentView = w.getDecorView();
        
        if (currentView.getParent() != null)
        {
        	((ViewGroup) currentView.getParent()).removeView(currentView);
        }		
        currentView.setVisibility(View.VISIBLE); 
        currentView.setFocusableInTouchMode(true);
        ((ViewGroup) currentView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        return currentView;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE, _localActivityManager.saveInstanceState());
    }

    @Override
    public void onResume() 
    {
        super.onResume();
        _localActivityManager.dispatchResume();
    }

    @Override
    public void onPause() 
    {
        super.onPause();
        _localActivityManager.dispatchPause(getActivity().isFinishing());
    }

    @Override
    public void onStop() 
    {
        super.onStop();
        _localActivityManager.dispatchStop();
    }

    @Override
    public void onDestroy() 
    {
        super.onDestroy();
        _localActivityManager.dispatchDestroy(getActivity().isFinishing());
    }
}
