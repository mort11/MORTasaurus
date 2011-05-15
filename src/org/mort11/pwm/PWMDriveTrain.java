package org.mort11.pwm;

import org.mort11.util.JaguarDealer;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.mort11.util.Constants;
import org.mort11.dt.ShifterSolenoid;

/**
*
* @author MORT
*/
public class PWMDriveTrain {

   private Jaguar frontLeft, frontRight, backLeft, backRight;
   private ShifterSolenoid shifter;
   //private Encoder encoderLeft, encoderRight; //These are used if the optical encoders are connected to the Analog ports conencted to the cRIO
   public DigitalInput left = new DigitalInput(14);
   public DigitalInput right = new DigitalInput(3);
   public DigitalInput center = new DigitalInput(4);

   private boolean atTriangle = false;
   private boolean offLine = false;

   public PWMDriveTrain(int solenoidPort) {
       frontLeft = JaguarDealer.getPWMJag(8);
       frontRight = JaguarDealer.getPWMJag(3);
       backLeft = JaguarDealer.getPWMJag(7);
       backRight = JaguarDealer.getPWMJag(4);
       shifter = new ShifterSolenoid(solenoidPort);
   }

   public Jaguar[] getSpeedControllers() {
       return new Jaguar[] {frontLeft, frontRight, backLeft, backRight};
   }

   /**
   * Very basic drive method. Just feeds the values to the speed controllers.
   * @param leftSpeed The percent voltage to send to the left side Jaguars.
   * @param rightSpeed The percent voltage to send to the right side Jaguars.
   */
   public void basicDrive(double leftSpeed, double rightSpeed) {
        frontLeft.set(-leftSpeed);
        backLeft.set(-leftSpeed);
        frontRight.set(rightSpeed);
        backRight.set(rightSpeed);
   }

    /**
    * Driving mode for percentVBus.
    *
    * @param leftSpeed The percent voltage for the left side
    * @param rightSpeed The percent voltage for the right side
	* @param squareValues True, the leftSpeed and rightSpeed values are squared. Otherwise, the raw values are fed.
    */
   public void drive(double leftSpeed, double rightSpeed, boolean squareValues) {
        if(squareValues) {
            //square speeds and check for negatives
            leftSpeed *= leftSpeed * ((leftSpeed < 0) ? -1 : 1);
            rightSpeed *= rightSpeed  * ((rightSpeed < 0) ? -1 : 1);
        }
        frontLeft.set(leftSpeed);
        backLeft.set(leftSpeed);
        frontRight.set(rightSpeed);
        backRight.set(rightSpeed);
   }

   /**
   * Shifts from low to high, or high to low.
   */
   public void shift() {
       shifter.shift();
   }

   /**
    * Shifts to a specific gear.
    * @param highGear True shifts to high gear, false shifts to low gear.
    */
   public void shift(boolean highGear) {
       shifter.shift(!highGear);
   }

   /**
   * Gets the state of the transmission.
   * @return True if in high gear, false if in low gear.
   */
   public boolean inHighGear() {
        return shifter.getHigh();
   }

   //Really need to implement wheel speed matching

   /**
   * Gets whether or not the drivetrain thinks it is at the triangle (during Autonomous).
   * @return True if it detects it is at the triangle (all three sensors detect a line), otherwise false.
   */
   public boolean atTriangle() {
       return atTriangle;
   }

   public void kill() {
       basicDrive(0, 0);
   }

   /**
   * Allows autonomous mode to change the boolean without me having to change this code.
   * @param b True if past the triangle, false if not. Follow this or it will break.
   */
   public void pastTriangle(boolean b) {
		atTriangle = b;
   }

   public boolean offLine() {
       return offLine;
   }

   public void reset() {
       offLine = false;
       atTriangle = false;
   }

   private double leftSpeed = 0;
   private double rightSpeed = 0;
   private boolean leftLast = false;
   /**
    * Follow line.
    * @param forward If true, robot follows line forward; false, it follows it backward.
    */
   public void followLine(boolean forward) {
       double lowSpeed = Constants.Auto.LOW_SPEED;
       double highSpeed = Constants.Auto.HIGH_SPEED;


       boolean leftDetected = !left.get();
       boolean rightDetected = !right.get();
       boolean centerDetected = !center.get();
//       System.out.println("Left: " + leftDetected + " Right: " + rightDetected + " Center: " + centerDetected);
       //Case 1: Only the center is on the line
       if(!leftDetected && !rightDetected && centerDetected) {
           leftSpeed = highSpeed;
           rightSpeed = highSpeed;

           //System.out.println("Going forward...");
       }
       //Case 2: Center and left detected OR just left
       if((leftDetected && !rightDetected)) {
           leftSpeed = lowSpeed;
           rightSpeed = highSpeed;
           leftLast = true;
           //System.out.println("Drifting right...");
       }
       //Case 3: Center and right detected OR just right
       if(!leftDetected && rightDetected) {
           leftSpeed = highSpeed;
           rightSpeed = lowSpeed;
           leftLast = false;
           //System.out.println("Drifting left...");
       }
       //Case 4: Hey we found the triangle
       if(!leftDetected && !rightDetected && !centerDetected) {
           if (leftLast) {
               //System.out.println("triright");
               leftSpeed = lowSpeed;
               rightSpeed = highSpeed;
           } else {
               //System.out.println("trileft");
               leftSpeed = highSpeed;
               rightSpeed = lowSpeed;
           }
           atTriangle = true;
           //System.out.println("At triangle.");
       } else {
           atTriangle = false;
       }

       if(forward) {
           basicDrive(leftSpeed, rightSpeed);
       } else {
           basicDrive(-leftSpeed, -rightSpeed);
       }
   }

   public void detectTriangle() {
       boolean leftDetected = !left.get();
       boolean rightDetected = !right.get();
       boolean centerDetected = !center.get();

       if((leftDetected && rightDetected && centerDetected) || (!leftDetected && !rightDetected && !centerDetected)) {
           atTriangle = true;
           //System.out.println("At triangle.");
       } else {
           atTriangle = false;
       }
   }

}
