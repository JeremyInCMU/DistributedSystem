
package cmu.edu.jiamingx;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SensorServer", targetNamespace = "http://jiamingx.edu.cmu/", wsdlLocation = "http://localhost:8080/Project3Task1Server/SensorServer?WSDL")
public class SensorServer_Service
    extends Service
{

    private final static URL SENSORSERVER_WSDL_LOCATION;
    private final static WebServiceException SENSORSERVER_EXCEPTION;
    private final static QName SENSORSERVER_QNAME = new QName("http://jiamingx.edu.cmu/", "SensorServer");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/Project3Task1Server/SensorServer?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SENSORSERVER_WSDL_LOCATION = url;
        SENSORSERVER_EXCEPTION = e;
    }

    public SensorServer_Service() {
        super(__getWsdlLocation(), SENSORSERVER_QNAME);
    }

    public SensorServer_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), SENSORSERVER_QNAME, features);
    }

    public SensorServer_Service(URL wsdlLocation) {
        super(wsdlLocation, SENSORSERVER_QNAME);
    }

    public SensorServer_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SENSORSERVER_QNAME, features);
    }

    public SensorServer_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SensorServer_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SensorServer
     */
    @WebEndpoint(name = "SensorServerPort")
    public SensorServer getSensorServerPort() {
        return super.getPort(new QName("http://jiamingx.edu.cmu/", "SensorServerPort"), SensorServer.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SensorServer
     */
    @WebEndpoint(name = "SensorServerPort")
    public SensorServer getSensorServerPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://jiamingx.edu.cmu/", "SensorServerPort"), SensorServer.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SENSORSERVER_EXCEPTION!= null) {
            throw SENSORSERVER_EXCEPTION;
        }
        return SENSORSERVER_WSDL_LOCATION;
    }

}