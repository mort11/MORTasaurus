package org.mort11.dt;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * 
 * @author MORT
 */
public class ShifterSolenoid {

    private Solenoid inShifter;

    private boolean inHighGear = true;

    public ShifterSolenoid(int inChannel) {
        inShifter = new Solenoid(inChannel);
        inShifter.set(false);
    }

    public void shift(boolean isHigh) {
        inShifter.set(isHigh);
        inHighGear = !isHigh;
    }

    public void shift() {
        inShifter.set(inHighGear);
        inHighGear = !inHighGear;
    }

    /**
     * Returns the state of the transmission.
     * 
     * @return True if in high gear, false if in low gear.
     */
    public boolean getHigh() {
        return inHighGear;
    }
}
