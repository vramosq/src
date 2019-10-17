package com.ice.sgpr.implementor;

import java.util.List;

import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.comun.Utils;
import com.ice.sgpr.dataaccess.NegociosDataAccess;
import com.ice.sgpr.entidades.Negocio;
import com.ice.sgpr.error.SgprError;
import com.ice.sgpr.error.SgprException;
import com.ice.sgpr.service.JSONHelper;
import com.ice.sgpr.service.RestHelper;
import com.ice.sgpr.service.SgprService;
/**
 * Implementor para la consulta de datos relacionados con Negocios.
 * @author eperaza
 * fecha de creaci�n: 07/08/2013.
 */
public class NegociosImplementor {
private NegociosDataAccess _dataAccess;
private static NegociosImplementor _instancia;
	
	private NegociosImplementor()
	{
		_dataAccess = new NegociosDataAccess();
	}
	
	public static NegociosImplementor getInstance(){
		if(_instancia == null)
			_instancia = new NegociosImplementor();
		return _instancia;
	}
	
	/**
	 * Se env�a el ID de la ruta para obtener la lista de negocios.
	 * @param pRutaId
	 * @return
	 */
	public List<Negocio> obtenerListaNegocios(){
		_dataAccess.openForReading();
		int nIdRuta = RutaImplementor.getInstance().obtenerIdRutaSeleccionada();
		List<Negocio> lNegocios = _dataAccess.obtenerListaNegocios(nIdRuta);
		_dataAccess.close();
		return lNegocios;
	}
	
	/**
	 * Se obtiene la informaci�n del negocio activo.
	 * @return: Negocio seleccionado en el mapa.
	 */
	public Negocio obtenerNegocioActivo(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		Negocio negocio = _dataAccess.obtenerNegocioActivo(sCodigoUsuario);
		_dataAccess.close();
		return negocio;
	}
	
	/**
	 * Se actualiza el estado del negocio.
	 * @param pNegocioId
	 * @param pEstado: 0 = Sin visitar, 1 = actual, 2 = visitado.
	 */
	public void actualizarEstadoNegocio(int pNegocioId, int pEstado, int pNegocioActivo, String pFecha){
		_dataAccess.openForReading();
		_dataAccess.actualizarEstadoNegocio(pNegocioId, pEstado, pNegocioActivo, pFecha);
		_dataAccess.close();
	}
	
	/**
	 * Actualiza el estado de un negocio.
	 * @param pNegocio: Datos del negocio.
	 */
	public void actualizarNegocios(Negocio pNegocio){
		_dataAccess.openForWriting();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.actualizarNegociosRuta(
				pNegocio.get_nRutaId(), 
				pNegocio.get_nNegocioId(), 
				pNegocio.get_sLatitud(), 
				pNegocio.get_sLongitud(), 
				pNegocio.get_sNombre(), 
				pNegocio.get_sDireccion(), 
				pNegocio.get_nEstado(), 
				pNegocio.get_nActivo(), 
				pNegocio.get_sDescripcion(), 
				pNegocio.get_nTipoComercio(), 
				pNegocio.get_nPrioridad(), 
				pNegocio.get_sTelefono(), 
				pNegocio.get_sNombreContacto(), 
				pNegocio.get_sTelefonoContacto(), 
				pNegocio.get_sUltimaVisita(),
				pNegocio.get_nActualizado(),
				pNegocio.get_sCelularContacto(),
				sCodigoUsuario,
				pNegocio.is_bHabilitado(),
				pNegocio.get_nCodigoDistrito(),
				pNegocio.get_nCodigoCanton(),
				pNegocio.get_nCodigoProvincia(),
				pNegocio.get_sObservaciones(),
				pNegocio.get_sFechaObservaciones(),
				pNegocio.get_nUsrModificaObservacion(),
				pNegocio.get_sEmail()
				);
		_dataAccess.close();
	}
	
	
	/**
	 * Se obtiene la lista de negoios actualizados en la BD.
	 * @return
	 */
	public List<Negocio> obtenerNegociosVersionamiento(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Negocio> listaNegocios = _dataAccess.obtenerNegociosVersionamiento(sCodigoUsuario);
		_dataAccess.close();
		return listaNegocios;
	}
	
	/**
	 * Lista de nuevos negocios agregados a la BD.
	 * @return
	 */
	public List<Negocio> obtenerNuevosNegociosVersionamiento(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Negocio> listaNegocios = _dataAccess.obtenerNuevosNegociosVersionamiento(sCodigoUsuario);
		_dataAccess.close();
		return listaNegocios;
	}
	
