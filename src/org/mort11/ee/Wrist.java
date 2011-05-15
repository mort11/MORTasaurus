package org.mort11.ee;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.mort11.util.Constants;
import org.mort11.util.JaguarDealer;

/**
 * End Effector: Wrist
 * @author MORT
 * @version 02.18.2011.1
 */
public class Wrist {

    private CANJaguar motor;

    private final double UPPER_LIMIT = Constants.Wrist.UPPER_LIMIT;
    private final double LOWER_LIMIT = Constants.Wrist.LOWER_LIMIT;

    public final double HOME  = Constants.Wrist.HOME;
    public final double TROLL = Constants.Wrist.TROLL;
    public final double HIGH  = Constants.Wrist.HIGH;

    public boolean runningPreset = false;

    /**
     * Constructs a new Wrist controlled by a CANJaguar with an ID of 5.
     */
    public Wrist() {
        try {
            motor = JaguarDealer.getJag(5);
            motor.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            motor.configPotentiometerTurns(1);
            motor.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        } catch(CANTimeoutException ex) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//            DriverStationLCD.getInstance().updateLCD();
            ex.printStackTrace();
        }
    }

    /**
     * The position of the Wrist.
     * @return A double that represents the percent rotation around the one-turn potentiometer.
     */
    public double getPos() {
        try {
            return motor.getPosition();
        } catch(CANTimeoutException ex) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//            DriverStationLCD.getInstance().updateLCD();
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Manual override to move the Wrist without constraints.
     * @param speed Percent voltage to send to the Arm speed controller.
     */
    public void manualMove(double speed) {
        try {
            motor.setX(-speed);
        } catch (CANTimeoutException ex) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//            DriverStationLCD.getInstance().updateLCD();
            ex.printStackTrace();
        }
    }

    /**
     * Moves the wrist to the pot value given.
     * @param preset pot value to go to
     */
    public void setPos(double preset, double speed) {
        if (Math.abs(getPos() - preset) < 0.005) {
            setSpeed(0);
            runningPreset = false;
        } else if (getPos() > preset) {
            setSpeed(speed);
        } else {
            setSpeed(-speed);
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
         speed = -speed;
        if(curPos < UPPER_LIMIT || curPos > LOWER_LIMIT || speed == 0.0) {
            try {
                motor.setX(0);
            } catch (CANTimeoutException ex) {
//                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//                DriverStationLCD.getInstance().updateLCD();
                ex.printStackTrace();
            }
        }      
        if(curPos < LOWER_LIMIT && speed > 0) {
            try {
                motor.setX(speed);
            } catch (CANTimeoutException ex) {
//                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//                DriverStationLCD.getInstance().updateLCD();
                ex.printStackTrace();
            }
        }
        if(curPos > UPPER_LIMIT && speed < 0) {
            try {
                motor.setX(speed);
            } catch (CANTimeoutException ex) {
//                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, ex.getMessage());
//                DriverStationLCD.getInstance().updateLCD();
                ex.printStackTrace();
            }
        }
    }

    /**
     * Get the speed controller associated with the Arm.
     * @return CANJaguar object that runs the Arm.
     */
     public CANJaguar getSpeedController() {
         return motor;
     }

}
