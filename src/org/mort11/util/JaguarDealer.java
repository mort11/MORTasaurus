package org.mort11.util;

import com.sun.squawk.util.Arrays;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Jaguar;

/**
 * Initiates all the CANJaguars, configures them, and keeps them alive.
 * @author MORT
 */
public class JaguarDealer {
    // NOTE Array index = boardID - 2
    private static CANJaguar[] canJags = new CANJaguar[7];
    private static int[] hashCodes = new int[7];

    private static void initJaguarDealer() {
        try {
            canJags[0] = new CANJaguar(2, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[0] = canJags[0].hashCode();
       
            canJags[1] = new CANJaguar(3, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[1] = canJags[1].hashCode();

            canJags[2] = new CANJaguar(4, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[2] = canJags[2].hashCode();

            canJags[3] = new CANJaguar(5, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[3] = canJags[3].hashCode();

            canJags[4] = new CANJaguar(6, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[4] = canJags[4].hashCode();

            canJags[5] = new CANJaguar(7, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[5] = canJags[5].hashCode();

            canJags[6] = new CANJaguar(8, CANJaguar.ControlMode.kPercentVbus);
            hashCodes[6] = canJags[6].hashCode();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Lazy way...
     * @param port PWM port number
     * @return PWM Jaguar on port
     */
    public static Jaguar getPWMJag(int port) {
        return new Jaguar(port);
    }

    public static CANJaguar getJag(int idNumber) {
        if (canJags[0] == null)
            initJaguarDealer();
        return canJags[idNumber - 2];
    }

    public static void checkJags() {
        for (int count = 0; count <= 6; count++) {
            try {
                if (canJags[count].getFirmwareVersion() == 0) {
                    canJags[count] = new CANJaguar(count + 2);
                }
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void reInitJag(CANJaguar can) {
        // binarySearch returns >-1 if object is found
        int index = Arrays.binarySearch(hashCodes, can.hashCode());
        if (index >= 0) {
            if ((index + 2) == 8
                    || (index + 2 == 7 || (index + 2) == 4 || (index + 2) == 3)) {
                try {
                    can = new CANJaguar(index + 2, CANJaguar.ControlMode.kSpeed);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
                hashCodes[index] = can.hashCode();
                try {
                    can.configNeutralMode(CANJaguar.NeutralMode.kCoast);

                    can.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);

                    can.configFaultTime(.5);

                    can.configEncoderCodesPerRev(250);

                    can.setPID(.4, .003, .003);

                    can.enableControl();
                } catch (CANTimeoutException ex1) {
                    ex1.printStackTrace();
                }
                
            } else {
                try {
                    //I don't think this has support for the Wrist jaguar
                    can = new CANJaguar(index + 2);

                    can.setPositionReference(CANJaguar.PositionReference.kPotentiometer);

                    can.configPotentiometerTurns(1);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
