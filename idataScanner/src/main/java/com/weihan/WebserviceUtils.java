package com.weihan;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

public class WebserviceUtils {

    public static final String url = "http://139.159.253.196:7131/FTK_Demo/WS/FTK/Page/PacakgeScanRec";

    public static final String namespace = "urn:microsoft-dynamics-schemas/page/pacakgescanrec";

    // SoapHeaders身份验证的方法名
    public static final String soapheadername = "HSSoapHeader";

    public static final String uesrid = "Tim";
    public static final String password = "Qwert!2345";

    /**
     * @return SoapHeaders身份验证
     */
    private static Element[] getSoapHeader() {
        Element[] header = new Element[1];
        header[0] = new Element().createElement(namespace, soapheadername);
        Element userName = new Element().createElement(namespace, "UserName");
        userName.addChild(Node.TEXT, uesrid);
        header[0].addChild(Node.ELEMENT, userName);
        Element pass = new Element().createElement(namespace, "Password");
        pass.addChild(Node.TEXT, password);
        header[0].addChild(Node.ELEMENT, pass);
        return header;
    }

    /**
     * 上传文件
     *
     * @return 操作结果信息Json
     * @throws Exception
     */
    public static String Read(String LittleBarcode, String BigBarcode) throws Exception {
        final String methodName = "Read";// 函数名
        System.out.println(url);
        HttpTransportSE transport = new HttpTransportSE(url, 15000);
        transport.debug = true;
        // 指定 WebService 的命名空间和函数名
        SoapObject soapObject = new SoapObject(namespace, methodName);
        soapObject.addProperty("LittleBarcode", LittleBarcode);
        soapObject.addProperty("BigBarcode", BigBarcode);
        System.out.println(soapObject.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.headerOut = getSoapHeader();
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        transport.call(namespace + methodName, envelope);

        SoapObject result = null;
        if (envelope.bodyIn instanceof SoapObject) {
            result = (SoapObject) envelope.bodyIn;
            for (int i = result.getPropertyCount(); i > 0; i--) {
                System.out.println(result.getProperty(i - 1).toString());
            }
            return result.getProperty(result.getPropertyCount() - 1).toString();
        } else {
            SoapPrimitive primitive = (SoapPrimitive) envelope.getResponse();
            return primitive.toString();
        }


    }

}

