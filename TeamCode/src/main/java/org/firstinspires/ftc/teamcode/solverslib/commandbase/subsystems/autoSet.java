package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class autoSet extends SubsystemBase {
    /*
        Okay, using these, we should be able to find the angle we need to align to.
        The auto align should be mapped to a face button.

        Then, we can find the exact speed we need. Physics wise, there is a window for the
        speed we can use. HOWEVER, I am aiming for the speed in the exact MIDDLE of the goal,
        which is about 2.6ft above the robot (shooter is 15 inches high). I use messy algebra,
        ew, to figure out the velocity in ft/s. (YES IK IM USING FT/S CUZ WE LUV FREEDOM UNITS)
        Then we convert in the disgusting made up unit of ticks/s which is about .0353, which
        is calculated with a variety of factors, which I didn't include in the code since it was
        just a bunch of static numbers. Just know if the flywheel changes, this conversion number
        NEEDS to be changed.

        Now, the only unfinished part is that we need to figure out how to use limelight effectively.
        That's what needs to be done IN ROBOTICS, and then we can test it.

        Hopefully we can finish this part this week, and move everything into the Outtake subsys.
        I'm doing it here since its purley experimental.
        -Adhithya Yuvaraj (Nov 24th 2025)
         */

    private final Robot robot = Robot.getInstance();

    Limelight3A limey; //idk twin limey is a fire name.

    public void diddyExperiments() {

        Robot robot = Robot.getInstance();
        boolean isBlue = true; //determines the alliance color. This is kinda impt ngl.

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
