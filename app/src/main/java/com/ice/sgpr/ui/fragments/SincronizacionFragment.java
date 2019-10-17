package com.ice.sgpr.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Gps;
import com.ice.sgpr.comun.Utils;
import com.ice.sgpr.entidades.Bitacora;
import com.ice.sgpr.entidades.Imagen;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.entidades.Parametro;
import com.ice.sgpr.entidades.Ruta;
import com.ice.sgpr.entidades.Versionamiento;
import com.ice.sgpr.implementor.BitacoraImplementor;
import com.ice.sgpr.implementor.ImagenesImplementor;
import com.ice.sgpr.implementor.NegociosImplementor;
import com.ice.sgpr.implementor.PaginacionImplementor;
import com.ice.sgpr.implementor.ParametrosImplementor;
import com.ice.sgpr.implementor.PreguntasAuditorImplementor;
import com.ice.sgpr.implementor.PreguntasImplementor;
import com.ice.sgpr.implementor.PreguntasPresenciaMarcaImplementor;
import com.ice.sgpr.implementor.RutaImplementor;
import com.ice.sgpr.implementor.SeguimientoImplementor;
import com.ice.sgpr.implementor.SupervTercerosImplementor;
import com.ice.sgpr.implementor.UsersTercerosImplementor;
import com.ice.sgpr.implementor.UsuariosImplementor;
import com.ice.sgpr.implementor.VersionamientoImplementor;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.service.RestHelper;
import com.ice.sgpr.service.SgprService;
import com.ice.sgpr.ui.activities.ActivityVacio;

