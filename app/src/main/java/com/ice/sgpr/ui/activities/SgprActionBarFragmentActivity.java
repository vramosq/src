package com.ice.sgpr.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.ActionBarActivity;

import com.ice.sgpr.R;

public class SgprActionBarFragmentActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        getSupportActionBar().setIcon(R.drawable.ic_kolbi_logo);
	}
}
