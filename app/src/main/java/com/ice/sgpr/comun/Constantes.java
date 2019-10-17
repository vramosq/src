package com.ice.sgpr.comun;

public class Constantes {
    public static final String TITULO_APP = "Sgpr";
    public static final String TITULO_LOGIN = "Login";
    public static final String TITULO_NEGOCIOS = "Negocios";
    public static final String TITULO_RUTAS = "Rutas";
    public static final String TITULO_TERCEROS = "Supervisión Terceros";
    public static final String TAB_RUTEO = "Ruteo";
    public static final String TAB_MAPA = "Mapa";
    public static final String TAB_INFORMACION = "Información";
    public static final String TAB_SINCRONIZACION = "Sincronización";
    public static final String TAB_FORMULARIO = "Formulario";
    public static final String TAB_BUSCADOR = "Buscador";
    public static final String TAB_IMAGENES = "Imágenes";
    public static final String TAB_OBSERVACIONES = "Observaciones";
    public static final String TAB_PRES_MARCA = "Pres. Marca";
    public static final String TAB_AUDITORIA = "Auditoría";
    public static final String TAB_FORMULARIO_INDEF = "Formulario";
    public static final String LOG_TAG = "Sgpr Log.";
    public static final int ZOOM_DEFAULT_MAPA = 17;
    public static final int ZOOM_INICIAL_MAPA = 10;
    public static final int ZOOM_FINAL_MAPA = 13;
    public static final String LATITUD_COSTA_RICA = "9.9333333";
    public static final String LONGITUD_COSTA_RICA = "-84.0833333";
    public static final String TAG_LISTA_RUTAS = "listaRutas";
    public static final String TITULO_MARCADOR_NEGOCIO = "Cómo llegar...";
    public static final int ESTADO_NEGOCIO_SIN_VISITAR = 0;
    public static final int ESTADO_NEGOCIO_ACTUAL = 1;
    public static final int ESTADO_NEGOCIO_VISITADO = 2;
    public static final int ESTADO_NEGOCIO_SIGUIENTE = 3;
    public static final int ESTADO_NEGOCIO_ACTIVO = 1;
    public static final int ESTADO_NEGOCIO_INACTIVO = 0;
    public static final int ESTADO_RUTA_PENDIENTE = 0;
    public static final int ESTADO_RUTA_VISITADA = 1;
    public static final String AVISO_NEGOCIO_SELECCIONADO = "Hay un negocio activo.";
    public static final String AVISO_NEGOCIO_YA_VISITADO = "Este negocio ya fue visitado.";
    public static final String AVISO_NEGOCIO_NO_SELECCIONADO = "Debe seleccionar un negocio.";
    public static final String AVISO_NEGOCIO_CARGADO = "Datos del negocio cargados";
    public static final String AVISO_NEGOCIO_INICIADO = "Iniciado";
    public static final String AVISO_NEGOCIO_FINALIZADO = "Finalizado";
    public static final String AVISO_NEGOCIO_INCORRECTO = "Negocio incorrecto, debe seleccionar el siguiente negocio de la ruta.";
    public static final int INDICADOR_PREGUNTA_CONTESTADA = 1;
    public static final String AVISO_ERROR_GUARDANDO_PREGUNTA = "Debe responder la pregunta para guardarla.";
    public static final String AVISO_ERROR_PREGUNTAS_PENDIENTES = "Debe terminar de contestar las preguntas del formulario.";
    public static final String AVISO_ERROR_PREGUNTAS_PM_PENDIENTES = "Debe contestar al menos una pregunta.";
    public static final String AVISO_ERROR_CARGANDO_RUTA = "Error cargando la ruta.";
    public static final String AVISO_FIN_PREGUNTAS = "Negocio finalizado.";
    public static final String AVISO_MAXIMO_IMAGENES = "Se ha alcanzado el máximo de imágenes para el negocio.";
    public static final String FIN_DE_PREGUNTAS_FORMULARIO = "Finalizar";
    public static final String FIN_FORMULARIO_SEGUNDA_PREG = "Finalizar el formulario aquí";
    public static final int CANTIDAD_DE_PREGUNTAS = 25;
    public static final int CANTIDAD_DE_PREGUNTAS_PM = 4;
    public static final String AVISO_FORMULARIO_GUARDADO = "Formulario guardado.";
    public static final String AVISO_FORMULARIO_FINALIZADO = "Formulario finalizado.";
    public static final String AVISO_RUTA_INICIADA = "Datos de la ruta cargados.";
    public static final String AVISO_RUTA_FINALIZADA = "Ruta finalizada.";
    public static final String TEXTO_NO_RESPONDO = "N/R";
    public static final int COLOR_SEMITRANSPARENTE_MAPA = 0x4000948F;
    public static final int ESTADO_TIPO_PREGUNTA = 0;
    public static final int ESTADO_PREGUNTA_PRINCIPAL = 1;
    public static final int ESTADO_PREGUNTA_ANIDADA = 2;
    public static final int ESTADO_PREGUNTA_CON_VALOR = 3;
    public static final String ERROR_VALOR_INVALIDO = "El valor ingresado no es válido.";
    public static final int ESTADO_PREGUNTA_SIN_VALOR_INGRESADO = 0;
    public static final String ERROR_LOGIN_USUARIO_INVALIDO = "El usuario no existe.";
    public static final String ERROR_LOGIN_PASS_INVALIDO = "La contraseña no concuerda.";
    public static final String ERROR_LOGIN_VACIO = "Debe ingresar el usuario y la contraseña.";
    public static final String ERROR_LOGIN_CONEXION = "Error de conexión, verifique su conexión a internet e Intente de nuevo.";
    public static final String TEXTO_BOTON_MAPA_CARGAR_NEG = "Más Negocios";
    public static final String TEXTO_BOTON_MAPA_BUSCAR_NEG = "Buscar Negocios";
    public static final String ERROR_NO_MAS_NEGOCIOS = "No hay más negocios qué cargar.";
    public static final String FECHA_POR_DEFECTO = "01-01-1900 23:59:59";
    public static final String[] ARREGLO_ACTUALIZANDO = {"Actualizando Nuevos Comercios...", "Actualizando Comercios...", "Actualizando Formularios Presencia de Marca...",
            "Actualizando Bitácoras...", "Actualizando Imágenes...", "Actualizando Formulario Auditoría...", "Actualizando Supervisiones de Distribuidores...",
            "Actualizando Superviciones de Marca...", "Actualizando Formularios de Publicidad...", "Actualizando Formularios de Promocionales...", "Actualizando Formularios de Pop Especial...",
            "Actualizando Observaciones...", "Actualizando Formularios de Atención...", "Actualizando Formularios de Incidencias...", "Actualizando Formularios de Socios PDV...", "Actualizando Formularios de PDV...",
            "Obteniendo Rutas...", "Obteniendo Parámetros...", "Actualizando Lista de usuarios..", "Actualizando Tipos de supervisión...", "Actualizando Socios Comerciales..." };
    public static final int RUTA_ID_NUEVOS_NEGOCIOS = 0;
    public static final String AVISO_INGRESAR_DATOS_NUEVO_NEGOCIO = "Ingrese los datos del nuevo negocio y pulse \"Grabar\"";
    public static final String AVISO_SELECCIONAR_RUTA = "Debe seleccionar una ruta.";
    public static final String ERROR_NO_NEGOCIOS_ACTIVOS = "No hay negocios activos.";
    public static final String AVISO_SINCRONIZADO_EXITOSO = "Datos sincronizados exitosamente";
    public static final String ERROR_SINCRONIZADO = "Error sincronizando los datos: ";
    public static final String ERROR_SINCRONIZADO_NEGOCIO_ACTIVO = "Existe un negocio activo, debe finalizar la visita para continuar...";

