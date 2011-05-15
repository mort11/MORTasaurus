package org.mort11.pwm;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.mort11.util.Constants;
import org.mort11.util.JaguarDealer;
import org.mort11.util.Potentiometer;

/**
 * End Effector: Wrist
 * @author MORT
 * @version 02.18.2011.1
 */
public class PWMWrist {

    private Jaguar motor;

    private Potentiometer pot;

    private final double UPPER_LIMIT = Constants.Wrist.UPPER_LIMIT;
    private final double LOWER_LIMIT = Constants.Wrist.LOWER_LIMIT;

    public final double HOME  = Constants.Wrist.HOME;
    public final double TROLL = Constants.Wrist.TROLL;
    public final double HIGH  = Constants.Wrist.HIGH;

    public boolean runningPreset = false;

    /**
     * Constructs a new Wrist controlled by a CANJaguar with an ID of 5.
     */
    public PWMWrist() {
            motor = JaguarDealer.getPWMJag(5);
            pot = new Potentiometer(2);
    }

    /**
     * The position of the Wrist.
     * @return A double that represents the percent rotation around the one-turn potentiometer.
     */
    public double getPos() {
        return pot.getPosition();
    }

    /**
     * Manual override to move the Wrist without constraints.
     * @param speed Percent voltage to send to the Arm speed controller.
     */
    public void manualMove(double speed) {
        motor.set(-speed);
    }

    /**
     * Moves the wrist to the pot value given.
     * @param preset pot value to go to
     */
    public void setPos(double preset) {
        if (Math.abs(getPos() - preset) < 0.005) {
            setSpeed(0);
            runningPreset = false;
        } else if (getPos() > preset) {
            setSpeed(1);
        } else {
            setSpeed(-1);
        }
    }

    /**
     * Stops the wrist and resets preset stuff.
     */
    public void stop() {
        runningPreset = false;
        setSpeed(0);
    }

    /**
     * Pass in the percent voltage and the Wrist moves within the constraints.
     * @param speed Percent voltage to send to the Arm speed controller.
     */
    public void setSpeed(double speed) {
        double curPos = getPos();
         speed = -1*speed;
        if(curPos < UPPER_LIMIT || curPos > LOWER_LIMIT || speed == 0.0) {
            motor.set(0);
        }
        if(curPos < LOWER_LIMIT && speed > 0) {
            motor.set(speed);
        }
        if(curPos > UPPER_LIMIT && speed < 0) {
            motor.set(speed);
        }
    }

    /**
     * Get the speed controller associated with the Arm.
     * @return Jaguar object that runs the Arm.
     */
     public Jaguar getSpeedController() {
         return motor;
     }

}
