package com.ice.sgpr.entidades;

/**
 * Entidad con las propiedades basicas de un formulario.
 * @author eperaza
 * Fecha de creacion 13/08/2013.
 */
public class Formulario {
	private String _sPreguntaPrincipal;
	private Boolean _bMarcado;
	private String _sPreguntaAnidadas;
	private int _nNumeroPregunta;
	private String _sEstados;
	
	public Formulario (String pPreguntaPrincipal, Boolean pMarcada, String pPreguntasAnidadas, int pNumeroPregunta, String pEstados){
		_sPreguntaPrincipal = pPreguntaPrincipal;
		_bMarcado = pMarcada;
		_sPreguntaAnidadas = pPreguntasAnidadas;
		_nNumeroPregunta = pNumeroPregunta;
		_sEstados = pEstados;
	}
	
	/* GET */
	public String getPreguntaPrincipal(){
		return _sPreguntaPrincipal;
	}
	
	public Boolean getMarcado(){
		return _bMarcado;
	}
	
	public String getPreguntasAnidadas(){
		return _sPreguntaAnidadas;
	}
	
	public int getNumeroPregunta(){
		return _nNumeroPregunta;
	}
	
	public String getEstados(){
		return _sEstados;
	}
}
