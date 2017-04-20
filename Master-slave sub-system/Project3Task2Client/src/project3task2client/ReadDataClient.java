/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2client;

/**
 * This class is designed for clients who read data from server.
 * @author Jeremy
 */
public class ReadDataClient extends Thread{
    
    public synchronized static void main(String[] args) {

        long start;
        long end;

        while(true) {
            start = System.currentTimeMillis();
            while (true) {
                end = System.currentTimeMillis();
                if (end - start >= 5000) { // Check recent room status every 5 seconds.
                    break;
                }
            }
            System.out.println(getRoomStatusMsg());
        }
    }

    /**
     * RPC.
     * @return 
     */
    private synchronized static String getRoomStatusMsg() {
        cmu.edu.jiamingx1.SensorServer_Service service = new cmu.edu.jiamingx1.SensorServer_Service();
        cmu.edu.jiamingx1.SensorServer port = service.getSensorServerPort();
        return port.getRoomStatusMsg();
    }
}