public class SincronizacionFragment extends SgprFragment {
    protected static final int TIMER_RUNTIME = 10000;
    private ProgressBar _pbarProgreso;
    private TextView _tvInfoSincronizando, _tvProgressAmount, _tvFinishBusiness;
    private LocationManager _locationManager;
    private String _sLatitud, _sLongitud;
    protected Activity mActivity;
    private OnSyncSuccessPass dataPasser;
    private Animation animAlpha = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sincronizacion, container, false);
        animAlpha = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_alpha);
        TextView tvSincronizar = (TextView) view.findViewById(R.id.tv_sincronizar);
        _pbarProgreso = (ProgressBar) view.findViewById(R.id.pbar_sincronizando);
        _tvInfoSincronizando = (TextView) view.findViewById(R.id.tv_info_sincronizando);
        _tvProgressAmount = (TextView) view.findViewById(R.id.tv_progress_amount);
        _tvFinishBusiness = (TextView) view.findViewById(R.id.tv_sinc_negociofin_negocio);
        //_pbarProgreso.getProgressDrawable().setColorFilter(getResources().getColor(R.color.kolbi_verde_claro), Mode.SRC_IN);
        tvSincronizar.setOnClickListener(onSincronizarClick);
        _tvFinishBusiness.setOnClickListener(onFinNegocio);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

        try {
            dataPasser = (OnSyncSuccessPass) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSyncSuccessPass");
        }
    }

    /**
     * Listener para el textview de "Sincronizar".
     * Se obtiene el negocio actual y si lo hay, se verifica que no est� en estado "activo" para poder sincronizar, de igual manera
     * se verifica que no exista ning�n formulario que haya sido  comenzado pero no terminado.
     */
    public View.OnClickListener onSincronizarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //int nPreguntasContestadas = PreguntasImplementor.getInstance().obtenerCantidadPreguntasContestadas();
            //int nPreguntasContestadas = PreguntasPresenciaMarcaImplementor.getInstance().obtenerCantidadPreguntasContestadasPM();
            /*
			Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
			if(negocioActivo != null && negocioActivo.get_nEstado() == Constantes.ESTADO_NEGOCIO_ACTUAL){
				Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_NEGOCIO_SELECCIONADO + " Finalice la visita para proseguir.", Toast.LENGTH_LONG).show();
				return;
			}
			
			//if(nPreguntasContestadas == Constantes.CANTIDAD_DE_PREGUNTAS || nPreguntasContestadas == 0 || nPreguntasContestadas == 2){
			if(negocioActivo == null || negocioActivo.get_nEstado() != Constantes.ESTADO_NEGOCIO_ACTUAL){
				if(verificarConexion())
					new AsycTaskVersionamiento().execute();
				else 
					Toast.makeText(getActivity().getApplicationContext(), "Verifique su conex�n a internet e intente m�s tarde.", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getActivity().getApplicationContext(), "Existe un negocio activo, debe finalizar la visita para continuar...", Toast.LENGTH_LONG).show();
				*/
            v.startAnimation(animAlpha);
            boolean hayNegocio = hayNegocioActivo();
            if (hayNegocio) {
                Toast.makeText(getActivity().getApplicationContext(), Constantes.AVISO_NEGOCIO_SELECCIONADO + " Finalice la visita para proseguir.", Toast.LENGTH_LONG).show();
                return;
            } else if (verificarConexion())
                new AsycTaskVersionamiento().execute();
            else
                Toast.makeText(getActivity().getApplicationContext(), Constantes.ERROR_LOGIN_CONEXION, Toast.LENGTH_LONG).show();
        }
    };

    private boolean hayNegocioActivo() {
        Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
        if (negocioActivo != null && negocioActivo.get_nEstado() == Constantes.ESTADO_NEGOCIO_ACTUAL) {
            return true;
        }

        if (negocioActivo == null || negocioActivo.get_nEstado() != Constantes.ESTADO_NEGOCIO_ACTUAL) {
            return false;
        } else {
            Toast.makeText(getActivity().getApplicationContext(), Constantes.ERROR_SINCRONIZADO_NEGOCIO_ACTIVO, Toast.LENGTH_LONG).show();
            return true;
        }
    }

    /**
     * Hilo para enviar y recibir los datos del versionamiento.
     * La variable "bDatosVersionados" se actualiza cada vez que se logra versionar o no un modulo para
     * continuar con el otro. Mientras se mantenga en verdadero, sigue el proceso de versionamiento, si
     * algun modulo falla y se encuenta en Falso, el proceso de versionamiento se detiene. En caso de que
     * falle, cada vez que se actualiza un modulo se escribe en la tabla "Versionamiento" indicando si un
     * modulo ya fue versionado o no.
     * Al final del versionamiento, si no hay errores, se borra toda la informacion del usuario (rutas,
     * negocios, bitacoras, imagenes, preguntas).
     *
     * @author eperaza
     */
    private class AsycTaskVersionamiento extends AsyncTask<Void, Integer, Boolean> {
        String[] sActualizando = Constantes.ARREGLO_ACTUALIZANDO;
        String sErrorProceso = "";
        Intent intent;

        @Override
        protected void onPreExecute() {
            _pbarProgreso.setMax(100);
            _pbarProgreso.setProgress(0);
            //((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            intent = new Intent(getActivity(), ActivityVacio.class);
            startActivity(intent);
            _tvProgressAmount.setText("0%");
            _tvProgressAmount.setTextColor(getResources().getColor(R.color.blanco));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            /**
             * Enviar los datos
             */
            Boolean bDatosVersionados = true;
            String sUid = Secure.getString(getActivity().getApplication().getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            String pCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];

            List<Versionamiento> lVersionamiento = VersionamientoImplementor.getInstance().obtenerVersionamiento();
            if (lVersionamiento.size() == 0) {
                VersionamientoImplementor.getInstance().insertarTablaVersionamiento();
                lVersionamiento = VersionamientoImplementor.getInstance().obtenerVersionamiento();
            }
            publishProgress(5, 0);

            if (lVersionamiento.get(Constantes.VERSION_NUEVOS_COMERCIOS).getEstado() == 0) {
                bDatosVersionados = enviarDatosNuevosNegocios(sUid);
                if (bDatosVersionados) {
                    publishProgress(10, 0);
                } else
                    sErrorProceso = "Nuevos Comercios";
            } else
                publishProgress(10, 0);

            if ((lVersionamiento.get(Constantes.VERSION_COMERCIOS).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosNegocios(sUid);
                if (bDatosVersionados) {
                    publishProgress(15, 1);
                } else
                    sErrorProceso = "Comercios";
            } else
                publishProgress(15, 2);
			/*if((lVersionamiento.get(Constantes.VERSION_FORMULARIOS).getEstado() == 0) && bDatosVersionados){
				bDatosVersionados = enviarDatosFormularios();
				if(bDatosVersionados){
					publishProgress(56);
				}
				else
					sErrorProceso = "Formularios";
			}
			else
				publishProgress(56);*/
            if ((lVersionamiento.get(Constantes.VERSION_FORMULARIOS_PM).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosPM(sUid);
                if (bDatosVersionados) {
                    publishProgress(20, 3);
                } else
                    sErrorProceso = "Formularios Presencia de marca";
            } else if (bDatosVersionados) {
                publishProgress(20, 3);
            }

            if ((lVersionamiento.get(Constantes.VERSION_BITACORAS).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosBitacora(sUid);
                if (bDatosVersionados) {
                    publishProgress(25, 4);
                } else
                    sErrorProceso = "Bitácoras";
            } else if (bDatosVersionados) {
                publishProgress(25, 4);
            }

            if ((lVersionamiento.get(Constantes.VERSION_IMAGENES).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosImagenes();
                /**
                 * Se incorpora el proceso de carga de imagenes para poder controlar
                 * el porcentaje de subida de cada una
                 */
                double imageInitialProgress = 30;
                List<Imagen> listaImagenes = ImagenesImplementor.getInstance().obtenerImagenesVersionamiento();
                double cantImagenes = listaImagenes.size();

                if (cantImagenes == 0)
                    bDatosVersionados = true;
                if (cantImagenes != 0) {
                    double imageWeigth = 12.0 / (cantImagenes + 0.0);
                    try {
                        for (Imagen imagen : listaImagenes) {
                            String sImagenesVersionamientoUrl = SgprService.getInstance().getImagenesVersionamientoUrl(imagen.getCodigoNegocio());
                            String sRespuesta = RestHelper.getInstance().PUT(sImagenesVersionamientoUrl, imagen.getImagen(), true);
                            String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
                            if (!sCodigoRespuesta.equals("0")) {
                                bDatosVersionados = false;
                                break;
                            } else if (imageInitialProgress + imageWeigth < 30) {
                                imageInitialProgress += imageWeigth;
                                publishProgress((int) imageInitialProgress, 5);
                            }
                        }
                        if (bDatosVersionados) {
                            for (Imagen imagenVersionada : listaImagenes) {
                                Log.i("IMAGEN ACT: ", Integer.toString(imagenVersionada.getImagenId()));
                                ImagenesImplementor.getInstance().actualizarEstadoImagenDespuesVerisionamiento(imagenVersionada.getImagenId());
                            }
                            VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_IMAGENES);
                            bDatosVersionados = true;
                        }
                    } catch (Exception e) {
                        Log.i("IMAGENES ERROR: ", e.toString());
                    }
                }
                ////////////////////////////////////////////////////
                if (bDatosVersionados) {
                    publishProgress(30, 5);
                } else
                    sErrorProceso = "Imágenes";
            } else if (bDatosVersionados) {
                publishProgress(30, 5);
            }

            if ((lVersionamiento.get(Constantes.VERSION_FORMULARIOS_AUDITOR).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosAuditor(sUid);
                if (bDatosVersionados) {
                    publishProgress(35, 6);
                } else
                    sErrorProceso = "Formulario Auditor";
            } else if (bDatosVersionados) {
                publishProgress(35, 6);
            }

            /*************************************************************/
            /************************* TERCEROS **************************/
            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_SUPERV_DIST).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.SUPERV_DIST,
                        Constantes.VERSION_TERCEROS_SUPERV_DIST,SgprService.SP_SUPERV_DIST);
                if (bDatosVersionados) {
                    publishProgress(40, 7);
                } else
                    sErrorProceso = "Terceros - Supervisión distribuidores";
            } else if (bDatosVersionados) {
                publishProgress(40, 7);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_PRES_MARCA).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.PRES_MARCA,
                        Constantes.VERSION_TERCEROS_PRES_MARCA,SgprService.SP_PRES_MARCA);
                if (bDatosVersionados) {
                    publishProgress(45, 8);
                } else
                    sErrorProceso = "Terceros - Supervisión de marca";
            } else if (bDatosVersionados) {
                publishProgress(45, 8);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_PUBLICIDAD).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.PUBLICIDAD,
                        Constantes.VERSION_TERCEROS_PUBLICIDAD,SgprService.SP_PUBLICIDAD);
                if (bDatosVersionados) {
                    publishProgress(50, 9);
                } else
                    sErrorProceso = "Terceros - Publicidad";
            } else if (bDatosVersionados) {
                publishProgress(50, 9);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_PROMOCIONAL).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.PROMOCIONAL,
                        Constantes.VERSION_TERCEROS_PROMOCIONAL,SgprService.SP_PROMOCIONAL);
                if (bDatosVersionados) {
                    publishProgress(55, 10);
                } else
                    sErrorProceso = "Terceros - Promocional";
            } else if (bDatosVersionados) {
                publishProgress(55, 10);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_POP_ESPECIAL).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.POP_ESPECIAL,
                        Constantes.VERSION_TERCEROS_POP_ESPECIAL,SgprService.SP_POP_ESPECIAL);
                if (bDatosVersionados) {
                    publishProgress(60, 11);
                } else
                    sErrorProceso = "Terceros - POP Especial";
            } else if (bDatosVersionados) {
                publishProgress(60, 11);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_OBSERVACIONES).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.OBSERVACIONES,
                        Constantes.VERSION_TERCEROS_OBSERVACIONES,SgprService.SP_OBSERVACIONES);
                if (bDatosVersionados) {
                    publishProgress(65, 12);
                } else
                    sErrorProceso = "Terceros - Observaciones";
            } else if (bDatosVersionados) {
                publishProgress(65, 12);
            }


            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_ATENCION).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.ATENCION,
                        Constantes.VERSION_TERCEROS_ATENCION,SgprService.SP_ATENCIONES);
                if (bDatosVersionados) {
                    publishProgress(70, 13);
                } else
                    sErrorProceso = "Terceros - Atenciones";
            } else if (bDatosVersionados) {
                publishProgress(70, 13);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_INCIDENCIAS).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.INCIDENCIAS,
                        Constantes.VERSION_TERCEROS_INCIDENCIAS,SgprService.SP_INCIDENCIAS);
                if (bDatosVersionados) {
                    publishProgress(75, 14);
                } else
                    sErrorProceso = "Terceros - Incidencias";
            } else if (bDatosVersionados) {
                publishProgress(75, 14);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_SOCIO_PDV).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.SOCIO_PDV,
                        Constantes.VERSION_TERCEROS_SOCIO_PDV,SgprService.SP_SOCIO_PDV);
                if (bDatosVersionados) {
                    publishProgress(80, 15);
                } else
                    sErrorProceso = "Terceros - Socio PDV";
            } else if (bDatosVersionados) {
                publishProgress(80, 15);
            }

            if ((lVersionamiento.get(Constantes.VERSION_TERCEROS_FORMULARIOS_PDV).getEstado() == 0) && bDatosVersionados) {
                bDatosVersionados = enviarDatosFormulariosTerceros(pCodigoUsuario, Constantes.ENCUESTA_PDV,
                        Constantes.VERSION_TERCEROS_FORMULARIOS_PDV,SgprService.SP_ENCUESTA_PDV);
                if (bDatosVersionados) {
                    publishProgress(85, 16);
                } else
                    sErrorProceso = "Terceros - Formulario PDV";
            } else if (bDatosVersionados) {
                publishProgress(85, 16);
            }

            /************************************************************/
            /************************************************************/

            if (bDatosVersionados) {
                borrarDatosVersionados();
                /**
                 * Obtener los Datos
                 */
                try {
                    //Rutas
                    String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
                    actualizarTextViewInformativo("Obteniendo Rutas");
                    sErrorProceso = "Obteniendo Rutas";
                    String sRequest = RestHelper.getInstance().GET(SgprService.getInstance().getRutasUrl(sCodigoUsuario), false);
                    List<Ruta> lRutas = JSONHelper.getInstance().obtenerRutasDesdeJson(sRequest);
                    RutaImplementor.getInstance().insertarRutas(lRutas);
                    publishProgress(90, 17);

                    //Parámetros
                    actualizarTextViewInformativo("Obteniendo Parámetros");
                    sErrorProceso = "Obteniendo Parámetros";
                    sRequest = RestHelper.getInstance().GET(SgprService.getInstance().getParametrosUrl(), false);
                    List<Parametro> lParametros = JSONHelper.getInstance().obtenerParametrosDesdeJson(sRequest);
                    publishProgress(95, 18);
                    ParametrosImplementor.getInstance().insertarParametros(lParametros);
                    UsuariosImplementor.getInstance().actualizarFechaSinc();
                    enviarDatosSeguimiento();

                    //Usuarios:
                    actualizarTextViewInformativo("Obteniendo Lista de usuarios");
                    sErrorProceso = "Obteniendo Usuarios";
                    sRequest = RestHelper.getInstance().GET(SgprService.getInstance().getUsuariosUrl(), false);
                    ArrayList<String[]> arrayGenerico = JSONHelper.getInstance().obtenerUsuariosDesdeJson(sRequest);
                    publishProgress(95, 19);
                    UsersTercerosImplementor.getInstance().insertarNuevosUsuarios(arrayGenerico);

                    //Tipo de supervisión:
                    actualizarTextViewInformativo("Obteniendo Lista tipos de supervisión");
                    sErrorProceso = "Obteniendo Tipos de supervisión";
                    sRequest = RestHelper.getInstance().GET(SgprService.getInstance().getTiposSupervisionUrl(), false);
                    lParametros = JSONHelper.getInstance().obtenerTiposSupervisionDesdeJson(sRequest, "T_INCI");
                    publishProgress(95, 20);
                    ParametrosImplementor.getInstance().insertarParametros(lParametros);

                    //Socios:
                    actualizarTextViewInformativo("Obteniendo Lista de socios comerciales");
                    sErrorProceso = "Obteniendo Socios";
                    sRequest = RestHelper.getInstance().GET(SgprService.getInstance().getSociosUrl(), false);
                    lParametros = JSONHelper.getInstance().obtenerTiposSupervisionDesdeJson(sRequest, "SOC");
                    publishProgress(100, 21);
                    ParametrosImplementor.getInstance().insertarParametros(lParametros);

                } catch (Exception ex) {
                    return false;
                }
            }
            return bDatosVersionados;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            int messagePosition = values[1].intValue();

            _pbarProgreso.setProgress(progreso);
            String strProgress = String.valueOf(progreso) + "%";
            _tvProgressAmount.setText(strProgress);
            if (progreso > 50)
                _tvProgressAmount.setTextColor(getResources().getColor(R.color.negro));
            if (progreso < 100) {
                _tvInfoSincronizando.setText(sActualizando[messagePosition]);
            }
            if (progreso == 100)
                _tvInfoSincronizando.setText(Constantes.AVISO_SINCRONIZADO_EXITOSO);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                try {
                    Thread.sleep(1500);
                    dataPasser.changeToFirstTab();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    dataPasser.changeToFirstTab();
                }
            } else
                _tvInfoSincronizando.setText(Constantes.ERROR_SINCRONIZADO + sErrorProceso);
            ActivityVacio.getInstance().finalizarActivity();
        }
    }

    /**
     * Actualiza el texto de un textview, que le indica al usuario si el proceso de sincronizaci�n se realiz� exitosamente
     * o si fall�, en cu�l m�dulo fall�.
     *
     * @param pTexto
     */
    public void actualizarTextViewInformativo(final String pTexto) {
        new Runnable() {
            public void run() {
                _tvInfoSincronizando.setText(pTexto);
            }
        };
    }

    /**
     * Genera un Json Array con la lista de negocios modificados y los env�a mediante PUT.
     * Si recibe una respuesta positiva, cambia el estado ("Activo") de cada negocio versionado a 0.
     *
     * @return true, si logr� hacer el versionamiento, false en caso contrario.
     */
    public Boolean enviarDatosNegocios(String pUid) {
        List<Negocio> listaNegocios = NegociosImplementor.getInstance().obtenerNegociosVersionamiento();
        if (listaNegocios.size() == 0)
            return true;
        try {
            String sJsonArrayNegocios = JSONHelper.getInstance().obtenerJsonNegocioVersionamiento(listaNegocios, false, pUid);
            String sUrl = SgprService.getInstance().getBitacoraNegocioVersionamientoUrl();
            String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayNegocios);
            String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
            if (sCodigoRespuesta.equals("0")) {
                for (Negocio negocioVersionado : listaNegocios) {
                    Log.i("NEGOCIO ACT: ", negocioVersionado.get_sNombre());
                    NegociosImplementor.getInstance().actualizarEstadoDespuesVerisionamiento(negocioVersionado.get_nNegocioId());
                }
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_COMERCIOS);
                return true;
            } else {
                Log.i("NEG ERROR VERSION: ", sRespuesta);
            }
        } catch (Exception e) {
            Log.i("NEG ERROR VERSION: ", e.toString());
        }
        return false;
    }

    /**
     * Genera un Json Array con la lista de Formularios y los env�a mediante PUT.
     * Si recibe una respuesta positiva, cambia el estado ("Activo") de cada formulario versionado a 0.
     * @return true, si logr� hacer el versionamiento, false en caso contrario.
     */
	/*public Boolean enviarDatosFormularios(){
		try {
			Boolean bRespuesta = true;
			while(bRespuesta){
				String sJsonArrayFormularios = PreguntasImplementor.getInstance().arrayFormulariosVersionamiento();
				if(sJsonArrayFormularios == null){
					VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_FORMULARIOS);
					return true;
				}
				String sUrl = SgprService.getInstance().getFormularioUrl();
				String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayFormularios);
				String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
				if(sCodigoRespuesta.equals("0")){
					bRespuesta = true;
				}
				else{
					Log.i("FORMULARIOS ERROR VERSIONAMIENTO: ", sRespuesta);
					return false;
				}
			}
		}
		catch (Exception e) {
			Log.i("FORMULARIOS ERROR VERSIONAMIENTO FORMULARIO: ", e.toString());
		}
		return false;
	}*/

    /**
     * Genera un Json Array con la lista de respuestas de presencia de marca y los envia mediante PUT.
     * Si recibe una respuesta positiva, cambia el estado ("Activo") de cada formulario versionado a 0.
     *
     * @return true, si logra hacer el versionamiento, false en caso contrario.
     */
    public Boolean enviarDatosFormulariosPM(String pUid) {
        try {
            //Boolean bRespuesta = true;
            //while(bRespuesta){
            String sJsonArrayFormulariosPM = PreguntasPresenciaMarcaImplementor.getInstance().arrayFormulariosPMVersionamiento(pUid);
            if (sJsonArrayFormulariosPM == null) {
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_FORMULARIOS_PM);
                return true;
            }
            String sUrl = SgprService.getInstance().getFormularioUrl();
            String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayFormulariosPM);
            String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
            if (sCodigoRespuesta.equals("0")) {
                return true;
            } else {
                Log.i("FORM ERROR VERSION: ", sRespuesta);
                return false;
            }
            //}
        } catch (Exception e) {
            Log.i("FORM ERROR VERSION: ", e.toString());
        }
        return false;
    }

    /**
     * Genera un Json Array con la lista de nuevos negocios registrados y los envia mediante PUT.
     * La respuesta es el ID temporal asignado en la app y el ID asignado por el SP, asi que tanto a los negocios, imagenes, preguntas y bitacoras
     * del nuevo negocio se les actualiza el ID para seguir con el proceso normal de versionamiento.
     *
     * @return true, si logra hacer el versionamiento, false en caso contrario.
     */
    public Boolean enviarDatosNuevosNegocios(String pUid) {
        List<Negocio> listaNegocios = NegociosImplementor.getInstance().obtenerNuevosNegociosVersionamiento();
        if (listaNegocios.size() == 0)
            return true;
        try {
            String sJsonArrayNegocios = JSONHelper.getInstance().obtenerJsonNegocioVersionamiento(listaNegocios, true, pUid);
            String sUrl = SgprService.getInstance().getBitacoraNegocioVersionamientoUrl();
            String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayNegocios);
            List<String[]> sCodigosNegocios = JSONHelper.getInstance().descifrarRespuestaVersionamientoNuevoNegocio(sRespuesta);
            if (sCodigosNegocios.size() > 0) {
                for (String[] negocioVersionado : sCodigosNegocios) {
                    NegociosImplementor.getInstance().actualizarIdNuevoNegocio(negocioVersionado);
                    ImagenesImplementor.getInstance().actualizarIdNuevoNegocio(negocioVersionado);
                    PreguntasImplementor.getInstance().actualizarIdNuevoNegocio(negocioVersionado);
                    BitacoraImplementor.getInstance().actualizarIdNuevoNegocio(negocioVersionado);
                    PreguntasPresenciaMarcaImplementor.getInstance().actualizarIdNuevoNegocio(negocioVersionado);
                    NegociosImplementor.getInstance().actualizarEstadoDespuesVerisionamiento(Integer.parseInt(negocioVersionado[1]));
                    PreguntasAuditorImplementor.getInstance().actualizarIdNuevoNegocio(negocioVersionado);
                }
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_NUEVOS_COMERCIOS);
                return true;
            } else {
                Log.i("NUEV NEG ERR VERSION: ", sRespuesta);
            }
        } catch (Exception e) {
            Log.i("NUEV NEG ERR VERSION: ", e.toString());
        }
        return false;
    }

    /**
     * Se genera un Json array con la lista de bitacoras y los envia mediante PUT.
     * Actualiza el estado del proceso versionado.
     *
     * @return true, si se logra hacer el versionamiento de la bitacora o no hay bitacoras, false hubo error en el versionamiento.
     */
    public Boolean enviarDatosBitacora(String pUid) {
        //Obtener datos
        List<Bitacora> listaBitacoras = BitacoraImplementor.getInstance().obtenerBitacorasVersionamiento();
        if (listaBitacoras.size() == 0)
            return true;
        try {
            String sJsonArrayBitacoras = JSONHelper.getInstance().obtenerJsonBitacoraVersionamiento(listaBitacoras, pUid);
            String sUrl = SgprService.getInstance().getBitacoraNegocioVersionamientoUrl();
            String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayBitacoras);
            String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
            if (sCodigoRespuesta.equals("0")) {
                for (Bitacora bitacoraVersionada : listaBitacoras) {
                    Log.i("BITACORA ACT: ", Integer.toString(bitacoraVersionada.getId()));
                    BitacoraImplementor.getInstance().actualizarEstadoBitacoraDespuesVersionamiento(bitacoraVersionada.getId());
                }
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_BITACORAS);
                return true;
            }
        } catch (Exception e) {
            Log.i("BIT ERROR VERSION: ", e.toString());
        }
        return false;
    }

    /**
     * Genera un Json array con la lista de imagenes y los envia mediante PUT
     *
     * @return true, si se logra hacer el versionamiento de la bitacora o no hay bitacoras, false hubo error en el versionamiento.
     */
    public Boolean enviarDatosImagenes() {
        List<Imagen> listaImagenes = ImagenesImplementor.getInstance().obtenerImagenesVersionamiento();
        if (listaImagenes.size() == 0)
            return true;
        try {
            for (Imagen imagen : listaImagenes) {
                String sImagenesVersionamientoUrl = SgprService.getInstance().getImagenesVersionamientoUrl(imagen.getCodigoNegocio());
                String sRespuesta = RestHelper.getInstance().PUT(sImagenesVersionamientoUrl, imagen.getImagen(), true);
                String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
                if (!sCodigoRespuesta.equals("0")) {
                    return false;
                }
            }
            for (Imagen imagenVersionada : listaImagenes) {
                Log.i("IMAGEN ACT: ", Integer.toString(imagenVersionada.getImagenId()));
                ImagenesImplementor.getInstance().actualizarEstadoImagenDespuesVerisionamiento(imagenVersionada.getImagenId());
            }
            VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_IMAGENES);
            return true;
        } catch (Exception e) {
            Log.i("IMA ERROR VERSION: ", e.toString());
        }
        return false;
    }


    /**
     * Genera un Json Array con la lista de formularios de auditoria y los envia mediante PUT.
     *
     * @return true, si se logro hacer el versionamiento, false hubo error.
     */
    private boolean enviarDatosAuditor(String pUid) {
        try {
            String[] arrayOperadoresAuditor = getResources().getStringArray(R.array.array_opciones_auditor);

            int nFormulariosSinContestar = PreguntasAuditorImplementor.getInstance().obtenerCantidadFormNoVersionados();
            if (nFormulariosSinContestar == 0) {
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_FORMULARIOS_AUDITOR);
                return true;
            }

            List<Negocio> lCodigosNegocios = NegociosImplementor.getInstance().obtenerListaNegociosPorUsuario();
            if (lCodigosNegocios.size() > 0) {
                for (Negocio negocio : lCodigosNegocios) {
                    String sJsonArrayFormulariosAudit = PreguntasAuditorImplementor.getInstance().arrayFormulariosVersionamientoAuditor(pUid, arrayOperadoresAuditor, negocio.get_nNegocioId());
                    if (sJsonArrayFormulariosAudit != null) {
                        String sUrl = SgprService.getInstance().getFormularioUrl();
                        String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArrayFormulariosAudit);
                        String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
                        if (sCodigoRespuesta.equals("0")) {
                            PreguntasAuditorImplementor.getInstance().actualizarEstadoActivoPregunta(negocio.get_nNegocioId(), 0);
                        } else {
                            Log.i("FORM ERROR VERSION: ", sRespuesta);
                            return false;
                        }
                    }
                }
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(Constantes.VERSION_NUEVOS_COMERCIOS);
                return true;
            }
        } catch (Exception e) {
            Log.i("FOR AUDIT ERR VER: ", e.toString());
        }
        return false;
    }

    /**
     * Genera un Json Array con la lista de ubicaciones con el seguimiento PUT.
     */
    public Boolean enviarDatosSeguimiento() {
        try {
            JSONObject sJsonArraySeguimientos = SeguimientoImplementor.getInstance().obtenerSeguimientosParaEnviar();
            String sUrl = SgprService.getInstance().getSendInfoSavedTracking();
            String sRespuesta = RestHelper.getInstance().PUT(sUrl, sJsonArraySeguimientos.toString());
            String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
            if (sCodigoRespuesta.equals("0")) {
                SeguimientoImplementor.getInstance().borrarSeguimientosUsuario();
                return true;
            } else {
                Log.i("FORM ERROR VERSION: ", sRespuesta);
                return false;
            }
            //}
        } catch (Exception e) {
            Log.i("FORM ERROR VERSION: ", e.toString());
        }
        return false;
    }

    /**
     * Una vez que se han versionado los datos, estos se borran de la BD del telefono (Versionamiento, Bitacoras, Imagenes, Negocios y Rutas).
     */
    public void borrarDatosVersionados() {
        VersionamientoImplementor.getInstance().borrarTablaVersionamiento();
        PaginacionImplementor.getInstance().borrarPaginacionUsuario();
        NegociosImplementor.getInstance().borrarNegociosUsuario();
        RutaImplementor.getInstance().borrarRutasUsuario();
        BitacoraImplementor.getInstance().borrarBitacorasUsuario(-1);
        ImagenesImplementor.getInstance().borrarImagenesUsuario();
        ParametrosImplementor.getInstance().borrarParametros();
        PreguntasImplementor.getInstance().borrarPreguntas();
        PreguntasPresenciaMarcaImplementor.getInstance().borrarPreguntasPresenciaMarca(-1);
        PreguntasAuditorImplementor.getInstance().borrarPreguntas();

        //Terceros
        SupervTercerosImplementor.getInstance().borrarSupervicionesUsuario();
        UsersTercerosImplementor.getInstance().borrarUsuariosTerceros();
    }

    /**
     * Verifica que exista conexi�n a internet.
     *
     * @return
     */
    public Boolean verificarConexion() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    /**
     * Listener del bot�n para indicar el fin de una ruta. Actualiza el estado de la ruta a "Visitado" en la
     * tabla de Negocio.
     */
    public View.OnClickListener onFinNegocio = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.startAnimation(animAlpha);
            boolean hayNegocio = hayNegocioActivo();

            if (hayNegocio) {
                comprobarGPS();
                finalizarNegocio(_sLatitud, _sLongitud);
            } else
                Toast.makeText(getActivity(), Constantes.ERROR_NO_NEGOCIOS_ACTIVOS, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Verifica que el GPS est� activado para iniciar el negocio.
     */
    public void comprobarGPS() {
        _locationManager = Gps.getInstance().revisarGPS(this.getActivity(), _locationManager);
        if (_locationManager != null) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, Constantes.SET_GPS);
        } else {
            _locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
            if (respuestaLocalizacion != null) {
                _sLatitud = Double.toString(respuestaLocalizacion.getLatitude());
                _sLongitud = Double.toString(respuestaLocalizacion.getLongitude());
            } else {
                _sLatitud = Constantes.LATITUD_COSTA_RICA;
                _sLongitud = Constantes.LONGITUD_COSTA_RICA;
            }
        }
    }

    /**
     * Finaliza el negocio activo.
     */
    public void finalizarNegocio(String pLatitud, String pLongitud) {
        Negocio negocioActivo = NegociosImplementor.getInstance().obtenerNegocioActivo();
        BitacoraImplementor.getInstance().actualizarBitacora(negocioActivo.get_nNegocioId(), pLatitud, pLongitud);
        NegociosImplementor.getInstance().actualizarEstadoNegocio(negocioActivo.get_nNegocioId(), Constantes.ESTADO_NEGOCIO_VISITADO,
                Constantes.ESTADO_NEGOCIO_ACTIVO, Utils.obtenerFechaActual());
        Toast.makeText(getActivity(), Constantes.AVISO_FIN_PREGUNTAS, Toast.LENGTH_SHORT).show();
    }

    /*Interface para implementar el cambio de tab en el activity*/
    public interface OnSyncSuccessPass {
        public void changeToFirstTab();
    }


    /************** METODOS VERSIONAMIENTO TERCEROS *************************/

    /**
     * Genera un JsonArray con la lista de opciones seleccionadas para los formularios.
     * Envía el json para los formularios de terceros
     * @param pUid: User ID
     * @param pTipo: Tipo de versionamiento ("PU", "PR", "PM", etc)
     * @param numeroPasoVersion: Número de paso por el que va el versionamiento
     * @param service: Nombre del servicio a llamar ("SP20", "SP21", etc):
     * @return
     */
    private boolean enviarDatosFormulariosTerceros(String pUid, String pTipo, int numeroPasoVersion, String service){
        try {
            JSONArray arrayAtencion = SupervTercerosImplementor.getInstance().obtenerAtencionVersionamiento(pUid, pTipo);

            if (arrayAtencion == null) {
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(numeroPasoVersion);
                return true;
            }
            //Si el tipo es encuesta del punto de venta, se cambian los identificadores de tipo
            // por otro más corto para el SP:
            if(pTipo.equals(Constantes.ENCUESTA_PDV)){
                String jsonArrayString = arrayAtencion.toString();
                jsonArrayString = jsonArrayString.replace("chk_", "c");
                jsonArrayString = jsonArrayString.replace("rb_", "r");
                jsonArrayString = jsonArrayString.replace("et_", "e");

                jsonArrayString = jsonArrayString.replace("_", "");

                arrayAtencion = new JSONArray(jsonArrayString);
            }
            String sUrl = SgprService.getInstance().getSupervDistUrl();
            JSONObject jsonParaEnviar = JSONHelper.getInstance().obtenerJsonTercerosParaEnviar(arrayAtencion, service);
            String sRespuesta = RestHelper.getInstance().PUT(sUrl, jsonParaEnviar.toString());
            String sCodigoRespuesta = JSONHelper.getInstance().descifrarRespuestaVersionamiento(sRespuesta);
            if (sCodigoRespuesta.equals("0")) {
                SupervTercerosImplementor.getInstance().actualizarSuperviciones(pUid, pTipo);
                VersionamientoImplementor.getInstance().actualizarProcesoVersionamiento(numeroPasoVersion);
                return true;
            } else {
                Log.i("FORM ERROR VERSION: ", sRespuesta);
                return false;
            }
        } catch (Exception e) {
            Log.i("FOR AUDIT ERR VER: ", e.toString());
        }
        return false;
    }
}

