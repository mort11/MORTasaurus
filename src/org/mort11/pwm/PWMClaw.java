package org.mort11.pwm;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.mort11.util.JaguarDealer;

/**
 * End Effector: Claw
 * @author MORT
 * @version 02.18.2011.1
 */
public class PWMClaw {

    private Jaguar motor;
    private Solenoid opener;
    private Solenoid closer;
    private boolean isOpen = false;

    private final double ROLLER_SPEED = 1;

    /**
     * Creates a new Claw.
     * @param openerChannel The channel number the Solenoid is on that opens the claw.
     * @param closerChannel The channel number the Solenoid is on that closes the claw.
     */
    public PWMClaw(int openerChannel, int closerChannel) {
        motor = JaguarDealer.getPWMJag(6);
        opener = new Solenoid(openerChannel);
        closer = new Solenoid(closerChannel);
    }

    /**
     * Rollers suck in the tube.
     */
    public void moveRollersForward() {
        motor.set(ROLLER_SPEED);
    }

    /**
     * Rollers push out the tube.
     */
    public void moveRollersBackward() {
        motor.set(-1*(ROLLER_SPEED));
    }

    /**
     * Stop the rollers.
     */
    public void stopRollers() {
        motor.set(0.0);
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
     * @return Jaguar object that runs the claw's rollers.
     */
    public Jaguar getSpeedController() {
        return motor;
    }

}
