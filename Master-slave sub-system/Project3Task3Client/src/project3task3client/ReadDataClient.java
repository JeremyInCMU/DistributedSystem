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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is designed for clients who read data from server.
 * @author Jeremy
 */
public class ReadDataClient extends Thread{
    
    private final static String urlStr = "http://localhost:8080/Project3Task3Server/serveSensorData";
    
    public synchronized static void main(String[] args) throws IOException {

        long start;
        long end;

        URL url = new URL(urlStr);
        while(true) {
            start = System.currentTimeMillis();
            while (true) {
                end = System.currentTimeMillis();
                if (end - start >= 5000) { // Check recent room status every 5 seconds.
                    break;
                }
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            conn.setRequestProperty( "Content-Type", "text/xml"); 
            conn.setRequestProperty( "charset", "utf-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            System.out.println(builder.toString());
        }
    }
}
