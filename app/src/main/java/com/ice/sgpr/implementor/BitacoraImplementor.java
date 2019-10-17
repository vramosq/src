package com.ice.sgpr.implementor;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

import com.ice.sgpr.comun.Utils;
import com.ice.sgpr.dataaccess.BitacoraDataAccess;
import com.ice.sgpr.entidades.Bitacora;
/**
 * Implementor para la consulta de datos relacionados con Negocios.
 * @author eperaza
 * fecha de creacion: 07/08/2013.
 */
@SuppressLint("SimpleDateFormat")
public class BitacoraImplementor {
private BitacoraDataAccess _dataAccess;
private static BitacoraImplementor _instancia;
	
	private BitacoraImplementor()
	{
		_dataAccess = new BitacoraDataAccess();
	}
	
	public static BitacoraImplementor getInstance(){
		if(_instancia == null)
			_instancia = new BitacoraImplementor();
		return _instancia;
	}
	
	/**
	 * Se envia la latitud y longitud actuales y registra en la bitacora.
	 * @param pLatitud
	 * @param pLongitud
	 * @param pNuevoNegocio: Indica si la bitacora que se esta guardando es de un nuevo negocio, en caso de ser asi, la ruta registrada es 0.
	 */
	public void insertarNuevaBitacora(String pLatitud, String pLongitud, boolean pNuevoNegocio){
		_dataAccess.openForWriting();
		String sHoraYFecha = Utils.obtenerFechaActual();
		int nIdRuta = 0;
		if(!pNuevoNegocio)
			nIdRuta = RutaImplementor.getInstance().obtenerIdRutaSeleccionada();
		int nIdNegocio = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.insertarNuevaBitacora(nIdRuta, nIdNegocio, sHoraYFecha,pLatitud, pLongitud, sCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Obtiene una lista con los eventos de la bitacora de la ruta actual.
	 */
	public List<Bitacora> obtenerBitacora(){
		_dataAccess.openForWriting();
		int nRutaId = RutaImplementor.getInstance().obtenerIdRutaSeleccionada();
		List<Bitacora> lBitacora = _dataAccess.obtenerBitacora(nRutaId);
		_dataAccess.close();
		return lBitacora;
	}
	
	/**
	 * Se obtiene el Id de negocio junto a la hora y fecha actuales para actualizar la bitacora.
	 */
	public void actualizarBitacora(int pNegocioId, String pLatitud, String pLongitud){
		String sHoraYFecha = Utils.obtenerFechaActual();
		_dataAccess.openForReading();
		_dataAccess.actualizarBitacora(pNegocioId, sHoraYFecha, pLatitud, pLongitud);
		_dataAccess.close();
	}
	
	/**
	 * Consulta la lista de Id de rutas asignadas al usuario y consulta las bitacoras para versionamiento.
	 * @return
	 */
	public List<Bitacora> obtenerBitacorasVersionamiento(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Bitacora> lBitacoras = _dataAccess.obtenerBitacorasVersionamiento(sCodigoUsuario);
		_dataAccess.close();
		return lBitacoras;
	}
	
	/**
	 * Actualiza el estado de la bitacora despues del versionamiento..
	 * @param pBitacoraId
	 */
	public void actualizarEstadoBitacoraDespuesVersionamiento(int pBitacoraId){
		_dataAccess.openForReading();
		_dataAccess.actualizarEstadoBitacoraVersionamiento(pBitacoraId);
		_dataAccess.close();
	}
	
	/**
	 * Actualiza el ID del negocio nuevo en la bit�acora.
	 * @param pCodigos
	 */
	public void actualizarIdNuevoNegocio(String[] pCodigos){
		_dataAccess.openForReading();
		_dataAccess.actualizarIdBitacoraDespuesVersionamiento(Integer.parseInt(pCodigos[0]), Integer.parseInt(pCodigos[1]));
		_dataAccess.close();
	}
	
	/**
	 * Borra las bitacoras de un determinado usuario.
	 * @param pIdNegocio Id de la bitacora del negocio a borrar. -1 = Todos
	 */
	public void borrarBitacorasUsuario(int pIdNegocio){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarBitacoraUsuario(sCodigoUsuario, pIdNegocio);
		_dataAccess.close();
	}

	/**
	 * Obtiene la fecha/hora de inicio de atención de un negocio.
	 */
	public Date obtenerFechaNegocio(int negocioId){
		_dataAccess.openForWriting();
		Date fechaHora = _dataAccess.obtenerFechaNegocio(negocioId);
		_dataAccess.close();
		return fechaHora;
	}
}