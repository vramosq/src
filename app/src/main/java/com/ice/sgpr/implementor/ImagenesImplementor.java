package com.ice.sgpr.implementor;

import java.util.List;

import android.graphics.Bitmap;

import com.ice.sgpr.dataaccess.ImagenesDataAccess;
import com.ice.sgpr.entidades.Imagen;

public class ImagenesImplementor {
private ImagenesDataAccess _dataAccess;
private static ImagenesImplementor _instancia;
	
	private ImagenesImplementor()
	{
		_dataAccess = new ImagenesDataAccess();
	}
	
	public static ImagenesImplementor getInstance(){
		if(_instancia == null)
			_instancia = new ImagenesImplementor();
		return _instancia;
	}
	
	/**
	 * Se envia la imagen capturada para ser almacenada en la BD.
	 */
	public void insertarImagen(byte[]pImagen){
		_dataAccess.openForWriting();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.insertarNuevaImagen(nNegocioId, pImagen, sCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Retorna la lista de imagenes.
	 * @return
	 */
	public List<Bitmap> obtenerListaImagenes(){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		List<Bitmap> lImagenes = _dataAccess.obtenerImagenes(nNegocioId);
		_dataAccess.close();
		return lImagenes;
	}
	
	/**
	 * Obtiene la cantidad de imagenes de un determinado negocio.
	 * @return
	 */
	public int obtenerCantidadImagenes(){
		_dataAccess.openForReading();
		int nNegocioId = NegociosImplementor.getInstance().obtenerNegocioActivo().get_nNegocioId();
		int nCantidad = _dataAccess.obtenerCantidadImagenes(nNegocioId);
		_dataAccess.close();
		return nCantidad;
	}
	
	/**
	 * Retorna la lista de imagenes.
	 * @return
	 */
	public List<Imagen> obtenerImagenesVersionamiento(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		List<Imagen> lImagenes = _dataAccess.obtenerImagenesVersionamiento(sCodigoUsuario);
		_dataAccess.close();
		return lImagenes;
	}
	
	/**
	 * Actualiza el ID del negocio nuevo en la imagen.
	 * @param pCodigos
	 */
	public void actualizarIdNuevoNegocio(String[] pCodigos){
		_dataAccess.openForReading();
		_dataAccess.actualizarIdImagenVersionamiento(Integer.parseInt(pCodigos[0]), Integer.parseInt(pCodigos[1]));
		_dataAccess.close();
	}
	/**
	 * Actualiza el estado "Actualizado" de la imagen a 0 luego de la sincronizacion.
	 */
	public void actualizarEstadoImagenDespuesVerisionamiento(int pIdImagen){
		_dataAccess.openForReading();
		_dataAccess.actualizarEstadoImagenVersionamiento(pIdImagen);
		_dataAccess.close();
	}
	
	/**
	 * Se borran las imagenes tomadas por un usuario.
	 */
	public void borrarImagenesUsuario(){
		_dataAccess.openForReading();
		String sCodigoUsuario = UsuariosImplementor.getInstance().obtenerUsuarioLogueado()[0];
		_dataAccess.borrarImagenesUsuario(sCodigoUsuario);
		_dataAccess.close();
	}
	
	/**
	 * Borra las imagenes de un negocio.
	 */
	public void borrarImagenesNegocio(int pCodigoNegocio){
		_dataAccess.openForReading();
		_dataAccess.borrarImagenesNegocio(pCodigoNegocio);
		_dataAccess.close();
	}
}
