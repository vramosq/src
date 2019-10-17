package com.ice.sgpr.ui.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.ActionBarActivity;

import com.ice.sgpr.R;

public class SgprTabListener<T extends Fragment> implements ActionBar.TabListener {
	/**
	 * Parent Activity
	 */
	private final AppCompatActivity _activity;
	/**
	 * Tag of the tab
	 */
	private final String _tag;
	/**
	 * Class of the fragment
	 */
	private final Class<T> _class;
	private final Bundle _args;
	private Fragment _fragment;
	
	/**
	 * Class Constructor
	 * @param pActivity Parent Class
	 * @param pTag Tag of the tab
	 * @param pClass Class of the tab
	 */
	public SgprTabListener(ActionBar pActivity, String pTag, Class<T> pClass){
		this(pActivity, pTag, pClass, new Bundle());
	}

	public SgprTabListener(AppCompatActivity pActivity, String pTag, Class<T> pClass, Bundle pArgs) {
		_activity = pActivity;
		_tag = pTag;
		_class = pClass;
		_args = pArgs;
		
		//Check if the fragment already exists, if it does, removes it from the UI
		_fragment = _activity.getSupportFragmentManager().findFragmentByTag(_tag);
		if (_fragment != null && !_fragment.isDetached())
		{
			FragmentTransaction ft = _activity.getSupportFragmentManager().beginTransaction();
			ft.detach(_fragment);
			ft.commit();
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (_fragment == null)
		{
			_fragment = Fragment.instantiate(_activity, _class.getName(), _args);
			ft.replace(R.id.contenido_principal, _fragment, _tag);
		}
		else
		{
			ft.attach(_fragment);
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (_fragment != null)
		{
			ft.detach(_fragment);
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
