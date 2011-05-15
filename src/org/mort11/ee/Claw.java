package org.mort11.ee;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.mort11.util.Constants;
import org.mort11.util.JaguarDealer;

/**
 * End Effector: Claw
 * @author MORT
 * @version 02.18.2011.1
 */
public class Claw {

    private CANJaguar motor;
    private Solenoid opener;
    private Solenoid closer;
    private boolean isOpen = false;

    private final double ROLLER_SPEED = 1;

    /**
     * Creates a new Claw.
     * @param openerChannel The channel number the Solenoid is on that opens the claw.
     * @param closerChannel The channel number the Solenoid is on that closes the claw.
     */
    public Claw() {
        motor = JaguarDealer.getJag(6);
        opener = new Solenoid(Constants.Claw.OPENER_CHANNEL);
        closer = new Solenoid(Constants.Claw.CLOSER_CHANNEL);
    }

    /**
     * Rollers suck in the tube.
     */
    public void moveRollersForward() {
        try {
            motor.setX(ROLLER_SPEED);
        } catch (CANTimeoutException ex) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//            DriverStationLCD.getInstance().updateLCD();
            ex.printStackTrace();
        }
    }

    /**
     * Rollers push out the tube.
     */
    public void moveRollersBackward() {
        try {
            motor.setX(-1*(ROLLER_SPEED));
        } catch (CANTimeoutException ex) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//            DriverStationLCD.getInstance().updateLCD();
            ex.printStackTrace();
        }
    }

    /**
     * Stop the rollers.
     */
    public void stopRollers() {
        try {
            motor.setX(0.0);
        } catch (CANTimeoutException ex) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//            DriverStationLCD.getInstance().updateLCD();
            ex.printStackTrace();
        }
    }

    /**
     * Open the claw.
     */
    public void open() {
        opener.set(true);
        closer.set(false);
        isOpen = true;
    }

    /**
     * Close the claw.
     */
    public void close() {
        opener.set(false);
        closer.set(true);
        isOpen = false;
    }

    /**
     * Is the claw open?
     * @return True if the claw is open, otherwise false.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Get the speed controller associated with the rollers.
     * @return CANJaguar object that runs the claw's rollers.
     */
    public CANJaguar getSpeedController() {
        return motor;
    }

}
