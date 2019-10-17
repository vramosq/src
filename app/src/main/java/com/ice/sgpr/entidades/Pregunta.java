package com.ice.sgpr.entidades;

public class Pregunta {
	int nNumero;
	String sRespuesta1;
	String sRespuesta2;
	int nPosicion; //Posicion que ocupa en la lista en caso de ser una opcion anidada multiple.
	Boolean bPreguntaContestada; //Cuando se re-cargan las respuestan anteriores indica que la pregunta ha sido contestada.
	String sValor;
	int nCodigoPreguntaPrincipal;
	int nCodigoPreguntaSecundaria;
	int nCodigoPreguntaDependencia;
	String sCodigoRespuesta1;
	String sCodigoRespuesta2;
	String sCodigoRespuesta3;
	String sCodigoRespuestaValor;
	String sCodigoPreguntaAuditor;
	
	public Pregunta(){
	}
	
	/* GET */
	public int getNumero(){
		return this.nNumero;
	}
	
	public String getRespuesta1(){
		return this.sRespuesta1;
	}
	
	public String getRespuesta2(){
		return this.sRespuesta2;
	}
	
	public int getPosicion(){
		return this.nPosicion;
	}
	
	public Boolean getPreguntaContestada(){
		return this.bPreguntaContestada;
	}
	
	public String getValor(){
		return this.sValor;
	}
	
	public int getCodigoPreguntaPrincipal(){
		return this.nCodigoPreguntaPrincipal;
	}
	
	public int getCodigoPreguntaSecundaria(){
		return this.nCodigoPreguntaSecundaria;
	}
	
	public int getCodigoPreguntaDependencia(){
		return this.nCodigoPreguntaDependencia;
	}
	
	public String getCodigoRespuesta1(){
		return this.sCodigoRespuesta1;
	}
	
	public String getCodigoRespuesta2(){
		return this.sCodigoRespuesta2;
	}
	
	public String getCodigoRespuestaValor(){
		return this.sCodigoRespuestaValor;
	}
	
	/* SET */
	public void setNumero(int pNumeroPregunta){
		this.nNumero = pNumeroPregunta;
	}
	
	public void setRespuesta1(String pRespuesta1){
		this.sRespuesta1 = pRespuesta1;
	}
	
	public void setRespuesta2(String pRespuesta2){
		this.sRespuesta2 = pRespuesta2;
	}
	
	public void setPosicion(int pPosicion){
		this.nPosicion = pPosicion;
	}
	
	public void setPreguntaContestada(Boolean pValor){
		this.bPreguntaContestada = pValor;
	}
	
	public void setValor(String pValor){
		this.sValor = pValor;
	}
	
	public void setCodigoPreguntaPrincipal(int pValor){
		this.nCodigoPreguntaPrincipal = pValor;
	}
	
	public void setCodigoPreguntaSecundaria(int pValor){
		this.nCodigoPreguntaSecundaria = pValor;
	}
	
	public void setCodigoPreguntaDependencia(int pValor){
		this.nCodigoPreguntaDependencia = pValor;
	}
	
	public void setCodigoRespuesta1(String pCodigoRespuesta1){
		this.sCodigoRespuesta1 = pCodigoRespuesta1;
	}
	
	public void setCodigoRespuesta2(String pCodigoRespuesta2){
		this.sCodigoRespuesta2 = pCodigoRespuesta2;
	}
	
	public void setCodigoRespuestaValor(String pCodigoRespuestaValor){
		this.sCodigoRespuestaValor = pCodigoRespuestaValor;
	}

	public String getCodigoRespuesta3() {
		return sCodigoRespuesta3;
	}

	public void setCodigoRespuesta3(String sCodigoRespuesta3) {
		this.sCodigoRespuesta3 = sCodigoRespuesta3;
	}

	public String getCodigoPreguntaAuditor() {
		return sCodigoPreguntaAuditor;
	}

	public void setCodigoPreguntaAuditor(String sCodigoPreguntaAuditor) {
		this.sCodigoPreguntaAuditor = sCodigoPreguntaAuditor;
	}
}
