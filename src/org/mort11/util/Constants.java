package org.mort11.util;

/**
 * 
 * @author MORT
 */
public class Constants {

    public class JaguarID {
        public static final int ARM = 2;
        public static final int FRONT_RIGHT_DT = 3;
        public static final int BACK_RIGHT_DT = 4;
        public static final int WRIST = 5;
        public static final int ROLLERS = 6;
        public static final int BACK_LEFT_DT = 7;
        public static final int FRONT_LEFT_DT = 8;
    }

    public class PhotoSensorChannels {
        public static final int LEFT = 14;
        public static final int RIGHT = 3;
        public static final int CENTER = 4;
    }

    public class Joystick {
        public static final int LEFT_DT_PORT = 1;
        public static final int RIGHT_DT_PORT = 2;
        public static final int WRIST_PORT = 3;
        public static final int ARM_PORT = 4;
    }

    public class Deployment {
        public static final int DEPLOY_CHANNEL = 4;
        public static final int RETRACT_CHANNEL = 5;
        public static final long MINIBOT_TIME = 110000;
        public static final long TRACK_DEPLOY_TIME = 150000;
        public static final int RELEASE_MINIBOT = 1;
        public static final int REVERSE_MINIBOT = 0;
    }

    public class DriveTrain {
        public static final int SHIFTER_CHANNEL = 1;
        public static final boolean LOW_GEAR = true;
        public static final boolean HIGH_GEAR = false;
    }

    public class Claw {
        public static final int OPENER_CHANNEL = 2;
        public static final int CLOSER_CHANNEL = 3;
    }

    public class Wrist {
        public static final double UPPER_LIMIT = 0.24; //.9
        public static final double LOWER_LIMIT = 0.615; //.2
        
        public static final double HOME  = UPPER_LIMIT; //.73
        public static final double TROLL = 0.534; //.57
        public static final double HIGH  = 0.38; //.63, 0.4 (prev), .47 (warehouse), .4 (palmettoo)
        public static final double OUTER_HIGH = HIGH + 0.03;

        public static final double AUTON_SPEED = 0.5;
        public static final double TELEOP_SPEED = 1;
    }

    public class Arm {
        public static final double UPPER_LIMIT = 0.52; //.7
        public static final double LOWER_LIMIT = 0.214; //.36

        public static final double HOME  = LOWER_LIMIT;
        public static final double TROLL = .225;
        
        public static final double HIGH  = UPPER_LIMIT-0.01; //, -.02, .62, upperlimit-0.03
        public static final double MID   = UPPER_LIMIT-0.16;
        public static final double LOW   = LOWER_LIMIT+0.03;
        public static final double OUTER_HIGH = HIGH - 0.03;
    }
    
    public class Auto {
        //Speed at which to follow the line. LOW_SPEED indicates the speed at which the slower side will
        //go in order to turn the robot.
        public static final double LOW_SPEED = -0.2; 
        public static final double HIGH_SPEED = 0.38; 
        public static final double LOW_SPEED_Y = -0.2;
        public static final double HIGH_SPEED_Y = 0.4;
        
        //----- Straight Autonomous -----

        //Distance from the wall to deploy the tube.
        public static final double STRAIGHT_DIST = 0.5; //.4, 0.55, .53, .38
        public static final double Y_FINAL_DIST = 0.37;

        //----- Y Autonomous -----

        //Distance to double check if the robot is actually on the Y(all photo sensors are off the line)
        //or if it just fell off the line. Once the robot is closer than this value, it is 'eligible' to
        //be on the triangle.
        public static final double DIST_ON_Y = 0.47; //.68
        public static final double DIST_Y_1 = .8;
        public static final double DIST_Y_2 = 0.69;

        //RIGHT = dial 2; LEFT = dial 3.
        //Once the robot starts following one of the forks, it will travel until it is at least these distances
        //away from the wall.
        public static final double RIGHT_DIST = 0.55; //.55
        public static final double LEFT_DIST  = 0.46; //.5
        public static final double TURN_SPEED = 0.45;
    }
}
