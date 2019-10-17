package com.ice.sgpr.error;

import android.content.Context;

import com.ice.sgpr.R;


public enum SgprError {
	HTTP_REQUEST_ERROR (R.string.error_http_request),
	NEGOCIOS_ERROR(R.string.error_obtener_negocios);
	
private int _stringDescriptionId;
	
	private SgprError(int pStringDescriptionId)
	{
		_stringDescriptionId = pStringDescriptionId;
	}
	
	public String getErrorDescription(Context pContext)
	{
		if (_stringDescriptionId == NONE)
			return "";
		return pContext.getString(_stringDescriptionId);
	}
	
	private static final int NONE = -1;
}
