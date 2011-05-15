
package org.mort11.util;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * No idea if this works. Created it for a worst case scenario of having to
 * switch from CAN to PWM. 
 * @author MORT1
 */
public class Potentiometer extends AnalogChannel {

    private double maxVoltage = 5.0;

    public Potentiometer(int channel) {
        super(channel);
    }

    /**
     * Pot output voltage ranges from 0 to 5 volts off the Analog Breakout.
     * This should give us a percent of how far the potentiometer has turned.
     * @return A percent turn of the potentiometer
     */
    public double getPosition() {
        return getVoltage()/maxVoltage;
    }
    
}
