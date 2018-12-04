package com.scancode.webservice;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.scancode.webservice.BaseWebservice.OnCallbackListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.util.Map;

public class WebServiceAsyncTask extends AsyncTask {
	
	private String serviceName;
	private Map<String, Object> params;
	private OnCallbackListener callbackListener;
	private MyHandler mHandler = new MyHandler(this);
	
	public WebServiceAsyncTask(String serviceName, Map<String, Object> params, OnCallbackListener callbackListener) {
		this.serviceName = serviceName;
		this.params = params;
		this.callbackListener = callbackListener;
	}

	private SoapObject createRequestSoapObject() {
		SoapObject request = new SoapObject(Settings.getWSNameSpace(), serviceName);
		if (params != null) {
//			String parName = "in";
//			String tmpName = "";
//			for (int i = 0; i < params.length; i++) {
//				tmpName = parName + i;
//				if (params[i] != null) {
//					Object pp = params[i];
//					if (pp instanceof Integer 
//							|| pp instanceof Double
//							|| pp instanceof Long 
//							|| pp instanceof Float
//							|| pp instanceof Boolean 
//							|| pp instanceof Short) {
//						request.addProperty(tmpName, pp);
//					} else {
//						request.addProperty(tmpName, params[i].toString());
//					}
//				} else {
//					request.addProperty(tmpName, "");
//				}
//			}
			for (String key : params.keySet()) {
				request.addProperty(key, params.get(key));
//				   System.out.println("key= "+ key + " and value= " + map.get(key));
			}
		}
		return request;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		SoapObject request = createRequestSoapObject();
		String soapAction = Settings.getWSNameSpace() + serviceName;
		SoapObject soapObject = request;
		String url = Settings.getWSURL();
		HttpTransportSE transport = new HttpTransportSE(url);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		String result = null;
		try {
			transport.call(soapAction, envelope);
			result = ""+(Object) envelope.getResponse();
			mHandler.obtainMessage(AsyncCaller.SUCCESS, result).sendToTarget();
			return null;
		} catch (ConnectException ce) {
			mHandler.obtainMessage(AsyncCaller.NETERROR, result).sendToTarget();
			return null;
		} catch (IOException e) {
			mHandler.obtainMessage(AsyncCaller.FAIL, result).sendToTarget();
			return null;
		} catch (XmlPullParserException e) {
			mHandler.obtainMessage(AsyncCaller.FAIL, result).sendToTarget();
			return null;
		}
	}
	
	private static class MyHandler extends Handler {
		
		private final WeakReference<WebServiceAsyncTask> ref;
		
		public MyHandler(WebServiceAsyncTask ref) {
			this.ref = new WeakReference<WebServiceAsyncTask>(ref);
		}
		
		@Override
		public void handleMessage(Message msg) {
			if(ref.get().callbackListener != null) {
				ref.get().callbackListener.onCallback(msg.obj, msg.what);
			}
		}
	}

}
