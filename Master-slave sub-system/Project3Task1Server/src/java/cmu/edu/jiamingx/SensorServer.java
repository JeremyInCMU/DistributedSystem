/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.jiamingx;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Jeremy
 */
@WebService(serviceName = "SensorServer")
public class SensorServer {
    
    private final SensorData dataSet;
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
    
    
    public SensorServer() {
        dataSet = new SensorData();
    }
    
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
    @WebMethod(operationName = "roomChangeinStatus")
    public synchronized String roomChangInStatus(
            @WebParam(name = "sensorID") String sensorID,
            @WebParam(name = "timeStamp") String timeStamp,
            @WebParam(name = "roomStatusMsg") String roomStatusMsg,
            @WebParam(name = "signature") String signature) {
        
        String info = "Invalid Message";       
        try {
            if (verifySignature(signature)) {
                String decryptSensorID = new String((new BigInteger(sensorID)).
                                            modPow(D, N).toByteArray(),"UTF-8");
                String decrypttimeStamp = new String((new BigInteger(timeStamp)).
                                            modPow(D, N).toByteArray(), "UTF-8");
                String decryptroomStatusMsg = new String((new BigInteger(roomStatusMsg)).
                                            modPow(D, N).toByteArray(), "UTF-8");

                dataSet.addSensorEvent(decryptSensorID, decryptSensorID, decryptroomStatusMsg, signature);
                info = "The last report at time " + decrypttimeStamp + " shows the room "
                       + "to be " + decryptroomStatusMsg;
                System.out.println(info);
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return info;
    }
    
    /**
     * This method is designed to extract for most recent room status and timestamp.
     * @return recent room status and time stamp.
     */
    @WebMethod(operationName = "getRoomStatusMsg")
    public synchronized String getRoomStatusMsg() {
        String info = "No Records";
        try {
            SensorData.SensorEvent temp = dataSet.getLastEvent();        
            info = "The last report at time " + temp.getTimeStamp() 
                   + " shows the room " + "to be " + temp.getRoomStatusMessage();
            System.out.println(info);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return info;
    }
}
