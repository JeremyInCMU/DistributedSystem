/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import static java.net.HttpURLConnection.HTTP_OK;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.ParserConfigurationException;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author Jeremy
 */
public class PublishDataClient {
    
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
    private final static String urlStr = "http://localhost:8080/Project3Task3Server/serveSensorData";
    
    /**
     * Main method
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws javax.xml.transform.TransformerException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.net.MalformedURLException
     */
    public synchronized static void main(String[] args) 
            throws NoSuchAlgorithmException, 
            TransformerException, 
            ParserConfigurationException,
            MalformedURLException,
            IOException {
        Sensor sensor = new Sensor(1);
        long start;
        long end;
        double prevDB = 0;
        String status;
        
        URL url = new URL(urlStr);        
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
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
                conn.setRequestProperty( "Content-Type", "text/xml"); 
                conn.setRequestProperty( "charset", "utf-8");
                conn.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                String postContent = encrypt("1", status);
                int respondCode = 0;
                while (respondCode != HTTP_OK) {
                    // Parse string to server.
                    out.write(postContent);
                    out.flush();
                    out.close();
                    respondCode = conn.getResponseCode();
                    BufferedReader reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    System.out.println(builder.toString());
                    reader.close();
                }
               // Execute the next steep based on current status.
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

    private static String encrypt(String sensorID, String roomStatus) 
            throws NoSuchAlgorithmException, 
                   ParserConfigurationException,
                   TransformerException {
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
        String xmlString = equipXML(encryptID, encryptRoomStatus, encryptTimeStamp, signature);
        return xmlString;
        // Task2 code starts from here: build xml format
        
    }

    /**
     * This method is designed in task2 for parse messages in the format of xml.
     * @param encryptID: encrypted sensor ID.
     * @param encryptRoomStatus: encrypted room status information.
     * @param encryptTimeStamp: encrypted time stamp information.
     * @param encryptSignature: encrypted signature.
     */
    private static  String equipXML(String encryptID, String encryptRoomStatus,
                                   String encryptTimeStamp, String encryptSignature) 
            throws  ParserConfigurationException, 
                    TransformerConfigurationException, 
                    TransformerException {
        
        String output = null;
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            
            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Document");
            rootElement.setAttribute("id", "SensorInfo");
            doc.appendChild(rootElement);
            
            // Sensor information element
            Element sensorElement = doc.createElement("SensorInfo");
            rootElement.appendChild(sensorElement);
            
            // Sensor ID tag.
            Element sensorID = doc.createElement("SensorID");
            sensorID.setTextContent(encryptID);
            sensorElement.appendChild(sensorID);
            
            // Time stamp tag.
            Element timeStamp = doc.createElement("TimeStamp");
            timeStamp.setTextContent(encryptTimeStamp);
            sensorElement.appendChild(timeStamp);
            
            // Room status message tag.
            Element roomStatus = doc.createElement("RoomStatus");
            roomStatus.setTextContent(encryptRoomStatus);
            sensorElement.appendChild(roomStatus);
            
            // Signature.
            Element signature = doc.createElement("Signature");
            signature.setTextContent(encryptSignature);
            sensorElement.appendChild(signature);
            
            // Transform the doc to a string.
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            
        } catch (ParserConfigurationException | DOMException e) {
            System.out.println(e.getMessage()); 
        }
        return output;
    }    
}
