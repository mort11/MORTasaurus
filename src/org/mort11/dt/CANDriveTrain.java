package org.mort11.dt;

import org.mort11.util.JaguarDealer;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.mort11.util.Constants;
import org.mort11.util.PhotoSensor;

/**
*
* @author MORT
*/
public class CANDriveTrain {

   private CANJaguar frontLeft, frontRight, backLeft, backRight;
   private ShifterSolenoid shifter;
   private PhotoSensor leftSensor;
   private PhotoSensor rightSensor;
   private PhotoSensor centerSensor;

   private double leftSet=0;
   private double rightSet = 0;

   private boolean atTriangle = false;
   private boolean offLine = false;

   public CANDriveTrain() {
       frontLeft = JaguarDealer.getJag(Constants.JaguarID.FRONT_LEFT_DT);
       frontRight = JaguarDealer.getJag(Constants.JaguarID.FRONT_RIGHT_DT);
       backLeft = JaguarDealer.getJag(Constants.JaguarID.BACK_LEFT_DT);
       backRight = JaguarDealer.getJag(Constants.JaguarID.BACK_RIGHT_DT);

       shifter = new ShifterSolenoid(Constants.DriveTrain.SHIFTER_CHANNEL);

       leftSensor = new PhotoSensor(Constants.PhotoSensorChannels.LEFT);
       rightSensor = new PhotoSensor(Constants.PhotoSensorChannels.RIGHT);
       centerSensor = new PhotoSensor(Constants.PhotoSensorChannels.CENTER);
   }

   public CANJaguar[] getSpeedControllers() {
       return new CANJaguar[] {frontLeft, frontRight, backLeft, /*backRight*/};
   }

   public PhotoSensor getLeftSensor() {
       return leftSensor;
   }

   public boolean checkLeftPhotoSensor() {
       return leftSensor.get();
   }

   public PhotoSensor getRightSensor() {
       return rightSensor;
   }

   public boolean checkRightPhotoSensor() {
       return rightSensor.get();
   }

   public PhotoSensor getCenterSensor() {
       return centerSensor;
   }

   public boolean checkCenterPhotoSensor() {
       return centerSensor.get();
   }
   
   /**
   * Very basic drive method. Just feeds the values to the speed controllers.
   * @param leftSpeed The percent voltage to send to the left side Jaguars.
   * @param rightSpeed The percent voltage to send to the right side Jaguars.
   */
   public void basicDrive(double leftSpeed, double rightSpeed) {
       boolean leftDetected = leftSensor.get();
       boolean rightDetected = rightSensor.get();
       boolean centerDetected = centerSensor.get();
       System.out.println("Left: " + leftDetected + " Right: " + rightDetected + " Center: " + centerDetected);
        //System.out.println(leftSpeed + " :: " + rightSpeed);
        try {
                frontLeft.setX(-leftSpeed);
                backLeft.setX(-leftSpeed);
                frontRight.setX(rightSpeed);
                backRight.setX(rightSpeed);
        } catch(CANTimeoutException ex){
                ex.printStackTrace();
        }
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
        try {
                frontLeft.setX(leftSpeed);
                backLeft.setX(leftSpeed);
                frontRight.setX(rightSpeed);
                backRight.setX(rightSpeed);
        } catch(CANTimeoutException ex){
                ex.printStackTrace();
        }
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

   public void stop() {
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
       return (!leftSensor.get() && !centerSensor.get() && !rightSensor.get());
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
    * @param forward If true, use straight speeds, if false, use Y speeds.
    */
   public void followLine(boolean b) {
       double lowSpeed, highSpeed;
       if(b) {
           lowSpeed = Constants.Auto.LOW_SPEED;
           highSpeed = Constants.Auto.HIGH_SPEED;
       } else {
           lowSpeed = Constants.Auto.LOW_SPEED_Y;
           highSpeed = Constants.Auto.HIGH_SPEED_Y;
       }
       

       boolean leftDetected = leftSensor.get();
       boolean rightDetected = rightSensor.get();
       boolean centerDetected = centerSensor.get();
//       System.out.println("Left: " + leftDetected + " Right: " + rightDetected + " Center: " + centerDetected);
       //Case 1: Only the center is on the line
       if(!leftDetected && !rightDetected && centerDetected) {
//           System.out.println("forward");
           leftSpeed = highSpeed;
           rightSpeed = highSpeed;
       }
       //Case 2: Center and left detected OR just left
       else if(leftDetected && !rightDetected) {
//           System.out.println("drift left");
           leftSpeed = lowSpeed;
           rightSpeed = highSpeed;
           leftLast = true;
       }
       //Case 3: Center and right detected OR just right
       else if(!leftDetected && rightDetected) {
//           System.out.println("drift right");
           leftSpeed = highSpeed;
           rightSpeed = lowSpeed;
           leftLast = false;
       }
       //Case 4: We found the triangle OR we're off the line
       else if(!leftDetected && !rightDetected && !centerDetected) {
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
       basicDrive(leftSpeed, rightSpeed);
   }

   public void detectTriangle() {
       boolean leftDetected = leftSensor.get();
       boolean rightDetected = rightSensor.get();
       boolean centerDetected = centerSensor.get();
       
       if((leftDetected && rightDetected && centerDetected) || (!leftDetected && !rightDetected && !centerDetected)) {
           atTriangle = true;
           //System.out.println("At triangle.");
       } else {
           atTriangle = false;
       }
   }
   
}