    //GPS
    public static final String ERROR_ACTIVAR_GPS = "Por favor active el GPS para una mejor ubicación.";
    public static final int PICK_LOCATION = 101;
    public static final int SET_GPS = 102;
    public static final String ERROR_GPS_NO_ACT_INI_NEGOCIO = "Es necesario activar el GPS para iniciar con el negocio.";
    public static final String ERROR_GPS_NO_ACT_FIN_NEGOCIO = "Es necesario activar el GPS para finalizar con el negocio.";
    public static final String ADVERTENCIA_ACTIVAR_GPS = "Debe activar el GPS para utilizar la aplicación.";
    public static final String ADVERTENCIA_DEBE_SINCRONIZAR = "Debe sincronizar primero.";
    public static final String AVISO_ACTIVAR_SEGUIMIENTO = "¿Desea activar el seguimiento GPS?";
    public static final String AVISO_SEGUIMIENTO_ACTIVADO = "Seguimiento GPS activado";

    //Versionamiento
    public static final int VERSION_NUEVOS_COMERCIOS = 0;
    public static final int VERSION_COMERCIOS = 1;
    public static final int VERSION_FORMULARIOS_PM = 2;
    public static final int VERSION_BITACORAS = 3;
    public static final int VERSION_IMAGENES = 4;
    public static final int VERSION_FORMULARIOS_AUDITOR = 5;
    /********
     * Terceros
     *******/
    public static final int VERSION_TERCEROS_SUPERV_DIST = 6;
    public static final int VERSION_TERCEROS_PRES_MARCA = 7;
    public static final int VERSION_TERCEROS_PUBLICIDAD = 8;
    public static final int VERSION_TERCEROS_PROMOCIONAL = 9;
    public static final int VERSION_TERCEROS_POP_ESPECIAL = 10;
    public static final int VERSION_TERCEROS_OBSERVACIONES = 11;
    public static final int VERSION_TERCEROS_ATENCION = 12;
    public static final int VERSION_TERCEROS_INCIDENCIAS = 13;
    public static final int VERSION_TERCEROS_SOCIO_PDV = 14;
    public static final int VERSION_TERCEROS_FORMULARIOS_PDV = 15;

