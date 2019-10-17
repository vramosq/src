package com.ice.sgpr;

import android.app.Application;
import android.content.Context;

public class SgprApplication extends Application{
	
	private static SgprApplication _instance; 
	
	public SgprApplication()
	{
		_instance = this;
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		//VersioningImplementor.checkForUdates();
	}	
	
	public static Context getContext() {
        return _instance;
    }
}
