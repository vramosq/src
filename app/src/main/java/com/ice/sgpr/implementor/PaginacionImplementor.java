package com.ice.sgpr.implementor;

import com.ice.sgpr.dataaccess.PaginacionDataAccess;
/**
 * Implementor para la consulta de datos relacionados con la paginacion.
 * @author eperaza
 * fecha de creacion:17/09/2013.
 */
public class PaginacionImplementor {
private PaginacionDataAccess _dataAccess;
private static PaginacionImplementor _instancia;
	
	private PaginacionImplementor()
	{
		_dataAccess = new PaginacionDataAccess();
	}
	
	public static PaginacionImplementor getInstance(){
		if(_instancia == null)
			_instancia = new PaginacionImplementor();
		return _instancia;
	}
	
	/**
	 * Se actualiza o inserta un nuevo campo en la tabla de paginacion.
	 * @param pPagina: Pagina actual.
	 */
	public void actualizarPagina(int pPagina){
		_dataAccess.openForWriting();
		int nIdRuta = RutaImplementor.getInstance().obtenerInformacionRuta().getId();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.actualizarPaginacion(nIdRuta, pPagina, sCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Obtiene la pagina actual de negocios de la ruta seleccionada.
	 * @return
	 */
	public int obtenerPaginaActual(){
		_dataAccess.openForWriting();
		int nRutaId = RutaImplementor.getInstance().obtenerInformacionRuta().getId();
		int nPagina = _dataAccess.obtenerPaginaActual(nRutaId);
		_dataAccess.close();
		return nPagina;
	}
	
	/**
	 * Borra la tabla de paginacion de un usuario
	 */
	public void borrarPaginacionUsuario(){
		_dataAccess.openForWriting();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarPaginacionUsuario(sCodigoUsuario);
		_dataAccess.close();
	}
}