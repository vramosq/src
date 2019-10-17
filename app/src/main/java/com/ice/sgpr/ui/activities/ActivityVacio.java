package com.ice.sgpr.ui.activities;

import com.ice.sgpr.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

/**
 * Activity que funciona para bloquear la aplicacion mientras se sincroniza. El activity es transparente y no
 * tiene accion alguna, simplemente impide al usuario tocar lo que se encuentra en el activity principal mientras se realiza
 * la sincronizacion.
 *
 * @author eperaza
 *         Fecha de creacion: 07/10/2013
 */
public class ActivityVacio extends Activity {

    private static ActivityVacio _instancia;

    public static ActivityVacio getInstance() {
        return _instancia;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_simple);
        RelativeLayout rlActivity = (RelativeLayout) findViewById(R.id.rl_fragment_simple);
        rlActivity.setBackgroundResource(R.color.transparente);
        _instancia = this;
    }

    public void finalizarActivity() {
        super.finish();
        _instancia = null;
    }
}
