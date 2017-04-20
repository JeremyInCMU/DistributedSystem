/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This servlet is designed to accept sensor data and post sensor data.
 * @author Jeremy
 */
@WebServlet(name = "SensorService",
        urlPatterns = {"/serveSensorData"})
public class SensorService extends HttpServlet { 
    
    private static SensorData dataSet;
    private final static BigInteger E = new BigInteger("65537");
    private final static BigInteger D = new BigInteger(
        "339177647280468990599683753475404338964037287357290649" +
        "639740920420195763493261892674937712727426153831055473238029100" + 
        "340967145378283022484846784794546119352371446685199413453480215" +
        "164979267671668216248690393620864946715883011485526549108913");
    private final static BigInteger N = new BigInteger(
        "268852025517901502623747873143657162103121815451557296" + 
        "872758837706559866377091251333301800665424865065625091311087483" + 
        "660777796686710629019261833666084998095639973296736997628150027" + 
        "0286450313199586861977623503348237855579434471251977653662553");

    /**
     * This method is implemented to initialize underlying instances.
     */
    @Override
    public void init() {
        dataSet = new SensorData();
    }
    
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            
            String ua = request.getHeader("User-Agent");
            boolean mobile;
            // prepare the appropriate DOCTYPE for the view pages
            if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
                mobile = true;
                /*
                 * This is the latest XHTML Mobile doctype. To see the difference it
                 * makes, comment it out so that a default desktop doctype is used
                 * and view on an Android or iPhone.
                 */
                request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
            } else {
                mobile = false;
                request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
            }
            
            String responseContent = getRoomStatusMsg();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(responseContent);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String ua = request.getHeader("User-Agent");
            boolean mobile;
            // prepare the appropriate DOCTYPE for the view pages
            if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
                mobile = true;
                /*
                 * This is the latest XHTML Mobile doctype. To see the difference it
                 * makes, comment it out so that a default desktop doctype is used
                 * and view on an Android or iPhone.
                 */
                request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
            } else {
                mobile = false;
                request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
            }
            
            // Deal with request body.
            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Document sensorInfo = xmlStr2Doc(builder.toString());

            String sensorID = sensorInfo.getElementsByTagName("SensorID").
                              item(0).getTextContent();
            String timeStamp = sensorInfo.getElementsByTagName("TimeStamp").
                              item(0).getTextContent();
            String roomStatusMsg = sensorInfo.getElementsByTagName("RoomStatus").
                              item(0).getTextContent();
            String signature = sensorInfo.getElementsByTagName("Signature").
                              item(0).getTextContent();
            String reContent = roomChangInStatus(sensorID, timeStamp, roomStatusMsg, signature);
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(reContent);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
    /**
     * This method is designed to get signature info of clients.
     * @param origin: original information
     * @param digest: digested information
     * @return true if signature is verified false if not.
     */
    private boolean verifySignature (String signature) {
        byte[] decryptDigest = null;
        byte[] hash = null;
        
        try {
            String origin = signature.split(",")[0];
            String digest = signature.split(",")[1];
        
            BigInteger temp = new BigInteger(digest);
            decryptDigest = temp.modPow(D, N).toByteArray();

            MessageDigest dig = MessageDigest.getInstance("SHA-256");
            hash = dig.digest(origin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return Arrays.equals(decryptDigest, hash);
    }
    
    /**
     * This method is designed to display events notified by sensor.
     * @param sensorID: the id of sensor.
     * @param timeStamp: time stamp of the message sent by sensor.
     * @param roomStatusMsg: room status message.
     * @param signature
     * @return the information sent by sensor.
     */
    public synchronized String roomChangInStatus( String sensorID,
        String timeStamp, String roomStatusMsg, String signature) {
        String info = "Invalid Message";       
        try {
            if (verifySignature(signature)) {
                String decryptSensorID = new String((new BigInteger(sensorID)).
                                            modPow(D, N).toByteArray(),"UTF-8");
                String decryptTimeStamp = new String((new BigInteger(timeStamp)).
                                            modPow(D, N).toByteArray(), "UTF-8");
                String decryptroomStatusMsg = new String((new BigInteger(roomStatusMsg)).
                                            modPow(D, N).toByteArray(), "UTF-8");

                dataSet.addSensorEvent(decryptSensorID, decryptTimeStamp, decryptroomStatusMsg, signature);
                info = "The last report at time " + decryptTimeStamp + " shows the room "
                       + "to be " + decryptroomStatusMsg;
                System.out.println(info);
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return info;
    }
    
    /**
     * This method is designed to convert xml string to a xml document.
     * @param xmlString: a string represents a xml file.
     * @return a document which represents a xml file.
     */
    private Document xmlStr2Doc(String xmlString) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(new InputSource (new StringReader(xmlString)));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * This method is designed to extract room status messages.
     * @return latest room status message.
     */
    public synchronized String getRoomStatusMsg() {
        String info = "No Records";
        try {
            SensorData.SensorEvent temp = dataSet.getLastEvent();        
            info = "The last report at time " + temp.getTimeStamp() 
                   + " shows the room " + "to be " + temp.getRoomStatusMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return info;
    }

}
