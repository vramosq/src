package com.ice.sgpr.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.ice.sgpr.comun.Constantes;
import com.ice.sgpr.error.SgprError;
import com.ice.sgpr.error.SgprException;

public class RestHelper {

	private static RestHelper _instance;
	
	private RestHelper()
	{
		
	}
	
	public String GET(String pUrl, boolean isBusiness) throws SgprException
	{
        try 
        {
        	HttpParams httpParameters = new BasicHttpParams();
        	int timeoutConnection = TIME_OUT;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	int timeoutSocket = TIME_OUT;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        	
        	HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(pUrl);
            
            HttpResponse response;
            
        	response = httpclient.execute(httpget);
 
            HttpEntity entity = response.getEntity();
 
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream, isBusiness);
            instream.close();

            return result; 
        }
        catch(Exception ex)
        {
        	SgprException kolbiEx = new SgprException(
        			SgprError.HTTP_REQUEST_ERROR, 
        			"There was an error excecuting an HTTP GET Request to: " + pUrl, 
        			ex);
        	throw kolbiEx;
        }
        
	}
	
	public String PUT(String pUrl, String pPutData) throws Exception
	{
		try
        {
			HttpParams httpParameters = new BasicHttpParams();
        	int timeoutConnection = TIME_OUT;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	int timeoutSocket = TIME_OUT;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpPut putRequest = new HttpPut(pUrl);

			StringEntity input = new StringEntity(pPutData);
			input.setContentType(CONTENT_TYPE);

			putRequest.setEntity(input);
			HttpResponse response = httpClient.execute(putRequest);
			
			HttpEntity entity = response.getEntity();
			 
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream, false);
            instream.close();
            Log.i("RESPUESTA", result);
            return result;
        }
		catch (IOException e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * Env�a los datos de la imagen al WS.
	 * @param pUrl
	 * @param pPutData
	 * @return
	 * @throws com.ice.sgpr.error.SgprException
	 */
	public String PUT(String pUrl, byte[] pPutData, boolean isBusiness) throws SgprException
	{
		try
        {
			HttpParams httpParameters = new BasicHttpParams();
        	int timeoutConnection = 30000;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	int timeoutSocket = 30000;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpPut putRequest = new HttpPut(pUrl);

			ByteArrayEntity input = new ByteArrayEntity(pPutData);

			putRequest.setEntity(input);
			HttpResponse response = httpClient.execute(putRequest);
			
			HttpEntity entity = response.getEntity();
			 
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream, isBusiness);
            instream.close();
            return result;
        }
		catch(Exception ex)
        {
			SgprException sgprEx = new SgprException(
        			SgprError.HTTP_REQUEST_ERROR, 
        			"There was an error excecuting an HTTP PUT Request to: " + pUrl,
        			ex);
			Log.i("ERROR IMAGEN VERSIONAM", ex.toString());
        	throw sgprEx;
        }
	}
	
	private static String convertStreamToString(InputStream pInputStream, boolean isBusiness) throws IOException
    {
		String encoding = Constantes.DEFAULT_ENCODING;
		if(isBusiness)
			encoding = Constantes.DEFAULT_ENCODING_BUSINESS;

		BufferedReader reader = new BufferedReader(new InputStreamReader(pInputStream, Charset.forName(encoding)));
        StringBuilder stringBuilder = new StringBuilder();
 
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }
	
	public static RestHelper getInstance()
	{
		if (_instance == null)
			_instance = new RestHelper();
		return _instance;
	}
	
	private static final int TIME_OUT = 7000;
	private static final String CONTENT_TYPE = "application/json";
}
