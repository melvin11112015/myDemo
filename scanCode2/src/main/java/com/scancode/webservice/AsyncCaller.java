package com.scancode.webservice;

import java.io.IOException;
import java.net.ConnectException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Handler;

public class AsyncCaller extends AsyncTask {
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	public static final int NETERROR = -1;

	@Override
	protected Object doInBackground(Object... pars) {
		String soapAction = pars[0].toString();
//		String methodName = pars[1].toString();
		SoapObject soapObject = (SoapObject) pars[2];
		String tn = (String) pars[3];
		HttpTransportSE transport = new HttpTransportSE(tn);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		envelope.setOutputSoapObject(soapObject);
		Handler mHandler = (Handler) pars[4];
		String result = null;
		try {
			transport.call(soapAction, envelope);
			SoapObject object = (SoapObject) envelope.bodyIn;
			result = object.getPropertyAsString(0);
			mHandler.obtainMessage(SUCCESS, result).sendToTarget();
			return null;
		} catch (ConnectException ce) {
			result = "ConnectException" + ce.getMessage();
			mHandler.obtainMessage(NETERROR, result).sendToTarget();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			result = "IOException:" + e.getMessage();
			mHandler.obtainMessage(FAIL, result).sendToTarget();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			result = "XmlPullParserException:" + e.getMessage();
			mHandler.obtainMessage(FAIL, result).sendToTarget();
			return null;
		} catch (Exception e) {
			 e.printStackTrace();
			result = "Exception:" + e.getMessage();
			mHandler.obtainMessage(FAIL, result).sendToTarget();
			return null;
		}
	}

}
