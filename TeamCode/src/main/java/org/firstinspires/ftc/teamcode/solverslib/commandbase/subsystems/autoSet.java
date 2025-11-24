package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class autoSet extends SubsystemBase {
    //twin ts is just for brainstorming.
    //made by diddy XD

    private final Robot robot = Robot.getInstance();

    Limelight3A limey; //idk twin limey is a fire name.

    public void diddyExperiments() {

        Robot robot = Robot.getInstance();
        boolean isBlue = true;

        //get the pose of the robot
        Pose pose = follower.getPose();

        //set the x and y values
        double x = pose.getX();
        double y = pose.getY();

        final double height = 2.6;

        double newAngle; //the angle it needs to be changed to.

        double howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
        double hoodAngle; //angle the hood should be set to. .5 for close approach, .7 for far.

        if(!isBlue){
            //make the angle if it is RED
            newAngle = Math.toDegrees(Math.atan2((144-y), (144-x)));
        }else{
            //make the angle if it is BLUE
            newAngle = Math.toDegrees(Math.atan2((144-y), -x) + 90);
        }

        //SET THE HEADING OF THE ROBOT TO NEW ANGLE FIRST!!!!!!

        if(howFar > 8.75){
            hoodAngle = 0.7; //40 deg
        }else{
            hoodAngle = 0.5; //30 deg
        }

        //YIKES ALGEBRA GOES BRRRRR CHAT THE MESSY ALGEBRA
        double newSpeedInFeet = (-1*howFar*Math.sqrt(16.1))/(Math.cos(hoodAngle) * Math.sqrt(howFar*Math.tan(hoodAngle)-height));
        double speedInTicks = Math.round(newSpeedInFeet/.0353) + 20; //the plus 20 is accounting for air resistance and backspin.
        //speedInTicks is what we should change the motor speed to!
        //this speed can be +/- about 10 it doesn't need to be entirely precise, but it needs to be close

    }
}