    //Codigo respuesta WS:
    public static final int RESPUESTA_USUARIO_VALIDO = 0;
    public static final int RESPUESTA_PASS_INVALIDO = 1;
    public static final int RESPUESTA_NO_ENCUENTRA_USUARIO = -1;

    //Camara
    public static final int IMAGEN_DESDE_CAMARA = 1;
    public static final int IMAGEN_DESDE_ARCHIVO = 2;
    public static final String SGPR_FOLDER_IMAGEN = "/Sgpr";
    public static final String SGPR_PREFIJO_IMAGEN = "sgpr_";
    public static final String SGPR_EXTENSION_IMAGEN = ".jpg";
    public static final String ERROR_FOTO = "Ha ocurrido un error capturando la foto.";

    public static final String VACIO = "";
    public static final int TEST_NUMERO_PREGUNTA = 0;
    public static final String DEFAULT_ENCODING = "ISO-8859-1";
    public static final String DEFAULT_ENCODING_BUSINESS = "UTF-8";

    //ROLES
    public static final String ROL_USUARIO_NORMAL = "0"; //	Rol de usuario normal: Ve todas las preguntas del formulario
    public static final String ROL_USUARIO_AUDITOR = "1"; // Rol de usuario auditor: Ve un tipo especial de preguntas
    public static final String ROL_USUARIO_PRINCIPAL = "2"; // Rol de usuario principal: Ve ambos formularios.

    public static final String[] ARREGLO_PREGUNTAS_AUDITORIA = {"PRE5", "PRE6", "PRE7"};

    //Colores
    public static final String COLOR_KOLBI = "C_kolbi";
    public static final String COLOR_CLARO = "C_claro";
    public static final String COLOR_MOVISTAR = "C_movistar";
    public static final String COLOR_TUYOMOVIL = "C_tuyoMovil";
    public static final String COLOR_FULLMOVIL = "C_fullMovil";
    public static final String COLOR_NINGUNO = "C_ninguno";
    public static final String COLOR_TODOS = "C_todos";
    public static final String COLOR_KOLBI_OTRO = "C_kolbiOtro";
    public static final String COLOR_CLARO_MOVISTAR = "C_claroMovistar";
    public static final String COLOR_NO_KOLBI = "C_No_kolbi";

    public static final String TAG_SGPR = "SGPR_LOG";

    public static final String ESTADO_NO_APLICA_OPERADOR = "EST_ROT10";
    public static final String RESPUESTA_NO_APLICA_OPERADOR = "ROT10";

    public static final String LOGIN_ERROR = "Usuario inválido o error de conexión";

    //Terceros
    public static final String CHK_SUP_DIST_FORMAT = "chk_*OPC_*OPER";
    public static final String CHK_PRES_MARCA_FORMAT = "chk_pres_marc_*OPC_*OPER";
    public static final String ET_PUBLICIDAD_FORMAT = "et_publicidad_";
    public static final String ET_PROMOCIONAL_FORMAT = "et_prom_";
    public static final String ET_POP_FORMAT = "et_pop_especial_";
    public static final String SPN_SOCIO_PDV_FORMAT = "chk_spdv_";

    public static final String TAB_ATENCION = "Atención";
    public static final String TAB_SUP_DIST = "Supervisión Distribuidores";
    public static final String TAB_SOCIO_PDV = "Asig. Socio PDV";
    public static final String TAB_SUP_MARCA = "Supervisión Marca";
    public static final String TAB_INCIDENCIAS = "Incidencias";
    public static final String TAB_ENCUESTA_PDV = "Encuesta PDV";

    public static final String TITLE_PRES_MARCA = "Presencia de Marca";
    public static final String TITLE_PUBLICIDAD = "Publicidad";
    public static final String TITLE_PROMOCIONAL = "Promocional";
    public static final String TITLE_POP_ESPECIAL = "POP Especial";
    public static final String TITLE_OBSERVACIONES = "Observaciones";

    //Opciones formularios:
    public static final String NA_OPTION = "na", KOLBI_OPTION = "k", CLARO_OPTION = "c",
            MOVISTAR_OPTION = "m", TUYO_OPTION = "t", FULL_OPTION = "f", INTERES_OPTION = "in";
    public static final String ERROR_JSON_TERCEROS = "Ha ocurrido un error obteniendo las opciones seleccionadas. Intenta de nuevo.";
    public static final String SAVED_INFO = "Información guardada";

    //Control Json: Siglas para terceros:
    //Terceros
    public static final String SUPERV_DIST = "SD";
    public static final String PRES_MARCA = "PM";
    public static final String PUBLICIDAD = "PU";
    public static final String PROMOCIONAL = "PR";
    public static final String POP_ESPECIAL = "POP";
    public static final String OBSERVACIONES = "OBS";
    public static final String INCIDENCIAS = "IN";
    public static final String ATENCION = "ATT";
    public static final String OPTION = "O";
    public static final String SOCIO_PDV = "SPDV";
    public static final String ENCUESTA_PDV = "EPDV";
    //public static final String FORMULARIOS_PDV = "FPDV";

    public static final int VERSION_AMOUNT = 16;
}
