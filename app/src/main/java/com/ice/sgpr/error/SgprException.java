package com.ice.sgpr.error;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.comun.Constantes;

import android.util.Log;

public class SgprException extends Exception{

	private static final long serialVersionUID = 1L;
	private SgprError _codigoError;
	private static final String LOG_FORMAT = "Error Code: %s. User Message: \"%s\". Error Message: \"%s\". Inner Exception Message: \"%s\"";
	
	public SgprException(SgprError pErrorCode)
	{
		super();
		_codigoError = pErrorCode;
	}
	
	public SgprException(SgprError pErrorCode, String pErrorDetail)
	{
		super(pErrorDetail);
		_codigoError = pErrorCode;
	}
	
	public SgprException (SgprError httpRequestError, String pErrorDetail, Exception pInnerException)
	{
		super(pErrorDetail, pInnerException);
		_codigoError = httpRequestError;
	}
	
	public void LogError()
	{
		if (getCause() == null)
			Log.e(Constantes.LOG_TAG, String.format(LOG_FORMAT, _codigoError, _codigoError.getErrorDescription(SgprApplication.getContext()), getMessage(), "none"));
		else
			Log.e(Constantes.LOG_TAG, String.format(LOG_FORMAT, _codigoError, _codigoError.getErrorDescription(SgprApplication.getContext()), getMessage(), getCause().getMessage()));
		printStackTrace();
	}
	
	public String getUserMessage()
	{
		return _codigoError.getErrorDescription(SgprApplication.getContext());
	}
}
