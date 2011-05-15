/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mort11.util;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Custom class for the photo sensors. 
 * @author MORT1
 */
public class PhotoSensor extends DigitalInput {

    public PhotoSensor(int channel) {
        super(channel);
    }

    /**
     * True if light on photo sensor is orange, otherwise false. Flips the value from the DigitalInput because
     * when it REALLY is true (or should be returning true), it returns false.
     * @return True if light on photo sensor is orange, otherwise false.
     */
    public boolean get() {
        return !super.get();
    }
}
