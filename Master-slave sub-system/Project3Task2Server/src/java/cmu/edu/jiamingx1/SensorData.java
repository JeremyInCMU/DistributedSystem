/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.jiamingx1;
import java.util.LinkedList;

/**
 * This method is created to temporarily store data sent by sensor.
 * @author Jeremy
 */
public final class SensorData {
    
    /**
     * Variables.
     */
    private static LinkedList<SensorEvent> events;
    
    /**
     * Constructor.
     */
    public SensorData() {
        events = new LinkedList<>();
    }
    
    /**
     * Add sensor event.
     * @param sensorID
     * @param timeStamp
     * @param roomStatusMsg
     * @param signature
     */
    public void addSensorEvent(String sensorID, String timeStamp,
                              String roomStatusMsg, String signature) {
        events.addLast(new SensorEvent(sensorID, timeStamp,
                                       roomStatusMsg, signature));
    }
    
    /**
     * Get latest event.
     * @return latest sensor event.
     */
    public SensorEvent getLastEvent() {
        return new SensorEvent(events.getLast());
    }
    
    /**
     * This class is designed to store data of sensor events.
     */
    public static class SensorEvent {

        /**
         * Variables.
         */
        private final String sensorID;
        private final String timeStamp;
        private final String roomStatusMsg;
        private final String signature;

        /**
         * Constructor1.
         */
        private SensorEvent(String sensorID, String timeStamp,
                            String roomStatusMsg, String signature) {
            this.sensorID = sensorID;
            this.timeStamp = timeStamp;
            this.roomStatusMsg = roomStatusMsg;
            this.signature = signature;
        }

        /**
         * Constructor2.
         */
        private SensorEvent(SensorEvent newEvent) {
            sensorID = newEvent.sensorID;
            timeStamp = newEvent.timeStamp;
            roomStatusMsg = newEvent.roomStatusMsg;
            signature = newEvent.signature;
        }

        /**
         * Get sensor id.
         * @return sensor ID.
         */
        public String getSensorID() {  
            return sensorID;
        }

        /**
         * Get time stamp.
         * @return time stamp of this sensor event.
         */
        public String getTimeStamp() {
            return timeStamp;
        }
        
        /**
         * Get room status message.
         * @return room status information of this sensor event.
         */
        public String getRoomStatusMessage() {
            return roomStatusMsg;
        }
        
        /**
         * Get signature.
         * @return signature of this sensor event.
         */
        public String getSignature() {
            return signature;
        }
    }
}
