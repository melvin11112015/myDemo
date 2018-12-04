package com.scancode.webservice;

import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.scancode.utils.Tools;

import org.ksoap2.serialization.SoapObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BaseWebservice {
	public static Executor threadPool = Executors.newCachedThreadPool();

	private static SoapObject createRequestSoapObject(String methodName, Object... parames) {
		SoapObject request = new SoapObject(Settings.getWSNameSpace(), methodName);
		if (parames != null) {
			String parName = "in";
			String tmpName = "";
			for (int i = 0; i < parames.length; i++) {
				tmpName = parName + i;
				if (Tools.isNotEmpty(parames[i])) {
					Object pp = parames[i];
					if (pp instanceof Integer || pp instanceof Double
							|| pp instanceof Long || pp instanceof Float
							|| pp instanceof Boolean || pp instanceof Short)
						request.addProperty(tmpName, pp);
					else
						request.addProperty(tmpName, parames[i].toString());
				}
				else
					request.addProperty(tmpName, "");
			}
		}
		return request;
	}

	private static void doRequest(SoapObject request, String serviceName, String methodName, final OnCallbackListener callbackListener) {
//		String name = Settings.getWSEndPoint() + "/" + serviceName;
		String name = "http://nyyxlm.smu.edu.cn/WebService/Api.asmx";
		System.out.println("--webService请求--" + name);

		String soapAction = Settings.getWSNameSpace() + methodName;
		System.err.println("soapAction: " + soapAction);
		try {
			Handler mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					callbackListener.onCallback(msg.obj, msg.what);
				}
			};
			
			AsyncCaller caller = new AsyncCaller();
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				caller.executeOnExecutor(threadPool, soapAction, methodName, request, name, mHandler);
			} else {
				caller.execute(soapAction, methodName, request, name, mHandler);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void requestWebservice(String serviceName, String methodName,
			OnCallbackListener callbackListener, Object... parames) {
		SoapObject request = createRequestSoapObject(methodName, parames);
		doRequest(request, serviceName, methodName, callbackListener);
	}

	public interface OnCallbackListener {
		public void onCallback(Object result, int state);
	}
}
