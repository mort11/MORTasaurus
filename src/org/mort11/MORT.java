/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.mort11;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

import org.mort11.dt.CANDriveTrain;

import org.mort11.ee.Arm;
import org.mort11.ee.Claw;
import org.mort11.ee.Deployment;
import org.mort11.ee.Wrist;

import org.mort11.util.Constants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * @version FIRST Championship Build
 */
public class MORT extends SimpleRobot {

    /* END EFFECTOR */
    Arm arm;
    Wrist wrist;
    Claw claw;
    Deployment deployment;
    Joystick wristJoy;
    Joystick armJoy;

    /* DRIVETRAIN */
    CANDriveTrain dt;
    Joystick leftJoyDT;
    Joystick rightJoyDT;

    /* GENERAL */
    Compressor comp;
    AnalogChannel rangeFinder;
    DigitalInput[] dial = {new DigitalInput(10), new DigitalInput(9), new DigitalInput(8), new DigitalInput(7)};

    boolean mbTrackDeployed;
    Servo miniBotLatch;

    public void robotInit() {

        /* END EFFECTOR */
        arm = new Arm();
        wrist = new Wrist();
        claw = new Claw();

        deployment = new Deployment();
        
        wristJoy = new Joystick(Constants.Joystick.WRIST_PORT);
        armJoy = new Joystick(Constants.Joystick.ARM_PORT);

        /* DRIVE TRAIN */
        dt = new CANDriveTrain();

        comp = new Compressor(1,1);
        leftJoyDT = new Joystick(Constants.Joystick.LEFT_DT_PORT);
        rightJoyDT = new Joystick(Constants.Joystick.RIGHT_DT_PORT);
        
        rangeFinder = new AnalogChannel(7);

        mbTrackDeployed = false;
        miniBotLatch = new Servo(1);
    }

    long teleTime, curTime;