	/**
	 * Actualiza el estado "Actualizado" del negocio a 0 luego de la sincronizaci�n.
	 * @param pIdNegocio
	 */
	public void actualizarEstadoDespuesVerisionamiento(int pIdNegocio){
		_dataAccess.openForReading();
		_dataAccess.actualizarEstadoNegocioDespuesVersionamiento(pIdNegocio);
		_dataAccess.close();
	}
	
	/**
	 * Actualiza el ID del nuevo negocio.
	 * @param pIdNegocio
	 */
	public void actualizarIdNuevoNegocio(String[] pCodigos){
		_dataAccess.openForReading();
		_dataAccess.actualizarIdNegocioDespuesVersionamiento(Integer.parseInt(pCodigos[0]), Integer.parseInt(pCodigos[1]));
		_dataAccess.close();
	}
	
	/**
	 * Obtiene una lista de negocios producto de una b�squeda.
	 * @param pCriterioBusqueda
	 * @return
	 * @throws com.ice.sgpr.error.SgprException
	 */
	public List<Negocio> buscarNegocios(String pCriterioBusqueda, int pIndex) throws SgprException
	{
		try
		{
			String sRespuesta = RestHelper.getInstance().GET(SgprService.getInstance().getBusquedaNegocioUrl(pCriterioBusqueda.replace(" ", "%20"), pIndex), true);
			return JSONHelper.getInstance().obtenerNegociosBusquedaDesdeJson(sRespuesta);
		}
		catch(SgprException kolbiEx)
		{
			kolbiEx.LogError();
			throw kolbiEx;
		}
		catch(Exception ex)
		{
			SgprException sgprEx = new SgprException(
        			SgprError.NEGOCIOS_ERROR, 
        			"Error buscando el negocio: " + pCriterioBusqueda, 
        			ex);
			sgprEx.LogError();
			throw sgprEx;
		}
	}
	
	/**
	 * Verifica si un negocio buscado se encuentra en la BD.
	 * @param pNegocioId
	 * @return
	 */
	public Boolean existeNegocio(int pNegocioId){
		_dataAccess.openForReading();
		Boolean bExiste = _dataAccess.existeNegocio(pNegocioId);
		_dataAccess.close();
		return bExiste;
	}
	
	/**
	 * Borra los negocios de un determinado usuario.
	 */
	public void borrarNegociosUsuario(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarNegociosUsuario(sCodigoUsuario, -1);
		_dataAccess.close();
	}
	
	/**
	 * Actualiza el estado del negocio (Habilitado/Desabilitado).
	 * @param pIdNegocio
	 */
	public void habilitarDesabilitarNegocio(int pIdNegocio, int pEstado){
		_dataAccess.openForReading();
		_dataAccess.habilitarDesabilitarNegocio(pIdNegocio, pEstado);
		_dataAccess.close();
	}
	
	/**
	 * Descarta un negocio.
	 * Si es un negocio nuevo (ruta 0) se borra.
	 * Si es un negocio existente solamente se pasa a estado inactivo.
	 * @param pIdNegocio
	 * @param pRutaId
	 */
	public void descartarNegocio(int pIdNegocio, int pRutaId){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		if(pRutaId == 0)
			_dataAccess.borrarNegociosUsuario(sCodigoUsuario, pIdNegocio);
		else
			_dataAccess.actualizarEstadoNegocio(pIdNegocio, Constantes.ESTADO_NEGOCIO_SIN_VISITAR, Constantes.ESTADO_NEGOCIO_INACTIVO, Utils.obtenerFechaActual());
		_dataAccess.close();
	}
	
	/**
	 * Se agregan los comentarios
	 * @param pObservacion
	 * @param pNegocioId
	 */
	public void agregarObservaciones(String pObservacion, int pNegocioId){
		_dataAccess.openForWriting();
		String sHoraYFecha = Utils.obtenerFechaActual();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.agregarObservacionesRuta(pObservacion, pNegocioId, sHoraYFecha, sCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Retorna las observaciones de un negocio.
	 * @param pNegocioId
	 * @return
	 */
	public String obtenerObservaciones(int pNegocioId){
		_dataAccess.openForWriting();
		String observaciones = _dataAccess.obtenerObservaciones(pNegocioId);
		_dataAccess.close();
		return observaciones;
	}
	
	public List<Negocio> obtenerListaNegociosPorUsuario(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Negocio> lNegocios = _dataAccess.obtenerListaNegociosPorUsusario(sCodigoUsuario);
		_dataAccess.close();
		return lNegocios;
	}
}
