package com.ice.sgpr.implementor;

import java.util.List;
import com.ice.sgpr.dataaccess.RutaDataAccess;
import com.ice.sgpr.entidades.Ruta;

public class RutaImplementor {
private RutaDataAccess _dataAccess;
private static RutaImplementor _instancia;
	
	private RutaImplementor()
	{
		_dataAccess = new RutaDataAccess();
	}
	
	public static RutaImplementor getInstance(){
		if(_instancia == null)
			_instancia = new RutaImplementor();
		return _instancia;
	}
	
	/**
	 * Obtiene la lista de rutas
	 * @return
	 */
	public List<Ruta> obtenerListaRutas(){
		_dataAccess.openForWriting();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Ruta> lRutas = _dataAccess.obtenerRutas(sCodigoUsuario);
		_dataAccess.close();
		return lRutas;
	}
	
	/**
	 * Se obtiene el ID de la ruta seleccionada.
	 * @return 
	 */
	public Ruta obtenerInformacionRuta(){
		_dataAccess.openForWriting();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		Ruta rutaInfo = _dataAccess.obtenerInformacionRuta(sCodigoUsuario);
		_dataAccess.close();
		return rutaInfo;	
	}
	
	/**
	 * Toma la lista de rutas provenientes del WS y las almacena en la BD del dispositivo.
	 * @param pRutas
	 */
	public void insertarRutas(List<Ruta> pRutas){
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.openForWriting();
		for(Ruta ruta:pRutas){
			_dataAccess.insertarRuta(ruta.getId(), ruta.getNombre(), ruta.getDescripcion(), ruta.getFecha(), ruta.getFrecuencia(),
					ruta.getPendiente(), sCodigoUsuario, ruta.getLatitud(), ruta.getLongitud(), ruta.getSeleccionada());
		}
		_dataAccess.close();
	}
	
	/**
	 * Borra todas las rutas de un usuario.
	 */
	public void borrarRutasUsuario(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarRutasUsuario(sCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Retorna el Id de la ruta seleccionada.
	 * @return
	 */
	public int obtenerIdRutaSeleccionada(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		int nRutaSeleccionada = _dataAccess.obtenerIdRutaSeleccionada(sCodigoUsuario);
		_dataAccess.close();
		return nRutaSeleccionada;
	}
	
	/**
	 * Activa o desactiva una ruta (Iniciar/Finalizar).
	 */
	public void activarDesactivarRuta(int pIdRuta, int pEstado){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		int pRutaActiva = _dataAccess.obtenerIdRutaSeleccionada(sCodigoUsuario);
		if(pRutaActiva != 0){
			_dataAccess.activarDesactivarRuta(pRutaActiva, 0, sCodigoUsuario);
		}
		_dataAccess.activarDesactivarRuta(pIdRuta, pEstado, sCodigoUsuario);
		_dataAccess.close();
	}
}