    boolean followingLine = true;
    boolean turning = false;
    boolean detectedLine = false;
    boolean wontonComplete = false;
    boolean backedUp = false;
    boolean yTurn = true;

//    public void autonomous() {
//        while(isAutonomous()) {
//            System.out.println(dial[0].get() + " || " + dial[1].get() + " || " + dial[2].get() + " || " + dial[3].get());
//            if(!dial[0].get() || !dial[3].get())
//                System.out.println("Straight");
//            else if(!dial[2].get())
//                System.out.println("Left");
//            else if(!dial[1].get())
//                System.out.println("Right");
//        }
//    }
    public void autonomous() {
//        /*double range;
//        comp.start();
//        dt.shift(Constants.DriveTrain.LOW_GEAR);
//        arm.runningPreset = true;
//        wrist.runningPreset = true;
//
//        while(isAutonomous() && isEnabled()) {
//            range = rangeFinder.getVoltage();
//
//            if(!wontonComplete) {
//                if(arm.runningPreset)
//                    arm.setPos(Constants.Arm.OUTER_HIGH);
//                if(wrist.runningPreset)
//                    wrist.setPos(Constants.Wrist.OUTER_HIGH, Constants.Wrist.AUTON_SPEED);
//            } else if (backedUp) {
//                if(arm.runningPreset)
//                    arm.setPos(Constants.Arm.HOME);
//                if(wrist.runningPreset)
//                    wrist.setPos(Constants.Wrist.HOME, Constants.Wrist.AUTON_SPEED);
//            }
//
//            if(!dial[0].get() || !dial[3].get()) { //STRAIGHT RUTABAGA
//                //System.out.println("straight " + rangeFinder.getVoltage());
//                if(!wontonComplete) {
//                    if(followingLine) {
//                        dt.followLine(true);
//                        if (range < Constants.Auto.STRAIGHT_DIST) {
//                            followingLine = false;
//                        }
//                    } else {
//                        dt.stop();
////                        System.out.println("stop " + arm.runningPreset + " :: " + wrist.runningPreset);
//                        if(!arm.runningPreset && !wrist.runningPreset) {
////                            System.out.println("roller");
//                            claw.moveRollersBackward();
//                            Timer.delay(0.6);
//                            claw.stopRollers();
//                            wontonComplete = true;
//                        }
//                    }
//                } else if (!backedUp) {
//                    dt.basicDrive(-0.5, -0.5);
//                    Timer.delay(2.0);
//                    dt.stop();
//                    backedUp = true;
//                    arm.runningPreset = true;
//                    wrist.runningPreset = true;
//                }
//            }
//
//            else if(!dial[2].get()) { //LEFT RUTABAGA
////                System.out.println("left tri RF: " + range);
//                /*if (followingLine) {
//                    if (range > Constants.Auto.DIST_Y_1) {
//                        dt.followLine(false);
//                    } else {
//                        followingLine = false;
//                        gtfoLine = true;
//                    }
//                } else if (gtfoLine) {
//                    dt.basicDrive(-Constants.Auto.TURN_SPEED, Constants.Auto.TURN_SPEED);
//                    dt.detectTriangle();
//                    if (dt.atTriangle()) {
//                        gtfoLine = false;
//                        goStraight = true;
//                    }
//                } else if (goStraight) {
//                    if (range > Constants.Auto.Y_FINAL_DIST) {
//                        dt.basicDrive(0.4, 0.4);
//                    } else {
//                        goStraight = false;
//                        dt.stop();
//                    }
//                } else {
//                    System.out.println("done");
//                }*/
//                if(followingLine) {
////                    System.out.println("Following line.");
//                    if (range > Constants.Auto.DIST_Y_1) {
//                        dt.followLine(false);
//                    } else if (range <= Constants.Auto.DIST_Y_1 && range > Constants.Auto.DIST_Y_2) {
////                        System.out.println("DEAD RECKONGING! ");
//                        dt.basicDrive(0.4, 0.4);
//                    } else if (range <= Constants.Auto.DIST_Y_2) {
////                        System.out.println("Else... RF: " + rangeFinder.getVoltage());
//                        followingLine = false;
//                        turning = true;
//                    }
//                } else if(turning) {
////                    System.out.println("Turning to get line");
//                    dt.basicDrive(-Constants.Auto.TURN_SPEED, Constants.Auto.TURN_SPEED); //turn left
//                    dt.detectTriangle();
//                    if (!dt.atTriangle()) { //the left photosensor picked up the line
//                        turning = false;
//                        detectedLine = true;
//                    }
//                } else if(detectedLine) { //follow line until specified distance (Constants.Auto.LEFT_DIST)
//                        dt.followLine(false);
////                        System.out.println("Following line after y");
//                        detectedLine = range > Constants.Auto.LEFT_DIST;
//                } else {
//                    if(yTurn) {
//                        dt.basicDrive(Constants.Auto.TURN_SPEED, -Constants.Auto.TURN_SPEED);
//                        yTurn = dt.checkCenterPhotoSensor();
////                        System.out.println("Turning right to get off line a bit: " + dt.offLine());
//                    } else {
//                        dt.stop();
////                        System.out.println("Done " + range);
//                        if(!arm.runningPreset && !wrist.runningPreset) {
//                            claw.moveRollersBackward();
//                            Timer.delay(1);
//                            claw.stopRollers();
//                        }
//                    }
//                }
//            }
//
//            else if(!dial[1].get()) { //RIGHT RUTABAGA
////                System.out.println("right tri");
//                if(followingLine) {
//                    if (rangeFinder.getVoltage() > Constants.Auto.DIST_Y_1) {
//                        dt.followLine(false);
//                    } else if (rangeFinder.getVoltage() < Constants.Auto.DIST_Y_1 && rangeFinder.getVoltage() > Constants.Auto.DIST_Y_2) {
//                        dt.basicDrive(0.4, 0.4);
//                    } else {
//                        followingLine = false;
//                        turning = true;
//                    }
//                } else if(turning) {
//                        dt.basicDrive(0.3, -0.3); //turn right
//                    if(dt.checkRightPhotoSensor()) { //the right photosensor picked up the line
//                        turning = false;
//                        detectedLine = true;
//                    }
//                } else if(detectedLine) { //follow line until specified distance (Constants.Auto.RIGHT_DIST)
//                        dt.followLine(false);
//                } else {
//                    dt.stop();
//                    if(!arm.runningPreset && !wrist.runningPreset) {
//                        claw.moveRollersBackward();
//                        Timer.delay(1);
//                        claw.stopRollers();
//                    }
//                }
//                detectedLine = rangeFinder.getVoltage() > Constants.Auto.RIGHT_DIST;
//            }
//        }
//        //reset autonomous
//        followingLine = true;
//        turning = false;
//        detectedLine = false;
//        wontonComplete = false;
//        backedUp = false;
//        yTurn = true;
    }


    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        teleTime = System.currentTimeMillis();
        comp.start();
        
