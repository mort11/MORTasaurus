package org.mort11.pwm;

import edu.wpi.first.wpilibj.Jaguar;
import org.mort11.util.Constants;
import org.mort11.util.JaguarDealer;
import org.mort11.util.Potentiometer;

/**
 * End Effector: Arm
 * @author MORT
 * @version 02.12.2011.3
 */
public class PWMArm {

    private Jaguar motor;

    private Potentiometer pot;

    private final double UPPER_LIMIT = Constants.Arm.UPPER_LIMIT;
    private final double LOWER_LIMIT = Constants.Arm.LOWER_LIMIT;

    public final double HOME  = Constants.Arm.HOME;
    public final double TROLL = Constants.Arm.TROLL;
    public final double HIGH  = Constants.Arm.HIGH;
    public final double MID   = Constants.Arm.MID;
    public final double LOW   = Constants.Arm.LOW;

    public boolean runningPreset;

    private boolean isRampUp, isRampDown, startRamp;
    private double armSpeed;

    /**
     * Constructs a new Arm with a CANJaguar set to ID 2.
     */
    public PWMArm() {
        isRampUp = false;
        isRampDown = false;
        startRamp = true;

        armSpeed = 0;

        motor = JaguarDealer.getPWMJag(2);
        pot = new Potentiometer(1);
    }

    /**
     * Get the speed controller associated with the Arm.
     * @return Jaguar object that runs the Arm.
     */
    public Jaguar getSpeedController() {
        return motor;
    }

    /**
     * The position of the Arm.
     * @return A double that represents the percent rotation around the one-turn potentiometer.
     */
    public double getPos() {
        return pot.getPosition();
    }

    public void setPos(double preset) {
        double distToPreset = Math.abs(getPos()-preset);
        boolean goingDown = (getPos()-preset > 0);

        if (startRamp) {
            isRampUp = true;
            startRamp = false;
        }

        if (isRampUp) {
            //System.out.println(rampUpDest-getPos());
            armSpeed += 0.05;
            if (armSpeed > 0.95) {
                isRampUp = false;
                armSpeed = 1;
            }
        } else if (isRampDown) {
            armSpeed = Math.abs((preset-getPos())/0.08);
            if (armSpeed < 0.5) {
                armSpeed = 0.5;
            }
        }

        if (distToPreset < 0.005) {
            startRamp = true;
            runningPreset = false;
            isRampDown = false;
            isRampUp = false;
            armSpeed = 0;
        } else if (distToPreset < 0.08) {
            isRampUp = false;
            isRampDown = true;
        }

        if (goingDown) {
            setSpeed(-armSpeed);
        } else {
            setSpeed(armSpeed);
        }
    }

    public void stop() {
        isRampDown = false;
        isRampUp = false;
        startRamp = true;
        armSpeed = 0;
        runningPreset = false;
    }


    /**
     * Pass in the percent voltage and the Arm moves within the constraints.
     * @param speed Percent voltage to send to the Arm speed controller.
     */
    public void setSpeed(double speed) {
        //TODO: Slow down when approaching limit
        speed = -1 * speed;
        double curPos = getPos();
        if(curPos > UPPER_LIMIT || curPos < LOWER_LIMIT || speed == 0.0) {
            motor.set(0);
        }
        if(curPos > LOWER_LIMIT && speed > 0) {
            motor.set(speed);
        }
        if(curPos < UPPER_LIMIT && speed < 0) {
            motor.set(speed);
        }
    }

    /**
     * Manual override to move the Arm without constraints.
     * @param speed Percent voltage to send to the Arm speed controller.
     */
    public void manualMove(double speed) {
        motor.set(-speed);
    }
}
