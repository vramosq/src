package com.ice.sgpr.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.ui.fragments.AtencionFragment;
import com.ice.sgpr.ui.fragments.EncuestaPDVFragment;
import com.ice.sgpr.ui.fragments.IncidenciasFragment;
import com.ice.sgpr.ui.fragments.SocioPDVFragment;
import com.ice.sgpr.ui.fragments.SupervDistribuidoresFragment;
import com.ice.sgpr.ui.fragments.SupervMarcaFragment;

public class SupervisionTercerosActivity extends SgprActionBarActivity {
    public static final String TAG_TERCEROS = "terceros";
    public static final String NEGOCIO_ID = "negocio_id";
    public long negocioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_simple);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Constantes.TITULO_TERCEROS);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
        negocioId = negocioActivo.get_nNegocioId();

        ActionBar abBarra = getSupportActionBar();
        abBarra.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabInfoAtencion = abBarra.newTab();
        tabInfoAtencion.setText(Constantes.TAB_ATENCION);
        tabInfoAtencion.setTabListener(new TercerosTabListener());

        ActionBar.Tab tabInfoSupDistribuidores = abBarra.newTab();
        tabInfoSupDistribuidores.setText(Constantes.TAB_SUP_DIST);
        tabInfoSupDistribuidores.setTabListener(new TercerosTabListener());

        ActionBar.Tab tabInfoSocioPDV = abBarra.newTab();
        tabInfoSocioPDV.setText(Constantes.TAB_SOCIO_PDV);
        tabInfoSocioPDV.setTabListener(new TercerosTabListener());

        ActionBar.Tab tabInfoSupMarca = abBarra.newTab();
        tabInfoSupMarca.setText(Constantes.TAB_SUP_MARCA);
        tabInfoSupMarca.setTabListener(new TercerosTabListener());

        ActionBar.Tab tabInfoIncidencias = abBarra.newTab();
        tabInfoIncidencias.setText(Constantes.TAB_INCIDENCIAS);
        tabInfoIncidencias.setTabListener(new TercerosTabListener());

        ActionBar.Tab tabInfoEncuestaPDV = abBarra.newTab();
        tabInfoEncuestaPDV.setText(Constantes.TAB_ENCUESTA_PDV);
        tabInfoEncuestaPDV.setTabListener(new TercerosTabListener());

        abBarra.addTab(tabInfoAtencion);
        abBarra.addTab(tabInfoSupDistribuidores);
        abBarra.addTab(tabInfoSocioPDV);
        abBarra.addTab(tabInfoSupMarca);
        abBarra.addTab(tabInfoIncidencias);
        abBarra.addTab(tabInfoEncuestaPDV);
    }

    private class TercerosTabListener implements ActionBar.TabListener{

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

            Bundle bundle = new Bundle();
            bundle.putLong(NEGOCIO_ID, negocioId);

            switch (tab.getPosition()){
                case 0:
                    AtencionFragment frag = new AtencionFragment();
                    frag.setArguments(bundle);
                    ft.replace(R.id.contenido_principal, frag, TAG_TERCEROS);
                    break;
                case 1:
                    SupervDistribuidoresFragment fragSupDist = new SupervDistribuidoresFragment();
                    fragSupDist.setArguments(bundle);
                    ft.replace(R.id.contenido_principal, fragSupDist, TAG_TERCEROS);
                    break;
                case 2:
                    SocioPDVFragment fragSocioPDV = new SocioPDVFragment();
                    fragSocioPDV.setArguments(bundle);
                    ft.replace(R.id.contenido_principal, fragSocioPDV, TAG_TERCEROS);
                    break;
                case 3:
                    SupervMarcaFragment fragSupMarca = new SupervMarcaFragment();
                    fragSupMarca.setArguments(bundle);
                    ft.replace(R.id.contenido_principal, fragSupMarca, TAG_TERCEROS);
                    break;
                case 4:
                    IncidenciasFragment fragIncidencias = new IncidenciasFragment();
                    fragIncidencias.setArguments(bundle);
                    ft.replace(R.id.contenido_principal, fragIncidencias, TAG_TERCEROS);
                    break;
                case 5:
                    EncuestaPDVFragment fragEncuestaPDV = new EncuestaPDVFragment();
                    fragEncuestaPDV.setArguments(bundle);
                    ft.replace(R.id.contenido_principal, fragEncuestaPDV, TAG_TERCEROS);
                    break;
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }
}
