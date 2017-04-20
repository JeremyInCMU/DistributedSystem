/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task1client;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Jeremy
 */
public class PublishDataClient extends Thread{
    
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
     * Main method
     * @param args
     * @throws NoSuchAlgorithmException
     */
    public synchronized static void main(String[] args) 
            throws NoSuchAlgorithmException {
        Sensor sensor = new Sensor(1);
        long start;
        long end;
        double prevDB = 0;
        String status;
        // 30, 30 ~ 50 , 50
        while(true) {
            start = System.currentTimeMillis();
            while (true) {
                end = System.currentTimeMillis();
                if (end - start >= 5000) {
                    break;
                }
            }
            double db = sensor.get();
            if (checkEvent(prevDB, db)) {
               if (db < 30) {
                   status = "unoccupied";
               } else if (db >= 50) {
                   status = "busy";
               } else {
                   status = "occupied";
               }
               //Call the sensor
               encryptAndSend("1", status);
            }
            prevDB = db;
        }
    }
    
    /**
     * This method to designed to check sensor event.
     * @param prevDB: previously collected decibel.
     * @param curDB: currently collected decibel.
     */
    private static boolean checkEvent(double prevDB, double curDB) {
        if (curDB < 30) {
            return prevDB >= 30;
        } else if (curDB >= 30 && curDB < 50) {
            return prevDB < 30 || prevDB >= 50;
        }
        return prevDB < 50;
    }

    private static void encryptAndSend(String sensorID, String roomStatus) 
            throws NoSuchAlgorithmException {
        // Build signature.
        String signature = "I am your father";
        MessageDigest dig = MessageDigest.getInstance("SHA-256");
        byte[] digestedSignature = dig.digest(signature.getBytes());
        String encryptDigSig = (new BigInteger(digestedSignature)).modPow(E, N).toString();
        signature = signature + "," + encryptDigSig;
        // Build encrypted sensor ID.
        String encryptID = (new BigInteger(sensorID.getBytes())).modPow(E, N).toString();
        String encryptRoomStatus = (new BigInteger(roomStatus.getBytes())).modPow(E, N).toString();
        // Set date in easiest way.
        Date dNow = new Date( );
        SimpleDateFormat ft = 
        new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        String timeStamp = ft.format(dNow);
        String encryptTimeStamp = (new BigInteger(timeStamp.getBytes())).modPow(E, N).toString();
        System.out.println(roomChangeinStatus(encryptID, encryptTimeStamp, encryptRoomStatus, signature));
    }

    /**
     * RPC
     * @param sensorID
     * @param timeStamp
     * @param roomStatusMsg
     * @param signature
     * @return 
     */
    private synchronized static String roomChangeinStatus(java.lang.String sensorID, java.lang.String timeStamp, java.lang.String roomStatusMsg, java.lang.String signature) {
        cmu.edu.jiamingx.SensorServer_Service service = new cmu.edu.jiamingx.SensorServer_Service();
        cmu.edu.jiamingx.SensorServer port = service.getSensorServerPort();
        return port.roomChangeinStatus(sensorID, timeStamp, roomStatusMsg, signature);
    }
}