        /* DRIVE TRAIN */
        boolean wasHeld = false;

        /* END EFFECTOR */
        boolean triggerWasHeld = false;
        boolean armTriggerWasHeld = false;
        boolean manualOverride = false;

        /* PRESETS */
        boolean runningPreset = false; //General for stopping joystick input
        boolean runningWristPreset = false; //General for stopping joystick input
        double preset = arm.MID;
        double wristPreset = 0.42;

        /* JOYSTICK VALUES */
        double wristY, armY, leftDTY, rightDTY;

        miniBotLatch.set(Constants.Deployment.REVERSE_MINIBOT);
        
        while(isOperatorControl()) {            
//            System.out.println("arm: " + arm.getPos() + " :: wrist: " + wrist.getPos() + " :: cur: " + curRange);
            //System.out.println("arm: " + arm.getPos() + " :: wrist: " + wrist.getPos());
//            System.out.println(!dial[0].get() + " " + !dial[1].get() + " " + !dial[2].get() + " " + !dial[3].get());
            System.out.println("Arm: " + arm.getPos() + " Wrist: " + wrist.getPos() + " RF: " + rangeFinder.getVoltage());
            
            /* DRIVE TRAIN */
            if(rightJoyDT.getTrigger() && !wasHeld) {
                dt.shift();
                wasHeld = true;
            } else if(!rightJoyDT.getTrigger() && wasHeld) {
                wasHeld = false;
            }

            leftDTY = -leftJoyDT.getY();
            rightDTY = -rightJoyDT.getY();
            dt.basicDrive(leftDTY, rightDTY); 

            /* END EFFECTOR */
            wristY = wristJoy.getY();
            armY = armJoy.getY();

            /* TEST AUTON PRESET HEIGHTS (WRIST JOYSTICK) */
            if(wristJoy.getRawButton(6) && !manualOverride) {
                runningPreset = true;
                arm.runningPreset = true;
                runningWristPreset = true;
                wrist.runningPreset = true;
                preset = arm.HIGH;
                wristPreset = wrist.HIGH;
            }

            if(!manualOverride) {
                for (int i = 7;i <= 12;i++) {
                    if (wristJoy.getRawButton(i)) {
                        runningPreset = true;
                        arm.runningPreset = true;

                        if (i == 7 || i == 8)
                            preset = arm.HIGH;
                        else if (i == 9 || i == 10)
                            preset = arm.MID;
                        else if (i == 11 || i == 12)
                            preset = arm.LOW;
                        if (i == 7 || i == 9 || i == 11)
                            preset -= 0.02;
                    }
                }

                /* HOME PRESET (ARM JOYSTICK) */
                if (armJoy.getRawButton(5) && !manualOverride) {
                    preset = arm.HOME;
                    wristPreset = wrist.HOME;
                    runningPreset = true;
                    arm.runningPreset = true;
                    runningWristPreset = true;
                    wrist.runningPreset = true;
                }

                /* TROLL PRESET (ARM JOYSTICK) */
                if(armJoy.getRawButton(3) && !manualOverride) {
                    preset = arm.TROLL;
                    wristPreset = wrist.TROLL;
                    runningPreset = true;
                    arm.runningPreset = true;
                    runningWristPreset = true;
                    wrist.runningPreset = true;
                }
            }

            /* MANUAL CONTROL TOGGLE */
            if(armJoy.getTrigger() && !armTriggerWasHeld) {
                manualOverride = !manualOverride;
                armTriggerWasHeld = true;
            } else if(!armJoy.getTrigger() && armTriggerWasHeld) {
                armTriggerWasHeld = false;
            }

            /* ARM CONTROL */
            if (runningPreset) {
                arm.setPos(preset);
                if (!arm.runningPreset) {
                    runningPreset = false;
                }
            } else {
                if(manualOverride) {
                    arm.manualMove(armY);
                } else {
                    arm.setSpeed(armY);
                }
            }

            /* WRIST CONTROL */
            if (runningWristPreset) {
                wrist.setPos(wristPreset, Constants.Wrist.TELEOP_SPEED);
                if (!wrist.runningPreset) {
                    runningWristPreset = false;
                }
            } else {
                if(manualOverride) {
                    wrist.manualMove(wristY);
                } else {
                    wrist.setSpeed(wristY);
                }
            }

            /* ROLLERS */
            if(wristJoy.getRawButton(5)) {
                claw.moveRollersBackward(); //rollers release
            } else if(wristJoy.getRawButton(3)) {
                claw.moveRollersForward(); //rollers suck
            } else {
                claw.stopRollers();
            }

            /* OPEN/CLOSE CLAW */
            if(wristJoy.getTrigger() && !triggerWasHeld) {
                if(!(claw.isOpen())) {
                    claw.open();
                } else {
                    claw.close();
                }
                triggerWasHeld = true;
            } else if(!wristJoy.getTrigger() && triggerWasHeld) {
                triggerWasHeld = false;
            }

            curTime = System.currentTimeMillis();

            /*
             * AUTONOMOUS MINIBOT DEPLOYMENT
             * If the teleop time is less than (or equal to) 10 seconds, and the "ready"
             * button (6 on the arm joystick) is held down.
             */
//            System.out.println("Time since start of teleop: " + (curTime - teleTime));
            if(armJoy.getRawButton(6)) {
                if ((!mbTrackDeployed) && (curTime - teleTime) >= Constants.Deployment.MINIBOT_TIME) {
                    deployment.deploy();
                    mbTrackDeployed = true;
                }
            }

            /* DEPLOY MINIBOT TWO-STAGE */
//            if ((armJoy.getRawButton(7) && mbTrackDeployed) || (mbTrackDeployed && (curTime - teleTime) >= Constants.Deployment.MINIBOT_TIME)) {
//                miniBotLatch.set(Constants.Deployment.RELEASE_MINIBOT);
//            }

            /* DRIVER DEPLOY MINIBOT */
            if(armJoy.getRawButton(6) && armJoy.getRawButton(7)) {
                if ((arm.getPos() > arm.MID-0.02) || manualOverride) //trusting that the drivers put the arm out of the way
                    deployment.deploy();
                    mbTrackDeployed = true;
            }

            /* DRIVER RETRACT MINIBOT */
            if(armJoy.getRawButton(11) && armJoy.getRawButton(10)) {
                deployment.retract();
                mbTrackDeployed = false;
            }

            /* END EFFECTOR KILLSWITCH (ARM JOYSTICK) */
            if (armJoy.getRawButton(9) || armJoy.getRawButton(8)) {
                arm.stop();
                wrist.stop();
            }
        }
        teleTime = 0;
        curTime = 0;
        mbTrackDeployed = false;
    }

    public void disabled() {
        deployment.retract();
        teleTime = 0;
        curTime = 0;       
    }

}
