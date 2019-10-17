package com.ice.sgpr.entidades;

/**
 * Entidad con las propiedades de los parametros.
 * @author eperaza
 * Fecha de creacion: 23/09/2013.
 */
public class Parametro{
	int codigo;
	String sValor;
	String sCategoria;

	
	public Parametro(int pCodigo, String pValor, String pCategoria){
		this.codigo = pCodigo;
		this.sValor = pValor;
		this.sCategoria = pCategoria;
	}
	
	/* GET */
	public int getCodigo(){
		return codigo;
	}
	public String getValor(){
		return sValor;
	}
	public String getCategoria(){
		return sCategoria;
	}
	
	@Override
	public String toString() {
		return sValor;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parametro other = (Parametro) obj;
		if (codigo !=(other.codigo))
			return false;
		return true;
	}
}