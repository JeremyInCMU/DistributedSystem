/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3client;
import java.util.Random;
/**
 *
 * @author Jeremy
 */
public class Sensor {

    double currentDBA = 0.0;    //Current decible value.
    Random rand;    // Random generator.
    int id;         // Sensor Id.

    /**
     * Constructor.
     */
    public Sensor (int id) {
        this.id = id;
        rand = new Random();
    }

    public double get() {
        double newNoise= rand.nextInt(70);
        currentDBA = newNoise;
        return currentDBA;
    }
}